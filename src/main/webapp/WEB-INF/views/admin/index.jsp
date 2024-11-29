<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aperçu | Tableau de bord Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container">
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Tableau de bord Admin</h3>
        <h2>Aperçu</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin" class="active">Aperçu</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/students">Étudiants</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/teachers">Enseignants</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/courses" >Cours</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/requests">Demandes d'inscription</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Bonjour ${admin.firstname},</h1>
                <h2>Bienvenue sur votre <span>Tableau de bord Admin</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${admin.firstname} ${admin.lastname}</span>
                    <span class="user-email">${admin.email}</span>
                </div>
            </div>
        </header>

        <div class="overviewStudent">
            <div class="kpi-cards">
                <div class="kpi-card">
                    <h3>Demandes en attentes</h3>
                    <p>${nbrRequestsPending}</p>
                </div>
                <!-- Total Verified Students -->
                <div class="kpi-card">
                    <h3>Étudiants vérifiés</h3>
                    <p>${nbrVerifiedStudents}</p>
                </div>

                <!-- Total Verified Teachers -->
                <div class="kpi-card">
                    <h3>Enseignants vérifiés</h3>
                    <p>${nbrVerifiedTeachers}</p>
                </div>

                <!-- Percentage of Students with Global Average >= 10 -->
                <div class="kpi-card">
                    <h3>Taux de réussite</h3>
                    <p><fmt:formatNumber value="${totalPassingStudents}" type="number" maxFractionDigits="2" minFractionDigits="2"/>%</p>
                </div>
            </div>
        </div>

        <div class="overviewStudent">
            <h2>Statistiques Globales </h2>
            <div class="stats">
                <div class="stat-card" style="height: 400px; display: flex; flex-direction: column; align-items: center; justify-content: center;">
                    <br>
                    <h3>Taux de réussite Par Département</h3>
                    <canvas id="passingStudentsByDepartmentChart" style="align-items: center;"></canvas>
                </div>

            </div>

        </div>

        <div class="overviewStudent">
            <h2>Sélectionnez un département :</h2>
            <div id="department-buttons">

                <c:forEach items="${coursesByDepartment.keySet()}" var="dept">
                    <button class="department-button" data-department="${dept}">${dept.name}</button>
                </c:forEach>
            </div>

            <div id="charts-container">
                <div class="stats">
                    <div class="stat-card">
                        <h3>Taux de Réussite Par Cours</h3>
                        <canvas id="passingStudentsByCourseChart" style=" width: 100px; height: 200px;"></canvas>
                    </div>
                    <div class="stat-card">
                        <h3>Moyennes de Classe Par Cours</h3>
                        <canvas id="averageGradesByCourseChart" style=" width: 100px; height:200px;"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    const departmentColorsMap = {
        'SCIENCES': '#4CAF50',
        'LITTERATURE': '#FF9F40FF',
        'MATHEMATIQUES': '#ff0b39',
        'HISTOIRE': '#9C27B0',
        'PHYSIQUE': '#9966FFFF',
        'INFORMATIQUE': '#1a3aff',
        'LANGUES': '#FFE000FF',
        'CHIMIE': '#8fffa6',
        'ARTS': '#4BC0C0FF',
        'GEOGRAPHIE': '#2196F3'
    };
    // Prepare data for Passing Students by Department
    const departmentLabels = [
        <c:forEach items="${passingStudentsByDepartment.keySet()}" var="dept" varStatus="loop">
        '${dept.getName()}'<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    const departmentColors = departmentLabels.map(department => departmentColorsMap[department]);
    const passingStudentsData = [
        <c:forEach items="${passingStudentsByDepartment.values()}" var="val" varStatus="loop">
        '${val}'<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    // Render chart for Passing Students by Department
    const ctx1 = document.getElementById('passingStudentsByDepartmentChart').getContext('2d');
    new Chart(ctx1, {
        type: 'pie',
        data: {
            labels: departmentLabels,
            datasets: [{
                label: 'Passing Students (%)',
                data: passingStudentsData,
                backgroundColor: departmentColors,
                borderColor: departmentColors,
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                legend: {
                    position: 'left', // Place la légende à droite du graphique
                    labels: {
                        boxWidth: 20, // Ajuste la taille des carrés de couleur
                        padding: 15 // Ajuste l'espace autour des étiquettes de légende
                    }
                }
            },
            responsive: true
        }
    });
    const coursesByDepartmentData = {
        <c:forEach items="${coursesByDepartment}" var="entry">
        '${entry.key}': {
            courseLabels: [
                <c:forEach items="${entry.value}" var="course" varStatus="courseStatus">
                '${course.name}'<c:if test="${!courseStatus.last}">,</c:if>
                </c:forEach>
            ],
            passingStudentsData: [
                <c:forEach items="${entry.value}" var="course" varStatus="courseStatus">
                ${passingStudentsByCourse[course.id]}<c:if test="${!courseStatus.last}">,</c:if>
                </c:forEach>
            ],
            averageGradesData: [
                <c:forEach items="${entry.value}" var="course" varStatus="courseStatus">
                ${averageGradesByCourse[course.id]}<c:if test="${!courseStatus.last}">,</c:if>
                </c:forEach>
            ],
            color : departmentColorsMap['${entry.key.getName()}']
        },
        </c:forEach>
    };
    // Fonction pour créer un graphique
    function createChart(ctx, type, labels, data, label, color) {
        return new Chart(ctx, {
            type: type,
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: data,
                    backgroundColor: color,
                    borderColor: color,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    // Sélection des éléments Canvas
    const ctxPassingStudents = document.getElementById('passingStudentsByCourseChart').getContext('2d');
    const ctxAverageGrades = document.getElementById('averageGradesByCourseChart').getContext('2d');
    let currentPassingChart, currentAverageChart;
    // Ajouter un écouteur sur chaque bouton de département
    document.querySelectorAll('.department-button').forEach(button => {
        const department = button.getAttribute('data-department');
        const departmentData = coursesByDepartmentData[department];
        // Set button background color based on department color
        if (departmentData && departmentData.color) {
            button.style.backgroundColor = departmentData.color;
        }
        button.addEventListener('click', function () {
            const department = this.getAttribute('data-department');
            const data = coursesByDepartmentData[department];
            // Supprimer les graphiques existants
            if (currentPassingChart) currentPassingChart.destroy();
            if (currentAverageChart) currentAverageChart.destroy();
            // Créer de nouveaux graphiques avec les données du département sélectionné
            currentPassingChart = createChart(ctxPassingStudents, 'bar', data.courseLabels, data.passingStudentsData, 'Passing Students (%)', data.color);
            currentAverageChart = createChart(ctxAverageGrades, 'bar', data.courseLabels, data.averageGradesData, 'Average Grades', data.color);
        });
    });
</script>
</body>
</html>
