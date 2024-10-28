

const typingText = document.querySelector(".typing-text p")
const inpField = document.querySelector(".wrapper .input-field")
const tryAgainBtn = document.querySelector(".content button")
const timeTag = document.querySelector(".time span b")
const mistakeTag = document.querySelector(".mistake span")
const wpmTag = document.querySelector(".wpm span")
const cpmTag = document.querySelector(".cpm span")

let timer;
let maxTime = 60;
let timeLeft = maxTime;
let charIndex = mistakes = isTyping = 0;

function loadParagraph() {
    // Récupérez le texte aléatoire depuis l'élément caché
    const randomText = document.getElementById("hiddenRandomText").innerText;
    typingText.innerHTML = ""; // Réinitialiser le champ de texte

    // Remplir le champ avec les caractères du randomText
    randomText.split("").forEach(char => {
        console.log(char); // Affiche chaque caractère
        let span = `<span>${char}</span>`; // Crée un élément span pour chaque caractère
        typingText.innerHTML += span; // Ajoute le span au champ de texte
    });

    typingText.querySelectorAll("span")[0].classList.add("active"); // Active le premier caractère
    document.addEventListener("keydown", () => inpField.focus()); // Focus sur le champ de saisie
    typingText.addEventListener("click", () => inpField.focus()); // Focus sur le champ de saisie au clic
}


let hasSubmitted = false; // Ajoutez cette variable en dehors de la fonction initTyping

function initTyping() {
    let characters = typingText.querySelectorAll("span");
    let typedChar = inpField.value.split("")[charIndex];

    if (charIndex < characters.length - 1 && timeLeft > 0) {
        if (!isTyping) {
            timer = setInterval(initTimer, 1000);
            isTyping = true;
        }
        if (typedChar == null) {
            if (charIndex > 0) {
                charIndex--;
                if (characters[charIndex].classList.contains("incorrect")) {
                    mistakes--;
                }
                characters[charIndex].classList.remove("correct", "incorrect");
            }
        } else {
            if (characters[charIndex].innerText == typedChar) {
                characters[charIndex].classList.add("correct");
            } else {
                mistakes++;
                characters[charIndex].classList.add("incorrect");
            }
            charIndex++;
        }
        characters.forEach(span => span.classList.remove("active"));
        characters[charIndex].classList.add("active");

        let wpm = Math.round(((charIndex - mistakes) / 5) / (maxTime - timeLeft) * 60);
        wpm = wpm < 0 || !wpm || wpm === Infinity ? 0 : wpm;

        wpmTag.innerText = wpm;
        mistakeTag.innerText = mistakes;
        cpmTag.innerText = charIndex - mistakes;
    } else {
        clearInterval(timer);
        inpField.value = "";
    }

    if (charIndex >= characters.length - 1 || timeLeft <= 0) {
        clearInterval(timer);

        // Calculer les résultats
        let finalWpm = Math.round(((charIndex - mistakes) / 5) / (maxTime - timeLeft) * 60);
        finalWpm = finalWpm < 0 || !finalWpm || finalWpm === Infinity ? 0 : finalWpm;

        let finalCpm = charIndex - mistakes;

        if(mistakes === 0){
            mistakes++;
        }

        // Créer un objet pour les résultats
        const results = {
            errors: mistakes,
            wpm: finalWpm,
            cpm: finalCpm
        };

        // Vérifier si les résultats ont déjà été soumis
        if (!hasSubmitted) {
            hasSubmitted = true; // Indiquer que la soumission a déjà eu lieu

            // Envoyer les résultats au servlet
            fetch('http://localhost:8080/Speed_war_exploded/saveResult', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(results)
            })
                .then(response => {
                    console.log(response);
                    if (response.ok) {
                        return response.json(); // Traitez la réponse si nécessaire
                    }
                    throw new Error('Network response was not ok.');
                })
                .then(data => {
                    console.log('Success:', data); // Vous pouvez traiter les données de la réponse ici
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }

        toggle(finalWpm,finalCpm,mistakes-1);

        inpField.value = ""; // Réinitialiser le champ de saisie
    }
}

function toggle(param1, param2, param3) {
    // Sélectionner les éléments
    const resultFinal = document.getElementById('resultat-final');
    const restart = document.getElementById('restart');
    const resultDetails = document.querySelector('.result-details');

    // Cacher l'élément <ul> contenant les détails
    resultDetails.style.display = 'none'; // Cacher le ul

    // Mettre à jour le texte dans le <p> avec l'id "resultat-final"
    resultFinal.innerText = `Résultats : Mots/minutes = ${param1}, Caractère/minutes = ${param2}, Erreur(s) = ${param3}`;
    // S'assurer que le p est affiché et styliser
    resultFinal.style.display = 'block'; // S'assurer que le p est affiché
    resultFinal.style.color = 'aliceblue'; // Modifier la couleur du texte
    resultFinal.style.textAlign = 'center'; // Centrer le texte
    resultFinal.style.marginBottom = '10px'; // Ajoute de l'espace en bas

    // Afficher le bouton "Réessayez"
    restart.style.display = 'inline-block'; // S'assurer que le bouton est affiché
}



function initTimer() {
    if (timeLeft > 0) {
        timeLeft--;
        timeTag.innerText = timeLeft;
        let wpm = Math.round(((charIndex - mistakes) / 5) / (maxTime - timeLeft) * 60);
        wpmTag.innerText = wpm;
    } else {
        clearInterval(timer);
    }
}


loadParagraph();
inpField.addEventListener("input", initTyping);
