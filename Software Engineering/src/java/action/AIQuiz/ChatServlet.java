package com.qst.action.AIQuiz;
import static com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary.shouldInclude;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.qst.action.AIQuiz.WordToImageConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AIQuiz/ChatServlet")
public class ChatServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    String question1 = request.getParameter("question1");
    String question2 = request.getParameter("question2");
    String question3 = request.getParameter("question3");
    String question4 = request.getParameter("question4");
    String question5 = request.getParameter("question5");
    String question6 = request.getParameter("question6");
    String question7 = request.getParameter("question7");
    String question8 = request.getParameter("question8");
    String question9 = request.getParameter("question9");
    String question10 = request.getParameter("question10");

    String selectedOptions = question1 + " " + question2 + " " + question3 + " " + question4 + " "
        + question5 + " " + question6 + " " + question7 + " " + question8 + " " + question9 + " "
        + question10;
    System.out.println(selectedOptions);
    String chatResult = BaiduAIAPI.exec(selectedOptions);

    // 为什么加上这段代码就运行不了了，不知道

    String keyword = TextRankKeyword.extractKeywords("", chatResult);

    // 将图像的二进制数据以 Base64 编码
    String base64Image = WordToImageConverter.word2pix(keyword);

    request.setAttribute("imageData", base64Image);
    request.setAttribute("chatResult", chatResult);

    // 转发到 JSP 页面进行显示
    request.getRequestDispatcher("/AIQuiz/chatResult.jsp").forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    request.getRequestDispatcher("/AIQuiz/chatResult.jsp").forward(request, response);
  }

  public static void saveBase64ImageToFile(String base64Image, String imageName) {
    try {
      // 解码Base64字符串
      byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

      // 将字节数组写入文件
      Files.write(Paths.get(imageName), decodedBytes, StandardOpenOption.CREATE);

      System.out.println("Image saved successfully: " + imageName);
    } catch (IOException e) {
      System.err.println("Error saving image: " + e.getMessage());
    }
  }
}
