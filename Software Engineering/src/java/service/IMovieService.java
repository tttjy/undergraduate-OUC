package com.qst.service;

import com.qst.entity.Movie;
import java.io.File;
import java.util.List;

public interface IMovieService {
  List<Movie> getAllMovies();

  Movie getMovieById(int id);

  void addMovie(Movie movie);

  void updateMovie(Movie movie);

  void deleteMovie(int id);
}
