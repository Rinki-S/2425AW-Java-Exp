package gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import console.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * 登录窗口类
 * 提供用户登录界面和验证功能
 */
public class LoginFrame {
    // 登录界面组件
    private JPanel InnerPanel_2;
    private JPanel InnerPanel_3;
    // 输入字段
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    // 标签和按钮
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton LoginButton;
    private JButton CancelButton;
    // 其他面板组件
    private JPanel CreditsPanel;
    private JPanel panel1;

    /**
     * 初始化登录窗口
     * 设置界面布局和事件监听
     */
    public LoginFrame() {
        if (panel1 == null) {
            throw new IllegalStateException("panel1 is not initialized");
        }

        JFrame frame = new JFrame("Login");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        // Center the frame
        frame.setVisible(true);

        customUI.macOSAdjustments(frame);

        addPlaceHolder(usernameTextField, "Username");
        addPlaceHolder(passwordPasswordField, "Password");

        DataProcessing dataProcessing = new DataProcessing();

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordPasswordField.getPassword());
                boolean isValid = false;
                AbstractUser currentUser;
                try {
                    currentUser = DataProcessing.searchUser(username, password);
                    if (currentUser != null) {
                        isValid = true;
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (isValid) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new MainFrame(currentUser);
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed.");
                }
            }
        });

        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameTextField.setText("");
                passwordPasswordField.setText("");
                addPlaceHolder(usernameTextField, "Username");
                addPlaceHolder(passwordPasswordField, "Password");
            }
        });
    }

    /**
     * 为文本组件添加占位符
     * 
     * @param textComponent 文本组件
     * @param placeholder   占位符文本
     */
    private void addPlaceHolder(JTextComponent textComponent, String placeholder) {
        textComponent.setText(placeholder);
        textComponent.setForeground(Color.GRAY);

        textComponent.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                    textComponent.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setText(placeholder);
                    textComponent.setForeground(Color.GRAY);
                }
            }
        });
    }
}
