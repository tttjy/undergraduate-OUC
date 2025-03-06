package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.entity.Schedule;
import com.qst.entity.Team;
import com.qst.entity.User;
import com.qst.service.IAssessmentService;
import com.qst.service.ITeamService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/create.action")
public class CreateServlet
    extends BaseServlet { // 在doAction方法中，创建初始化的测试安排对象，并为初始化的测试安排对象赋初始化值

  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final ITeamService teamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = getCurrentUser(req);
    Schedule schedule = (Schedule) req.getAttribute("schedule");
    if (schedule == null) {
      schedule = new Schedule();
      Date today = new Date(System.currentTimeMillis());
      SimpleDateFormat stf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      schedule.setBeginDate(stf.format(today));
      schedule.setEndDate(stf.format(today));

      req.setAttribute("schedule", schedule);
    }

    List<Team> teamList = teamService.findByCreator(user.getId());
    req.setAttribute("teamList", teamList);
    List<AssessmentType> assessmentList = assessmentService.findAllAssessment();
    req.setAttribute("assessmentList", assessmentList);

    WebUtil.forward(req, resp, "/schedule/create.jsp");
  }
}
