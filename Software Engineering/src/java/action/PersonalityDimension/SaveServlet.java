package com.qst.action.PersonalityDimension;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.PersonalityDimension;
import com.qst.service.IAssessmentService;
import com.qst.service.IDimensionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dimension/save.action")
public class SaveServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PersonalityDimension kp = new PersonalityDimension();
    kp.setTitle(RequestUtil.getString(req, "title"));
    kp.setDepict(RequestUtil.getString(req, "depict"));
    kp.setAssessmentId(RequestUtil.getInt(req, "assessmentId"));
    try {
      dimensionService.saveDimension(kp); // 对问卷维度信息进行添加
      addMessage(
          req, "问卷维度信息已保存到数据库"); // 在请求中添加问卷维度保存提示，并跳转到显示详情请求
      WebUtil.redirect(req, resp, "/dimension/view.action?id=" + kp.getId());
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      req.setAttribute("dimension", kp);
      req.setAttribute("assessment", assessmentService.findAssessmentById(kp.getAssessmentId()));
      WebUtil.forward(req, resp, "/dimension/create.jsp");
    }
  }
}
