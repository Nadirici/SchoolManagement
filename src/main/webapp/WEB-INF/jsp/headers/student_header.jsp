<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/22/2024
  Time: 11:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar">
    <div class="dashboard-icon">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Tableau de bord de l'étudiant</h3>
    <h2>Vue d'ensemble</h2>
    <ul>
        <li><a href="/students/${student.id}" id="link-dashboard">Dashboard</a></li>
        <li><a href="/students/${student.id}/enroll" id="link-enroll">S'inscrire au cours</a></li>
        <li><a href="/students/${student.id}/schedule">Emploi du temps</a> </li>
        <li><a href="/students/${student.id}/profile" id="link-profile">Profile</a></li>
        <li><a href="/logout">Se déconnecter</a></li>
    </ul>
</div>

<div class="main-content">
    <header class="header">
        <div class="header-left">
            <h1>👋 Bonjour ${student.firstname},</h1>
            <h2>Bienvenue sur ton <span>Tableau de bord.</span></h2>
        </div>
        <div class="header-right">
            <div class="user-profile">
                <span class="username">${student.firstname} ${student.lastname}</span>
                <span class="user-email">${student.email}</span>
            </div>
        </div>
    </header></div>

<script>
    // JavaScript to set the active class based on the current URL
    document.addEventListener('DOMContentLoaded', function () {
        const currentUrl = window.location.pathname;
        const navLinks = document.querySelectorAll('.sidebar ul li a');

        navLinks.forEach(link => {
            if (link.getAttribute('href') === currentUrl) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    });
</script>