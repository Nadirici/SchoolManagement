function filterTable() {
    // Récupérer la valeur de la recherche
    const input = document.getElementById('searchInput');
    const filter = input.value.toLowerCase();
    const table = document.getElementById('myTable');
    const rows = table.getElementsByTagName('tr'); // Récupérer toutes les lignes du tableau
    // Parcourir toutes les lignes du tableau et les filtrer
    for (let i = 1; i < rows.length; i++) { // Commencer à 1 pour ignorer l'en-tête du tableau
        const cells = rows[i].getElementsByTagName('td');
        let match = false;
        // Vérifier si l'une des cellules de la ligne contient le texte de recherche
        for (let j = 0; j < cells.length; j++) {
            const cell = cells[j];
            if (cell.textContent.toLowerCase().includes(filter)) {
                match = true;
                break;
            }
        }
        // Afficher ou cacher la ligne en fonction de la recherche
        if (match) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
}