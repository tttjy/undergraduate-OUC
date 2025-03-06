package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Schedule;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/view.action")
public class ViewServlet extends BaseServlet { // 继承BaseServlet,使用findByld方法通过ID查找题目信息

  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Integer id = RequestUtil.getInt(req, "id");
    Schedule schedule = scheduleService.findById(id);
    req.setAttribute("schedule", schedule);
    WebUtil.forward(req, resp, "/schedule/view.jsp");
  }
}
