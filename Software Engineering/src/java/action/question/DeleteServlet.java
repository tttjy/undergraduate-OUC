package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Question;
import com.qst.service.IQuestionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/question/delete.action")
public class DeleteServlet extends BaseServlet {
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    Question q = questionService.findById(id);
    try {
      questionService.delete(id);
      addMessage(req, "题目已删除");
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    String url = "/question/list.action?assessmentId=" + q.getAssessmentId()
        + "&status=" + q.getStatus() + "&type=" + q.getType();
    WebUtil.redirect(req, resp, url);
  }
}
