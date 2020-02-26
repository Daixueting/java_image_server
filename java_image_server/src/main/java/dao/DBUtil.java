package dao;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @PACKAGE_NAME: dao
 * @NAME: DBUtil
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/2/16
 **/
public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/java_image_server?characterEncoding=utf8&useSSL=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "daixueting52o99";

    private static volatile DataSource dataSource=null;

    public static DataSource getDataSource() {  //获取一个数据源用来连接
        if (dataSource==null){
            synchronized (DBUtil.class){
                if (dataSource==null){
                    dataSource = new MysqlDataSource();
                    MysqlDataSource tmpDataSource = (MysqlDataSource) dataSource;
                    tmpDataSource.setURL(URL);
                    tmpDataSource.setUser(USERNAME);
                    tmpDataSource.setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

        public static Connection getConnection() {
            try {
                return getDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    public static void close(Connection connection, Statement statement,ResultSet resultSet){
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

