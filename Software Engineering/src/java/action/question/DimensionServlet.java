package com.qst.action.question;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.PersonalityDimension;
import com.qst.entity.Question;
import com.qst.service.IDimensionService;
import com.qst.service.IQuestionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 关联问卷维度，通过调用数据库中的数据保存到数据库
// 数据保存成功后，通过重定向的方式跳转到/question/dimension.action中
@WebServlet("/question/dimension.action")
public class DimensionServlet extends BaseServlet {
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String method = RequestUtil.getString(req, "method");
    // 通过method的值判断修改和添加
    if ("save".equals(method)) {
      save(req, resp);
    } else {
      // 因为SaveAction在跳转时，只传递题目ID，所以，在DimensionServlet中调用edit方法
      edit(req, resp);
    }
  }

  protected void edit(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 首先获取题目ID，根据题目ID查询题目内容，将题目内容存储到request对象中
    int id = RequestUtil.getInt(req, "id");
    // 下一步通过考核类型ID查找本类型下的所有问卷维度
    Question question = questionService.findById(id);
    req.setAttribute("question", question);
    List<PersonalityDimension> dimensionList =
        dimensionService.findDimensionByAssessment(question.getAssessmentId());
    req.setAttribute("dimensionList", dimensionList);
    // 下一步查询题目是否已经关联了问卷维度内容，通过QuestionServiceImpl中的findDimensionIdByQuestion方法，查找题目已经关联的问卷维度ID
    List<Integer> attachedDimension = questionService.findDimensionIdByQuestion(id);
    for (PersonalityDimension p : dimensionList) {
      // 将关联的问卷维度与考核类型下所有的问卷维度进行匹配
      // 如果匹配上，就在PersonalityDimension中的map集合extras中，将键都设置成checked
      // 如果问卷维度ID匹配上了，值就是checked，如果没有匹配上则为空
      p.getExtras().put("checked", attachedDimension.contains(p.getId()) ? "checked" : "");
    }
    // 最后通过请求转发的方式跳转到question文件夹下的dimension.jsp页面。
    WebUtil.forward(req, resp, "/question/dimension.jsp");
  }

  protected void save(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int qid = RequestUtil.getInt(req, "id");
    if (qid == 0) {
      qid = RequestUtil.getInt(req, "qid");
    }
    int[] pids = RequestUtil.getIntArray(req, "pid");
    if (pids == null) {
      Question question = questionService.findById(qid);
      List<PersonalityDimension> dimensionList =
          dimensionService.findDimensionByAssessment(question.getAssessmentId());
      req.setAttribute("dimensionList", dimensionList);
      List<Integer> attachedDimension = questionService.findDimensionIdByQuestion(qid);
      for (PersonalityDimension p : dimensionList) {
        p.getExtras().put("checked", attachedDimension.contains(p.getId()) ? "checked" : "");
      }
      req.setAttribute("error", "你还未关联任何维度！");
      req.setAttribute("qid", qid);
      WebUtil.forward(req, resp, "/question/dimension.jsp");
    }
    questionService.attachDimension(qid, pids);
    WebUtil.redirect(req, resp, "/question/view.action?id=" + qid);
  }
}
