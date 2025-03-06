package com.qst.action.exam;

import static com.qst.Constant.*;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Exam;
import com.qst.entity.Schedule;
import com.qst.service.IAssessmentService;
import com.qst.service.IExamService;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exam/result.action")
public class ResultServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IExamService examService = ServiceFactory.getService(IExamService.class);
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    try {
      List<Exam> exams = examService.examResult(id);
      String result = null;
      for (Exam e : exams) {
        result = e.getResult();
      }
      String stf = "../analysis/" + result + ".jsp";
      req.setAttribute("exam", exams);
      req.setAttribute("examresult", stf);
      WebUtil.forward(req, resp, "/exam/result.jsp");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
