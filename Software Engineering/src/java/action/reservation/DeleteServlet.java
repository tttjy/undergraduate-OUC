package com.qst.action.reservation;

import com.qst.BaseServlet;
import com.qst.service.IReservationService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservation/delete.action")
public class DeleteServlet extends BaseServlet {
  private final IReservationService reservationService =
      ServiceFactory.getService(IReservationService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int userId = Integer.parseInt(request.getParameter("userId"));
    int movieId = Integer.parseInt(request.getParameter("movieId"));
    if (userId == 0 || movieId == 0) {
      addError(request, "参数错误");
      return;
    }
    try {
      System.out.println("userId: " + userId + " movieId: " + movieId);
      reservationService.deleteReservation(userId, movieId);
    } catch (Exception e) {
      e.printStackTrace();
      addError(request, "删除失败");
      return;
    }
    addMessage(request, "删除成功");
    request.getRequestDispatcher("/movie/list.action").forward(request, response);

    // response.sendRedirect(request.getContextPath() + "/movie/list.action");
  }
}