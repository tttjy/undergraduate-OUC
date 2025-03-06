package com.qst.action.team;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.WebUtil;
import com.qst.entity.User;
import com.qst.service.ITeamService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 王世广
 * @usage 列出当前登录教师创建的所有班级,forward到list.jsp显示
 */
@WebServlet("/team/list.action")
public class ListServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取当前登录的用户
    User user = getCurrentUser(req);
    try {
      if (user.getType() == 1) {
        req.setAttribute("teamList", classTeamService.findAll());
      } else {
        req.setAttribute("teamList", classTeamService.findByCreator(user.getId()));
      }

    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    WebUtil.forward(req, resp, "/team/list.jsp");
  }
}
