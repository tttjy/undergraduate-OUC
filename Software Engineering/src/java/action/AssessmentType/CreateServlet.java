package com.qst.action.AssessmentType;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/assessment/create.action")
public class CreateServlet extends BaseServlet {
  // private IassessmentService assessmentService =
  // ServiceFactory.getService(IassessmentService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    WebUtil.forward(req, resp, "/assessment/create.jsp");
  }
}
