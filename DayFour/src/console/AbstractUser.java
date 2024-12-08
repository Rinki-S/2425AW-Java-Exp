package console;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * TODO 抽象用户类，为各用户子类提供模板
 *
 * @author gongjing
 *         &#064;date 2016/10/13
 */
public abstract class AbstractUser implements Serializable {
    private String name;
    private String password;
    private String role;

    String uploadPath = "./DayThree/src/console/upload/";
    String downloadPath = "./DayThree/src/console/download/";

    AbstractUser(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    /**
     * TODO 修改用户自身信息
     *
     * @param password 口令
     * @return boolean 修改是否成功
     */
    public boolean changeSelfInfo(String password) throws SQLException {
        if (DataProcessing.updateUser(name, password, role)) {
            this.password = password;
            System.out.println("修改成功");
            return true;
        } else {
            return false;
        }
    }

    /**
     * TODO 下载档案文件
     *
     * @param id 档案编号
     * @return boolean 下载是否成功
     */
    public boolean downloadFile(String id) throws SQLException, IOException {
        byte[] buffer = new byte[1024];
        Doc doc = DataProcessing.searchDoc(id);

        if (doc == null) {
            System.out.println("No such doc");
            return false;
        }

        File tempFile = new File(uploadPath + doc.getFilename());
        String filename = tempFile.getName();

        BufferedInputStream infile = new BufferedInputStream(new FileInputStream(tempFile));
        BufferedOutputStream targetfile = new BufferedOutputStream(new FileOutputStream(downloadPath + filename));

        while (true) {
            int byteRead = infile.read(buffer);
            if (byteRead == -1) {
                break;
            }
            targetfile.write(buffer, 0, byteRead);
        }
        infile.close();
        targetfile.close();
        System.out.println("Download complete");

        return true;
    }

    public boolean uploadFile() throws IOException {
        byte[] buffer = new byte[1024];
        File uploadDir = new File(uploadPath);
        File[] files = uploadDir.listFiles();
        if (files != null) {
            int index = 1;
            for (File file : files) {
                System.out.println(index + " File: " + file.getName());
                index++;
            }
        }
        int fileIndex;
        System.out.println("请输入你需要上传的文件的序号: ");
        Scanner scanner = new Scanner(System.in);
        fileIndex = scanner.nextInt();
        scanner.nextLine();
        String fileName = null;
        if (files != null) {
            fileName = files[fileIndex - 1].getName();
        }
        File uploadFile = new File(uploadPath + fileName);
        System.out.println("请输入你需要上传的文件的作者: ");
        String creator = scanner.nextLine();
        System.out.println("请输入你需要上传的文件的描述: ");
        String description = scanner.nextLine();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            DataProcessing.insertDoc("000" + fileIndex, creator, currentTimestamp, description, fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        BufferedInputStream infile;
        infile = new BufferedInputStream(new FileInputStream(uploadFile));
        BufferedOutputStream targetfile = new BufferedOutputStream(new FileOutputStream(downloadPath + fileName));

        while (true) {
            int byteRead = infile.read(buffer);
            if (byteRead == -1) {
                break;
            }
            targetfile.write(buffer, 0, byteRead);
        }
        infile.close();
        targetfile.close();
        System.out.println("Upload complete");
        return true;
    }

    /**
     * TODO 展示档案文件列表
     */
    public void showFileList() throws SQLException {
        Enumeration<Doc> e = DataProcessing.listDoc();
        Doc doc;
        while (e.hasMoreElements()) {
            doc = e.nextElement();
            System.out.println("Id:" + doc.getId() + "\t Creator:" + doc.getCreator() + "\t Time:" + doc.getTimestamp()
                    + "\t Filename:" + doc.getFilename());
            System.out.println("Description:" + doc.getDescription());
        }

    }

    /**
     * TODO 展示菜单，需子类加以覆盖
     */
    public abstract void showMenu();

    /**
     * TODO 退出系统
     */
    public void exitSystem() {
        System.out.println("系统退出, 谢谢使用 ! ");
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AbstractUser{" +
                "downloadPath='" + downloadPath + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", uploadPath='" + uploadPath + '\'' +
                '}';
    }
}
