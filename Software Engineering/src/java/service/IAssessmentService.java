package com.qst.service;

import com.qst.entity.AssessmentType;
import java.util.List;

public interface IAssessmentService {
  List<AssessmentType> findAllAssessment();
  AssessmentType findAssessmentById(int id);
  void updateAssessment(AssessmentType assessmentType); // 修改考核类型信息
  void saveAssessment(AssessmentType assessmentType); // 通过接收考核类型对象保存考核类型
  void deleteAssessment(int id);
}
