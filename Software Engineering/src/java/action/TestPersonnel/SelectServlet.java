package com.qst.action.TestPersonnel;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.Team;
import com.qst.entity.TestPersonnel;
import com.qst.service.ITeamService;
import com.qst.service.ITestPersonnelService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 通过URL映射路径 /testPersonnel/select.action 来接收请求。
@WebServlet("/testPersonnel/select.action")
public class SelectServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取班级id
    int teamId = RequestUtil.getInt(req, "teamId");
    String name = RequestUtil.getString(req, "stuname");
    String phone = RequestUtil.getString(req, "phone");

    // 调用测试人员服务的query方法，根据班级id、姓名和手机号查询测试人员信息
    List<TestPersonnel> testPersonnel = testPersonnelService.query(teamId, name, phone);

    // 将查询结果设置为请求属性，以供JSP页面使用和显示
    req.setAttribute("testPersonnel", testPersonnel);

    // 获取所有的班级列表，并设置为请求属性
    List<Team> teamList = classTeamService.findAll();
    req.setAttribute("teamList", teamList);

    // 使用WebUtil类的forward方法将请求转发到/list.jsp页面
    WebUtil.forward(req, resp, "/testPersonnel/list.jsp");
  }
}
