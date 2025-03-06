package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.PersonalityDimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DimensionDAO {
  // 通过考核类型ID从数据库查询问卷维度列表，如果传入的值为0则查询所有问卷维度
  // findByAssessment调用create将每个问卷维度变成一个对象存储到一个list集合中
  public List<PersonalityDimension> findByAssessment(int assessmentId) {
    String sql;
    if (assessmentId > 0) {
      sql = "select id,title,depict,assessment_id from personality_dimension where assessment_id=?";
    } else {
      sql = "select id,title,depict,assessment_id from personality_dimension";
    }
    List<PersonalityDimension> kps = new ArrayList<>();
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      if (assessmentId > 0)
        stmt.setObject(1, assessmentId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        kps.add(create(rs));
      }
      rs.close();
      return kps;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  // 通过问卷维度ID在数据库中查询问卷维度信息
  public PersonalityDimension findById(int id) {
    String sql = "select id,title,depict,assessment_id from personality_dimension where id=?";
    PersonalityDimension kp = null;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        kp = create(rs);
      }
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return kp;
  }
  // 检测问卷维度是否已经存在
  // 通过考核类型ID和问卷维度标题查询是否有相同的问卷维度，如果不存在则利用insert将问卷维度插入到数据库中，通过传入问卷维度的值向数据库插入数据
  public PersonalityDimension findByAssessmentAndTitle(int assessmentId, String title) {
    String sql =
        "select id,title,depict,assessment_id from personality_dimension where assessment_id=? and title=?";
    PersonalityDimension kp = null;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, assessmentId);
      stmt.setObject(2, title);

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        kp = create(rs);
      }
      rs.close();

    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new ExamException(sql, ex);
    }
    return kp;
  }
  public int insert(PersonalityDimension kp) {
    String sql = "insert into  personality_dimension(title,depict,assessment_id) values(?,?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, kp.getTitle());
      stmt.setObject(2, kp.getDepict());
      stmt.setObject(3, kp.getAssessmentId());
      stmt.executeUpdate();
      kp.setId(Db.getGeneratedInt(stmt));
      return kp.getId();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  public void update(PersonalityDimension kp) {
    String sql = "update personality_dimension set title=?,depict=?,assessment_id=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, kp.getTitle());
      stmt.setObject(2, kp.getDepict());
      stmt.setObject(3, kp.getAssessmentId());
      stmt.setObject(4, kp.getId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public List<PersonalityDimension> findDimensionByQuestion(int questionId) {
    String sql =
        "select id,title,depict,assessment_id from personality_dimension where id in (select dimension_id from question_dimension where question_id=?)";
    List<PersonalityDimension> kps = new ArrayList<>();
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, questionId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        kps.add(create(rs));
      }
      rs.close();
      return kps;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void delete(int id) {
    String sql = "delete from personality_dimension where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  private PersonalityDimension create(ResultSet rs) throws SQLException {
    PersonalityDimension kp = new PersonalityDimension();
    kp.setId(rs.getInt("id"));
    kp.setTitle(rs.getString("title"));
    kp.setDepict(rs.getString("depict"));
    kp.setAssessmentId(rs.getInt("assessment_id"));
    return kp;
  }
}
