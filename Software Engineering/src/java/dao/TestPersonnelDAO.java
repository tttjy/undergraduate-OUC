package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestPersonnelDAO {
  private static final String select =
      "select users.id as id,login,name,passwd,type,status,last_login,phone,gender,email,birthdate,team_id from users join testPersonnel on users.id=testPersonnel.id ";
  // 这是一个SQL查询语句，用于从数据库中检索测试人员的信息。它联接了两个表（users 和
  // testPersonnel）并选择了多个字段。这个查询语句使用了别名来重命名一些字段，例如 users.id as id。
  /*
kk
   */

  // 根据给定的团队ID从数据库中查询相关的测试人员信息，并将这些信息以列表的形式返回。
  public List<TestPersonnel> findByTeam(int id) throws SQLException {
    String sql = select; //// 创建SQL查询字符串，初始化为预定义的查询语句
    if (id > 0)
      sql +=
          "where team_id=?"; // 如果提供的团队ID大于0，向SQL查询字符串添加where子句，用于过滤出与指定团队ID相关的测试人员。
    try (Connection connection = Db.getConnection(); //// 获取数据库连接
         PreparedStatement stmt =
             connection.prepareStatement(sql)) { //// 准备SQL语句并与数据库连接关联
      if (id > 0)
        stmt.setObject(1, id); //// 这里将团队ID绑定到SQL查询中的第一个占位符
      List<TestPersonnel> users = new ArrayList<>(); //// 用于存储从数据库中查询到的测试人员对象。
      ResultSet rs = stmt.executeQuery(); //// 执行SQL查询并获取结果集。
      while (rs.next()) {
        users.add(create(rs));
      }
      rs.close();
      return users; //// 返回测试人员列表，其中包含了与指定班级相关的测试人员信息
    }
  }

  public List<TestPersonnel> query(int teamId, String name, String stdNo) throws SQLException {
    // 创建SQL查询语句，从select中选择相应列，初始化查询条件为班级ID
    String sql = select + "where team_id=?";
    // 如果姓名不为空，追加条件到SQL语句中
    if (!"".equals(name))
      sql += " and name=?";
    // 如果学号不为空，追加条件到SQL语句中
    if (!"".equals(stdNo))
      sql += " and phone=?";

    try (Connection connection = Db.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      // 设置第一个参数（班级ID）的值
      stmt.setInt(1, teamId);
      // 如果姓名不为空，设置第二个参数（姓名）的值
      if (!"".equals(name))
        stmt.setObject(2, name);
      // 如果学号不为空，设置第二个参数（学号）的值
      else if (!"".equals(stdNo))
        stmt.setObject(2, stdNo);
      // 如果同时有姓名和学号，设置第三个参数（学号）的值
      if (!"".equals(name) && !"".equals(stdNo))
        stmt.setObject(3, stdNo);
      // 111es
      //  创建用于存储结果的测试人员列表
      List<TestPersonnel> users = new ArrayList<>();
      // 执行SQL查询语句
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        // 从结果集中获取数据并创建测试人员对象，然后添加到列表中
        users.add(create(rs));
      }
      // 关闭结果集
      rs.close();
      return users; // 返回包含查询结果的测试人员列表
    } catch (SQLException ex) {
      System.out.print(ex.getMessage());
      // 如果发生异常，打印错误信息并抛出自定义异常
      throw new ExamException("不存在该学生");
    }
  }

  private TestPersonnel create(ResultSet rs) throws SQLException {
    User u = new User();
    u.setId(rs.getInt("id"));
    u.setLogin(rs.getString("login"));
    u.setName(rs.getString("name"));
    u.setPasswd(rs.getString("passwd"));
    u.setType(rs.getInt("type"));
    u.setStatus(rs.getInt("status"));
    TestPersonnel testPersonnel = new TestPersonnel();
    testPersonnel.setId(u.getId());
    testPersonnel.setPhone(rs.getString("phone"));
    testPersonnel.setGender(rs.getString("gender"));
    testPersonnel.setBirthDate(rs.getDate("birthdate"));
    testPersonnel.setTeamId(rs.getInt("team_id"));
    testPersonnel.setUser(u);
    return testPersonnel;
  }

  /**
   * @param id 学生id
   * @return 指定id的学生
   * @throws SQLException
   */
  public TestPersonnel findById(int id) throws SQLException {
    String sql = select + "where testPersonnel.id=?";
    TestPersonnel testPersonnel = null;
    try (Connection connection = Db.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        testPersonnel = create(rs);
      }
      rs.close();
    }
    return testPersonnel;
  }

  // 定义一个公开的方法，名为update，接受一个TestPersonnel类型的参数s，并且在执行过程中可能会抛出SQLException
  public void update(TestPersonnel s) throws SQLException {
    // 定义一个字符串sql，它是一个SQL更新语句，用于更新testPersonnel表中的数据
    String sql = "update testPersonnel set phone=?,gender=?,birthdate=?  where id=?";

    // 使用Java
    // 7引入的try-with-resources语句，通过该语句可以自动关闭实现了AutoCloseable接口的资源（如Connection，PreparedStatement等）
    try (Connection connection = Db.getConnection();
         // 创建一个PreparedStatement对象，用于执行参数化的SQL语句
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      // 将TestPersonnel对象s的id属性设置到SQL语句中的where子句中，参数索引为4（从1开始计数）
      stmt.setObject(4, s.getId());

      // 将TestPersonnel对象s的phone属性设置到SQL语句中的第一个set子句中，参数索引为1
      stmt.setObject(1, s.getPhone());

      // 将TestPersonnel对象s的gender属性设置到SQL语句中的第二个set子句中，参数索引为2
      stmt.setObject(2, s.getGender());

      // 将TestPersonnel对象s的birthDate属性设置到SQL语句中的第三个set子句中，参数索引为3
      stmt.setObject(3, s.getBirthDate());

      // 执行SQL更新语句
      stmt.executeUpdate();
    }
  }

  // 定义一个公共方法“findByphone”，该方法接受一个字符串类型的参数“phone”，并抛出SQL异常
  public TestPersonnel findByphone(String phone) throws SQLException {
    // 定义一个字符串“sql”，它是一个SQL查询语句，用于从数据库中查找特定电话的人员信息
    String sql = select + "where phone=?";

    // 初始化一个“TestPersonnel”对象，该对象将在找到匹配的记录后被赋值，初始值为null
    TestPersonnel testPersonnel = null;

    // 使用Java
    // 7引入的try-with-resources语句，通过该语句可以自动关闭实现了AutoCloseable接口的资源（如Connection，PreparedStatement等）
    try (Connection connection = Db.getConnection();
         // 创建一个PreparedStatement对象，用于执行参数化的SQL语句
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      // 将传入的电话号码设置为SQL语句中“?”位置的值
      stmt.setObject(1, phone);

      // 执行SQL查询，并将结果集存储在“rs”中
      ResultSet rs = stmt.executeQuery();

      // 如果结果集中有下一行数据（也就是找到了匹配的记录）
      if (rs.next()) {
        // 从结果集中读取数据并创建一个“TestPersonnel”对象
        testPersonnel = create(rs);
      }

      // 关闭结果集，释放资源
      rs.close();
    }

    // 返回创建的“TestPersonnel”对象（如果找到了匹配的记录）或者初始值为null的对象
    return testPersonnel;
  }

  public void insert(TestPersonnel s) throws SQLException {
    String sql = "insert into testPersonnel(id,phone,gender,birthdate,team_id) values(?,?,?,?,?)";
    try (Connection connection = Db.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setObject(1, s.getId());
      stmt.setObject(2, s.getPhone());
      stmt.setObject(3, s.getGender());
      stmt.setDate(4, s.getBirthDate());
      stmt.setObject(5, s.getTeamId());
      stmt.executeUpdate();
    }
  }

  public void delete(int id) throws SQLException {
    String sql = "delete from testPersonnel  where id=?";
    try (Connection connection = Db.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    }
  }
}
