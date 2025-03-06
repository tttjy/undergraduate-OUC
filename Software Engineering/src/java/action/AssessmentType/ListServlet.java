package com.qst.action.AssessmentType;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.service.IAssessmentService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/assessment/list.action")
public class ListServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    List<AssessmentType> assessmentList = assessmentService.findAllAssessment(); // 查找考核类型列表
    req.setAttribute("assessmentList", assessmentList);
    WebUtil.forward(req, resp, "/assessment/list.jsp"); // 在assessment文件夹下的list.jsp页面显示
  }
}
