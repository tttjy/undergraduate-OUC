package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.entity.Choice;
import com.qst.entity.Question;
import com.qst.service.IAssessmentService;
import com.qst.service.IQuestionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 当用户点击修改按钮时，通过超链接方式跳转到question/edit.action
@WebServlet("/question/edit.action")
public class EditServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Question question = questionService.findById(RequestUtil.getInt(req, "id"));
    List<Choice> choices = questionService.findChoices(question.getId());
    AssessmentType assessment = assessmentService.findAssessmentById(question.getAssessmentId());
    req.setAttribute("assessment", assessment);
    req.setAttribute("question", question);
    req.setAttribute("choices", choices);
    WebUtil.forward(req, resp, "/question/edit.jsp");
  }
}
