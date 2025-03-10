<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath }/css/commonCss.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/res/styles.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/input.css"/>
    <title>问卷维度列表</title>
</head>
<body>
<div style="padding: 10px;">
    <form action="${pageContext.request.contextPath }/dimension/list.action" method="post">
        <label>
            <select class="form-control wid" name="id">
                <jsp:useBean id="assessmentList" scope="request"
                             type="java.util.List<com.qst.entity.AssessmentType >"/>
                <c:forEach items="${assessmentList }" var="sj">
                    <option value="${sj.id }" ${sj.id eq param.id?'selected':''}>
                            ${sj.title}
                    </option>
                </c:forEach>
            </select>
        </label>
        <input class="btn btn-warning" style="margin-left: 10px;margin-bottom: 3px" type="submit" name="list"
               value="查看问卷维度"/>
        <input class="btn btn-warning" style="margin-left: 10px;margin-bottom: 3px" type="submit" name="create"
               value="添加问卷维度"/>
    </form>

    <%@ include file="/inc/msg.jsp" %>
    <table class="table table-bordered table-hover table-striped">
        <tr>
            <th>序号</th>
            <th>问卷维度名称</th>
            <th>问卷维度说明</th>
            <th>操作</th>
        </tr>
        <jsp:useBean id="dimensionList" scope="request" type="java.util.List<com.qst.entity.PersonalityDimension>"/>
        <c:forEach var="p" varStatus="vs" items="${dimensionList }">
            <tr class="body">
                <td>${vs.index+1 }</td>
                <td>${p.title }</td>
                <td>${p.depict }</td>

                <td>
                    <a class="button" href="${pageContext.request.contextPath }/dimension/view.action?id=${p.id }">
                        查看
                    </a>
                    <a class="button" href="${pageContext.request.contextPath }/dimension/edit.action?id=${p.id }">
                        修改
                    </a>
                    <a class="button"
                       href="${pageContext.request.contextPath }/dimension/view.action?id=${p.id }&delete=1">
                        删除
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>

</div>
</body>
</html>