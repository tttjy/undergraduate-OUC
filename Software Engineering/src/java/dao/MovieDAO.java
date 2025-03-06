package com.qst.dao;

import com.qst.Db;
import com.qst.ExamException;
import com.qst.SMMSUploader;
import com.qst.entity.Movie;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
  public static Movie createMovie(ResultSet rs) throws SQLException {
    Movie movie =
        new Movie(rs.getString("title"), rs.getString("description"), rs.getTimestamp("date"),
            rs.getInt("capacity"), rs.getInt("reservedSeats"), rs.getString("posterUrl"));
    movie.setId(rs.getInt("id"));
    return movie;
  }

  public List<Movie> getAllMovies() {
    String sql =
        "select id, title, description, date, capacity, reservedseats, posterUrl from movies";
    try (Connection conn = Db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      List<Movie> movies = new ArrayList<>();
      while (rs.next()) {
        movies.add(createMovie(rs));
      }
      return movies;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public Movie getMovieById(int id) {
    String sql = "select * from movies where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return createMovie(rs);
      }
      return null;
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void addMovie(Movie movie) {
    String sql =
        "insert into movies(title,description,date,capacity,reservedSeats,posterUrl) values(?,?,?,?,?,?)";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setObject(1, movie.getTitle());
      stmt.setObject(2, movie.getDescription());
      stmt.setObject(3, movie.getDate());
      stmt.setObject(4, movie.getCapacity());
      stmt.setObject(5, movie.getReservedSeats());
      stmt.setObject(6, movie.getPosterUrl());
      stmt.executeUpdate();
      movie.setId(Db.getGeneratedInt(stmt));
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void updateMovie(Movie movie) {
    String sql =
        "update movies set title=?,description=?,date=?,capacity=?,reservedSeats=? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, movie.getTitle());
      stmt.setObject(2, movie.getDescription());
      stmt.setObject(3, movie.getDate());
      stmt.setObject(4, movie.getCapacity());
      stmt.setObject(5, movie.getReservedSeats());
      stmt.setObject(6, movie.getId());
      stmt.setObject(7, movie.getPosterUrl());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void deleteMovie(int id) {
    String sql = "delete from movies where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, id);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }

  public void reserve(int id, int num) {
    Movie movie = getMovieById(id);
    if (movie.getReservedSeats() + num > movie.getCapacity()) {
      throw new IllegalArgumentException("预约的座位数超过了电影的总座位数");
    }

    String sql = "update movies set reservedSeats=reservedSeats+? where id=?";
    try (Connection conn = Db.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setObject(1, num);
      stmt.setObject(2, id);
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new ExamException(sql, ex);
    }
  }
}
