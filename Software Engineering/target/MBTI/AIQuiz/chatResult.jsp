<%@ page import="java.util.Date" %> <% response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", -10); %> <%@ page language="java"
contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />

    <meta charset="UTF-8" />
    <title>AI 情绪测试</title>
    <link
      href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&display=swap"
      rel="stylesheet"
    />
    <style>
      body {
        font-family: 'Open Sans', sans-serif;
        margin: 30px;
        text-align: center;
        background: linear-gradient(to right, #f2e7ca 20%, #f9f1e1 80%);
      }

      h2 {
        color: #3498db;
        font-size: 36px;
        margin-bottom: 30px;
      }

      form {
        max-width: 700px;
        margin: 0 auto;
        background-color: #fff;
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
        text-align: left;
        transition: box-shadow 0.3s, transform 0.3s, border-color 0.3s;
        border: 3px solid #3498db;
      }

      form:hover {
        box-shadow: 0 0 20px rgba(52, 152, 219, 0.5);
        transform: scale(1.02);
        border-color: #2980b9;
      }

      form label {
        display: block;
        margin-bottom: 15px;
        font-weight: bold;
        color: #3498db;
        font-size: 18px;
      }

      select {
        width: 100%;
        padding: 12px;
        margin-bottom: 25px;
        border: 2px solid #ccc;
        border-radius: 8px;
        box-sizing: border-box;
        transition: border-color 0.3s, box-shadow 0.3s;
      }

      select:focus {
        border-color: #2980b9;
        box-shadow: 0 0 12px rgba(52, 152, 219, 0.3);
      }

      input[type='submit'] {
        padding: 14px;
        background-color: #3498db;
        color: #fff;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        width: 100%;
        font-size: 18px;
        transition: transform 0.3s, background-color 0.3s, box-shadow 0.3s;
        box-shadow: 0 6px 12px rgba(52, 152, 219, 0.3);
      }

      input[type='submit']:hover {
        background-color: #2980b9;
        transform: scale(1.05);
        box-shadow: 0 8px 16px rgba(52, 152, 219, 0.5);
      }

      p {
        font-size: 20px;
        margin-top: 40px;
        padding: 20px;
        background-color: #ecf0f1;
        border-radius: 12px;
        box-shadow: 0 0 12px rgba(52, 152, 219, 0.3);
        color: #3498db;
        max-width: 700px;
        margin: 30px auto 0;
        line-height: 1.6;
        font-weight: bold;
        transition: background-color 0.3s, color 0.3s, margin-top 0.3s,
          box-shadow 0.3s;
      }

      p:hover {
        background-color: #d5d8dc;
        color: #2980b9;
        margin-top: 40px;
        box-shadow: 0 0 14px rgba(52, 152, 219, 0.5);
      }

      .loading-spinner {
        display: none;
        border: 4px solid rgba(0, 0, 0, 0.1);
        border-left: 4px solid #3498db;
        border-radius: 50%;
        width: 40px;
        height: 40px;
        animation: spin 1s linear infinite;
        margin: 20px auto;
      }

      @keyframes spin {
        0% {
          transform: rotate(0deg);
        }
        100% {
          transform: rotate(360deg);
        }
      }

      /* Additional styles for animations */
      body.transition-background {
        background: linear-gradient(to right, #3498db, #1abc9c) !important;
        transition: background 5s !important;
      }

      form.zoom-out {
        transform: scale(90%);
        transition: transform 0.5s;
      }
      button {
        display: block;
        width: 100px;
        padding: 10px;
        margin: 20px auto;
        border: none;
        border-radius: 5px;
        background-color: #007bff;
        color: white;
        cursor: pointer;
        text-align: center;
      }
    </style>
  </head>

  <body>
    <button
      onclick="location.href='${pageContext.request.contextPath }/otherTests.jsp'"
    >
      返回
    </button>
    <h2>ERNIE情绪测试</h2>
    <div>
      <form id="quizForm" action="ChatServlet" method="post">
        <label for="question1">1. 你对关系最密切的人是否满意?</label>
        <select id="question1" name="question1" required>
          <option value="不满意">不满意</option>
          <option value="非常满意">非常满意</option>
          <option value="基本满意">基本满意</option>
        </select>

        <label for="question2">2. 看到最近拍摄的照片有何想法?</label>
        <select id="question2" name="question2" required>
          <option value="觉得很好">觉得很好</option>
          <option value="觉得完美">觉得完美</option>
          <option value="觉得可以">觉得可以</option>
        </select>

        <label for="question3">3. 你喜欢吃炸酱面还是拉面?</label>
        <select id="question3" name="question3" required>
          <option value="觉得很好">炸酱面</option>
          <option value="觉得完美">拉面</option>
        </select>

        <label for="question4">4. 你喜欢旅游吗?</label>
        <select id="question4" name="question4" required>
          <option value="觉得很好">特别喜欢</option>
          <option value="觉得完美">喜欢</option>
          <option value="觉得可以">不喜欢</option>
        </select>

        <label for="question5">5. 你喜欢玩游戏吗?</label>
        <select id="question5" name="question5" required>
          <option value="觉得很好">喜欢</option>
          <option value="觉得完美">不喜欢</option>
        </select>

        <label for="question6">6. 你喜欢玩原神吗?</label>
        <select id="question6" name="question6" required>
          <option value="觉得很好">喜欢</option>
          <option value="觉得完美">不喜欢</option>
        </select>

        <label for="question7">7. 你喜欢文科还是理科?</label>
        <select id="question7" name="question7" required>
          <option value="觉得很好">觉得很好</option>
          <option value="觉得完美">觉得完美</option>
          <option value="觉得可以">觉得可以</option>
        </select>

        <label for="question8">8. 你喜欢小动物吗?</label>
        <select id="question8" name="question8" required>
          <option value="觉得很好">觉得很好</option>
          <option value="觉得完美">觉得完美</option>
          <option value="觉得可以">觉得可以</option>
        </select>

        <label for="question9">9. 你喜欢喝可乐吗?</label>
        <select id="question9" name="question9" required>
          <option value="觉得很好">觉得很好</option>
          <option value="觉得完美">觉得完美</option>
          <option value="觉得可以">觉得可以</option>
        </select>

        <label for="question10">10. 你经常做噩梦吗?</label>
        <select id="question10" name="question10" required>
          <option value="觉得很好">觉得很好</option>
          <option value="觉得完美">觉得完美</option>
          <option value="觉得可以">觉得可以</option>
        </select>

        <div class="loading-spinner" id="loadingSpinner"></div>

        <input type="submit" value="提交答案" onclick="submitForm()" />
      </form>
    </div>

    <p id="resultDisplay">${chatResult}</p>

    <p id="resultDisplay1">
      <img src="data:image/png;base64,${imageData}" alt="图片" height="300px" />
    </p>

    <%--'C:\Users\21483\Desktop\coursecode-master\servlet\'+${path}--%>

    <script>
      // 获取对应的 img 元素
      //like12 add,20211223,bug,解决360浏览器刷新时会报“确认重新提交表单 您所查找的网页要使用已输入的信息”的问题

      function submitForm() {
        // 获取对应的 img 元素

        // Trigger background color transition
        document.body.classList.add('transition-background')

        // Zoom out animation
        document.querySelector('form').classList.add('zoom-out')

        // Show loading spinner
        document.getElementById('loadingSpinner').style.display = 'block'

        // Submit the form after a delay (adjust the delay time as needed)
        setTimeout(function () {
          // Simulate receiving the result
          var result = '正在分析性格'

          // Update the content and fade it in
          var resultDisplay = document.getElementById('resultDisplay')
          resultDisplay.innerHTML = result
          fadeIn(resultDisplay, 1500) // 500 milliseconds fade in
        }, 500) // 500 milliseconds delay

        setTimeout(function () {
          // Simulate receiving the result
          var result = '正在用一张图片描述您的情绪'

          // Update the content and fade it in
          var resultDisplay1 = document.getElementById('resultDisplay1')
          resultDisplay1.innerHTML = result
          fadeIn(resultDisplay1, 1500) // 500 milliseconds fade in
        }, 500) // 500 milliseconds delay

        // 强制清空缓存的操作
        location.reload(true)
      }

      // Function to fade in an element
      function fadeIn(element, duration) {
        var opacity = 0
        var interval = 50 // interval time in milliseconds
        var delta = interval / duration

        function increaseOpacity() {
          opacity += delta
          element.style.opacity = opacity

          if (opacity >= 1) {
            clearInterval(fadeInterval)
          }
        }

        var fadeInterval = setInterval(increaseOpacity, interval)
      }

      // 清空缓存的
    </script>
  </body>
</html>
