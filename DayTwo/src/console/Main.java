package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Xinyuan Song
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("--- 欢迎使用档案管理系统 ---");
            System.out.println("1. 登录");
            System.out.println("2. 退出");
            System.out.println("请选择功能: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入一个整数。");
                scanner.next();
                choice = -1;
            }
            if (choice == 1) {
                System.out.println("请输入用户名: ");
                String username = scanner.nextLine();
                System.out.println("请输入密码: ");
                String password = scanner.nextLine();
                AbstractUser user = null;
                try {
                    user = DataProcessing.searchUser(username, password);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                if (user != null) {
                    try {
                        user.showMenu();
                    } catch (SQLException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("找不到对应的用户");
                    choice = -1;
                }
            } else if (choice != 2) {
                System.out.println("输入的功能序号无效");
                choice = -1;
            }
        } while (choice != 2);
        System.out.println("再见");
        System.exit(0);
    }
}