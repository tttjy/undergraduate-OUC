package com.qst.service;

import com.qst.entity.TestPersonnel;
import java.util.List;

public interface ITestPersonnelService {
  List<TestPersonnel> findByTeamId(Integer id);
  TestPersonnel findById(int id);
  void importTestPersonnel(List<TestPersonnel> testPersonnelList);
  void updateTestPersonnel(TestPersonnel s);
  void addTestPersonnel(TestPersonnel s);
  List<TestPersonnel> query(int teamId, String name, String stdNo);
  TestPersonnel deleteTestPersonnel(int id);
}
