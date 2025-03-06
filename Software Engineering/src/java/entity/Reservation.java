package com.qst.entity;

public class Reservation extends BaseEntity {
  private int userId;
  private int movieId;

  public Reservation() {}

  public Reservation(int userId, int movieId) {
    this.userId = userId;
    this.movieId = movieId;
  }

  @Override
  public String toString() {
    return "Reservation{"
        + "userId=" + userId + ", movieId=" + movieId + ", id=" + id + '}';
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getMovieId() {
    return movieId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }
}
