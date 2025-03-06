package com.qst.service.impl;

import com.qst.dao.DAOFactory;
import com.qst.dao.ReservationDAO;
import com.qst.entity.Reservation;
import com.qst.service.IReservationService;
import java.util.List;

public class ReservationServiceImpl implements IReservationService {
  private final ReservationDAO reservationDAO = DAOFactory.getDAO(ReservationDAO.class);

  @Override
  public List<Reservation> getReservationsByUser(int user) {
    return reservationDAO.getReservationsByUser(user);
  }

  @Override
  public List<Reservation> getReservationsByMovie(int movie) {
    return reservationDAO.getReservationsByMovie(movie);
  }

  @Override
  public boolean isReserved(int userId, int movieId) {
    return reservationDAO.isReserved(userId, movieId);
  }
  @Override
  public void addReservation(int userId, int movieId) {
    reservationDAO.addReservation(new Reservation(userId, movieId));
  }

  @Override
  public void deleteReservation(int user, int movie) {
    reservationDAO.deleteReservation(user, movie);
  }
}