package gui;

import console.DataProcessing;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * 新建用户对话框
 * 提供添加新用户的界面
 */
public class NewUserDialog extends JDialog {
    // 对话框组件
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    // 用户信息输入字段
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox roleComboBox;
    // 标签组件
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel roleLabel;
    // 面板组件
    private JPanel operationsField;
    private JPanel buttonsField;

    /**
     * 初始化对话框
     * 设置事件监听器和快捷键
     */
    public NewUserDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(_ -> {
            onOK();
        });

        buttonCancel.addActionListener(_ -> {
            onCancel();
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 确认按钮处理
     * 验证输入并添加新用户
     */
    private void onOK() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        if (role != null) {
            role = role.toLowerCase();
        }

        if (username.isEmpty() || password.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(null, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = DataProcessing.insertUser(username, password, role);
            if (success) {
                JOptionPane.showMessageDialog(null, "User added successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void display() {
        NewUserDialog dialog = new NewUserDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
