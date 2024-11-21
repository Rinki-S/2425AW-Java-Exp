package console;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author Xinyuan Song
 */
public class Administrator extends AbstractUser {
    public Administrator(String name, String password, String role) {
        super(name, password, role);
    }

    private static void outputUsers() {
        Enumeration<AbstractUser> userEnumeration = null;
        try {
            userEnumeration = DataProcessing.listUser();
        } catch (SQLException e) {
            System.out.println("获取用户列表时发生错误: " + e.getMessage());
        }
        System.out.printf("%-15s %-15s %-15s%n", "username", "password", "role");
        if (userEnumeration != null) {
            while (userEnumeration.hasMoreElements()) {
                AbstractUser user = userEnumeration.nextElement();
                System.out.printf("%-15s %-15s %-15s%n", user.getName(), user.getPassword(), user.getRole());
            }
        }
    }

    private static void modifyUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        AbstractUser user = null;
        try {
            user = DataProcessing.searchUser(username);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
            try {
                if (DataProcessing.updateUser(username, newPassword, newRole)) {
                    System.out.println("用户信息修改成功");
                } else {
                    System.out.println("用户信息修改失败");
                }
            } catch (SQLException e) {
                System.out.println("修改用户信息时发生错误: " + e.getMessage());
            }
        } else {
            System.out.println("找不到该用户");
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        try {
            if (DataProcessing.deleteUser(username)) {
                System.out.println("成功删除");
            } else {
                System.out.println("找不到该用户");
            }
        } catch (SQLException e) {
            System.out.println("删除用户时发生错误: " + e.getMessage());
        }
    }

    private static void addUser(Scanner scanner) {
        System.out.println("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.println("请输入密码: ");
        String password = scanner.nextLine();
        System.out.println("请选择角色: 1. 系统管理员 2. 档案录入人员 3. 档案浏览人员");
        int roleNum = scanner.nextInt();
        scanner.nextLine();
        switch (roleNum) {
            case 1:
                try {
                    if (DataProcessing.insertUser(username, password, "administrator")) {
                        System.out.println("成功增加用户");
                    } else {
                        System.out.println("增加失败");
                    }
                } catch (SQLException e) {
                    System.out.println("增加用户时发生错误: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    if (DataProcessing.insertUser(username, password, "operator")) {
                        System.out.println("成功增加用户");
                    } else {
                        System.out.println("增加失败");
                    }
                } catch (SQLException e) {
                    System.out.println("增加用户时发生错误: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    if (DataProcessing.insertUser(username, password, "browser")) {
                        System.out.println("成功增加用户");
                    } else {
                        System.out.println("增加失败");
                    }
                } catch (SQLException e) {
                    System.out.println("增加用户时发生错误: " + e.getMessage());
                }
                break;
            default:
                System.out.println("没有该角色");
                break;
        }
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
                    addUser(scanner);
                    break;
                }
                case 2: {
                    deleteUser(scanner);
                    break;
                }
                case 3: {
                    modifyUser(scanner);
                    break;
                }
                case 4: {
                    outputUsers();
                    break;
                }
                case 5: {
                    try {
                        String id;
                        System.out.println("请输入要下载的档案的编号: ");
                        id = scanner.nextLine();
                        if (DataProcessing.searchDoc(id) != null) {
                            this.downloadFile(id);
                        } else {
                            System.out.println("没有您要找的文件");
                        }
                    } catch (IOException | SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 6: {
                    try {
                        this.showFileList();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 7: {
                    modifySelf(scanner);
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

    private void modifySelf(Scanner scanner) {
        System.out.println("请输入新密码: ");
        String password = scanner.nextLine();
        try {
            this.changeSelfInfo(password);
        } catch (SQLException e) {
            System.out.println("更改信息时发生错误: " + e.getMessage());
        }
    }
}
