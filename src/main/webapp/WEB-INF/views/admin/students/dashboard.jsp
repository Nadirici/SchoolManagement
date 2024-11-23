<%@ page import="fr.cyu.schoolmanagementsystem.entity.Student" %>
<%@ page import="fr.cyu.schoolmanagementsystem.entity.Course" %>
<%@ page import="fr.cyu.schoolmanagementsystem.entity.Enrollment" %>
<%@ page import="fr.cyu.schoolmanagementsystem.util.CompositeStats" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>School Admin Dashboard</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css?v=1.0">
</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <!-- Sidebar content here -->
    </div>
    <h3>Admin Dashboard</h3>
    <h2>Overview</h2>
    <ul>
      <li><a href="/student/${sessionScope.userId}" class="active">Overview</a></li>

      <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Hi <%= ((Student) request.getAttribute("student")).getFirstname() %>,</h1>
        <h2>Welcome to <span>Overview Dashboard!</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username"><%= ((Student) request.getAttribute("student")).getFirstname() %> <%= ((Student) request.getAttribute("student")).getLastname() %></span>
          <span class="user-email"><%= ((Student) request.getAttribute("student")).getEmail() %></span>
        </div>
      </div>
    </header>

    <!-- Overview section displaying student and course info -->
    <div class="overviewStudent">
      <!-- Student Info -->
      <h2>Student Information</h2>
      <p>First Name: <%= ((Student) request.getAttribute("student")).getFirstname() %></p>
      <p>Last Name: <%= ((Student) request.getAttribute("student")).getLastname() %></p>
      <!-- Enrolled Courses with Stats -->
      <h2>Enrolled Courses</h2>
      <table border="1">
        <thead>
        <tr>
          <th>Course</th>
          <th>Grade Average</th>
          <th>Max Grade</th>
          <th>Min Grade</th>
          <th>Details</th>
        </tr>
        </thead>
        <tbody>
        <%
          Map<Enrollment, CompositeStats> enrollmentStats =
                  (Map<Enrollment, CompositeStats>) request.getAttribute("courseStats");
          for (Map.Entry<Enrollment, CompositeStats> entry : enrollmentStats.entrySet()) {
            Enrollment enrollment = entry.getKey();
            CompositeStats stats = entry.getValue();
        %>
        <tr>
          <td><%= enrollment.getCourse().getName() %></td>
          <td><%= stats.getAverage() != 0.0 ? stats.getAverage() : "N/A" %></td>
          <td><%= stats.getMax() != 0.0 ? stats.getMax() : "N/A" %></td>
          <td><%= stats.getMin() != 0.0 ? stats.getMin() : "N/A" %></td>
          <td><a href="/student/<%= ((Student) request.getAttribute("student")).getId() %>/course">View</a> </td>
        </tr>
        <% } %>
        </tbody>
      </table>

      <!-- Available Courses -->
      <h2>Available Courses</h2>
      <ul>
        <%
          List<Course> availableCourses =
                  (List<Course>) request.getAttribute("availableCourses");
          for (Course course : availableCourses) {
        %>
        <li><%= course.getName() %></li>
        <% } %>
      </ul>

      <!-- Overall Average -->
      <h2>Overall Average</h2>
      <p><%= request.getAttribute("overallAverage") != null ? request.getAttribute("overallAverage") : "N/A" %></p>
    </div>
  </div>
</div>
</body>
</html>
