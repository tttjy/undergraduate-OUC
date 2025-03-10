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
        href="${pageContext.request.contextPath }/assessment/list.action"
        class="button"
      >
        <button class="btn btn-warning" style="margin-bottom: 10px">
          返回列表
        </button>
      </a>
      <%@ include file="/inc/msg.jsp" %>
      <form
        action="${pageContext.request.contextPath }/assessment/save.action"
        method="post"
        class="table"
        style="width: 80%"
      >
        <table class="table table-bordered table-hover table-striped">
          <tr>
            <td class="head">名称：</td>
            <td>
              <input type="text" name="title" />
            </td>
          </tr>
          <tr>
            <td class="head">状态：</td>
            <td>
              <input type="radio" name="status" value="1" checked />
              在用
              <input type="radio" name="status" value="0" />
              作废
            </td>
          </tr>
          <tr>
            <td class="head">费用：</td>
            <td>
              <textarea rows="8" cols="25" name="cost"></textarea>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="edit">
              <input class="btn btn-warning" type="submit" value="保存" /><input
                class="btn btn-warning"
                type="reset"
                value="重置"
              />
            </td>
          </tr>
        </table>
      </form>
    </div>
  </body>
</html>
