<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/22/2024
  Time: 10:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>${course.name}</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Student Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
  <%@include file="../headers/teacher_header.jsp" %>
    <div class="main-content">
    <div class="overviewStudent">
      <%@include file="../courses/course_details_table.jsp" %>
    </div>
  </div>
  </div>
</div>
</body>
</html>
