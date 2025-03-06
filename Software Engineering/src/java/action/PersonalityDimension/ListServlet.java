package com.qst.action.PersonalityDimension;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.AssessmentType;
import com.qst.entity.PersonalityDimension;
import com.qst.service.IAssessmentService;
import com.qst.service.IDimensionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dimension/list.action")
public class ListServlet extends BaseServlet {
  private final IAssessmentService assessmentService =
      ServiceFactory.getService(IAssessmentService.class);
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String submitButton = RequestUtil.getString(req, "create");
    if (submitButton.length() > 0) // 添加操作
    {
      doCreate(req, resp);
    } else {
      doList(req, resp);
    }
  }

  // 用于添加问卷维度的跳转控制
  // 通过考核类型ID查找出考核类型信息，并通过请求转发到dimension文件夹下的create.jsp页面
  private void doCreate(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int assessmentId = RequestUtil.getInt(req, "id");
    AssessmentType assessment = assessmentService.findAssessmentById(assessmentId);
    req.setAttribute("assessment", assessment);
    WebUtil.forward(req, resp, "/dimension/create.jsp");
  }

  // 调用AssessmentServiceImpl的findAllAssessment方法，查询出所有的考核类型列表，
  // 再通过考核类型ID查找问卷维度列表，并跳转到dimension文件夹下的list.jsp页面
  private void doList(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute("assessmentList", assessmentService.findAllAssessment());
    // 如果是点击左边导航栏进来，则没有ID，所以就不显示知识点列表
    // 如果是点击查看知识点按钮进来的，就有科目ID，所以就显示该科目下的知识点列表
    int id = RequestUtil.getInt(req, "id");
    List<PersonalityDimension> dimensionList;
    if (id > 0) {
      dimensionList = dimensionService.findDimensionByAssessment(id);
    } else {
      dimensionList = new ArrayList<>();
    }
    req.setAttribute("dimensionList", dimensionList);
    WebUtil.forward(req, resp, "/dimension/list.jsp");
  }
}
