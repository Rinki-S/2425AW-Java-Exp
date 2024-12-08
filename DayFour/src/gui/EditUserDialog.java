package gui;

import console.DataProcessing;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

/**
 * 编辑用户对话框类
 * 用于修改用户信息，包括密码和角色
 */
public class EditUserDialog extends JDialog {
    // GUI组件
    private JPanel contentPane; // 主面板
    private JButton buttonOK; // 确认按钮
    private JButton buttonCancel; // 取消按钮
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JPasswordField passwordConfirmField; // 确认密码输入框
    private JComboBox roleComboBox; // 角色选择下拉框
    private int isSelf;

    public JComboBox getRoleComboBox() {
        return roleComboBox;
    }

    // 用户数据
    private String role; // 用户角色
    private String username; // 用户名

    /**
     * 构造函数
     *
     * @param role     用户角色
     * @param username 用户名
     */
    public EditUserDialog(String role, String username, int isSelf) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.isSelf = isSelf;
        if (isSelf == 1) {
            roleComboBox.setEnabled(false);
        }

        this.role = role;
        this.username = username;
        for (int i = 0; i < roleComboBox.getItemCount(); i++) {
            if (((String) roleComboBox.getItemAt(i)).toLowerCase().equals(role)) {
                roleComboBox.setSelectedIndex(i);
                break;
            }
        }
        usernameField.setText(username);

        buttonOK.addActionListener(_ -> {
            onOK();
        });

        buttonCancel.addActionListener(_ -> {
            onCancel();
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction((var _) -> {
            onCancel();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 确认按钮点击处理方法
     * 验证输入并更新用户信息
     */
    private void onOK() {
        String newPassword = new String(passwordField.getPassword());
        String newPasswordConfirm = new String(passwordConfirmField.getPassword());
        String newRole = (String) roleComboBox.getSelectedItem();
        if (newRole != null) {
            newRole = newRole.toLowerCase();
        }
        if (newPassword.isEmpty() || newPasswordConfirm.isEmpty() || newRole == null) {
            JOptionPane.showMessageDialog(null, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DataProcessing.updateUser(username, newPassword, newRole);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dispose();
    }

    /**
     * 取消按钮点击处理方法
     * 关闭对话框
     */
    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    /**
     * 显示编辑用户对话框的静态方法
     *
     * @param role     用户角色
     * @param username 用户名
     */
    public static void display(String role, String username, int isSelf) {
        EditUserDialog dialog = new EditUserDialog(role, username, isSelf);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void display(String role, String username) {
        display(role, username, 0);
    }
}
