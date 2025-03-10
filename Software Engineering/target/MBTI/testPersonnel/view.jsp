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
  </head>
  <body>
    <div style="padding: 10px">
      <a
        href="${pageContext.request.contextPath }/testPersonnel/list.action?tid=${testPersonnel.teamId }"
        class="button"
      >
        <button class="btn btn-warning" style="margin-bottom: 10px">
          返回列表
        </button>
      </a>
      <%@ include file="/inc/msg.jsp" %>
      <table
        class="table table-bordered table-hover table-striped"
        style="width: 90%"
      >
        <tr>
          <td class="head">登录名称</td>
          <td>${testPersonnel.user.login }</td>
        </tr>
        <tr>
          <td class="head">真实姓名</td>
          <td>${testPersonnel.user.name }</td>
        </tr>

        <tr>
          <td class="head">手机号</td>
          <td>${testPersonnel.phone }</td>
        </tr>
        <tr>
          <td class="head">性别</td>
          <td>${testPersonnel.gender eq 'F'?'女':'男' }</td>
        </tr>
        <tr>
          <td class="head">出生日期</td>
          <td>${testPersonnel.birthDate }</td>
        </tr>
        <tr>
          <td class="head">状态</td>
          <td>${testPersonnel.user.statusDesc }</td>
        </tr>

        <c:if test="${param.delete eq 1 }">
          <tr>
            <td colspan="2" style="text-align: center">
              <a
                href="${pageContext.request.contextPath }/testPersonnel/delete.action?id=${testPersonnel.id }"
                class="button"
                >确认删除</a
              >
            </td>
          </tr>
        </c:if>
      </table>
    </div>
  </body>
</html>
