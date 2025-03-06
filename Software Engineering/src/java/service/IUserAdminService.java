package com.qst.service;

import com.qst.entity.User;
import java.util.List;

public interface IUserAdminService {
  List<User> findUsers();
  User findUserById(int id); // 添加查询用户的接口
  void saveUser(User u);
  void updateUser(User user); // 修改
  void deleteUserById(int id);
  void resetPassword(int id);
}
