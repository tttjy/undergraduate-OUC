package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.AssessmentTypeDAO;
import com.qst.dao.DAOFactory;
import com.qst.dao.QuestionDAO;
import com.qst.entity.AssessmentType;
import com.qst.service.IAssessmentService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AssessmentServiceImpl implements IAssessmentService { // 实现IAssessmentService接口
  private final AssessmentTypeDAO assessmentDAO = DAOFactory.getDAO(AssessmentTypeDAO.class);
  private final QuestionDAO questionDAO = DAOFactory.getDAO(QuestionDAO.class);

  // findAll方法用于在数据库中查询出所有考核类型信息
  public List<AssessmentType> findAll() {
    return assessmentDAO.findAll();
  }

  private AssessmentType createAssessmentType(ResultSet rs) throws SQLException {
    AssessmentType sj = new AssessmentType();
    sj.setId(rs.getInt("id"));
    sj.setTitle(rs.getString("title"));
    sj.setCost(rs.getDouble("cost"));
    sj.setStatus(rs.getInt("status"));
    return sj;
  }

  // 在AssessmentServiceImpl中调用AssessmentTypeDao的findById方法，通过考核类型ID在数据库中查询考核类型信息的方法，在AssessmentTypeDAO类中添加findById方法，通过id查询考核类型
  public AssessmentType findById(int id) {
    return assessmentDAO.findById(id);
  }

  @Override
  public List<AssessmentType> findAllAssessment() {
    return assessmentDAO.findAll(); // 查询所有考核类型
  }

  @Override
  public AssessmentType findAssessmentById(int id) {
    return assessmentDAO.findById(id); // 数据查询
  }

  // 通过查询考核标题是否存在判断是否重复，如果重复则抛出异常，不重复则调用Dao层方法修改考核类型
  @Override
  public void updateAssessment(AssessmentType assessment) {
    AssessmentType temp = assessmentDAO.findByTitle(assessment.getTitle());
    if (temp != null && !temp.getId().equals(assessment.getId())) {
      throw new ExamException("考核类型重复");
    }
    assessmentDAO.update(assessment);
  }

  // 通过查询此考核类型的标题是否存在，如果存在则抛出异常，否则添加考核类型数据到数据库中
  @Override
  public void saveAssessment(AssessmentType assessment) {
    AssessmentType temp = assessmentDAO.findByTitle(assessment.getTitle());
    if (temp != null) {
      throw new ExamException("考核类型已存在");
    }
    assessmentDAO.insert(assessment);
  }

  // 先查看本考核类型中有没有试题，如果没用则调用删除方法，否则抛出异常
  @Override
  public void deleteAssessment(int id) {
    if (questionDAO.findCountByAssessment(id) > 0) {
      throw new ExamException("本考核类型已有试题，不能删除");
    }
    assessmentDAO.delete(id);
  }
}
