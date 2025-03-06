package com.qst.service;

import com.qst.entity.Schedule;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import java.util.List;

public interface IScheduleService {
  List<Schedule> findByCreator(User user);
  Schedule findById(Integer id);
  void saveSchedule(Schedule h);
  void updateSchedule(Schedule h);
  Schedule deleteSchedule(Integer id);
}
