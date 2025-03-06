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

@WebServlet("/dimension/update.action")
public class UpdateServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PersonalityDimension kp = new PersonalityDimension();
    kp.setId(RequestUtil.getInt(req, "id"));
    kp.setTitle(RequestUtil.getString(req, "title"));
    kp.setDepict(RequestUtil.getString(req, "depict"));
    kp.setAssessmentId(RequestUtil.getInt(req, "assessmentId"));
    try {
      // 修改问卷维度信息，添加问卷维度保存状态到请求中并跳转到dimension/view.action映射中，显示修改问卷维度的信息
      dimensionService.updateDimension(kp);
      addMessage(req, "问卷维度信息已保存到数据库");
      WebUtil.redirect(req, resp, "/dimension/view.action?id=" + kp.getId());
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      req.setAttribute("dimension", kp);
      req.setAttribute("assessment", assessmentService.findAssessmentById(kp.getAssessmentId()));
      WebUtil.forward(req, resp, "/dimension/edit.jsp");
    }
  }
}
