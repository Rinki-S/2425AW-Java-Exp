package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Xinyuan Song
 */
public class Browser extends AbstractUser {
    public Browser(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("--- 档案浏览人员功能目录 ---");
            System.out.println("1. 下载档案");
            System.out.println("2. 档案列表");
            System.out.println("3. 修改个人密码");
            System.out.println("4. 退出登录");
            System.out.println("请选择功能: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入一个整数。");
                scanner.next();
                choice = -1;
            }
            switch (choice) {
                case 1: {
                    try {
                        this.downloadFile("foo");
                    } catch (IOException | SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2: {
                    try {
                        this.showFileList();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: {
                    modifySelf(scanner);
                    break;
                }
                default: {
                    if (choice != 4) {
                        System.out.println("功能选择无效");
                        choice = -1;
                    }
                    break;
                }
            }
        } while (choice != 4);
    }

    private void modifySelf(Scanner scanner) {
        System.out.println("请输入新密码: ");
        String password = scanner.nextLine();
        try {
            this.changeSelfInfo(password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
