package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import com.qst.entity.Schedule;
import com.qst.entity.User;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/list.action")
public class ListServlet extends BaseServlet {
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = getCurrentUser(req);

    List<Schedule> scheduleList =
        scheduleService.findByCreator(user); // 使用findByCreater函数查询测试数据
    req.setAttribute("scheduleList", scheduleList);

    WebUtil.forward(req, resp, "/schedule/list.jsp");
  }
}
