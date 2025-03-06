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

// 在用户输入添加题目信息后点击保存后，通过form表单形式跳转到question/save.action，进行数据保存逻辑
// 接收前端页面数据并保存题目
@WebServlet("/question/save.action")
public class SaveServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 创建QuestionHelper
    Question question = QuestionHelper.createQuestion(req);
    List<Choice> choices = QuestionHelper.createChoice(req);
    try {
      // todo 完成登陆后更改此处为当前用户
      // 通过getSession().getAttribute()方法，从session中获取当前用户的信息
      // 调用QuestionService中的save方法
      // 调用question/dimension.action
      // 将题目的ID一起传值过去 ,创建获取题目的内容与选项
      User currentUser = (User) req.getSession().getAttribute(Constant.CURRENT_USER);
      question.setUserId(currentUser.getId());
      questionService.save(question, choices);
      addMessage(req, "试题保存到数据库中");
      WebUtil.redirect(req, resp, "/question/dimension.action?id=" + question.getId());
    } catch (ExamException ex) {
      System.out.println(ex);
      addError(req, ex.getMessage());
      req.setAttribute("question", question);
      req.setAttribute("choices", choices);
      req.setAttribute(
          "assessment", assessmentService.findAssessmentById(question.getAssessmentId()));
      WebUtil.forward(req, resp, "/question/create.jsp");
    }
  }
}
