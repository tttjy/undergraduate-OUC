<jsp:useBean id="qid" scope="request" type="java.lang.Integer" />
<jsp:useBean id="question" scope="request" type="com.qst.entity.Question" />
<jsp:useBean id="error" scope="request" type="java.lang.String" />
<%@ page pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <script
      type="text/javascript"
      src="${pageContext.request.contextPath }/css/commonCss.js"
    ></script>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/css/input.css"
    />
    <title></title>
  </head>
  <body>
    <div style="padding: 10px">
      <%@ include file="/inc/msg.jsp" %>
      <span style="color: red">${error}</span>
      <form
        action="${pageContext.request.contextPath }/question/dimension.action"
        method="post"
        id="from1"
      >
        <input type="hidden" name="method" value="save" />
        <input type="hidden" name="id" value="${question.id }" />
        <input type="hidden" value="${qid}" name="qid" />
        <table
          class="table table-bordered table-hover table-striped"
          style="width: 90%"
        >
          <tr>
            <td class="head">序号</td>
            <td class="head">问卷维度名称</td>
            <td class="head">问卷维度说明</td>
          </tr>
          <jsp:useBean
            id="dimensionList"
            scope="request"
            type="java.util.List<com.qst.entity.PersonalityDimension>"
          />
          <c:forEach var="p" varStatus="vs" items="${dimensionList }">
            <tr class="body">
              <td>
                ${vs.index+1 }
                <label>
                  <input type="checkbox" value="${p.id }" name="pid"
                  ${p.extras['checked'] } />
                </label>
              </td>
              <td>${p.title }</td>
              <td>${p.depict }</td>
            </tr>
          </c:forEach>
        </table>
        <input class="btn btn-warning" type="submit" value="关联问卷维度" />
      </form>
    </div>
  </body>
</html>
