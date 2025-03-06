package com.qst;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
public class SMMSUploader {
  private static final String UPLOAD_URL = "https://sm.ms/api/v2/upload";
  private static final String API_KEY = "fj7ObOG3CEIjieEW3qDnFADGyvux50TI"; // 替换为你的 API 密钥

  public String upload(File file) throws IOException {
    HttpResponse<String> response =
        Unirest.post(UPLOAD_URL).header("Authorization", API_KEY).field("smfile", file).asString();

    String responseBody = response.getBody();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(responseBody);
    if (jsonNode.get("success").asBoolean()) {
      return jsonNode.get("data").get("url").asText();
    } else {
      String message = jsonNode.get("message").asText();
      if (message.startsWith("Image upload repeated limit")) {
        String existingUrl = message.substring(message.lastIndexOf("https://"));
        return existingUrl;
      } else {
        throw new IOException("Upload failed: " + message);
      }
    }
  }
}