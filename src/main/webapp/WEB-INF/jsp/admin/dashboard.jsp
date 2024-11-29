<%@ page import="fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement" %><%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 06/11/2024
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    Class<Departement> departementClass = fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement.class;
    pageContext.setAttribute("Departement", departementClass);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>School Admin Dashboard</title>
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

</head>
<body>
<div class="container">
    <%@include file="../headers/admin_header.jsp"%>
    <div class="main-content">

        <div class="overview">
            <div class="stat-card">
                <h3>Demandes En Attente</h3>
                <br>
                <strong>${nbrRequestsPending}</strong>
            </div>


            <div class="stat-card">
                <h3>Nombre d'Etudiants</h3>
                <br>
                <strong>${nbrVerifiedStudents}</strong>
            </div>


            <div class="stat-card">
                <h3>Nombre d'Enseignants</h3>
                <br>
                <strong>${nbrVerifiedTeachers}</strong>
            </div>


            <div class="stat-card">
                <h3>Taux de réussite</h3>

                <p><fmt:formatNumber value="${totalPassingStudents}" type="number" maxFractionDigits="2" minFractionDigits="2" /> %</p>
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
