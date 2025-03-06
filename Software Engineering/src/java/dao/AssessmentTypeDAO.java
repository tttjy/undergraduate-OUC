package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.AssessmentType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AssessmentTypeDAO {
  public List<AssessmentType> findAll() {
    String sql = "select id,title,cost,status from assessments";
    List<AssessmentType> assessments = new ArrayList<>();
    try (Connection conn = Db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        assessments.add(createAssessmentType(rs));
      }
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return assessments;
  }
  // 首先通过findByTitle()查询考核类型是否已经存在，如果考核类型已经存在，则抛出异常信息，并提示考核类型重复
  // 如果校验通过则通过insert()修改考核类型信息
  public int insert(AssessmentType sj) {
    String sql = "insert into  assessments (title,cost,status) values(?,?,?)";

    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, sj.getTitle());
      stmt.setObject(2, sj.getCost());
      stmt.setObject(3, sj.getStatus());
      stmt.executeUpdate();
      sj.setId(Db.getGeneratedInt(stmt));
      return sj.getId();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  // 通过id删除对应的考核信息
  public void delete(int id) {
    String sql = "delete from assessments where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  // 查询考核类型名称是否存在，如果存在则利用update方法将将数据库中的相关数据进行更改，通过传入的考核类型数据并根据id修改考核类型
  public AssessmentType findByTitle(String title) {
    String sql = "select id,title,cost,status from assessments where title=?";
    AssessmentType assessment = null;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, title);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        assessment = createAssessmentType(rs);
      }
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return assessment;
  }
  public void update(AssessmentType sj) {
    String sql = "update  assessments set title=?,cost=?,status=? where id=?";

    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, sj.getTitle());
      stmt.setObject(2, sj.getCost());
      stmt.setObject(3, sj.getStatus());
      stmt.setObject(4, sj.getId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  private AssessmentType createAssessmentType(ResultSet rs) throws SQLException {
    AssessmentType sj = new AssessmentType();
    sj.setId(rs.getInt("id"));
    sj.setTitle(rs.getString("title"));
    sj.setCost(rs.getDouble("cost"));
    sj.setStatus(rs.getInt("status"));
    return sj;
  }

  public AssessmentType findById(int id) {
    String sql = "select id,title,cost,status from assessments where id=?";
    AssessmentType assessment = null;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        assessment = createAssessmentType(rs);
      }
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return assessment;
  }
}
