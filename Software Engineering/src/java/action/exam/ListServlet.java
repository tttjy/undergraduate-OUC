package com.qst.action.exam;

import com.qst.BaseServlet;
import com.qst.Constant;
import com.qst.WebUtil;
import com.qst.entity.Schedule;
import com.qst.entity.TestPersonnel;
import com.qst.service.IAssessmentService;
import com.qst.service.IExamService;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exam/list.action")
public class ListServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IExamService examService = ServiceFactory.getService(IExamService.class);
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    TestPersonnel stu =
        (TestPersonnel) req.getSession().getAttribute(Constant.CURRENT_TESTPERSONNEL);
    List<Schedule> scheduleList = examService.findScheduleByTestPersonnel(stu);
    req.setAttribute("scheduleList", scheduleList);
    WebUtil.forward(req, resp, "/exam/list.jsp");
  }
}
