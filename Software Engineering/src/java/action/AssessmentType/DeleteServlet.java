package com.qst.action.AssessmentType;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.service.IAssessmentService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/assessment/delete.action")
public class DeleteServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");

    try {
      assessmentService.deleteAssessment(
          id); // 通过考核类型ID删除考核类型信息，并删除结果返回到前端
      addMessage(req, "科目已删除");
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    WebUtil.redirect(req, resp, "/assessment/list.action");
  }
}
