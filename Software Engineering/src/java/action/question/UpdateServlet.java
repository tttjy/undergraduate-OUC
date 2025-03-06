package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.Constant;
import com.qst.ExamException;
import com.qst.WebUtil;
import com.qst.entity.Choice;
import com.qst.entity.Question;
import com.qst.entity.User;
import com.qst.service.IAssessmentService;
import com.qst.service.IQuestionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/question/update.action")
public class UpdateServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取修改页面中的题目与选项内容
    Question question = QuestionHelper.createQuestion(req);
    List<Choice> choices = QuestionHelper.createChoice(req);
    try {
      // todo 完成登陆后更改此处为当前用户
      // 通过Session中获取当前登录用户的信息，将题目修改用户的ID改为当前登录用户的ID，调用QuestionService中的修改方法，修改题目与选项内容。
      User currentUser = (User) req.getSession().getAttribute(Constant.CURRENT_USER);
      question.setUserId(currentUser.getId());
      questionService.update(question, choices);
      addMessage(req, "题目已经保存到数据库中,请关联问卷维度");
      WebUtil.redirect(req, resp, "/question/dimension.action?method=show&id=" + question.getId());
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      req.setAttribute("question", question);
      req.setAttribute("choices", choices);
      req.setAttribute(
          "assessment", assessmentService.findAssessmentById(question.getAssessmentId()));
      WebUtil.forward(req, resp, "/question/edit.jsp");
    }
  }
}
