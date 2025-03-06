package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/delete.action")
public class DeleteServlet
    extends BaseServlet { // 在doAction方法中获取要删除测试安排的ID，通过调用service层方法删除对应ID的测试。

  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Integer id = RequestUtil.getInt(req, "id");
    try {
      scheduleService.deleteSchedule(id);
    } catch (ExamException ex) {
      ex.printStackTrace();
      addError(req, ex.getMessage());
    }
    WebUtil.redirect(req, resp, "/schedule/list.action");
  }
}
