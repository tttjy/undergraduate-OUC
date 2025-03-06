package com.qst.dao;

import java.util.HashMap;
import java.util.Map;

public class DAOFactory {
  private static final Map<Class, Object> daos = new HashMap<>();

  static {
    daos.put(UserDAO.class, new UserDAO());
    daos.put(QuestionDAO.class, new QuestionDAO());
    daos.put(DimensionDAO.class, new DimensionDAO());
    daos.put(ChoiceDAO.class, new ChoiceDAO());
    daos.put(TeamDAO.class, new TeamDAO());
    daos.put(TestPersonnelDAO.class, new TestPersonnelDAO());
    daos.put(ScheduleDAO.class, new ScheduleDAO());
    daos.put(ExamDAO.class, new ExamDAO());
    daos.put(ExamQuestionDAO.class, new ExamQuestionDAO());
    daos.put(AssessmentTypeDAO.class, new AssessmentTypeDAO());
    daos.put(MovieDAO.class, new MovieDAO());
    daos.put(ReservationDAO.class, new ReservationDAO());
  }

  public static <T> T getDAO(Class<T> clazz) {
    return (T) daos.get(clazz);
  }
}
