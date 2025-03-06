package com.qst.action.reservation;

import com.qst.BaseServlet;
import com.qst.entity.Reservation;
import com.qst.service.IReservationService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservation/add.action")

public class AddServlet extends BaseServlet {
  private final IReservationService reservationService =
      ServiceFactory.getService(IReservationService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String userId = request.getParameter("userId");
    String movieId = request.getParameter("movieId");
    try {
      reservationService.addReservation(Integer.parseInt(userId), Integer.parseInt(movieId));
      addMessage(request, "预约成功");
      request.getRequestDispatcher("/movie/list.action").forward(request, response);
    } catch (Exception e) {
      addError(request, "预约失败");
    }
  }
}