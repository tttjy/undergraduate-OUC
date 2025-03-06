package com.qst.action.AIQuiz;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * 文字转图像api
 */
public class WordToImageConverter {
  private static final String AK = "f2sePkCVOeOABKD9F021hWTP";
  private static final String SK = "YSyi6ecSEsv4u2AUb7LNIFhIy3I8Eqi9";

  public static String getAccessToken() throws IOException {
    String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="
        + AK + "&client_secret=" + SK;
    HttpURLConnection connection = createConnection(url, "POST");

    try (BufferedReader reader =
             new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      //            System.out.println("Access Token Response: " + response.toString());

      // 解析JSON响应，获取access token
      return response.toString().split("\"access_token\":\"")[1].split("\"")[0];
    }
  }

  public static String getTextToImage(String accessToken, String textInput) throws IOException {
    String url =
        "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/text2image/sd_xl?access_token="
        + accessToken;
    String postData = "{\"prompt\":\"" + textInput
        + "\",\"size\":\"1024x1024\",\"n\":1,\"steps\":20,\"sampler_index\":\"Euler a\"}";
    HttpURLConnection connection = createConnection(url, "POST");

    // 设置请求头
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));

    try (OutputStream os = connection.getOutputStream()) {
      os.write(postData.getBytes());
    }

    try (BufferedReader reader =
             new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      //            System.out.println("Text to Image Response: " + response.toString());

      // 解析JSON响应，获取Base64图像数据
      return response.toString().split("\"b64_image\":\"")[1].split("\"")[0];
    }
  }

  public static HttpURLConnection createConnection(String urlString, String method)
      throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(method);
    connection.setDoOutput(true);
    return connection;
  }

  // 程序入口，输入字符串，产生图像

  public static String word2pix(String text) throws IOException {
    String textInput = text; // 替换为所需的文本输入
    String accessToken = getAccessToken();
    //            System.out.println("Access Token: " + accessToken);
    String base64Image = getTextToImage(accessToken, textInput);

    //            saveBase64ImageToFile(base64Image, outputPath);
    return base64Image;
  }
}
