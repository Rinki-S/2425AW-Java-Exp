package gui;

import console.AbstractUser;
import console.DataProcessing;
import console.Doc;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * 主窗口界面类
 * 包含文档管理、个人信息和用户管理三个主要功能面板
 */
public class MainFrame extends JFrame {
    // 主要标签页面板
    private JTabbedPane tabbedPane1;
    // 主面板
    private JPanel panel1;
    // 文档管理面板
    private JPanel dmPane;
    // 个人信息面板
    private JPanel piPane;
    // 文档管理内容包装面板
    private JPanel dmContentWrapper;
    // 文档管理内容面板
    private JPanel dmContent;
    // 文档列表表格
    private JTable documentList;
    // 文档下载按钮
    private JButton downloadDocumentButton;
    // 文档上传按钮
    private JButton uploadDocumentButton;
    private JPanel piContentWrapper;
    private JPanel piContent;
    private JPanel currentUserInfo;
    private JTextField usernameField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton changePasswordButton;
    private JButton logOutButton;
    private JPanel umPane;
    private JPanel umContentWrapper;
    private JPanel umContent;

    public JTable getUserList() {
        return userList;
    }

    public JTable getDocumentList() {
        return documentList;
    }

    private AbstractUser currentUser;

    public AbstractUser getCurrentUser() {
        return currentUser;
    }

    private JTable userList;
    private JButton addUserButton;
    private JButton deleteUserButton;
    private JButton modifyUserButton;
    private JScrollPane userListWrapper;
    private JScrollPane documentListWrapper;
    private JPasswordField passwordField;
    private JButton searchUserButton;

    /**
     * 主窗口构造函数
     *
     * @param currentUser 当前登录的用户对象
     */
    public MainFrame(AbstractUser currentUser) {
        // 创建并配置主窗口
        JFrame frame = new JFrame("Home");
        frame.setContentPane(tabbedPane1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // 将窗口置于屏幕中央
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 400);
        frame.setVisible(true);

        // 应用macOS特定的UI调整
        customUI.macOSAdjustments(frame);

        // 初始化数据和界面
        loadUserData();
        loadDocData();
        loadCurrentUserData(currentUser);
        functionAvailabilityAdjustments(currentUser);
        this.currentUser = currentUser;

        // 创建用户操作处理器
        UserOperations userOperations = new UserOperations(this);

        // 配置用户管理按钮的事件监听器
        addUserButton.addActionListener(_ -> {
            userOperations.addUser();
            loadUserData(); // 刷新用户列表
        });

        deleteUserButton.addActionListener(_ -> {
            userOperations.deleteUser();
            loadUserData();
        });

        modifyUserButton.addActionListener(_ -> {
            userOperations.editUser();
            loadUserData();
        });

        searchUserButton.addActionListener(_ -> {
            userOperations.searchUser();
        });

        // 创建文档操作处理器
        DocOperations docOperations = new DocOperations(this);

        // 配置文档管理按钮的事件监听器
        uploadDocumentButton.addActionListener(_ -> {
            docOperations.uploadDoc();
            loadDocData(); // 刷新文档列表
        });

        downloadDocumentButton.addActionListener(_ -> {
            docOperations.downloadDoc();
        });

        // 窗口关闭时保存数据
        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DataProcessing.saveData();
                    }
                });

        // 修改密码按钮事件处理
        changePasswordButton.addActionListener(_ -> {
            EditUserDialog dialog = new EditUserDialog(currentUser.getRole(), currentUser.getName(), 1);
            dialog.display(currentUser.getRole(), currentUser.getName(), 1);
        });

        // 注销按钮事件处理
        logOutButton.addActionListener(_ -> {
            // 弹出确认对话框
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                frame.dispose(); // 关闭当前窗口
                new LoginFrame(); // 打开登录窗口
            }
        });
    }

    /**
     * 加载用户列表数据
     * 从数据处理层获取所有用户信息并显示在表格中
     */
    private void loadUserData() {
        // 定义表格列名
        String[] columnNames = { "Username", "Role" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            // 获取并遍历所有用户
            Enumeration<AbstractUser> users = DataProcessing.listUser();
            while (users.hasMoreElements()) {
                AbstractUser user = users.nextElement();
                // 首字母大写处理角色名
                String role = user.getRole();
                role = Character.toUpperCase(role.charAt(0)) + role.substring(1);
                model.addRow(new Object[] { user.getName(), role });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 设置表格模型和选择模式
        userList.setModel(model);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * 加载文档列表数据
     * 从数据处理层获取所有文档信息并显示在表格中
     */
    private void loadDocData() {
        String[] columnNames = {
                "ID",
                "Creator",
                "Time",
                "Description",
                "Filename", };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            Enumeration<Doc> docs = DataProcessing.listDoc();
            while (docs.hasMoreElements()) {
                Doc doc = docs.nextElement();
                String id = doc.getId();
                String creator = doc.getCreator();
                String timestamp = doc.getTimestamp().toString();
                String description = doc.getDescription();
                String filename = doc.getFilename();
                model.addRow(
                        new Object[] {
                                id,
                                creator,
                                timestamp,
                                description,
                                filename, });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        documentList.setModel(model);
        documentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * 加载当前用户信息
     * 在个人信息面板中显示当前用户的用户名和密码
     *
     * @param currentUser 当前登录的用户对象
     */
    private void loadCurrentUserData(AbstractUser currentUser) {
        usernameField.setText(currentUser.getName());
        passwordField.setText(currentUser.getPassword());
    }

    /**
     * 根据用户角色调整功能权限
     * administrator: 不能上传文档
     * operator: 不能管理用户
     * browser: 不能管理用户和上传文档
     *
     * @param currentUser 当前登录的用户对象
     */
    private void functionAvailabilityAdjustments(AbstractUser currentUser) {
        String role = currentUser.getRole();
        switch (role) {
            // 管理员不能上传文档
            case "administrator" -> {
                uploadDocumentButton.setEnabled(false);
            }
            // 操作员不能管理用户
            case "operator" -> {
                addUserButton.setEnabled(false);
                modifyUserButton.setEnabled(false);
                deleteUserButton.setEnabled(false);
            }
            // 浏览者权限最低
            case "browser" -> {
                addUserButton.setEnabled(false);
                modifyUserButton.setEnabled(false);
                deleteUserButton.setEnabled(false);
                uploadDocumentButton.setEnabled(false);
            }
        }
    }
}
