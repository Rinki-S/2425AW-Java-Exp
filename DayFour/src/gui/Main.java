package gui;

import console.*;
import javax.swing.*;

/**
 * 程序入口类
 * 初始化UI和数据，启动登录窗口
 */
public class Main {
    /**
     * 主方法
     * 设置UI风格，初始化数据，显示登录窗口
     */
    public static void main(String[] args) {
        // 设置自定义UI
        customUI.customUISetup();
        // 初始化数据处理
        DataProcessing.init();
        // 启动登录窗口
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
