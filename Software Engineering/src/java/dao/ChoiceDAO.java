package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.Choice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChoiceDAO {
  public void deleteByQuestion(int id) {
    String sql = "delete from choices where question_id=? order by id";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void update(Choice ch) {
    String sql = "update choices set title=?,hint=?,checked=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, ch.getTitle());
      stmt.setObject(2, ch.getHint());
      stmt.setObject(3, ch.isChecked());
      stmt.setObject(4, ch.getId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  public void insert(Choice ch) {
    String sql = "insert into choices(question_id,title,hint,checked) values(?,?,?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, ch.getQuestionId());
      stmt.setObject(2, ch.getTitle());
      stmt.setObject(3, ch.getHint());
      stmt.setObject(4, ch.isChecked());
      stmt.executeUpdate();
      ch.setId(Db.getGeneratedInt(stmt));

    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
  public List<Choice> findByQuestion(Integer id) {
    String sql = "select id,title,hint,checked,question_id from choices where question_id=?";
    List<Choice> list = new ArrayList<>();
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(create(rs));
      }
      rs.close();

    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
    return list;
  }

  private Choice create(ResultSet rs) throws SQLException {
    Choice ch = new Choice();
    ch.setId(rs.getInt("id"));
    ch.setTitle(rs.getString("title"));
    ch.setHint(rs.getString("hint"));
    ch.setChecked(rs.getBoolean("checked"));
    ch.setQuestionId(rs.getInt("question_id"));
    return ch;
  }
}
