<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
    <title>Edit Movie</title>
  </head>
  <body>
    <h1>Edit Movie</h1>
    <div class="card">
      <form action="edit" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${movie.id}" />
        <div class="form-group">
          <label for="title">Title:</label>
          <input type="text" id="title" name="title" value="${movie.title}" />
        </div>
        <div class="form-group">
          <label for="description">Description:</label>
          <input
            type="text"
            id="description"
            name="description"
            value="${movie.description}"
          />
        </div>
        <div class="form-group">
          <label for="date">Date:</label>
          <input type="text" id="date" name="date" value="${movie.date}" />
        </div>
        <div class="form-group">
          <label for="capacity">Capacity:</label>
          <input
            type="text"
            id="capacity"
            name="capacity"
            value="${movie.capacity}"
          />
        </div>
        <div class="form-group">
          <label for="poster">Poster:</label>
          <input type="file" id="poster" name="poster" />
        </div>
        <input type="submit" value="Save" />
      </form>
    </div>
  </body>
</html>
