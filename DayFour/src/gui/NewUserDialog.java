public class newUserDialog extends javax.swing.JDialog {
private javax.swing.JPanel contentPane;
private javax.swing.JButton buttonOK;
private javax.swing.JButton buttonCancel;

public newUserDialog(){
setContentPane(contentPane);
setModal(true);
getRootPane().setDefaultButton(buttonOK);

buttonOK.addActionListener(new java.awt.event.ActionListener(){public void actionPerformed(java.awt.event.ActionEvent e){onOK();}});

buttonCancel.addActionListener(new java.awt.event.ActionListener(){public void actionPerformed(java.awt.event.ActionEvent e){onCancel();}});

 // 点击 X 时调用 onCancel()
setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
addWindowListener(new java.awt.event.WindowAdapter() {
  public void windowClosing(java.awt.event.WindowEvent e) {
   onCancel();
  }
});

 // 遇到 ESCAPE 时调用 onCancel()
contentPane.registerKeyboardAction(  new java.awt.event.ActionListener() {    public void actionPerformed(java.awt.event.ActionEvent e) {      onCancel();
    }  },  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),  javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);}

private void onOK(){
 // 在此处添加您的代码
dispose();
}

private void onCancel(){
 // 必要时在此处添加您的代码
dispose();
}

public static void main(String[] args){
newUserDialog dialog = new newUserDialog();
dialog.pack();
dialog.setVisible(true);
System.exit(0);
}
}
