package com.qst.dao;

import com.qst.Db;
import com.qst.entity.Team;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 王世广
 * @usage Team的数据库访问对象
 *
 */
public class TeamDAO {
  /*

  *//**
	 * @usage 根据创建者id查找班级
	 * @param creator_id
	 *            :班级创建者id
	 * @return 指定创建者所创建的所有班级
	 * @throws SQLException
	 *//*
*/
  public List<Team> findAll() throws SQLException {
    String sql = "select id,name,begin_year,status,creator_id from class_teams ";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      List<Team> lists = new ArrayList<>();
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        lists.add(create(rs));
      }
      rs.close();
      return lists;
    }
  }
  public List<Team> findByCreator(Integer creator_id) throws SQLException {
    String sql = "select id,name,begin_year,status,creator_id from class_teams where creator_id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, creator_id);
      List<Team> lists = new ArrayList<>();
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        lists.add(create(rs));
      }
      rs.close();
      return lists;
    }
  }
  /**
   * @usage将rs 的一行组装为一个classTeam对象返回
   * @param rs
   * @return
   * @throws SQLException
   */
  private Team create(ResultSet rs) throws SQLException {
    Team t = new Team();
    t.setId(rs.getInt("id"));
    t.setName(rs.getString("name"));
    t.setBeginYear(rs.getDate("begin_year"));
    t.setStatus(rs.getInt("status"));
    t.setCreatorId(rs.getInt("creator_id"));
    return t;
  }

  /**
   *
   * @param id
   *            :要查找的班级id
   * @return：指定id的班级
   */
  public Team findById(Integer id)
      throws SQLException { // 使用findById方法在数据库中查询到批次情况通过create封装返回
    String sql = "select id,name,begin_year,status,creator_id from class_teams where id=?";
    Team team = null;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        team = create(rs);
      }
      rs.close();
    }
    return team;
  }
  public void save(Team t) throws SQLException {
    String sql = "insert into class_teams(name,begin_year,status,creator_id) values(?,?,?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, t.getName());
      stmt.setObject(2, t.getBeginYear());
      stmt.setObject(3, t.getStatus());
      stmt.setObject(4, t.getCreatorId());
      stmt.executeUpdate();
      t.setId(Db.getGeneratedInt(stmt));
    }
  }
  public void update(Team t) throws SQLException {
    String sql = "update class_teams set name=?,begin_year=?,status=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, t.getName());
      stmt.setDate(2, t.getBeginYear());
      stmt.setObject(3, t.getStatus());
      stmt.setObject(4, t.getId());
      stmt.executeUpdate();
    }
  }
  public void delete(int id) throws SQLException {
    String sql = "delete from class_teams where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    }
  }
}
