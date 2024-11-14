package console;

import java.util.*;
import java.sql.*;

/**
 * TODO 数据处理类
 *
 * @author gongjing
 * &#064;date  2016/10/13
 */
public class DataProcessing {
    static final double EXCEPTION_CONNECT_PROBABILITY = 0.1;
    static final double EXCEPTION_SQL_PROBABILITY = 0.9;
    static Hashtable<String, AbstractUser> users;
    private static boolean connectToDB = false;

    static {
        users = new Hashtable<>();
        users.put("jack", new Operator("jack", "123", "operator"));
        users.put("rose", new Browser("rose", "123", "browser"));
        users.put("kate", new Administrator("kate", "123", "administrator"));
        init();
    }

    /**
     * TODO 初始化，连接数据库
     */
    public static void init() {
// update database connection status
        double ranValue = Math.random();
        connectToDB = ranValue > EXCEPTION_CONNECT_PROBABILITY;
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
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Query");
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
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Query");
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
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Query");
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
        AbstractUser user;
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Update");
        }
        if (users.containsKey(name)) {
            return getRole(name, password, role);
        } else {
            return false;
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
     * TODO 插入新用户
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return boolean
     * @throws SQLException
     */
    public static boolean insertUser(String name, String password, String role) throws SQLException {
        AbstractUser user;
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Insert");
        }
        if (users.containsKey(name)) {
            return false;
        } else {
            return getRole(name, password, role);
        }
    }

    /**
     * TODO 删除指定用户
     *
     * @param name 用户名
     * @return boolean
     * @throws SQLException
     */
    public static boolean deleteUser(String name) throws SQLException {
        if (!connectToDB) {
            throw new SQLException("Not Connected to Database");
        }
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_SQL_PROBABILITY) {
            throw new SQLException("Error in excecuting Delete");
        }
        if (users.containsKey(name)) {
            users.remove(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * TODO 关闭数据库连接
     *
     * @param
     * @return void
     * @throws
     */
    public static void disconnectFromDataBase() {
        if (connectToDB) {
// close Statement and Connection
            try {
                if (Math.random() > EXCEPTION_SQL_PROBABILITY) {
                    throw new SQLException("Error in disconnecting DB");
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } finally {
                connectToDB = false;
            }
        }
    }

    static enum ROLE_ENUM {
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

        private String role;

        ROLE_ENUM(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

}