package com.qst.service;

import com.qst.entity.PersonalityDimension;
import java.util.List;

public interface IDimensionService {
  List<PersonalityDimension> findDimensionByAssessment(int assessmentId);
  PersonalityDimension findDimensionById(int id);
  void saveDimension(PersonalityDimension kp);
  void updateDimension(PersonalityDimension kp);
  void deleteDimension(int id);
}
