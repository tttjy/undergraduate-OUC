<%@ page pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <% String basePath = "http://" + request.getServerName() + ":" +
  request.getServerPort() + request.getContextPath() + "/"; %>
  <head>
    <base href="<%=basePath%>" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
  </head>
  <body>
    <div style="font-size: 2em; color: red">您无权使用此功能</div>
  </body>
</html>
