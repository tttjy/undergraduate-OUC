package com.qst.action.AssessmentType;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.service.IAssessmentService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/assessment/update.action")
public class UpdateServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    AssessmentType sj = new AssessmentType();
    sj.setId(RequestUtil.getInt(req, "id"));
    sj.setTitle(RequestUtil.getString(req, "title"));
    sj.setCost(RequestUtil.getDouble(req, "cost"));
    sj.setStatus(RequestUtil.getInt(req, "status"));
    try {
      assessmentService.updateAssessment(sj);
      addMessage(req, "考核类型信息已被保存到数据库");
      WebUtil.redirect(req, resp, "/assessment/view.action?id=" + sj.getId());
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      req.setAttribute("assessment", sj);
      WebUtil.forward(req, resp, "/assessment/edit.jsp");
    }
  }
}
