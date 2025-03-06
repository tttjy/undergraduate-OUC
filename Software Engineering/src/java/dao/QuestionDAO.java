package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.action.question.QuestionQueryParam;
import com.qst.entity.Question;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
  // 查询考核类型中是否还存在题目
  public int findCountByAssessment(int assessmentId) {
    String sql = "select count(*) from questions where assessment_id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, assessmentId);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  // 通过问卷维度ID查询有关联此问卷维度的题目数量，用于验证问卷维度是否能被删除
  public int findCountByDimension(int dimensionId) {
    String sql = "select count(*) from question_dimension where dimension_id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, dimensionId);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  // 查找考题列表
  // 调用create方法，通过一个ResultSet对象生成为题对象，当传递默认查询参数时则查询全部问题数据
  public List<Question> find(QuestionQueryParam param) {
    String sql;
    if (param.getAssessmentId() > 0) {
      sql =
          "select id,assessment_id,user_id,type,title,status,difficulty,hint from questions where assessment_id=? and status=? and type=? order by id desc ";
    } else {
      sql =
          "select id,assessment_id,user_id,type,title,status,difficulty,hint from questions order by id desc ";
    }
    List<Question> questionList = new ArrayList<>();
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      if (param.getAssessmentId() > 0) {
        stmt.setObject(1, param.getAssessmentId());
        stmt.setObject(2, param.getStatus());
        stmt.setObject(3, param.getType());
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        questionList.add(create(rs));
      }

      rs.close();

    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new ExamException();
    }
    return questionList;
  }

  public Question findById(int id) {
    String sql =
        "select id,assessment_id,user_id,type,title,status,difficulty,hint from questions where id=? ";
    Question question = null;

    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        question = create(rs);
      }
      rs.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new ExamException();
    }
    return question;
  }

  public void insert(Question q) {
    String sql =
        "insert into questions(title,hint,type,status,difficulty,assessment_id,user_id) values(?,?,?,?,?,?,?)";

    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, q.getTitle());
      stmt.setObject(2, q.getHint());
      stmt.setObject(3, q.getType());
      stmt.setObject(4, q.getStatus());
      stmt.setObject(5, q.getDifficulty());
      stmt.setObject(6, q.getAssessmentId());
      stmt.setObject(7, q.getUserId());
      stmt.executeUpdate();
      // 获取考题ID的逻辑
      q.setId(Db.getGeneratedInt(stmt));
      ResultSet rs = stmt.getGeneratedKeys();
      rs.next();
      int qid = rs.getInt(1);
    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new ExamException(sql, ex);
    }
  }

  public void detachDimension(int id) {
    String sql = "delete from question_dimension where question_id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();

    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void attachDimension(int qid, int pid) {
    String sql = "insert into question_dimension(question_id,dimension_id) values(?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, qid);
      stmt.setObject(2, pid);
      stmt.executeUpdate();

    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void update(Question q) {
    String sql = "update  questions set title=?,hint=?,type=?,status=?,difficulty=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, q.getTitle());
      stmt.setObject(2, q.getHint());
      stmt.setObject(3, q.getType());
      stmt.setObject(4, q.getStatus());
      stmt.setObject(5, q.getDifficulty());
      stmt.setObject(6, q.getId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void delete(int id) {
    String sql = "delete from questions where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();

    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public boolean isUsed(int id) {
    String sql = "select 1 from  paper_questions where question_id=? limit 1";
    boolean used = false;
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        used = true;
      }
      rs.close();
    } catch (SQLException ex) {
      // ex.printStackTrace();
      throw new ExamException(sql, ex);
    }
    return used;
  }

  public List<Integer> findDimension(int questionId) {
    String sql = "select dimension_id from question_dimension where question_id=?";
    List<Integer> lists = new ArrayList<>();
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, questionId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        lists.add(rs.getInt(1));
      }
      rs.close();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return lists;
  }

  private Question create(ResultSet rs) throws SQLException {
    Question q = new Question();
    q.setDifficulty(rs.getInt("difficulty"));
    q.setHint(rs.getString("hint"));
    q.setId(rs.getInt("id"));
    q.setStatus(rs.getInt("status"));
    q.setAssessmentId(rs.getInt("assessment_id"));
    q.setTitle(rs.getString("title"));
    q.setType(rs.getInt("type"));
    q.setUserId(rs.getInt("user_id"));
    return q;
  }
}
