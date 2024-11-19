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
    <div class="sidebar">
        <div class="dashboard-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--! Font Awesome Free --><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
        </div>
        <h3>Student Dashboard</h3>
        <ul>
            <li><a href="/students/${student.id}" class="active">Dashboard</a></li>
            <li><a href="/students/${student.id}/enroll">S'inscrire au cours</a></li>

            <li><a href="/students/${student.id}/profile">Profile</a></li>
            <form action="/logout" method="post">
                <li><input type="submit" value="Logout"></li>
            </form>
        </ul>
    </div>

    <div class="main-content">
        <header class="header">
            <div class="header-left">
                <h1>👋 Bonjour ${student.firstname},</h1>
                <h2>Bienvenue sur ton <span>Dashboard!</span></h2>
            </div>
            <div class="header-right">
                <div class="user-profile">
                    <span class="username">${student.firstname} ${student.lastname}</span>
                    <span class="user-email">${student.email}</span>
                </div>
            </div>
        </header>

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

                // Vérifiez si le flashMessage est nul et, dans ce cas, récupérez le paramètre de l'URL
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
        </div>
    </div>
</div>
</body>
</html>
