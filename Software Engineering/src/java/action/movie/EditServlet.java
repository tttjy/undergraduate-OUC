package com.qst.action.movie;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Movie;
import com.qst.service.IMovieService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/movie/edit.action")
public class EditServlet extends BaseServlet {
  private final IMovieService movieService = ServiceFactory.getService(IMovieService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int movieId = RequestUtil.getInt(request, "id");
    Movie movie = movieService.getMovieById(movieId);
    String dateStr = request.getParameter("date");
    // 将字符串转换为时间戳
    Timestamp date = Timestamp.valueOf(dateStr);
    request.setAttribute("movie", movie);
    WebUtil.forward(request, response, "/movie/edit.jsp");
  }
}