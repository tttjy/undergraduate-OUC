package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.DAOFactory;
import com.qst.dao.DimensionDAO;
import com.qst.dao.QuestionDAO;
import com.qst.entity.PersonalityDimension;
import com.qst.service.IDimensionService;
import java.util.List;

public class DimensionServiceImpl implements IDimensionService {
  private final DimensionDAO dimensionDAO = DAOFactory.getDAO(DimensionDAO.class);
  private final QuestionDAO questionDAO = DAOFactory.getDAO(QuestionDAO.class);

  @Override
  public List<PersonalityDimension> findDimensionByAssessment(int assessmentId) {
    return dimensionDAO.findByAssessment(assessmentId);
  }

  @Override
  public PersonalityDimension findDimensionById(int id) {
    return dimensionDAO.findById(id);
  }

  // 先查询同一个考核类型中是否有相同标题的问卷维度，如果不存在则添加问卷维度，否则抛出异常
  @Override
  public void saveDimension(PersonalityDimension kp) {
    PersonalityDimension temp =
        dimensionDAO.findByAssessmentAndTitle(kp.getAssessmentId(), kp.getTitle());
    if (temp == null) {
      dimensionDAO.insert(kp);

    } else {
      throw new ExamException("问卷维度名称已存在");
    }
  }

  // 首先调用findByAssessmentAndTitle()通过考核类型ID和问卷维度名称查找问卷维度，如果问卷维度已经存在，则抛出异常信息
  // 如果校验通过，调用update修改问卷维度信息，通过传入问卷维度对象向数据库中添加问卷维度
  @Override
  public void updateDimension(PersonalityDimension kp) {
    PersonalityDimension temp =
        dimensionDAO.findByAssessmentAndTitle(kp.getAssessmentId(), kp.getTitle());
    if (temp == null || temp.getId().equals(kp.getId())) {
      dimensionDAO.update(kp);
    } else {
      throw new ExamException("本问卷维度名称已存在");
    }
  }

  // 首先调用findCountByDimension通过问卷维度查询是否有题目关联了本问卷维度
  // 如果问卷维度已经被题目关联，则抛出异常并提示本问卷维度已录入题目，不能再删除
  @Override
  public void deleteDimension(int id) {
    int count = questionDAO.findCountByDimension(id);
    if (count > 0) {
      throw new ExamException("本知识点已录入考题，不能再删除");
    }
    dimensionDAO.delete(id);
  }
}
