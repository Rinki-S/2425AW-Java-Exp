package gui;

import console.DataProcessing;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 用户操作类
 * 处理用户管理相关的操作，包括添加、删除和编辑用户
 */
public class UserOperations {

    // 主窗口引用
    private final MainFrame mainFrame;

    public UserOperations(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * 显示添加用户对话框
     */
    void addUser() {
        NewUserDialog.display();
    }

    /**
     * 删除选中的用户
     * 显示确认对话框，确认后从数据库中删除用户
     */
    void deleteUser() {
        JTable userList = mainFrame.getUserList();
        int selectedRow = userList.getSelectedRow();
        if (selectedRow != -1) {
            int response = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Are you sure you want to delete this user?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                String deletedUserName = (String) userList.getValueAt(selectedRow, 0);
                try {
                    DataProcessing.deleteUser(deletedUserName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ((DefaultTableModel) userList.getModel()).removeRow(selectedRow);
            }
        }
    }

    /**
     * 编辑选中的用户
     * 显示编辑用户对话框
     */
    void editUser() {
        JTable userList = mainFrame.getUserList();
        int selectedRow = userList.getSelectedRow();
        if (selectedRow != -1) {
            String username = (String) userList.getValueAt(selectedRow, 0);
            String role = (String) userList.getValueAt(selectedRow, 1);
            if (mainFrame.getCurrentUser().getName().equals(username)) {
                EditUserDialog.display(role, username, 1);
            } else {
                EditUserDialog.display(role, username);
            }
        }
    }

    /**
     * 搜索用户
     * 显示搜索用户对话框
     */
    void searchUser() {
        SearchUserDialog.display(mainFrame);
    }
}
