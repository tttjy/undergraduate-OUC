package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
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

// 题目增加：用户在选中考核类型后，点击添加本考核类型题目按钮，通过form表单方式提交到后台的question/list.action，后台通过判断点击按钮的值判断用户是否点击添加按钮
@WebServlet("/question/list.action")
public class ListServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  // 查询考核类型和通过请求中的考核类型的参数查询问题，返回给前端数据并跳转到展示页面
  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String submitButton = RequestUtil.getString(req, "create");
    // 判断submitButton变量的长度判断用户是否点击了添加按钮
    if (submitButton.length() > 0) // 添加操作
    {
      doCreate(req, resp);
    } else {
      doList(req, resp);
    }
  }
  // 从request对象中取出考核类型ID、状态、题目类型三个字段，并将其封装到QuestionQueryParam对象中并返回
  // 调用QuestionServiceImpl类的find方法，将create对象中返回的对象以参数形式传入，在得到题目信息列表后封装到request对象中，并通过请求转发的形式跳转到question文件夹下的list.jsp页面
  protected void doList(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    List<AssessmentType> assessmentList = assessmentService.findAllAssessment();
    req.setAttribute("assessmentList", assessmentList);

    QuestionQueryParam param = QuestionQueryParam.create(req);
    req.setAttribute("query", param);
    // if (param.getAssessmentId() > 0) {
    List<Question> questionList = questionService.find(param);
    req.setAttribute("questionList", questionList);
    // }
    WebUtil.forward(req, resp, "/question/list.jsp");
  }

  protected void doCreate(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 首先获取一个param对象
    QuestionQueryParam param = QuestionQueryParam.create(req);
    // 再通过考核类型ID查找考核的信息，并存放到request对象中，用于页面中内容的显示
    req.setAttribute("assessment", assessmentService.findAssessmentById(param.getAssessmentId()));
    Question question = new Question();
    question.setType(param.getType());
    question.setDifficulty(4);
    req.setAttribute("question", question);
    WebUtil.forward(req, resp, "/question/create.jsp");
  }
}
