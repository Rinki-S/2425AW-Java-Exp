package console;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Operator extends User {
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
                    this.downloadFile("foo");
                    break;
                }
                case 3: {
                    this.showFileList();
                    break;
                }
                case 4: {
                    ModifySelf(scanner);
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

    private void ModifySelf(Scanner scanner) {
        System.out.println("请输入新密码: ");
        String password = scanner.nextLine();
        this.changeSelfInfo(password);
    }
}
