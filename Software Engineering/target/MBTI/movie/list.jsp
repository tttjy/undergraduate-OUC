<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/res/styles.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath }/css/commonCss.js"></script>
    <title>Movie List</title>
    <style>
        .poster-wall {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            align-items: flex-start;
        }

        .movie-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            align-items: flex-start;
        }

        .movie-poster {
            text-align: center;
            position: relative;
        }

        .movie-poster img {
            max-width: 100%;
            height: auto;
        }

        .words {
            position: absolute;
            bottom: 10px;
            left: 10px;

        }

        .words h3 {
            font-family: "YaHei", sans-serif;
            color: #fff;
            text-shadow: 0 0 10px #000;
            font-size: 22px;
            margin: 0;
            text-align: left;
            white-space: normal;
            height: auto;
            overflow: visible;
            background-color: transparent !important;
        }

        .date {
            font-size: .6em;
            color: #ccc;
        }

        button {
            color: #000;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #4CAF50; /* Green */
            font-size: 18px;
            font-family: "YaHei", sans-serif;
            cursor: pointer;
            width: 20vw;
            color: #fff;
            transition: transform 0.3s ease-in-out;
        }

        button:hover {
            transform: scale(1.1);
        }

        .actions {
            display: flex;
        }

        .movie-poster:hover {
            transform: scale(1.05);
        }

        .movie-item {
            width: 20%;
        }
    </style>
</head>
<body>
<jsp:useBean id="current_user" scope="session" type="com.qst.entity.User"/>
<div class="movie-container">
    <c:forEach var="movie" items="${movies }">
        <div class="movie-item">
            <div class="movie-poster">
                <img src="${movie.posterUrl}" alt="${movie.title}">
                <div class="words">
                    <h3>${movie.title}
                            <%--                    <span>${movie.description}</span>--%>
                        <span class="date"> ${movie.date}</span></h3>
                        <%--            <p>Reserved Seats: ${movie.reservedSeats}</p>--%>
                </div>
            </div>
            <div class="actions">
<%--                <c:if test="${movie.reservedSeats < movie.capacity }}">--%>
                    <c:if test="${movie.reserved == 'false'}">
                        <a href="${pageContext.request.contextPath }/reservation/add.action?movieId=${movie.id }&userId=${current_user.id }"
                        >
                            <button>预约</button>
                        </a>
                    </c:if>
<%--                </c:if>--%>
                <c:if test="${movie.reserved == 'true'}">
                    <a href="${pageContext.request.contextPath }/reservation/delete.action?movieId=${movie.id }&userId=${current_user.id }"
                    >
                        <button>取消预约</button>
                    </a>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
