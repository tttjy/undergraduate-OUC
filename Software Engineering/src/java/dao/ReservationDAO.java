package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.entity.Reservation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
  public static Reservation createReservation(ResultSet rs) throws SQLException {
    return new Reservation(rs.getInt("userId"), rs.getInt("movieId"));
  }

  public List<Reservation> getReservationsByUser(int userId) {
    String sql = "select * from reservation where userId =?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, userId);
      ResultSet rs = stmt.executeQuery();
      List<Reservation> reservations = new ArrayList<>();
      while (rs.next()) {
        reservations.add(createReservation(rs));
      }
      return reservations;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Reservation> getReservationsByMovie(int movieId) {
    String sql = "select * from reservation where movieId =?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, movieId);
      ResultSet rs = stmt.executeQuery();
      List<Reservation> reservations = new ArrayList<>();
      while (rs.next()) {
        reservations.add(createReservation(rs));
      }
      return reservations;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void addReservation(Reservation reservation) {
    String sql = "insert into reservation(userId,movieId) values(?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, reservation.getUserId());
      stmt.setObject(2, reservation.getMovieId());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void deleteReservation(int userId, int movieId) {
    String sql = "delete from reservation where userId =? and movieId =?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, userId);
      stmt.setObject(2, movieId);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public boolean isReserved(int userId, int movieId) {
    String sql = "select * from reservation where userId =? and movieId =?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, userId);
      stmt.setObject(2, movieId);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
}
