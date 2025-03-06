package com.qst.action.movie;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.service.IMovieService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/movie/delete.action")

public class DeleteServlet extends BaseServlet {
  private final IMovieService movieService = ServiceFactory.getService(IMovieService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int movieId = RequestUtil.getInt(request, "id");
    movieService.deleteMovie(movieId);
    WebUtil.redirect(request, response, "/movie/list.action");
  }
}