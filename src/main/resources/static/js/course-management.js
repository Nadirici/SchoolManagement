document.addEventListener('DOMContentLoaded', function () {
  // Récupérer les informations de l'admin depuis l'élément HTML
  const adminElement = document.getElementById('admin');
  const admin = {
    id: adminElement.getAttribute('data-id'),
    firstname: adminElement.getAttribute('data-firstname'),
    lastname: adminElement.getAttribute('data-lastname'),
    email: adminElement.getAttribute('data-email')
  };

  console.log("Admin Info:", admin);

  const teacherSelect = document.getElementById('teacherSelect');
  const submitButton = document.getElementById('submitButton');

  if (teacherSelect) {
    teacherSelect.addEventListener('change', validateForm);
  }

  if (submitButton) {
    submitButton.disabled = true;  // Désactiver le bouton par défaut
  }
});

// Fonction pour charger les enseignants dynamiquement selon le département
function loadTeachers(department) {
  const teacherSelect = document.getElementById('teacherSelect');
  const submitButton = document.getElementById('submitButton');
  teacherSelect.innerHTML = '';  // Réinitialiser la liste des enseignants

  submitButton.disabled = true;

  if (department) {
    console.log("Fetching teachers for department:", department);

    fetch(`/admin/${admin.id}/teachers/` + department, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })
        .then(response => response.json())
        .then(data => {
          const defaultOption = document.createElement('option');
          defaultOption.value = '';
          defaultOption.text = 'Selectionner un professeur de ce département';
          teacherSelect.appendChild(defaultOption);

          if (data.length === 0) {
            defaultOption.text = 'Aucun professeur disponible pour ce département';
          } else {
            data.forEach(teacher => {
              const option = document.createElement('option');
              option.value = teacher.email;
              option.text = teacher.firstname + ' ' + teacher.lastname;
              teacherSelect.appendChild(option);
            });
          }
        })
        .catch(error => {
          console.error('Error fetching teachers:', error);
        });
  }
}

// Fonction pour vérifier si tous les champs sont remplis
function validateForm() {
  const teacherSelect = document.getElementById('teacherSelect');
  const submitButton = document.getElementById('submitButton');

  if (teacherSelect.value && teacherSelect.value !== 'Aucun professeur disponible pour ce département') {
    submitButton.disabled = false;
  } else {
    submitButton.disabled = true;
  }
}
