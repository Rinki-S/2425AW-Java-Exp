package gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchUserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameField;
    private JLabel usernameLabel;

    // 传入 MainFrame 引用
    private MainFrame mainFrame;

    public SearchUserDialog(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

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
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(_ -> {
            onCancel();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String targetUsername = usernameField.getText().trim();
        JTable userList = this.mainFrame.getUserList();
        DefaultTableModel model = (DefaultTableModel) userList.getModel();

        // 如果搜索词为空，清空选择
        if (targetUsername.isEmpty()) {
            userList.clearSelection();
            dispose();
            return;
        }

        // 查找匹配用户
        ListSelectionModel selectionModel = userList.getSelectionModel();
        selectionModel.clearSelection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String username = (String) model.getValueAt(i, 0);
            if (username.toLowerCase().contains(targetUsername.toLowerCase())) {
                selectionModel.addSelectionInterval(i, i);
                userList.scrollRectToVisible(userList.getCellRect(i, 0, true));
            }
        }

        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void display(MainFrame mainFrame) {
        SearchUserDialog dialog = new SearchUserDialog(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }
}
