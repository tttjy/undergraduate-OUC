package com.qst.action.AssessmentType;

import com.qst.BaseServlet;
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

// 在doAction方法中调用AssessmentServiceImpl的findAssessmentById方法，通过考核类型ID查找考核类型信息，并在assessment文件夹下的view.jsp页面显示
@WebServlet("/assessment/view.action")
public class ViewServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    // String str = req.getParameter("id");
    // int id = Integer.parseInt(str);
    AssessmentType assessment = assessmentService.findAssessmentById(id);
    req.setAttribute("assessment", assessment);
    WebUtil.forward(req, resp, "/assessment/view.jsp");
  }
}
