package gui;

import console.DataProcessing;
import console.Doc;
import java.awt.event.*;
import java.sql.Timestamp;
import java.io.File;
import java.sql.SQLException;
import javax.swing.*;
import java.io.IOException;

public class UploadDocDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel filePanel;
    private JButton selectFileButton;
    private JTextField filePathField;
    private JTextField idField;
    private JTextField descriptionField;
    private JTextField creatorField;
    private File selectedFile;
    private Doc doc;

    public UploadDocDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        selectFileButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
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

    private void onOK() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a file first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建文档对象
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        doc = new Doc(idField.getText(),
                creatorField.getText(),
                currentTimestamp,
                descriptionField.getText(),
                selectedFile.getName());

        try {
            // 先写入文件
            DataProcessing.uploadFile(selectedFile);
            // 再插入数据库记录
            DataProcessing.insertDoc(doc.getId(),
                    doc.getCreator(),
                    doc.getTimestamp(),
                    doc.getDescription(),
                    doc.getFilename());
            dispose();
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error uploading file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void onCancel() {
        UploadDocDialog dialog = new UploadDocDialog();
        dialog.pack();
        dialog.setVisible(true);
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public Doc getDoc() {
        return doc;
    }

    public static void display() {
        UploadDocDialog dialog = new UploadDocDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
