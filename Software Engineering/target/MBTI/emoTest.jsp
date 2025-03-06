<%-- Created by IntelliJ IDEA. User: lenovo Date: 2023/12/18 Time: 19:59 To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>心理测试</title>
    <!-- 添加 Bootstrap CSS 链接 -->
  </head>
  <style>
    #quiz-container {
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      background-color: #fff;
    }

    .question {
      margin-bottom: 20px;
    }

    .options {
      margin-top: 10px;
    }

    select {
      width: 100%;
      padding: 8px;
      margin-top: 5px;
      margin-bottom: 10px;
      box-sizing: border-box;
    }

    #submit-btn {
      display: block;
      width: 100%;
      padding: 10px;
      background-color: #4caf50;
      color: #fff;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    #submit-btn:hover {
      background-color: #45a049;
    }

    #result-container {
      margin-top: 20px;
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
  <body>
    <button
      onclick="location.href='${pageContext.request.contextPath }/otherTests.jsp'"
    >
      返回
    </button>
    <div id="quiz-container">
      <div class="question">
        <p>1. 我的头脑跟平常一样清楚</p>
        <div class="options">
          <select name="q1">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>2. 我觉得经常做的事情并没有困难</p>
        <div class="options">
          <select name="q2">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>3. 我觉得不安而平静不下来</p>
        <div class="options">
          <select name="q3">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>4. 我对将来抱有希望</p>
        <div class="options">
          <select name="q4">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>5. 我比平时容易生气激动</p>
        <div class="options">
          <select name="q5">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>6. 我觉得做出决定是容易的</p>
        <div class="options">
          <select name="q6">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>7.我觉得自己是个有用的人，有人需要我</p>
        <div class="options">
          <select name="q7">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>8. 我的生活过得很有意思</p>
        <div class="options">
          <select name="q8">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>9. 我常感兴趣的事我仍然照样感兴趣</p>
        <div class="options">
          <select name="q9">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>10. 我认为如果我死了，别人会生活得好</p>
        <div class="options">
          <select name="q10">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>11. 我无缘无故地感到疲乏</p>
        <div class="options">
          <select name="q11">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>12. 我心跳比平常快</p>
        <div class="options">
          <select name="q12">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>13. 我有便秘的苦恼</p>
        <div class="options">
          <select name="q13">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>14.我发觉我的体重在下降</p>
        <div class="options">
          <select name="q14">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>15. 我与异性密切接触时和以往一样感到愉快<br /></p>
        <div class="options">
          <select name="q15">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>16. 我吃得跟平时一样多</p>
        <div class="options">
          <select name="q16">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>17.我晚上睡眠不好</p>
        <div class="options">
          <select name="q17">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>18. 我一阵阵哭出来或觉得想哭</p>
        <div class="options">
          <select name="q18">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>19. 我觉得一天之中早晨最好</p>
        <div class="options">
          <select name="q19">
            <option value="4">没有或很少时间</option>
            <option value="3">少部分时间</option>
            <option value="2">相当多时间</option>
            <option value="1">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>20. 我觉得闷闷不乐，情绪低沉</p>
        <div class="options">
          <select name="q20">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <!-- Add more questions as needed -->

      <button id="submit-btn" onclick="submitQuiz()">提交答案</button>

      <div id="result-container"></div>
    </div>
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->
    <!-- Bootstrap JS 和 Popper.js 脚本（用于 Bootstrap 组件） -->

    <script>
      function submitQuiz() {
        // Get the selected answers and calculate the total score
        const answer1 = parseInt(
          document.querySelector('select[name="q1"]').value
        )
        const answer2 = parseInt(
          document.querySelector('select[name="q2"]').value
        )
        const answer3 = parseInt(
          document.querySelector('select[name="q3"]').value
        )
        const answer4 = parseInt(
          document.querySelector('select[name="q4"]').value
        )
        const answer5 = parseInt(
          document.querySelector('select[name="q5"]').value
        )
        const answer6 = parseInt(
          document.querySelector('select[name="q6"]').value
        )
        const answer7 = parseInt(
          document.querySelector('select[name="q7"]').value
        )
        const answer8 = parseInt(
          document.querySelector('select[name="q8"]').value
        )
        const answer9 = parseInt(
          document.querySelector('select[name="q9"]').value
        )
        const answer10 = parseInt(
          document.querySelector('select[name="q10"]').value
        )
        const answer11 = parseInt(
          document.querySelector('select[name="q11"]').value
        )
        const answer12 = parseInt(
          document.querySelector('select[name="q12"]').value
        )
        const answer13 = parseInt(
          document.querySelector('select[name="q13"]').value
        )
        const answer14 = parseInt(
          document.querySelector('select[name="q14"]').value
        )
        const answer15 = parseInt(
          document.querySelector('select[name="q15"]').value
        )
        const answer16 = parseInt(
          document.querySelector('select[name="q16"]').value
        )
        const answer17 = parseInt(
          document.querySelector('select[name="q17"]').value
        )
        const answer18 = parseInt(
          document.querySelector('select[name="q18"]').value
        )
        const answer19 = parseInt(
          document.querySelector('select[name="q19"]').value
        )
        const answer20 = parseInt(
          document.querySelector('select[name="q20"]').value
        )

        const totalScore =
          answer1 +
          answer2 +
          answer3 +
          answer4 +
          answer5 +
          answer6 +
          answer7 +
          answer8 +
          answer9 +
          answer10 +
          answer11 +
          answer12 +
          answer13 +
          answer14 +
          answer15 +
          answer16 +
          answer17 +
          answer18 +
          answer19 +
          answer20
        let psychologicalState = ''
        if (totalScore < 10) {
          psychologicalState = '未答完题'
        } else if (totalScore >= 53 && totalScore <= 62) {
          psychologicalState = '轻度抑郁'
        } else if (totalScore > 62 && totalScore <= 72) {
          psychologicalState = '中度抑郁'
        } else if (totalScore > 72) {
          psychologicalState = '重度抑郁'
        } else {
          psychologicalState = '恭喜你，无抑郁症状！'
        }

        document.getElementById('result-container').innerHTML =
          '心理状态: ' + psychologicalState
      }
    </script>
  </body>
</html>
