package com.qst.action.TestPersonnel;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.ITeamService;
import com.qst.service.ITestPersonnelService;
import com.qst.service.ServiceFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// 1、导入参测人员按钮的映射地址为"/testPersonnel/import.action"，映射地址com.qst.action.TestPersonnel包下的ImportServlet，参考给出的文档模板编写参测人员信息到txt文件中，选择好文件后，进行上传。
//
// 2、当点击“导入参测人员”按钮的时候，通过请求转发的方式转到testPersonnel/import.jsp页面，在WebContent/testPersonnel目录下创建import.jsp页面用来实现用户导入文件。
//
// 3、当导入文件后会再返回到ImportServlet控制器中进行文件中文档格式的校验，接收文件并判断文件格式，如果文件格式不正确则返回提示，否则根据文件内容生成ArrayList集合，并调用service层方法批量添加到数据库中，最后返回展示页面。
//  Declare this class as a servlet for handling file uploads
@WebServlet("/testPersonnel/import.action")
@MultipartConfig
public class ImportServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  // Handle GET requests
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Redirect to the import page, allowing users to choose a file for upload
    WebUtil.forward(req, resp, "/testPersonnel/import.jsp");
  }

  // Handle POST requests
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Get the team ID from the request
    int tid = RequestUtil.getInt(req, "tid");

    // Get the uploaded file
    Part part = req.getPart("file");

    // Create a file reader
    BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()));
    String line;
    List<TestPersonnel> testPersonnelList = new ArrayList<>();

    // Read the uploaded file line by line
    while ((line = reader.readLine()) != null) {
      // Use a comma to separate data in each line
      String[] data = line.split(",");
      if (data.length < 4) {
        // If the data format is incorrect, add an error message and redirect to the import page
        addError(req, "Import file format error: " + line);
        WebUtil.forward(req, resp, "/testPersonnel/import.jsp");
        return;
      }
      // Create TestPersonnel and User objects
      TestPersonnel testPersonnel = new TestPersonnel();
      User user = new User();
      // Set the team ID for the TestPersonnel object
      testPersonnel.setTeamId(tid);
      // Associate the User object with the TestPersonnel object
      testPersonnel.setUser(user);
      // Set the phone number for the TestPersonnel object from the CSV data
      testPersonnel.setPhone(data[0]);
      // Set the name for the associated User object from the CSV data
      user.setName(data[1]);
      // Set the gender for the TestPersonnel object from the CSV data
      testPersonnel.setGender(data[2]);
      // Set the birth date for the TestPersonnel object by converting the CSV data to a Date object
      testPersonnel.setBirthDate(Date.valueOf(data[3]));
      // Add the TestPersonnel object to the list for further processing
      testPersonnelList.add(testPersonnel);
    }

    try {
      // Call a service layer method to import the data into the database
      testPersonnelService.importTestPersonnel(testPersonnelList);
      // Add a success message and redirect to the test personnel list page
      addMessage(req, "Successfully imported " + testPersonnelList.size() + " test personnel");
      WebUtil.redirect(req, resp, "/testPersonnel/list.action?tid=" + tid);
    } catch (ExamException ex) {
      // If an exception occurs during import, add an error message and redirect to the team list
      // page
      addError(req, ex.getMessage());
      WebUtil.redirect(req, resp, "/team/list.action");
    }
  }
}
