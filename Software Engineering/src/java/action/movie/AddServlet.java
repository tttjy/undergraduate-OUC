package com.qst.action.movie;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.SMMSUploader;
import com.qst.WebUtil;
import com.qst.entity.Movie;
import com.qst.service.IMovieService;
import com.qst.service.ServiceFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/movie/add.action")
@MultipartConfig
public class AddServlet extends BaseServlet {
  private final IMovieService movieService = ServiceFactory.getService(IMovieService.class);

  protected void doAction(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    //    movieService.addMovie(movie);
    Movie movie = new Movie();
    try {
      movie.setTitle(request.getParameter("title"));
      movie.setDescription(request.getParameter("description"));

      String dateStr = request.getParameter("date");
      if (dateStr == null || dateStr.isEmpty()) {
        throw new ExamException("日期不能为空");
      }

      dateStr = dateStr.replace('T', ' ');

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      Date parsedDate;
      try {
        parsedDate = dateFormat.parse(dateStr);
      } catch (ParseException e) {
        throw new ExamException("日期格式不正确");
      }
      Timestamp timestamp = new Timestamp(parsedDate.getTime());
      movie.setDate(timestamp);
      movie.setCapacity(Integer.parseInt(request.getParameter("capacity")));
      movie.setReservedSeats(0);

      Part filePart = request.getPart("poster"); // 获取文件
      String fileName =
          Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // 获取文件名
      InputStream fileContent = filePart.getInputStream(); // 获取文件内容
      File posterFile = new File(fileName);
      try (OutputStream outputStream = new FileOutputStream(posterFile)) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileContent.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      }

      SMMSUploader uploader = new SMMSUploader();
      String posterUrl = uploader.upload(posterFile);
      movie.setPosterUrl(posterUrl);

      movieService.addMovie(movie);
      addMessage(request, "电影信息已保存");
      WebUtil.redirect(request, response, "/movie/list.action");

    } catch (ExamException ex) {
      request.setAttribute("movie", movie);
      request.setAttribute("ERROR_KEY", ex.getMessage());
      System.out.println(ex.getMessage());
      WebUtil.forward(request, response, "/movie/manage.jsp");
    }
    //    response.sendRedirect(request.getContextPath()+"/movie/list.action");
  }
}