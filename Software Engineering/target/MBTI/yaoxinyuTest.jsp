<%-- Created by IntelliJ IDEA. User: lenovo Date: 2023/12/18 Time: 19:23 To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>心理测试</title>
    <!-- 添加 Bootstrap CSS 链接 -->
    <%--
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
    />
    --%>
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
        <p>1. 我觉得比平时容易紧张和着急</p>
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
        <p>2. 我无缘无故的害怕</p>
        <div class="options">
          <select name="q2">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>3. 我容易心里烦乱或觉得惊恐</p>
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
        <p>4. 我觉得我可能将要发疯</p>
        <div class="options">
          <select name="q4">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>5. 我觉得一切都很好，也不会发生什么不幸</p>
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
        <p>6. 我手脚发抖打颤</p>
        <div class="options">
          <select name="q6">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>7.我因为头痛、颈痛和背痛而苦恼</p>
        <div class="options">
          <select name="q7">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>8. 我感觉容易衰弱和疲乏</p>
        <div class="options">
          <select name="q8">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>9. 我觉得心平气和，并且容易安静坐着</p>
        <div class="options">
          <select name="q9">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>10. 我觉得心跳加快</p>
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
        <p>11. 我因为一阵阵头晕而苦恼</p>
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
        <p>12. 我有过晕倒发作，或觉得要晕倒似的</p>
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
        <p>13. 我呼气吸气都感觉很容易</p>
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
        <p>14.我手脚发麻和刺痛</p>
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
        <p>15. 我因胃痛和消化不良而苦恼</p>
        <div class="options">
          <select name="q15">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>16. 我常常要小便</p>
        <div class="options">
          <select name="q16">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>17.我的手常常是干燥温暖的</p>
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
        <p>18. 我脸红发热</p>
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
        <p>19. 我容易入睡并且一夜睡得很好</p>
        <div class="options">
          <select name="q19">
            <option value="1">没有或很少时间</option>
            <option value="2">少部分时间</option>
            <option value="3">相当多时间</option>
            <option value="4">绝大部分或全部时间</option>
          </select>
        </div>
      </div>
      <div class="question">
        <p>20. 我做恶梦</p>
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
          answer10
        let psychologicalState = ''
        if (totalScore < 10) {
          psychologicalState = '未答完题'
        } else if (totalScore >= 10 && totalScore <= 40) {
          psychologicalState =
            '对于某种东西或某种状态的强烈欲望，可能伴随着对未来的期待和向往。'
        } else if (totalScore > 40 && totalScore <= 60) {
          psychologicalState =
            '沉浸在内省和深思中的状态，思考生活、自我和存在的意义。'
        } else if (totalScore > 60 && totalScore <= 80) {
          psychologicalState =
            '由于刺激或期待某种令人高兴的事情而引起的兴奋和愉快的状态。可能伴随着兴奋感和活力。'
        } else {
          psychologicalState =
            '兴奋、满足、愉快的状态。通常伴随着积极的体验和情感。'
        }

        document.getElementById('result-container').innerHTML =
          '心理状态: ' + psychologicalState
      }
    </script>
  </body>
</html>
