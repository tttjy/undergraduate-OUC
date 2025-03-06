package com.qst.service;

import com.qst.entity.Reservation;
import java.util.List;

public interface IReservationService {
  List<Reservation> getReservationsByUser(int userId);

  List<Reservation> getReservationsByMovie(int movieId);

  boolean isReserved(int userId, int movieId);

  void addReservation(int userId, int movieId);

  void deleteReservation(int userId, int movieId);
}
