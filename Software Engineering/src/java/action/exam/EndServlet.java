package com.qst.action.exam;

import static com.qst.Constant.*;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.WebUtil;
import com.qst.entity.Exam;
import com.qst.entity.Schedule;
import com.qst.service.IAssessmentService;
import com.qst.service.IExamService;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exam/end.action") // 改变映射路径
public class EndServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IExamService examService = ServiceFactory.getService(IExamService.class);
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Exam exam = (Exam) req.getSession().getAttribute(CURRENT_EXAM);
    try {
      examService.end(exam);
      String stf = "../analysis/" + exam.getResult() + ".jsp";
      req.setAttribute("examresult", stf);
      req.setAttribute("score", exam.getResult());
    } catch (ExamException ex) {
      ex.printStackTrace();
      req.setAttribute("score", "您未能成功交卷，请重新交卷");
    }
    WebUtil.forward(req, resp, "/exam/end.jsp");
  }
}
