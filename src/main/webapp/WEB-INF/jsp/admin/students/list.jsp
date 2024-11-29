
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
    <tbody>
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.id}</td>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td>${student.email}</td>
            <td>
                <a href="students/${student.id}">Admin View</a> |
                <%-- Student View est temporaire le temps que
                le formulaire de connexion soit implémenté --%>
                <a href="/students/${student.id}">* Student View</a> |
                <a href="students/${student.id}/courses">Courses</a> |
                <form action="students/${student.id}" method="post" style="display:inline;">
                    <input type="hidden" name="_method" value="delete"/>
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<a href="students/new">Add New Student</a>
<p>* Student View | Temporaire, le temps que le formaulaire de connexion soit implémenter</p>
</body>
</html>
