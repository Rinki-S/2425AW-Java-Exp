package gui;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;

public class DownloadDocDialog extends JDialog {
    private static final String REPOSITORY_PATH = "./DayFour/src/console/download/"; // 文件仓库路径
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton selectDirButton;
    private JTextField pathField;
    private File saveDir;
    private String filename;

    public DownloadDocDialog(String filename) {
        this.filename = filename;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        selectDirButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                saveDir = fileChooser.getSelectedFile();
                pathField.setText(saveDir.getAbsolutePath());
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
        if (saveDir == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a directory to save the file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 构建源文件和目标文件路径
            File sourceFile = new File(REPOSITORY_PATH, filename);
            File targetFile = new File(saveDir, filename);

            if (!sourceFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "The source file does not exist: " + filename,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 使用文件流复制文件
            try (FileInputStream in = new FileInputStream(sourceFile);
                    FileOutputStream out = new FileOutputStream(targetFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Download successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error downloading file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void display(String filename) {
        DownloadDocDialog dialog = new DownloadDocDialog(filename);
        dialog.pack();
        dialog.setVisible(true);
    }
}
