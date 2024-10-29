


document.getElementById('overlayBtn').addEventListener('click', () => {
    const container = document.getElementById('container');
    const overlayBtn = document.getElementById('overlayBtn');
    container.classList.toggle('right-panel-active');
    overlayBtn.classList.remove('btnScaled');
    window.requestAnimationFrame(() => {
        overlayBtn.classList.add('btnScaled');
    });
});

function toggleDepartmentField() {
    const userType = document.getElementById('userType').value;
    const birthDateField = document.getElementById('birthDateField');
    const departmentField = document.getElementById('departmentField');

    if (userType === "teacher") {
        birthDateField.style.display = "none";
        departmentField.style.display = "block";
    } else {
        birthDateField.style.display = "block";
        departmentField.style.display = "none";
    }
}

function toggleFields() {
    const userType = document.getElementById("userType").value;
    const birthDateField = document.getElementById("birthDateField");
    const departmentField = document.getElementById("departmentField");
    const dateOfBirthInput = document.getElementById("date_of_birth");
    const departmentSelect = document.getElementById("department");

    if (userType === "student") {
        birthDateField.style.display = "block";
        departmentField.style.display = "none";
        dateOfBirthInput.setAttribute("required", "required");
        dateOfBirthInput.style.display = "block"; // Assurez-vous que le champ est visible
        departmentSelect.removeAttribute("required");
    } else if (userType === "teacher") {
        birthDateField.style.display = "none";
        departmentField.style.display = "block";
        dateOfBirthInput.removeAttribute("required");
        dateOfBirthInput.style.display = "none"; // Masquer le champ date de naissance
        departmentSelect.setAttribute("required", "required");
    }
}

document.querySelector("form").addEventListener("submit", function(event) {
    const userType = document.getElementById("userType").value;
    const dateOfBirthInput = document.getElementById("date_of_birth");
    const departmentSelect = document.getElementById("department");

    if (userType === "student" && dateOfBirthInput.style.display !== "none" && !dateOfBirthInput.value) {
        alert("Veuillez saisir votre date de naissance.");
        event.preventDefault(); // Empêche la soumission
        return;
    }

    if (userType === "teacher" && departmentSelect.style.display !== "none" && !departmentSelect.value) {
        alert("Veuillez choisir un département.");
        event.preventDefault(); // Empêche la soumission
        return;
    }
});
