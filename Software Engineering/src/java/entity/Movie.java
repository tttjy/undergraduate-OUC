package com.qst.entity;

import java.sql.Timestamp;
import java.util.Date;

public class Movie extends BaseEntity {
  private String title;
  private String description;
  private Timestamp date;
  private int capacity;
  private int reservedSeats;
  private String posterUrl;

  public Movie() {}

  public Movie(String title, String description, Timestamp date, int capacity, int reservedSeats,
      String posterUrl) {
    this.title = title;
    this.description = description;
    this.date = date;
    this.capacity = capacity;
    this.reservedSeats = reservedSeats;
    this.posterUrl = posterUrl;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  @Override
  public String toString() {
    return "Movie{"
        + "title='" + title + '\'' + ", description='" + description + '\'' + ", date=" + date
        + ", capacity=" + capacity + ", reservedSeats=" + reservedSeats + ", id=" + id + '}';
  }

  public int getReservedSeats() {
    return reservedSeats;
  }

  public void setReservedSeats(int reservedSeats) {
    this.reservedSeats = reservedSeats;
  }
}
