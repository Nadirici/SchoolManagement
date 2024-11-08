<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
  <h1>Admin Dashboard</h1>
  <a href="/admin/${id}/students">Liste des étudiants</a>
  <a href="/admin/${id}/requests">Demandes d'inscriptions</a>
</body>
</html>
