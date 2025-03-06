package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.action.question.QuestionQueryParam;
import com.qst.dao.ChoiceDAO;
/*import com.qst.dao.ChoiceDAO;*/
import com.qst.dao.DAOFactory;
import com.qst.dao.DimensionDAO;
import com.qst.dao.QuestionDAO;
import com.qst.entity.Choice;
import com.qst.entity.PersonalityDimension;
import com.qst.entity.Question;
import com.qst.service.IQuestionService;
import java.util.List;

public class QuestionServiceImpl implements IQuestionService {
  private final QuestionDAO questionDAO = DAOFactory.getDAO(QuestionDAO.class);
  private final ChoiceDAO choiceDAO = DAOFactory.getDAO(ChoiceDAO.class);
  private final DimensionDAO dimensionDAO = DAOFactory.getDAO(DimensionDAO.class);

  @Override
  public List<Question> find(QuestionQueryParam param) {
    return questionDAO.find(param);
  }
  @Override
  public Question findById(int id) {
    return questionDAO.findById(id);
  }

  @Override
  public List<Choice> findChoices(Integer id) {
    return choiceDAO.findByQuestion(id);
  }
  @Override
  public List<PersonalityDimension> findDimensionByQuestion(Integer id) {
    return dimensionDAO.findDimensionByQuestion(id);
  }
  @Override
  public void save(Question q, List<Choice> choices) {
    // 校验输入的题目信息是否有误
    // 当校验通过的时候将题目的状态设置为可用状态
    isChoiceValid(q.getType(), choices);
    q.setStatus(2);
    // 这个insert方法里有获取考题id的逻辑
    // 将题目内容添加到数据库中
    questionDAO.insert(q);
    for (Choice ch : choices) {
      // 上面的insert方法里有获取考题id的逻辑
      // 通过遍历选项集合，将选项与题目主键关联
      ch.setQuestionId(q.getId());
      choiceDAO.insert(ch);
    }
  }
  private void isChoiceValid(int type, List<Choice> choices) {
    int count = 0;
    for (Choice ch : choices) {
      if (ch.isChecked()) {
        count++;
      }
    }
    if (count == 0) {
      throw new ExamException("请选择本题的正确选项");
    }
    if (type == 1 && count != 1) {
      throw new ExamException("单选题只能有一个正确选项");
    }
    if (type == 2 && count < 2) {
      throw new ExamException("多选题至少要有两个正确选项");
    }
  }
  @Override
  public void update(Question q, List<Choice> choices) {
    isChoiceValid(q.getType(), choices);
    questionDAO.update(q);
    for (Choice ch : choices) {
      ch.setQuestionId(q.getId());
      choiceDAO.update(ch);
    }
  }
  @Override
  public void delete(int questionId) {
    // 首先校验题目是否在使用
    if (questionDAO.isUsed(questionId)) {
      // 如果在使用，则抛出异常，提示使用中的试题，不能被删除
      throw new ExamException("使用中的试题，不能被删除");
    }
    // 校验通过，删除题目与问卷维度的对应关系、删除题目的选项、删除题目本身
    questionDAO.detachDimension(questionId);
    choiceDAO.deleteByQuestion(questionId);
    questionDAO.delete(questionId);
  }

  // 将题目与问卷维度关联
  @Override
  public void attachDimension(int questionId, int[] dimension) {
    // 通过题目ID删除原先的关联关系
    questionDAO.detachDimension(questionId);
    for (int p : dimension) {
      // 遍历已选择问卷维度ID的数组
      questionDAO.attachDimension(questionId, p); // 将新的题目问卷维度关联关系添加到数据库中
    }
  }
  @Override
  public List<Integer> findDimensionIdByQuestion(int id) {
    return questionDAO.findDimension(id);
  }
}
