<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Information sur l'enseignant</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
     <!-- Lien vers votre fichier CSS -->
</head>
<body>
<div class="container">
<%@include file="../../headers/admin_header.jsp" %>
<div class="main-content">
<h1>Information sur ${teacher.firstname} ${teacher.lastname}</h1>


    <div class="overviewStudent">


<h2>informations sur l'enseignant</h2>
    <div class="stats">

<table>
<thead>
    <tr>
       <th>ID:</th>
       <th>Prénom:</th>
         <th>Nom:</th>
         <th>Email:</th>
          <th>Département</th>

    </tr>
    </thead>
    <tbody>
     <td>${teacher.id}</td>
      <td>${teacher.firstname}</td>
      <td>${teacher.lastname}</td>
     <td>${teacher.email}</td>
      <td>${teacher.department}</td>
      </tbody>
</table>
</div>
    </div>

</div>
</body>
</html>
