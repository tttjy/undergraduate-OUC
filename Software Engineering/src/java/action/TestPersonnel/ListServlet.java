
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

/**
 * 这个Servlet的主要功能是获取与特定班级相关的测试人员列表以及所有的团队列表，
 * 并将这些数据设置到请求属性中，以便在JSP页面中使用和显示。
 */
// 注解将该类标记为Web Servlet，并定义了它的URL映射路径为"/testPersonnel/list.action"
// 用户在浏览器中输入或点击与此URL路径匹配的网址时，此Servlet将处理该HTTP请求
@WebServlet("/testPersonnel/list.action")
public class ListServlet extends BaseServlet {
  // Servlet中声明了两个成员变量，分别是 classTeamService 和
  // testPersonnelService，它们用于获取班级团队和测试人员的相关数据。
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 代码从HTTP请求中获取班级id
    int tid = RequestUtil.getInt(req, "tid"); // 它从请求中获取名为“tid”的参数值，该参数代表班级的ID
    // testPersonnelService的findByTeamId方法来获取与该班级相关的测试人员列表。
    List<TestPersonnel> testPersonnelList = testPersonnelService.findByTeamId(tid);
    // 这段代码将testPersonnelList作为一个属性添加到req对象中，属性名为testPersonnel。
    req.setAttribute("testPersonnel", testPersonnelList);
    ////findAll方法来获取所有的团队列表，并将其设置到请求属性中，属性名为“teamList”
    List<Team> teamList = classTeamService.findAll();
    req.setAttribute("teamList", teamList);
    // 使用WebUtil.forward方法将请求转发到/testPersonnel/list.js
    WebUtil.forward(req, resp, "/testPersonnel/list.jsp");
  }
}

/**
 *
 */