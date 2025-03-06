package com.qst.action.schedule;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.entity.Schedule;
import com.qst.entity.Team;
import com.qst.entity.User;
import com.qst.service.IAssessmentService;
import com.qst.service.IScheduleService;
import com.qst.service.ITeamService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/schedule/edit.action")
public class EditServlet extends BaseServlet {
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final ITeamService teamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = getCurrentUser(req);
    Integer id = RequestUtil.getInt(req, "id");
    Schedule schedule = (Schedule) req.getAttribute("schedule");
    if (schedule == null) {
      schedule = scheduleService.findById(id);
      req.setAttribute("schedule", schedule);
    }

    List<Team> teamList = teamService.findByCreator(user.getId());
    req.setAttribute("teamList", teamList);
    List<AssessmentType> assessmentList = assessmentService.findAllAssessment();
    req.setAttribute("assessmentList", assessmentList);
    WebUtil.forward(req, resp, "/schedule/edit.jsp");
  }
}
