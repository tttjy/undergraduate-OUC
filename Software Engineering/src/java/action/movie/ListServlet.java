package com.qst.action.movie;

import com.qst.BaseServlet;
import com.qst.Constant;
import com.qst.EntityToMapConverter;
import com.qst.WebUtil;
import com.qst.entity.Movie;
import com.qst.entity.User;
import com.qst.service.IMovieService;
import com.qst.service.IReservationService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/movie/list.action")
public class ListServlet extends BaseServlet {
  private final IMovieService movieService = ServiceFactory.getService(IMovieService.class);
  private final IReservationService reservationService =
      ServiceFactory.getService(IReservationService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    List<Movie> movies = movieService.getAllMovies();
    List<Map<String, Object>> movieMaps = null;
    try {
      movieMaps = EntityToMapConverter.convertEntityListToMapList(movies);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    for (Map<String, Object> movie : movieMaps) {
      User user = (User) request.getSession().getAttribute(Constant.CURRENT_USER);
      if (reservationService.isReserved(user.getId(), (Integer) movie.get("id")))
        movie.put("reserved", "true");
      else
        movie.put("reserved", "false");
    }
    request.setAttribute("movies", movieMaps);

    //        get user type from request if eq 4 then to list else to manage
    User user = getCurrentUser(request);
    if (user.getType() != 4)
      WebUtil.forward(request, response, "/movie/manage.jsp");
    else
      WebUtil.forward(request, response, "/movie/list.jsp");
  }
}