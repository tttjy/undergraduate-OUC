<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>菲尔性格测试</title>
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
      background-color: lightgoldenrodyellow;
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
        <p>1. 你何时感觉最好？</p>
        <div class="options">
          <select name="q1">
            <option value="2">早晨</option>
            <option value="4">下午及傍晚</option>
            <option value="6">夜里</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>2. 你走路时是：</p>
        <div class="options">
          <select name="q2">
            <option value="6">大步地快走</option>
            <option value="4">小步地走</option>
            <option value="7">不快，仰着头面对着世界</option>
            <option value="2">不快，低着头</option>
            <option value="1">很慢</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>3. 和人说话时，你......：</p>
        <div class="options">
          <select name="q3">
            <option value="4">手臂交叠站着</option>
            <option value="2">双手紧握着</option>
            <option value="5">一只手或两手放在臀部</option>
            <option value="7">碰着或推着与你说话的人</option>
            <option value="6">玩着你的耳朵、摸着你的下巴或用手整理头发</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>4. 坐着休息时，你的姿势是：</p>
        <div class="options">
          <select name="q4">
            <option value="4">两膝盖并拢</option>
            <option value="6">两腿交叉</option>
            <option value="2">两腿伸直</option>
            <option value="1">一腿蜷在身下</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>5. 碰到你感到发笑的事时，你的反应是……</p>
        <div class="options">
          <select name="q5">
            <option value="6">一个欣赏的大笑</option>
            <option value="4">笑着，但不大声</option>
            <option value="3">轻声地咯咯地笑</option>
            <option value="5">羞怯的微笑</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>6. 当你去一个派对或社交场合时，你……</p>
        <div class="options">
          <select name="q6">
            <option value="6">很大声地入场以引起注意</option>
            <option value="4">安静地入场，找你认识的人</option>
            <option value="2">非常安静地入场，尽量保持不被注意</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>7. 当你非常专心工作时，有人打断你，你会……</p>
        <div class="options">
          <select name="q7">
            <option value="6">欢迎他</option>
            <option value="2">感到非常恼怒</option>
            <option value="4">在上述两极端之间</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>8. 你最喜欢哪一种颜色</p>
        <div class="options">
          <select name="q8">
            <option value="6">红色、橘色</option>
            <option value="7">黑色</option>
            <option value="5">黄色、浅蓝色</option>
            <option value="4">绿色</option>
            <option value="3">深蓝色、紫色</option>
            <option value="2">白色</option>
            <option value="1">棕色、灰色</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>9. 临入睡的前几分钟，你在床上的姿势是:</p>
        <div class="options">
          <select name="q9">
            <option value="7">仰躺、伸直</option>
            <option value="6">俯躺，伸直</option>
            <option value="4">侧躺、微蜷</option>
            <option value="2">头睡在一手臂上</option>
            <option value="1">被子盖过头</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>10. 你经常梦到自己在：</p>
        <div class="options">
          <select name="q10">
            <option value="4">落下</option>
            <option value="2">打架或挣扎</option>
            <option value="3">找东西或人</option>
            <option value="5">飞、漂浮</option>
            <option value="6">你平常不做梦</option>
            <option value="1">你的梦都是愉快的</option>
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
            '内向的悲观者:你是一个害羞的、神经质的、优柔寡断的人，永远要别人为你做决定。你是一个杞人忧天者，有些人认为你令人乏味，只有那些深知你的人知道你不是这样。'
        } else if (totalScore >= 21 && totalScore <= 30) {
          psychologicalState =
            '缺乏信心的挑剔者:你勤勉、刻苦、挑剔，是一个谨慎小心的人。如果你做任何冲动的事或无准备的事，朋友们都会大吃一惊。自我保护者'
        } else if (totalScore >= 31 && totalScore <= 40) {
          psychologicalState =
            '以牙还牙的自我保护者:你是一个明智、谨慎、注重实效的人，也是一个伶俐、有天赋、有才干且谦虚的人。你不容易很快和人成为朋友，却是一个对朋友非常忠诚的人，同时要求朋友对你也忠诚。要动摇你对朋友的信任很难，同样，一旦这种信任被破坏，也就很难恢复。'
        } else if (totalScore >= 41 && totalScore <= 50) {
          psychologicalState =
            '平衡的中道者。你是一个有活力、有魅力、讲究实际，而且永远有趣的人。你经常是群众注意力的焦点，但你是一足够平衡的人，不至于因此而昏了头。你亲切、和蔼、体贴、宽容，是一个永远会使人高兴、乐于助人的人。'
        } else if (totalScore >= 51 && totalScore <= 60) {
          psychologicalState =
            '吸引人的冒险家。你是一个令人兴奋、活泼、易冲动的人，是一个天生的领袖，能够迅速做了决定，虽然你的决定不总是对的。你是一个愿意尝试机会，欣赏冒险的人，周围人喜欢跟你在一起。'
        } else {
          psychologicalState =
            '傲慢的孤独者。你是自负的自我中心主义者，是个有极端支配欲、统治欲的人。别人可能钦佩你，但不会永远相信你。'
        }

        document.getElementById('result-container').innerHTML =
          '菲尔性格测试结果: ' + psychologicalState
      }
    </script>
  </body>
</html>
