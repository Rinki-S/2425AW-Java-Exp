package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Xinyuan Song
 */
public class Operator extends AbstractUser {
    public Operator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("--- 档案录入人员功能目录 ---");
            System.out.println("1. 上传档案");
            System.out.println("2. 下载档案");
            System.out.println("3. 档案列表");
            System.out.println("4. 修改个人密码");
            System.out.println("5. 退出登录");
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
                    System.out.println("上传档案...");
                    break;
                }
                case 2: {
                    try {
                        this.downloadFile("foo");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3: {
                    try {
                        this.showFileList();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4: {
                    modifySelf(scanner);
                    break;
                }
                default: {
                    if (choice != 5) {
                        System.out.println("功能选择无效");
                        choice = -1;
                    }
                    break;
                }
            }
        } while (choice != 5);
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
