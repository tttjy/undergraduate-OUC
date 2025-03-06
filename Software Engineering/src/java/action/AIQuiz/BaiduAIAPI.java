package com.qst.action.AIQuiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
// 生成文字

/**
 * 文心一言生成文字的api
 */
public class BaiduAIAPI {
  public static String exec(String allAnswers) {
    //        System.out.println("allanswer"+allAnswers);
    // 替换为你的百度AI密钥
    final String AK = "f2sePkCVOeOABKD9F021hWTP";
    final String SK = "YSyi6ecSEsv4u2AUb7LNIFhIy3I8Eqi9";
    String access_token = "24.ee9b9b5a5e0df15bbafecbec37d08e26.2592000.1703130934.282335-43396849";
    String resultFinal = null;

    try {
      // 获取访问令牌
      URL tokenUrl = new URL(
          "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + AK
          + "&client_secret=" + SK);
      HttpURLConnection tokenConnection = (HttpURLConnection) tokenUrl.openConnection();
      tokenConnection.setRequestMethod("POST");
      tokenConnection.setRequestProperty("Content-Type", "application/json");

      BufferedReader tokenReader =
          new BufferedReader(new InputStreamReader(tokenConnection.getInputStream()));
      StringBuilder tokenResponse = new StringBuilder();
      String line;
      while ((line = tokenReader.readLine()) != null) {
        tokenResponse.append(line);
      }
      tokenReader.close();

      // 获取访问令牌成功后更新变量
      String tokenData = tokenResponse.toString();
      String accessTokenKey = "\"access_token\":\"";
      int accessTokenIndex = tokenData.indexOf(accessTokenKey);
      if (accessTokenIndex != -1) {
        int accessTokenStart = accessTokenIndex + accessTokenKey.length();
        int accessTokenEnd = tokenData.indexOf("\"", accessTokenStart);
        if (accessTokenEnd != -1) {
          access_token = tokenData.substring(accessTokenStart, accessTokenEnd);
        }
      }

      // 执行聊天请求
      URL chatUrl = new URL(
          "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token="
          + access_token);
      HttpURLConnection chatConnection = (HttpURLConnection) chatUrl.openConnection();
      chatConnection.setRequestMethod("POST");
      chatConnection.setRequestProperty("Content-Type", "application/json");
      chatConnection.setDoOutput(true);
      OutputStream chatOutputStream = chatConnection.getOutputStream();

      String params = "{\"messages\": [{ \"role\": \"user\", \"content\": \""
          + "有十个题1. 对关系最密切的人是否满意?2.2. 看到最近拍摄的照片有何想法?3. 你喜欢吃炸酱面还是拉面?，4. 你喜欢旅游吗?答案为：5. 你喜欢玩游戏吗?6. 你喜欢玩原神吗?7. 你喜欢文科还是理科?8. 你喜欢小动物吗?9. 你喜欢喝可乐吗?10. 你经常做噩梦吗?"
          + allAnswers + "用一句话总结情绪"
          + "\" }]}";
      System.out.println(allAnswers);
      chatOutputStream.write(params.getBytes(StandardCharsets.UTF_8));
      chatOutputStream.flush();

      // Check the response headers
      Map<String, List<String>> headers = chatConnection.getHeaderFields();

      // Read the response
      BufferedReader chatReader = new BufferedReader(
          new InputStreamReader(chatConnection.getInputStream(), StandardCharsets.UTF_8));
      StringBuilder chatResponse = new StringBuilder();
      while ((line = chatReader.readLine()) != null) {
        chatResponse.append(line);
      }
      chatReader.close();

      // 输出聊天结果
      String chatData = chatResponse.toString();

      // 解析聊天结果
      String resultKey = "\"result\":\"";
      int resultIndex = chatData.indexOf(resultKey);
      if (resultIndex != -1) {
        int resultStart = resultIndex + resultKey.length();
        int resultEnd = chatData.indexOf("\"", resultStart);
        if (resultEnd != -1) {
          resultFinal = chatData.substring(resultStart, resultEnd);
          System.out.println("答案：" + resultFinal);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(resultFinal + "result");

    String result = resultFinal.replace("\\n", "");

    return result;
  }
}
