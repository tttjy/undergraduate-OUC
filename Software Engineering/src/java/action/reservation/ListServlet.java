package com.qst.action.reservation;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Reservation;
import com.qst.service.IReservationService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reservation/list.action")
public class ListServlet extends BaseServlet {
  private final IReservationService reservationService =
      ServiceFactory.getService(IReservationService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(request, "id");
    List<Reservation> reservations = reservationService.getReservationsByUser(id);
    request.setAttribute("reservations", reservations);
    WebUtil.forward(request, response, "/reservation/list.jsp");
  }
}