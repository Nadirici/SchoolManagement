<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/22/2024
  Time: 3:10 AM
  To change this template use File | Settings | File Templates.

  le mettre en modal sur la page des cours
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Nouveau Devoir</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="/css/style.css">
  <script>function showStudentGradesSection() {
      document.getElementById("assignment-section").style.display = "none";
      document.getElementById("grades-section").style.display = "block";
  }</script>
</head>
<body>
<div class="container">
  <%@include file="../headers/teacher_header.jsp"%>
  <div class="main-content">

    <div class="overviewStudent">

          <c:choose>
            <c:when test="${assignment.id == null}">
            <h2>Ajouter Un Devoir</h2>
            <div class="stats">
                <div class="form-container">
              <form action="../${courseId}/assignment" method="post">
            </c:when>
            <c:otherwise>
              <h2>Modifier Un Devoir</h2>
              <div class="stats">
                  <div class="form-container">
              <form action="../assignment/${assignment.id}/edit" method="post">
            </c:otherwise>
          </c:choose>
                <div id="assignment-section">
                    <label for="title">Devoir:</label>
                    <input type="text" id="title" name="title" value="${assignment.title}" required><br>

                    <label for="description">Description:</label>
                    <input type="text" id="description" name="description" value="${assignment.description}" required><br>
                    <label for="coeff">Coefficient:</label>
                    <input type="number" id="coeff" step="0.1" name="coefficient" value="${assignment.coefficient}" required><br>
                    <button type="button" onclick="showStudentGradesSection()">Ajouter le devoir</button>
                </div>
                <div id="grades-section" style="display: none;">
                  <%@include file="../lists/grade_table.jsp" %>
                </div>
            </form>
          </div>
        </div>
    </div>
</div>
</div>

</div>
</body>
</html>
</body>
</html>
