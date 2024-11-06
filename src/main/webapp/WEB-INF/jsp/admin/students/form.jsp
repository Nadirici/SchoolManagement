<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${student.id == null ? 'Add Student' : 'Edit Student'}</title>
    <%--<link rel="stylesheet" href="/css/style.css">--%>
</head>
<body>
<h1>${student.id == null ? 'Add New Student' : 'Edit Student'}</h1>

<form action="${student.id == null ? '/admin/students' : '/admin/students/' + student.id}" method="post">
    <c:if test="${student.id != null}">
        <input type="hidden" name="_method" value="put" />
    </c:if>

    <label for="firstname">First Name:</label>
    <input type="text" id="firstname" name="firstname" value="${student.firstname}" required><br>

    <label for="lastname">Last Name:</label>
    <input type="text" id="lastname" name="lastname" value="${student.lastname}" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${student.email}" required><br>

    <label for="dateOfBirth">Date Of Birth:</label>
    <input type="date" id="dateOfBirth" name="dateOfBirth" value="${student.dateOfBirth}" required>

    <button type="submit">${student.id == null ? 'Add Student' : 'Update Student'}</button>
</form>

<a href="/admin/students">Back to List</a>
</body>
</html>
