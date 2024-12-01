<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Emploi du Temps de l'Enseignant</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.css" rel='stylesheet' />
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.js"></script>
</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="dashboard-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><!--!Font Awesome Free 6.6.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2024 Fonticons, Inc.--><path d="M304 240l0-223.4c0-9 7-16.6 16-16.6C443.7 0 544 100.3 544 224c0 9-7.6 16-16.6 16L304 240zM32 272C32 150.7 122.1 50.3 239 34.3c9.2-1.3 17 6.1 17 15.4L256 288 412.5 444.5c6.7 6.7 6.2 17.7-1.5 23.1C371.8 495.6 323.8 512 272 512C139.5 512 32 404.6 32 272zm526.4 16c9.3 0 16.6 7.8 15.4 17c-7.7 55.9-34.6 105.6-73.9 142.3c-6 5.6-15.4 5.2-21.2-.7L320 288l238.4 0z"/></svg>
    </div>
    <h3>Tableau de bord Enseignant</h3>
    <h2>Emploi du Temps</h2>
    <ul>
      <li><a href="${pageContext.request.contextPath}/teachers" >Aperçu</a></li>
      <li><a href="${pageContext.request.contextPath}/teachers/courses">Cours</a></li>
      <li><a href="${pageContext.request.contextPath}/teachers/schedule" class="active">Emploi du temps</a></li>
      <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
    </ul>
  </div>

  <div class="main-content">
    <header class="header">
      <div class="header-left">
        <h1>👋 Bonjour ${teacher.firstname},</h1>
        <h2>Bienvenue sur votre <span>Tableau de bord Enseignant</span></h2>
      </div>
      <div class="header-right">
        <div class="user-profile">
          <span class="username">${teacher.firstname} ${teacher.lastname}</span>
          <span class="user-email">${teacher.email}</span>
        </div>
      </div>
    </header>



    <div class="overviewS">
    <h1>Emploi du Temps de l'enseignant</h1>
    <div class="calendar-container">
      <div id="calendar">
      </div>
    </div>
  </div>

</div>
</div>

<script>
  $(document).ready(function () {
    const tooltip = $('<div class="event-tooltip"></div>').appendTo('body');

    if (typeof FullCalendar === 'undefined') {
      console.error('FullCalendar is not loaded');
      return;
    }

    const Calendar = FullCalendar.Calendar;
    const calendarEl = document.getElementById('calendar');

    if (!calendarEl) {
      console.error('Calendar element not found');
      return;
    }

    // Retrieve course schedule data from the backend and populate JavaScript array
    const events = [];
    <c:forEach var="course" items="${formattedEvents}">
    events.push({
      id: "${course.id}",
      title: "${course.title}",
      startRecur: '2024-11-20',  // Set start of the recurring week (arbitrary Monday date)
      daysOfWeek: [getDayOfWeekNumber("${course.dayOfWeek}")], // 1 for Monday, 2 for Tuesday, etc.
      startTime: "${course.startTime}", // E.g., "08:30"
      endTime: "${course.endTime}",     // E.g., "10:00"
      backgroundColor: 'rgba(34,133,246,0.96)',
      borderColor: 'rgba(153,211,252,0.96)'
    });
    </c:forEach>

    // Function to map French days to FullCalendar day numbers
    function getDayOfWeekNumber(frenchDay) {
      switch (frenchDay) {
        case 'Lundi': return 1;    // Monday
        case 'Mardi': return 2;    // Tuesday
        case 'Mercredi': return 3; // Wednesday
        case 'Jeudi': return 4;    // Thursday
        case 'Vendredi': return 5; // Friday
        case 'Samedi': return 6;   // Saturday
        case 'Dimanche': return 0; // Sunday
        default: return -1;        // Invalid
      }
    }

    console.log(events); // Debugging statement

    // Initialize FullCalendar
    const calendar = new Calendar(calendarEl, {
      locale: 'fr',                // Set to French
      initialView: 'timeGridWeek', // Week view
      slotMinTime: "08:00:00",     // Start day at 8:30 AM
      slotMaxTime: "20:00:00",     // End day at 7:30 PM
      slotDuration: '00:30:00',
      height: 'auto',   // Adjusts height automatically based on the events
      expandRows: true,    // Slots of 30 minutes
      headerToolbar: {
        left: '',
        center: 'title',
        right: 'timeGridWeek,timeGridDay'
      },   // Bootstrap theme if needed
      events: events,              // Attach the events data
      editable: false,             // Disable editing
      droppable: false,            // Disable drag-and-drop
      allDaySlot: false,
      hiddenDays: [0],  // Hide all-day slot
      eventMouseEnter: function (info) {
        // Set the content of the tooltip
        tooltip.html('<strong>Course: </strong>' + info.event.title + '<br><strong>Time: </strong>' + info.event.start.toLocaleString());

        // Position the tooltip near the mouse
        tooltip.css({
          top: info.jsEvent.pageY + 15 + 'px',
          left: info.jsEvent.pageX + 15 + 'px',
          display: 'block'
        });
      },
      eventMouseLeave: function () {
        // Hide the tooltip when the mouse leaves the event
        tooltip.hide();
      }
    });

    calendar.render();

  });
</script>

</body>
</html>
