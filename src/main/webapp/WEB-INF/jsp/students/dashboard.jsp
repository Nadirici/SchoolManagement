<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.StudentDTO" %>
<%@ page import="java.util.UUID" %>
<%@ page import="fr.cyu.schoolmanagementsystem.model.dto.CourseDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <%@include file="../headers/student_header.jsp"%>
    <div class="main-content">
    <div class="overviewStudent">
            <h2>Overview</h2>
            <div class="stats">
                <div class="stat-card">
                    <h3>Moyenne générale</h3>
                    <p>${studentGlobalAverage}</p>
                </div>
                <div class="stat-card">
                    <h3>Nombre de cours auxquels tu es inscrit</h3>
                    <p>${courses.size()}</p>
                </div>
            </div>

            <h2>Les cours auxquels tu es inscrit</h2>

            <%
                boolean noCourses = (boolean) request.getAttribute("noCourses");
                StudentDTO studentDTO = (StudentDTO) request.getAttribute("student");
            %>

            <p><a href="/students/<%= studentDTO.getId() %>/enroll">Inscris-toi maintenant aux cours</a></p>

            <%
                String flashMessage = (String) request.getAttribute("flashMessage");

                if (flashMessage == null) {
                    flashMessage = request.getParameter("flashMessage");
                }

                if (flashMessage != null) {
                    switch (flashMessage) {
                        case "enrollmentSuccess":
                            out.println("<div class='flash-message flash-success'>Inscription réussie!</div>");
                            break;
                        case "enrollmentFailed":
                            out.println("<div class='flash-message flash-error'>Votre compte étudiant n'est pas encore vérifié.</div>");
                            break;
                        default:
                            out.println("<div class='flash-message flash-error'>Erreur inconnue.</div>");
                    }
                }
            %>

            <% if (!noCourses) { %>
            <table>
                <thead>
                <tr>
                    <th>Cours</th>
                    <th>Ta moyenne</th>
                    <th>Moyenne minimum</th>
                    <th>Moyenne maximum</th>
                    <th>Détails</th>
                </tr>
                </thead>
                <tbody>
                <%
                    // Récupérer les paramètres avant la boucle
                    Map<UUID, String> courseAveragesMap = (Map<UUID, String>) request.getAttribute("courseAverages");
                    Map<UUID, Double> courseMinAveragesMap = (Map<UUID, Double>) request.getAttribute("minAverages");
                    Map<UUID, Double> courseMaxAveragesMap = (Map<UUID, Double>) request.getAttribute("maxAverages");
                    List<CourseDTO> courses = (List<CourseDTO>) request.getAttribute("courses");

                    // Boucle classique pour itérer sur les cours
                    for (CourseDTO course : courses) {

                        UUID courseId = course.getId(); // Récupérer l'ID du cours

                        // Récupérer les valeurs de moyenne, minimum et maximum ou définir un message si l'étudiant n'a pas de notes
                        String courseAverage = courseAveragesMap.get(courseId);
                        double minAverage = courseMinAveragesMap.getOrDefault(courseId, 0.0);  // Valeur par défaut si pas trouvé
                        double maxAverage = courseMaxAveragesMap.getOrDefault(courseId, 0.0);  // Valeur par défaut si pas trouvé
                %>
                <tr>
                    <td><%= course.getName() %></td>
                    <td><%= courseAverage %></td>
                    <td><%= minAverage %></td>
                    <td><%= maxAverage %></td>
                    <td><a href="/students/<%= studentDTO.getId() %>/courses/<%= course.getId() %>">Voir</a></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
            <% } %>

        <form action="/students/<%= studentDTO.getId() %>/report/pdf" method="get" target="_blank">
            <button type="submit" class="download-pdf-button">Télécharger le bulletin en PDF</button>
        </form>


    </div>
    </div>
</div>
</body>
</html>
