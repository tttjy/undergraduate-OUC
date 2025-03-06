<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>其他测试</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
  </head>
  <style>
    body {
      background-color: #f2e7ca;
    }
    .card {
      width: 20em;
      margin: 0 auto;
      background-color: #fff;
      border-radius: 5px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
      padding: 20px;
      font-family: 'Roboto', sans-serif;
      font-weight: 400;
      font-size: 18px;
      color: #333;
      text-align: center;
    }
    .card h2 {
      margin: 0;
      padding: 1em;
      font-size: 24px;
      color: #555;
    }
    .card ul {
      margin: 0;
      padding: 0;
      list-style: none;
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
    }
    .card ul li {
      margin: 0;
      padding: 0;
      width: calc(50% - 10px);
      height: 150px;
      font-size: 16px;
      color: #555;
      line-height: 30px;
      border-bottom: 1px solid #eee;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: 0.3s;
    }
    .card ul li a {
      text-decoration: none;
      color: #555;
      display: block;
      /* transition: 0.3s; */
    }
    .card ul li:hover {
      background-color: #eee;
      color: #333;
    }
  </style>
  <body>
    <div class="card">
      <h2>其他测试</h2>
      <ul>
        <li>
          <a href="${pageContext.request.contextPath }/AIQuiz/ChatServlet"
            >AI测试</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath }/feierTest.jsp"
            >菲尔性格测试</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath }/yaoxinyuTest.jsp"
            >焦躁症测试</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath }/lixingnanTest.jsp"
            >不安全感测试</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath }/EQTest.jsp"
            >Emotion Quiz测试</a
          >
        </li>
        <li>
          <a href="${pageContext.request.contextPath }/emoTest.jsp"
            >抑郁症测试</a
          >
        </li>
      </ul>
    </div>
  </body>
</html>
