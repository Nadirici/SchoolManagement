<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Information sur l'étudiant</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
     <!-- Lien vers votre fichier CSS -->
</head>
<body>
<div class="container">
<%@include file="../../headers/admin_header.jsp" %>
<div class="main-content">
<h1>Informations sur ${student.firstname} ${student.lastname}</h1>


    <div class="overviewStudent">


<h2>informations sur l'élève</h2>
    <div class="stats">

<table>
<thead>
    <tr>
       <th>ID:</th>
       <th>Prénom:</th>
         <th>Nom:</th>
         <th>Email:</th>
          <th>Date de naissance</th>

    </tr>
    </thead>
    <tbody>
     <td>${student.id}</td>
      <td>${student.firstname}</td>
      <td>${student.lastname}</td>
     <td>${student.email}</td>
      <td>${student.dateOfBirth}</td>
      </tbody>
</table>
</div>
    </div>

</div>
</body>
</html>
