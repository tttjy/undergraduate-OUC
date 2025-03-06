package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Schedule;
import com.qst.entity.User;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/save.action")
public class SaveServlet
    extends BaseServlet { // 让SaveServlet继承BaseServlet，调用service层方法对测试安排信息进行添加，并跳转到显示详情请求

  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Schedule schedule = new Schedule();
    try {
      User user = getCurrentUser(req);
      schedule.setBeginDate(RequestUtil.getString(req, "beginDate"));
      schedule.setEndDate(RequestUtil.getString(req, "endDate"));
      schedule.setDuration(RequestUtil.getInt(req, "duration"));
      schedule.setCreateDate(RequestUtil.getDate(req, "createDate"));
      schedule.setStatus(1);
      schedule.setAssessmentId(RequestUtil.getInt(req, "assessmentId"));
      schedule.setTeamId(RequestUtil.getInt(req, "teamId"));
      schedule.setQuestionNumber(RequestUtil.getInt(req, "questionNumber"));
      schedule.setCreatorId(user.getId());
      scheduleService.saveSchedule(schedule);
      WebUtil.redirect(req, resp, "/schedule/view.action?id=" + schedule.getId());
    } catch (ExamException ex) {
      ex.printStackTrace();
      addError(req, ex.getMessage());
      req.setAttribute("schedule", schedule);
      WebUtil.forward(req, resp, "/schedule/create.action");
    }
  }
}
