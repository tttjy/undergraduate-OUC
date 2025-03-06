package com.qst.service.impl;

import com.qst.dao.DAOFactory;
import com.qst.dao.MovieDAO;
import com.qst.entity.Movie;
import com.qst.service.IMovieService;
import java.io.File;
import java.util.List;

public class MovieServiceImpl implements IMovieService {
  private final MovieDAO movieDAO = DAOFactory.getDAO(MovieDAO.class);

  @Override
  public List<Movie> getAllMovies() {
    return movieDAO.getAllMovies();
  }

  @Override
  public Movie getMovieById(int id) {
    return movieDAO.getMovieById(id);
  }

  @Override
  public void addMovie(Movie movie) {
    movieDAO.addMovie(movie);
  }

  @Override
  public void updateMovie(Movie movie) {
    movieDAO.updateMovie(movie);
  }

  @Override
  public void deleteMovie(int id) {
    movieDAO.deleteMovie(id);
  }
}