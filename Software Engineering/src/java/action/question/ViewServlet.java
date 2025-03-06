package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.entity.Choice;
import com.qst.entity.PersonalityDimension;
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

@WebServlet("/question/view.action")
public class ViewServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 通过题目ID查找题目、选项、考核类型以及问卷维度，将其添加到请求中，并返回显示页面
    Question question = questionService.findById(RequestUtil.getInt(req, "id"));
    List<Choice> choices = questionService.findChoices(question.getId());
    AssessmentType assessment = assessmentService.findAssessmentById(question.getAssessmentId());
    List<PersonalityDimension> dimension =
        questionService.findDimensionByQuestion(question.getId());
    req.setAttribute("assessment", assessment);
    req.setAttribute("question", question);
    req.setAttribute("choices", choices);
    req.setAttribute("dimension", dimension);

    WebUtil.forward(req, resp, "/question/view.jsp");
  }
}
