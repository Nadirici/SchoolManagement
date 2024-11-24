
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>${course.name}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <%@include file="../headers/student_header.jsp"%>
    <div class="main-content">

        <div class="overviewStudent">
            <h2>${course.name}</h2>
            <div class="stats">
                <div class="stat-card">
                    <h3>Description :</h3>
                    <p> ${course.description}</p>
                </div>
                <div class="stat-card">
                    <h3>Enseignant :</h3>
                    <p>${course.teacher.firstname} ${course.teacher.lastname}</p>
                </div>
            </div>
            <%@ include file="../courses/course_details_table.jsp" %>


        </div>
    </div>
</div>
</body>
</html>
