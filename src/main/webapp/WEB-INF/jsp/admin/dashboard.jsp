<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>School Admin Dashboard</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Admin Dashboard</h3>
        <h2>Overview</h2>
        <ul>
            <li><a href="/admin/${id}">Dashboard</a></li>
            <li><a href="/admin/${id}/students">Students</a></li>
            <li><a href="#">Teachers</a></li>
            <li><a href="#">Courses</a></li>
            <li><a href="#">Results</a></li>
            <li><a href="/admin/${id}/requests">Demandes d'inscription</a></li>
            <li><a href="#">Settings</a></li>
            <li><a href="#">Logout</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Hi ${admin.firstname},</h1>
                <h2>Welcome to <span>Overview Dashboard!</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${admin.firstname} ${admin.lastname}</span>
                    <span class="user-email">${admin.email}</span>
                </div>
            </div>
        </header>

        <div class="overview">
            <div class="card">
                <h3>Total Students</h3>
                <p>450</p>
            </div>
            <div class="card">
                <h3>Total Teachers</h3>
                <p>25</p>
            </div>
            <div class="card">
                <h3>Total Courses</h3>
                <p>15</p>
            </div>
            <div class="card">
                <h3>Upcoming Events</h3>
                <p>3 Events</p>
            </div>
        </div>

        <div class="section">
            <h2>Recent Activities</h2>
            <table>
                <thead>
                <tr>
                    <th>Activity</th>
                    <th>Date</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>New Student Enrollment</td>
                    <td>2023-11-25</td>
                    <td>5 new students enrolled in the computer science program.</td>
                </tr>
                <tr>
                    <td>Teacher Assigned</td>
                    <td>2023-11-24</td>
                    <td>Mr. John Doe assigned to Math 101.</td>
                </tr>
                <tr>
                    <td>Course Scheduled</td>
                    <td>2023-11-23</td>
                    <td>Course on Data Science scheduled for next semester.</td>
                </tr>
                </tbody>
            </table>
        </div>

        <footer class="footer">
            <p>&copy; 2023 School Management System. All rights reserved.</p>
        </footer>
    </div>
</div>
</body>
</html>
