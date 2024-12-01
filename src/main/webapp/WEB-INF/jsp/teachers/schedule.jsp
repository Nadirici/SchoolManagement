<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Emploi du Temps de l'Enseignant</title>
  <link rel="stylesheet" href="/css/style.css" />
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.css' rel='stylesheet' />
  <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.10.1/main.min.js'></script>

</head>
<body>
<div class="container">
<%@include file="../headers/teacher_header.jsp"%>
<div class="main-content">


    <div class="overviewS">
        <h2>Emploi du Temps de l'Enseignant</h2>
        <div class="calendar-container">
          <div id="calendar">
          </div>
        </div>
    </div>

</div>
</div>

<script>
    $(document).ready(function () {
        var tooltip = $('<div class="event-tooltip"></div>').appendTo('body');

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