package console;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 数据处理类
 * 负责用户和文档数据的持久化存储和管理
 * 提供用户认证、文档管理等功能
 */
public class DataProcessing {
    // 数据库连接状态（后续实现）
    private static boolean connectToDB = false;

    // 用户和文档的内存存储
    static Hashtable<String, AbstractUser> users; // 用户信息哈希表
    static Hashtable<String, Doc> docs; // 文档信息哈希表

    static String uploadPath = "./DayFour/src/console/upload/";
    static String downloadPath = "./DayFour/src/console/download/";

    public enum ROLE_ENUM {
        /**
         * administrator
         */
        administrator("administrator"),
        /**
         * operator
         */
        operator("operator"),
        /**
         * browser
         */
        browser("browser");

        private final String role;

        ROLE_ENUM(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    static {
        init();
    }

    /**
     * 系统初始化方法
     * 负责连接数据库和加载序列化数据
     */
    public static void init() {
        connectToDB = true;

        // Create a file first to make sure that the program can still run when there's
        // no such file
        // Program will crash at first run if not do so
        File userFile = new File("user.ser");
        if (userFile.exists()) {
            // Read data from file if there is one
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
                users = (Hashtable<String, AbstractUser>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                users = new Hashtable<>();
            }
        } else {
            // Add template data to the file if there isn't one
            users = new Hashtable<>();
            users.put("rose", new Browser("rose", "123", "browser"));
            users.put("jack", new Operator("jack", "123", "operator"));
            users.put("kate", new Administrator("kate", "123", "administrator"));
            saveData();
        }

        File docsFile = new File("docs.ser");
        if (docsFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("docs.ser"))) {
                docs = (Hashtable<String, Doc>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                docs = new Hashtable<>();
            }
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            docs = new Hashtable<>();
            docs.put("0001", new Doc("0001", "jack", timestamp, "Doc Source Java", "Doc.txt"));
            saveData();
        }
    }

    /**
     * 数据保存方法
     * 将用户和文档数据序列化到文件
     */
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("docs.ser"))) {
            oos.writeObject(docs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索文档
     *
     * @param id 文档编号
     * @return 返回查找到的文档对象，未找到返回null
     */
    public static Doc searchDoc(String id) throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }
        if (docs.containsKey(id)) {
            return docs.get(id);
        }
        return null;
    }

    /**
     * 列出所有文档
     *
     * @return 返回所有文档的枚举器
     */
    public static Enumeration<Doc> listDoc() throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        return docs.elements();
    }

    /**
     * 插入新文档
     *
     * @param id          文档编号
     * @param creator     创建者
     * @param timestamp   创建时间
     * @param description 文档描述
     * @param filename    文件名
     * @return 插入成功返回true，失败返回false
     */
    public static boolean insertDoc(String id, String creator, Timestamp timestamp, String description, String filename)
            throws SQLException {
        Doc doc;

        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        if (docs.containsKey(id)) {
            return false;
        } else {
            doc = new Doc(id, creator, timestamp, description, filename);
            docs.put(id, doc);
            return true;
        }
    }

    /**
     * 下载文档文档
     *
     * @param targetFile   文档编号
     * @param downloadPath 下载路径
     * @return 下载是否成功
     */
    public static boolean downloadFile(File targetFile, String downloadPath)
            throws SQLException, IOException {
        byte[] buffer = new byte[1024];
        File tempFile = targetFile;
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

    /**
     * 上传文档
     *
     * @param uploadFile 文件名
     * @return 上传是否成功
     */
    public static boolean uploadFile(File uploadFile) throws IOException {
        byte[] buffer = new byte[1024];
        BufferedInputStream infile = new BufferedInputStream(new FileInputStream(uploadFile));
        BufferedOutputStream targetfile = new BufferedOutputStream(
                new FileOutputStream(downloadPath + uploadFile.getName()));

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
     * 搜索用户
     *
     * @param name 用户名
     * @return 返回查找到的用户对象，未找到返回null
     */
    public static AbstractUser searchUser(String name) throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        if (users.containsKey(name)) {
            return users.get(name);
        }
        return null;
    }

    /**
     * 用户登录验证
     *
     * @param name     用户名
     * @param password 密码
     * @return 验证成功返回用户对象，失败返回null
     */
    public static AbstractUser searchUser(String name, String password) throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        if (users.containsKey(name)) {
            AbstractUser temp = users.get(name);
            if ((temp.getPassword()).equals(password)) {
                return temp;
            }
        }
        return null;
    }

    /**
     * TODO 取出所有的用户
     *
     * @return Enumeration<AbstractUser>
     */
    public static Enumeration<AbstractUser> listUser() throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        return users.elements();
    }

    /**
     * 修改用户信息
     *
     * @param name     用户名
     * @param password 新密码
     * @param role     新角色
     * @return 修改成功返回true，失败返回false
     */
    public static boolean updateUser(String name, String password, String role) throws SQLException {
        if (users.containsKey(name)) {
            return getRole(name, password, role);
        } else {
            return false;
        }
    }

    /**
     * 插入新用户
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return 插入成功返回true，失败返回false
     */
    public static boolean insertUser(String name, String password, String role) throws SQLException {
        if (users.containsKey(name)) {
            return false;
        } else {
            return getRole(name, password, role);
        }
    }

    private static boolean getRole(String name, String password, String role) {
        AbstractUser user;
        user = switch (ROLE_ENUM.valueOf(role.toLowerCase())) {
            case administrator -> new Administrator(name, password, role);
            case operator -> new Operator(name, password, role);
            default -> new Browser(name, password, role);
        };
        users.put(name, user);
        return true;
    }

    /**
     * TODO 删除指定用户
     *
     * @param name 用户名
     * @return boolean
     */
    public static boolean deleteUser(String name) throws SQLException {
        if (users.containsKey(name)) {
            users.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
    }

}
