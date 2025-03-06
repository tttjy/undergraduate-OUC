package com.qst.action.user;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.WebUtil;
import com.qst.entity.Team;
import com.qst.service.ITeamService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 用.do是为了避免被登录验证的过滤器过滤
@WebServlet("/user/showReg.do")
public class ShowRegServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      List<Team> teamList = classTeamService.findAll();
      req.setAttribute("teamList", teamList);
      WebUtil.forward(req, resp, "/user/reg.jsp");
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      // req.setAttribute("ERROR_KEY", ex.getMessage());
      // WebUtil.forward(req, resp, "/user/create.jsp");
    }
  }
}