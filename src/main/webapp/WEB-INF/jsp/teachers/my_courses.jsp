<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/22/2024
  Time: 3:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<html>
<head>
  <title>${course.name}</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Course Details</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="container">
      <%@include file="/WEB-INF/jsp/headers/teacher_header.jsp" %>

  <div class="main-content">
    <div class="overviewStudent">
      <h2>Liste des cours</h2>
      <%@include file="../lists/courses_list.jsp" %>
    </div>
  </div>
</div>
</div>

</div>
</body>
</html>
