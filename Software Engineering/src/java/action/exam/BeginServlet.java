package com.qst.action.exam;

import static com.qst.Constant.CURRENT_EXAM;
import static com.qst.Constant.CURRENT_TESTPERSONNEL;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Exam;
import com.qst.entity.TestPersonnel;
import com.qst.service.IAssessmentService;
import com.qst.service.IExamService;
import com.qst.service.IScheduleService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exam/begin.action") // 修改其映射路径
public class BeginServlet extends BaseServlet { // 通过传来下标计算题目下标并将题目信息写入到请求中
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IExamService examService =
      ServiceFactory // -1：前一题，－２　后一题，　其他：指定下标
          .getService(IExamService.class);
  private final IScheduleService scheduleService =
      ServiceFactory.getService(IScheduleService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    TestPersonnel testPersonnel =
        (TestPersonnel) req.getSession().getAttribute(CURRENT_TESTPERSONNEL);
    int scheduleId = RequestUtil.getInt(req, "id");
    try {
      // 生成某个考生的试题：包含考生信息和试题集合
      Exam exam = examService.begin(testPersonnel, scheduleId);
      req.getSession().setAttribute(CURRENT_EXAM, exam);
      WebUtil.redirect(req, resp, "/exam/exam.action");
    } catch (ExamException ex) {
      ex.printStackTrace();
      addError(req, ex.getMessage());
      WebUtil.redirect(req, resp, "/exam/list.action");
    }
  }
}
