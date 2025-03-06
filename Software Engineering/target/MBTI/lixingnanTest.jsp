<%-- Created by IntelliJ IDEA. User: lenovo Date: 2023/12/18 Time: 19:26 To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>不安全感测试</title>
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
        <p>1.从来不敢说出自己的看法</p>
        <div class="options">
          <select name="q1">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>2. 感到生活充满不确定性</p>
        <div class="options">
          <select name="q2">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>3. 习惯放弃自己的要求</p>
        <div class="options">
          <select name="q3">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>4. 总是担心不测发生</p>
        <div class="options">
          <select name="q4">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>5. 从不拒绝朋友的要求</p>
        <div class="options">
          <select name="q5">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>6. 遇到不开心的e事情独自痛苦或生气</p>
        <div class="options">
          <select name="q6">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>7. 觉得自己一直很倒霉</p>
        <div class="options">
          <select name="q7">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>8. 别人说你是害羞退缩的人</p>
        <div class="options">
          <select name="q8">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>9. 总是担心最好的朋友以后关系变坏</p>
        <div class="options">
          <select name="q9">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>10. 对领导一直敬而远之</p>
        <div class="options">
          <select name="q10">
            <option value="1">非常符合</option>
            <option value="2">基本符合</option>
            <option value="3">不确定</option>
            <option value="4">基本不符合</option>
            <option value="5">非常不符合</option>
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
        } else if (totalScore >= 10 && totalScore <= 20) {
          psychologicalState =
            '你充满危机感和不安全感，严重缺乏自信心。你往往过于谦虚和自我压抑，总觉得自己比别人稍逊一筹，因此经常受人支配。你很在意自己身上所谓的“缺陷”，每次遇到有关的问题你都十分敏感，所以你总是有一种防备的心理，对很多事情都不能放心对待和接受。'
        } else if (totalScore >= 21 && totalScore <= 30) {
          psychologicalState =
            '你对自己比较有信心，但你仍或多或少缺乏安全感。你会经常对自己产生怀疑，这也许来源于你对不确定的担忧或对自身缺点的焦虑。但你对待别人很友善，大家喜欢与你交往，这有利于你的工作和人际关系。'
        } else {
          psychologicalState =
            '你对自己多方面都比较有信心，对很多事情的感觉都很不错。你对他人很信任，也认为他人会对你很信任，所以你总会按照你的想法去做，没有过多的顾虑和担忧。你与不同身份的人都能相处得很好，你的心态很开放，也乐于与他人分享自己，你对周围人和事的关系能处理得当，也自信能掌控很多生活中的事件。'
        }

        document.getElementById('result-container').innerHTML =
          '不安全感测试结果: ' + psychologicalState
      }
    </script>
  </body>
</html>
