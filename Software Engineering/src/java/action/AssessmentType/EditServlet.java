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

// 用来接收前端发送的信息，然后通过调用service层方法查询并将数据返回到前端页面edit.jsp中
@WebServlet("/assessment/edit.action")
public class EditServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    AssessmentType assessment = assessmentService.findAssessmentById(id);
    req.setAttribute("assessment", assessment);
    WebUtil.forward(req, resp, "/assessment/edit.jsp");
  }
}
