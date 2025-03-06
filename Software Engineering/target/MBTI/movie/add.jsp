<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath }/res/styles.css"
    />
    <title>Add Movie</title>
  </head>
  <body>
    <h1>Add Movie</h1>
    <div class="card">
      <form
        action="${pageContext.request.contextPath }/movie/add.action"
        method="post"
        enctype="multipart/form-data"
      >
        <div class="input-group">
          <label for="title">Title:</label>
          <input type="text" id="title" name="title" />
        </div>
        <div class="input-group">
          <label for="description">Description:</label>
          <input type="text" id="description" name="description" />
        </div>
        <div class="input-group">
          <label for="date">Date:</label>
          <input type="datetime-local" id="date" name="date" />
        </div>
        <div class="input-group">
          <label for="capacity">Capacity:</label>
          <input type="number" id="capacity" name="capacity" />
        </div>
        <div class="input-group">
          <label for="poster">Poster:</label>
          <input type="file" id="poster" name="poster" />
        </div>
        <input type="submit" value="Save" />
      </form>
    </div>
  </body>
</html>
