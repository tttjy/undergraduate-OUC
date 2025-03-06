package com.qst.service;

import com.qst.action.question.QuestionQueryParam;
import com.qst.entity.Choice;
import com.qst.entity.PersonalityDimension;
import com.qst.entity.Question;
import java.util.List;

public interface IQuestionService {
  List<Question> find(QuestionQueryParam param);
  Question findById(int id); // 通过题目ID查询题目
  List<Choice> findChoices(Integer id); // 通过题目ID查询题目选项
  List<PersonalityDimension> findDimensionByQuestion(Integer id); // 通过题目ID查询问卷维度
  void save(Question q, List<Choice> choices);
  List<Integer> findDimensionIdByQuestion(int id);
  void attachDimension(int questionId, int[] dimension);
  void update(Question q, List<Choice> choices);
  void delete(int questionId);

  /*












          */
}
