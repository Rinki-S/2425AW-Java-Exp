package console;

import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Administrator extends User {
    public Administrator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("--- 系统管理员功能目录 ---");
            System.out.println("1. 新增用户");
            System.out.println("2. 删除用户");
            System.out.println("3. 修改用户");
            System.out.println("4. 用户列表");
            System.out.println("5. 下载档案");
            System.out.println("6. 档案列表");
            System.out.println("7. 修改个人密码");
            System.out.println("8. 退出登录");
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
                    AddUser(scanner);
                    break;
                }
                case 2: {
                    DeleteUser(scanner);
                    break;
                }
                case 3: {
                    ModifyUser(scanner);
                    break;
                }
                case 4: {
                    OutputUsers();
                    break;
                }
                case 5: {
                    this.downloadFile("foo");
                    break;
                }
                case 6: {
                    this.showFileList();
                    break;
                }
                case 7: {
                    ModifySelf(scanner);
                    break;
                }
                default: {
                    if (choice != 8) {
                        System.out.println("功能选择无效");
                        choice = -1;
                    }
                    break;
                }
            }
        } while (choice != 8);
    }

    private void ModifySelf(Scanner scanner) {
        System.out.println("请输入新密码: ");
        String password = scanner.nextLine();
        this.changeSelfInfo(password);
    }

    private static void OutputUsers() {
        Enumeration<User> userEnumeration = DataProcessing.getAllUser();
        System.out.printf("%-15s %-15s %-15s%n", "username", "password", "role");
        while (userEnumeration.hasMoreElements()) {
            User user = userEnumeration.nextElement();
            System.out.printf("%-15s %-15s %-15s%n", user.getName(), user.getPassword(), user.getRole());
        }
    }

    private static void ModifyUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        User user = DataProcessing.searchUser(username);
        if (user != null) {
            System.out.println("请输入新密码: ");
            String newPassword = scanner.nextLine();
            System.out.println("请选择新角色: 1. 系统管理员 2. 档案录入人员 3. 档案浏览人员");
            int newRoleNum = scanner.nextInt();
            scanner.nextLine();
            String newRole = "browser";
            switch (newRoleNum) {
                case 1:
                    newRole = "administrator";
                    break;
                case 2:
                    newRole = "operator";
                    break;
                case 3:
                    newRole = "browser";
                    break;
                default:
                    System.out.println("没有该角色");
                    break;
            }
            if (DataProcessing.update(username, newPassword, newRole)) {
                System.out.println("用户信息修改成功");
            } else {
                System.out.println("用户信息修改失败");
            }
        } else {
            System.out.println("找不到该用户");
        }
    }

    private static void DeleteUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        if (DataProcessing.delete(username))
            System.out.println("成功删除");
        else
            System.out.println("找不到该用户");
    }

    private static void AddUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.println("请输入密码: ");
        String password = scanner.nextLine();
        System.out.println("请选择角色: 1. 系统管理员 2. 档案录入人员 3. 档案浏览人员");
        int roleNum = scanner.nextInt();
        scanner.nextLine();
        switch (roleNum) {
            case 1:
                if (DataProcessing.insert(username, password, "administrator"))
                    System.out.println("成功增加用户");
                else
                    System.out.println("增加失败");
                break;
            case 2:
                if (DataProcessing.insert(username, password, "operator"))
                    System.out.println("成功增加用户");
                else
                    System.out.println("增加失败");
                break;
            case 3:
                if (DataProcessing.insert(username, password, "browser"))
                    System.out.println("成功增加用户");
                else
                    System.out.println("增加失败");
                break;
            default:
                System.out.println("没有该角色");
                break;
        }
    }
}
