
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <title>Liste d'étudiants</title>
  <%--<link rel="stylesheet" href="/css/style.css">--%>
</head>
<body>
<div class="container">
  <%@include file="../../headers/admin_header.jsp" %>
  <div class="main-content">
    <h1>Liste d'étudiants</h1>

    <div class="overviewStudent">
      <table border="1">
      <thead>
        <tr>
        <th>ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Actions</th>
        </tr>
        </thead>
         <c:forEach var="student" items="${students}">
      <tr>
      <td>${student.id}</td>
      <td>${student.firstname}</td>
      <td>${student.lastname}</td>
      <td>${student.email}</td>
      <td>
        <a href="students/${student.id}">Plus d'information sur l'étudiant</a> |
        <a href="students/${student.id}/courses">Courses</a> |

        <a href="students/${student.id}/delete">Supprimer l'étudiant</a>
      </td>
    </tr>
  </c:forEach>
      </table>

    </div>

  </div>
</div>




</body>
</html>
