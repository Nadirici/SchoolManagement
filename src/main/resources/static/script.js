// Bouton overlay
document.getElementById('overlayBtn').addEventListener('click', () => {
    const container = document.getElementById('container');
    const overlayBtn = document.getElementById('overlayBtn');
    container.classList.toggle('right-panel-active');
    overlayBtn.classList.remove('btnScaled');
    window.requestAnimationFrame(() => {
        overlayBtn.classList.add('btnScaled');
    });
});

// Gestion des champs en fonction du type d'utilisateur
function toggleFields() {
    const userType = document.getElementById("userType").value;
    const birthDateField = document.getElementById("birthDateField");
    const departmentField = document.getElementById("departmentField");
    const dateOfBirthInput = document.getElementById("dateOfBirth");
    const departmentSelect = document.getElementById("department");

    if (userType === "student") {
        birthDateField.style.display = "block";
        departmentField.style.display = "none";
        dateOfBirthInput.setAttribute("required", "required");
        departmentSelect.removeAttribute("required");
    } else if (userType === "teacher") {
        birthDateField.style.display = "none";
        departmentField.style.display = "block";
        dateOfBirthInput.removeAttribute("required");
        departmentSelect.setAttribute("required", "required");
    }
}

// Écouteur de changement pour le type d'utilisateur
document.getElementById("userType").addEventListener("change", toggleFields);

// Validation du formulaire lors de la soumission
document.querySelector("form").addEventListener("submit", function(event) {
    const userType = document.getElementById("userType").value;
    const dateOfBirthInput = document.getElementById("dateOfBirth");
    const departmentSelect = document.getElementById("department");

    if (userType === "student" && dateOfBirthInput.style.display !== "none" && !dateOfBirthInput.value) {
        alert("Veuillez saisir votre date de naissance.");
        event.preventDefault();
    }

    if (userType === "teacher" && departmentSelect.style.display !== "none" && !departmentSelect.value) {
        alert("Veuillez choisir un département.");
        event.preventDefault();
    }
});

// Initialiser les champs en fonction de l'état initial
toggleFields();
