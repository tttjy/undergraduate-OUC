<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
    <title>Movie List</title>
  </head>
  <body>
    <table>
      <thead>
        <tr>
          <th>Title</th>
          <th>Description</th>
          <th>Date</th>
          <th>Capacity</th>
          <th>Reserved Seats</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:set var="user" value="${sessionScope.user}" scope="page" />
        <c:forEach var="movie" items="${movies }">
          <tr>
            <td>${movie.title }</td>
            <td>${movie.description }</td>
            <td>${movie.date }</td>
            <td>${movie.capacity }</td>
            <td>${movie.reservedSeats }</td>
            <td>
              <a
                href="${pageContext.request.contextPath }/movie/edit.action?id=${movie.id }"
                >Edit</a
              >
              <a
                href="${pageContext.request.contextPath }/movie/delete.action?id=${movie.id }"
                >Delete</a
              >
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <a href="${pageContext.request.contextPath }/movie/add.jsp">Add Movie</a>
  </body>
</html>
