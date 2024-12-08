package gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import java.awt.*;

/**
 * UI定制工具类
 * 提供UI样式设置和macOS特定的界面调整
 */
public class customUI {
    /**
     * 设置全局UI样式
     * 配置macOS菜单栏、字体和界面外观
     */
    public static void customUISetup() {
        if (SystemInfo.isMacOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "UMS");
            System.setProperty("apple.awt.application.appearance", "system");
        }

        FlatMacLightLaf.setup();
        UIManager.put("TextComponent.arc", 10);

        Font defaultFont = new Font("IBM Plex Sans", Font.PLAIN, 14);
        UIManager.put("defaultFont", defaultFont);
    }

    /**
     * macOS平台特定的窗口调整
     * 设置透明标题栏和全屏支持
     */
    public static void macOSAdjustments(JFrame frame) {
        if (SystemInfo.isMacOS) {
            if (SystemInfo.isMacFullWindowContentSupported) {
                frame.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);

                // hide window title
                if (SystemInfo.isJava_17_orLater)
                    frame.getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
                else
                    frame.setTitle(null);
            }

            // enable full screen mode for this window (for Java 8 - 10; not necessary for
            // Java 11+)
            if (!SystemInfo.isJava_11_orLater)
                frame.getRootPane().putClientProperty("apple.awt.fullscreenable", true);
        }
    }
}
