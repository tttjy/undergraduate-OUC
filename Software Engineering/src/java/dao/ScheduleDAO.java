package com.qst.dao;

import com.qst.Db;
import com.qst.entity.Schedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO { // 查询测试安排的数据然后使用create方法封装返回该数据

  private static final String select =
      "select id,begin_date,end_date,duration,status,creator_id,assessment_id,team_id,create_date,question_number from schedules ";

  public List<Schedule> findByCreatorId(Integer id) throws SQLException {
    String sql = select + " where creator_id=? order by team_id,assessment_id";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      List<Schedule> list = new ArrayList<>();
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(create(rs));
      }
      rs.close();
      return list;
    }
  }
  private Schedule create(ResultSet rs) throws SQLException {
    Schedule h = new Schedule();
    h.setId(rs.getInt("id"));
    h.setBeginDate(rs.getString("begin_date"));
    h.setEndDate(rs.getString("end_date"));
    h.setDuration(rs.getInt("duration"));
    h.setCreateDate(rs.getDate("create_date"));
    h.setQuestionNumber(rs.getInt("question_number"));
    h.setStatus(rs.getInt("status"));
    h.setCreatorId(rs.getInt("creator_id"));
    h.setAssessmentId(rs.getInt("assessment_id"));
    h.setTeamId(rs.getInt("team_id"));
    return h;
  }
  public Schedule findById(Integer id) throws SQLException {
    String sql = select + " where  id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      Schedule schedule = null;
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        schedule = create(rs);
      }
      rs.close();
      return schedule;
    }
  }
  public void save(Schedule h) throws SQLException {
    String sql =
        "insert into schedules(begin_date,end_date,duration,create_date,status,creator_id,assessment_id,team_id,question_number) values (?,?,?,?,?,?,?,?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, h.getBeginDate());
      stmt.setObject(2, h.getEndDate());
      stmt.setObject(3, h.getDuration());
      stmt.setObject(4, h.getCreateDate());
      stmt.setObject(5, h.getStatus());
      stmt.setObject(6, h.getCreatorId());
      stmt.setObject(7, h.getAssessmentId());
      stmt.setObject(8, h.getTeamId());
      stmt.setObject(9, h.getQuestionNumber());
      stmt.executeUpdate();
      h.setId(Db.getGeneratedInt(stmt));
    }
  }
  public void update(Schedule h) throws SQLException {
    String sql =
        "update  schedules set begin_date=?,end_date=?,duration=?,create_date=?,status=?,assessment_id=?,team_id=?,question_number=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, h.getBeginDate());
      stmt.setObject(2, h.getEndDate());
      stmt.setObject(3, h.getDuration());
      stmt.setObject(4, h.getCreateDate());
      stmt.setObject(5, h.getStatus());
      stmt.setObject(6, h.getAssessmentId());
      stmt.setObject(7, h.getTeamId());
      stmt.setObject(8, h.getQuestionNumber());
      stmt.setObject(9, h.getId());

      stmt.executeUpdate();
    }
  }
  public void delete(Integer id) throws SQLException {
    String sql = "delete from schedules where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    }
  }

  public List<Schedule> findByTeamId(int teamId) throws SQLException {
    String sql = select + " where team_id=? order by id desc";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, teamId);
      List<Schedule> list = new ArrayList<>();
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(create(rs));
      }
      rs.close();
      return list;
    }
  }
}
