<%-- Created by IntelliJ IDEA. User: lenovo Date: 2023/12/18 Time: 19:35 To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>情商测试</title>
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
        <p>1.我有能力克服各种困难：</p>
        <div class="options">
          <select name="q1">
            <option value="1">是的</option>
            <option value="2">不一定</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>2.如果我能到一个新的环境，我要把生活安排得：</p>
        <div class="options">
          <select name="q2">
            <option value="1">和从前相仿</option>
            <option value="2">不一定</option>
            <option value="3">和从前不一样</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>3.一生中，我觉得自己能达到我所预想的目标：</p>
        <div class="options">
          <select name="q3">
            <option value="1">是的</option>
            <option value="2">不一定</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>4.不知为什么，有些人总是回避或冷淡我：</p>
        <div class="options">
          <select name="q4">
            <option value="1">不是的</option>
            <option value="2">不一定</option>
            <option value="3">是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>5.在大街上，我常常避开我不愿打招呼的人：</p>
        <div class="options">
          <select name="q5">
            <option value="1">从未如此</option>
            <option value="2">偶尔如此</option>
            <option value="3">有时如此</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>6.当我集中精力工作时，假如有人在旁边高谈阔论：</p>
        <div class="options">
          <select name="q6">
            <option value="1">我仍能专心工作</option>
            <option value="2">介于AC之间</option>
            <option value="3">我不能专心且感到愤怒</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>7.我不论到什么地方，都能清楚地辨别方向：</p>
        <div class="options">
          <select name="q7">
            <option value="1">是的</option>
            <option value="2">不一定</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>8.我热爱所学的专业和所从事的工作：</p>
        <div class="options">
          <select name="q8">
            <option value="1">是的</option>
            <option value="2">不一定</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>9.气候的变化不会影响我的情绪：</p>
        <div class="options">
          <select name="q9">
            <option value="1">是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>10.我从不因流言蜚语而生气：</p>
        <div class="options">
          <select name="q10">
            <option value="1">是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>11.我善于控制自己的面部表情：</p>
        <div class="options">
          <select name="q11">
            <option value="1">是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">不是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>12.在就寝时，我常常：</p>
        <div class="options">
          <select name="q12">
            <option value="1">极易入睡</option>
            <option value="2">介于AC之间</option>
            <option value="3">不易入睡</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>13.有人侵扰我时，我：</p>
        <div class="options">
          <select name="q13">
            <option value="1">不露声色</option>
            <option value="2">介于AC之间</option>
            <option value="3">大声抗议以泄己愤</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>
          14.在和人争辩或工作出现失误后，我常常感到震颤、精疲力竭，而不能继续安心工作：
        </p>
        <div class="options">
          <select name="q14">
            <option value="1">不是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>15.我常常被一些无谓的小事困扰：</p>
        <div class="options">
          <select name="q15">
            <option value="1">不是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>16.我宁愿住在僻静的郊区，也不愿住在嘈杂的市区：</p>
        <div class="options">
          <select name="q16">
            <option value="1">不是的</option>
            <option value="2">介于AC之间</option>
            <option value="3">是的</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>17.我被朋友、同事起过绰号挖苦过：</p>
        <div class="options">
          <select name="q17">
            <option value="1">从来没有</option>
            <option value="2">偶尔有过</option>
            <option value="3">这是常有的事</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>18.有一种食物使我吃后呕吐：</p>
        <div class="options">
          <select name="q18">
            <option value="1">没有</option>
            <option value="2">记不清</option>
            <option value="3">有</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>19.除去看见的世界外，我的心中没有另外的世界：</p>
        <div class="options">
          <select name="q19">
            <option value="1">没有</option>
            <option value="2">记不清</option>
            <option value="3">有</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>20.我会想到若干年后有什么使自己极为不安的事：</p>
        <div class="options">
          <select name="q20">
            <option value="1">从来没有想过</option>
            <option value="2">偶尔想到</option>
            <option value="3">经常想到</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>
          21.我常常觉得自己的家庭对自己不好，但是我又确切地知道他们的确对我好：
        </p>
        <div class="options">
          <select name="q21">
            <option value="1">否</option>
            <option value="2">不清楚</option>
            <option value="3">是</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>22.每天我一回家就立刻把门关上：</p>
        <div class="options">
          <select name="q22">
            <option value="1">否</option>
            <option value="2">不清楚</option>
            <option value="3">是</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>23.我坐在小房间里把门关上，但仍觉得心里不安</p>
        <div class="options">
          <select name="q23">
            <option value="1">否</option>
            <option value="2">偶尔是</option>
            <option value="3">是</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>24.当一件事需要我作决定时，我常觉得很难：</p>
        <div class="options">
          <select name="q24">
            <option value="1">否</option>
            <option value="2">偶尔是</option>
            <option value="3">是</option>
          </select>
        </div>
      </div>

      <div class="question">
        <p>25.我常常用抛硬币、翻纸、抽签之类的游戏来预测吉凶：</p>
        <div class="options">
          <select name="q25">
            <option value="1">否</option>
            <option value="2">偶尔是</option>
            <option value="3">是</option>
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
        // Get the selected answers and calculate the total scaore
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
        const answer21 = parseInt(
          document.querySelector('select[name="q21"]').value
        )
        const answer22 = parseInt(
          document.querySelector('select[name="q22"]').value
        )
        const answer23 = parseInt(
          document.querySelector('select[name="q23"]').value
        )
        const answer24 = parseInt(
          document.querySelector('select[name="q24"]').value
        )
        const answer25 = parseInt(
          document.querySelector('select[name="q25"]').value
        )

        const totalScore =
          (3 - answer1) * 3 +
          (3 - answer2) * 3 +
          (3 - answer3) * 3 +
          (3 - answer4) * 3 +
          (3 - answer5) * 3 +
          (3 - answer6) * 3 +
          (3 - answer7) * 3 +
          (3 - answer8) * 3 +
          (3 - answer9) * 3 +
          (3 - answer10) * 3 +
          (3 - answer11) * 3 +
          (3 - answer12) * 3 +
          (3 - answer13) * 3 +
          (3 - answer14) * 3 +
          (3 - answer15) * 3 +
          (3 - answer16) * 3 +
          (3 - answer17) * 3 +
          (3 - answer18) * 3 +
          (3 - answer19) * 3 +
          (3 - answer20) * 3 +
          (3 - answer21) * 3 +
          (3 - answer22) * 3 +
          (3 - answer23) * 3 +
          (3 - answer24) * 3 +
          (3 - answer25) * 3
        let psychologicalState = ''

        if (totalScore <= 50) {
          psychologicalState =
            '你的EQ较低，自我意识差；无确定的目标，也不打算付诸实践。严重依赖他人。处理人际关系能力差。应对焦虑能力差。生活无序。无责任感，爱抱怨。你常常不能控制自己，你极易被自己的情绪所影响。很多时候，' +
            '你轻易被击怒、动火、发脾气，这是非常危险的信号——你的事业可能会毁于你的暴躁对于此最好的解决办法是能够给不好的东西一个好的解释保持头脑冷静使自己心情开朗正如富兰克林所说:”任何人生机都是有理的但很少有令人信服的理由。'
        } else if (totalScore > 50 && totalScore <= 100) {
          psychologicalState =
            '你的EQ一般，易受他人影响，自己的目标不明确，对于一件事，你不同时候的表现可能不一，这与你的意识有关，你比低情商者更具有EQ意识，但这种意识不是常常都有，因此需要你多加注重、时时提醒。比低情商者' +
            '善于原谅，能控制大脑。能应付较轻的焦虑情绪。把自尊建立在他人认同的基础上。缺乏坚定的自我意识。人际关系较差。'
        } else if (totalScore > 100 && totalScore <= 150) {
          psychologicalState =
            '你的EQ较高，你是一个快乐的人，不易恐惊担忧，对于工作你热情投入、敢于负责，你为人更是正义正直、同情关怀，这是你的长处，应该努力保持。你是负责任的“好”公民。自尊。有独立人格，但在一些情况下易受' +
            '别人焦虑情绪的感染。比较自信而不自满。较好的人际关系。能应对大多数的问题，不会有太大的心理压力。'
        }

        document.getElementById('result-container').innerHTML =
          '情商指数: ' + totalScore + ' ' + psychologicalState
      }
    </script>
  </body>
</html>
