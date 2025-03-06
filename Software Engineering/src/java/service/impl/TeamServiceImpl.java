package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.DAOFactory;
import com.qst.dao.TeamDAO;
import com.qst.dao.TestPersonnelDAO;
import com.qst.entity.Team;
import com.qst.entity.TestPersonnel;
import com.qst.service.ITeamService;
import java.sql.SQLException;
import java.util.List;

public class TeamServiceImpl implements ITeamService {
  private final TeamDAO classTeamDAO = DAOFactory.getDAO(TeamDAO.class);
  private final TestPersonnelDAO testPersonnelDAO = DAOFactory.getDAO(TestPersonnelDAO.class);

  @Override
  public List<Team> findByCreator(Integer creator_id) {
    try {
      return classTeamDAO.findByCreator(creator_id);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }
  @Override
  public List<Team> findAll() {
    try {
      return classTeamDAO.findAll();
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }
  @Override
  public Team findById(Integer id) {
    try {
      return classTeamDAO.findById(id);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }
  @Override
  public void saveTeam(Team t) {
    try {
      classTeamDAO.save(t);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }

  @Override
  public void updateTeam(Team t) {
    try {
      classTeamDAO.update(t);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }
  @Override
  public void deleteTeam(int id) {
    try {
      List<TestPersonnel> personnels = testPersonnelDAO.findByTeam(id);
      if (personnels.size() > 0) {
        throw new ExamException("有参测人员的批次不能删除");
      }
      classTeamDAO.delete(id);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }
  /*






          */
}
