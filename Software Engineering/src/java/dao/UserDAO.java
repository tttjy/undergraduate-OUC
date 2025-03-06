package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
  public List<User> findAll() {
    String sql = "select id,login,name,passwd,type,status from users where type<4";
    List<User> users = new ArrayList<>();
    try (Connection conn = Db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        users.add(createUser(rs));
      }
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return users;
  }

  public User findByLogin(String login) {
    String sql = "select id,login,name,passwd,type,status from users where login=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, login);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return createUser(rs);
      }
      return null;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public static User findById(int id) {
    String sql = "select id,login,name,passwd,type,status from users where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return createUser(rs);
      }
      return null;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public int insert(User u) {
    // SQL插入语句，将用户信息插入到"users"表中
    String sql = "insert into users(login,passwd,name,type,status) values(?,?,?,?,?)";
    try (Connection conn = Db.getConnection(); // 获取数据库连接
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      // 设置SQL语句中的参数
      stmt.setObject(1, u.getLogin()); // 设置登录名
      stmt.setObject(2, u.getPasswd()); // 设置密码
      stmt.setObject(3, u.getName()); // 设置姓名
      stmt.setObject(4, u.getType()); // 设置用户类型
      stmt.setObject(5, u.getStatus()); // 设置用户状态

      stmt.executeUpdate(); // 执行SQL插入操作

      // 获取生成的自增主键（用户ID）并将其设置到用户对象中
      u.setId(Db.getGeneratedInt(stmt));

      return u.getId(); // 返回插入的用户ID
    } catch (SQLException ex) {
      // 捕获SQL异常，抛出自定义ExamException异常，携带SQL语句和异常信息
      throw new ExamException(sql, ex);
    }
  }

  public void update(User u) {
    String sql = "update users set login=?,name=?,type=?,status=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, u.getLogin());
      stmt.setObject(2, u.getName());
      stmt.setObject(3, u.getType());
      stmt.setObject(4, u.getStatus());
      stmt.setObject(5, u.getId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public static void updatePassword(int userId, String password) {
    String sql = "update users set passwd=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, password);
      stmt.setObject(2, userId);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void delete(int userId) {
    String sql = "delete from users where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, userId);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void updateLastLogin(Integer userId, Timestamp lastLogin) {
    String sql = "update users set last_login=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, lastLogin);
      stmt.setObject(2, userId);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public static User createUser(ResultSet rs) throws SQLException {
    return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
        rs.getInt(6));
  }
}
