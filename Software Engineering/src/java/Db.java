package com.qst;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class Db {
  private static final String url =
      "jdbc:mysql://124.70.74.77:3306/mbti?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true";
  private static final String userName = "rankD";
  private static final String password = "12345678";

  static {
    try {
      com.mysql.cj.jdbc.Driver.class.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() throws SQLException {
    Connection conn = DriverManager.getConnection(url, userName, password);
    conn.setAutoCommit(true);
    return conn;
  }

  public static int getGeneratedInt(Statement stmt) throws SQLException {
    ResultSet rs = stmt.getGeneratedKeys();
    rs.next();
    return rs.getInt(1);
  }
}
