package com.qst.service;

import com.qst.service.impl.*;
import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
  private static final Map<Class<?>, Object> services = new HashMap<>();

  static {
    services.put(IScheduleService.class, new ScheduleServiceImpl());
    services.put(IQuestionService.class, new QuestionServiceImpl());
    services.put(IDimensionService.class, new DimensionServiceImpl());
    services.put(IAssessmentService.class,
        new AssessmentServiceImpl()); // 添加创建IAssessmentService接口实例
    services.put(IUserAdminService.class, new UserAdminServiceImpl());
    services.put(IUserService.class, new UserServiceImpl());
    services.put(ITeamService.class, new TeamServiceImpl());
    services.put(ITestPersonnelService.class, new TestPersonnelServiceImpl());
    services.put(IExamService.class, new ExamServiceImpl());
    services.put(IMovieService.class, new MovieServiceImpl());
    services.put(IReservationService.class, new ReservationServiceImpl());
  }

  public static <T> T getService(Class<T> clazz) {
    return clazz.cast(services.get(clazz));
  }
}
