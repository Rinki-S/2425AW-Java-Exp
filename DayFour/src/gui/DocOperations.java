package gui;

import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * 文档操作类
 * 处理文档管理相关的操作，包括上传和下载文档
 */

public class DocOperations {
    private final MainFrame mainFrame;

    public DocOperations(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public static void uploadDoc() {
        UploadDocDialog uploadDocDialog = new UploadDocDialog();
        uploadDocDialog.display();
    }

    public void downloadDoc() {
        JTable docList = mainFrame.getDocumentList();
        int selectedRow = docList.getSelectedRow();
        if (selectedRow != -1) {
            // 获取选中文件的文件名
            String filename = (String) docList.getValueAt(selectedRow, 4); // 假设文件名在第5列
            DownloadDocDialog downloadDocDialog = new DownloadDocDialog(filename);
            downloadDocDialog.display(filename);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Please select a document to download.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
