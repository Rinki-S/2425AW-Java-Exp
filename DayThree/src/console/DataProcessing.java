package console;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.sql.*;

/**
 * TODO 数据处理类
 *
 * @author gongjing
 * {@code @date} 2016/10/13
 */
public class DataProcessing {
    // Database connection status (will implement later)
    private static boolean connectToDB = false;

    static Hashtable<String, AbstractUser> users;
    static Hashtable<String, Doc> docs;

    enum ROLE_ENUM {
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
     * TODO 初始化，连接数据库
     */
    public static void init() {
        connectToDB = true;

        // Create a file first to make sure that the program can still run when there's no such file
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
     * TODO 按档案编号搜索档案信息，返回null时表明未找到
     *
     * @return Doc
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
     * TODO 列出所有档案信息
     *
     * @return Enumeration<Doc>
     */
    public static Enumeration<Doc> listDoc() throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }

        return docs.elements();
    }

    /**
     * TODO 插入新的档案
     *
     * @return boolean
     */
    public static boolean insertDoc(String id, String creator, Timestamp timestamp, String description, String filename) throws SQLException {
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
     * TODO 按用户名搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name 用户名
     * @return AbstractUser
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
     * TODO 按用户名、密码搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name     用户名
     * @param password 密码
     * @return AbstractUser
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
     * TODO 修改用户信息
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return boolean
     */
    public static boolean updateUser(String name, String password, String role) throws SQLException {
        if (users.containsKey(name)) {
            return getRole(name, password, role);
        } else {
            return false;
        }
    }

    /**
     * TODO 插入新用户
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return boolean
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
