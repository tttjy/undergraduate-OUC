<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reservation List</title>
</head>
<body>
<h1>Reservation List</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>User</th>
        <th>Movie</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${reservations}" var="reservation">
        <tr>
            <td>${reservation.id}</td>
            <td>${reservation.user.username}</td>
            <td>${reservation.movie.title}</td>
            <td>
                <form action="delete" method="post">
                    <input type="hidden" name="id" value="${reservation.id}">
                    <input type="submit" value="Cancel">
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>