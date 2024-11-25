<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Index</title>
    <link rel="stylesheet" href="./css/style.css">
    <script>
        // Redirection automatique après 0 secondes
        setTimeout(function() {
            window.location.href = "login"; // Changez l'URL cible ici
        }, 1);
    </script>
    <style>
        .spinner {
            animation: rotator 1.4s linear infinite;
        }
        @keyframes rotator {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(270deg);
            }
        }
        .path {
            stroke-dasharray: 187;
            stroke-dashoffset: 0;
            transform-origin: center;
            animation: dash 1.4s ease-in-out infinite, colors 5.6s ease-in-out infinite;
        }
        @keyframes colors {
            0% {
                stroke: #4285f4;
            }
            25% {
                stroke: #de3e35;
            }
            50% {
                stroke: #f7c223;
            }
            75% {
                stroke: #1b9a59;
            }
            100% {
                stroke: #4285f4;
            }
        }
        @keyframes dash {
            0% {
                stroke-dashoffset: 187;
            }
            50% {
                stroke-dashoffset: 46.75;
                transform: rotate(135deg);
            }
            100% {
                stroke-dashoffset: 187;
                transform: rotate(450deg);
            }
        }
    </style>
</head>
<body>
<div style="display: flex; justify-content: center; align-items: center; height: 100vh; flex-direction: column">
    <h1 style="margin-bottom: 20px">Veuillez patienter quelques instants</h1>
    <svg class="spinner" width="65px" height="65px" viewBox="0 0 66 66" xmlns="http://www.w3.org/2000/svg">
        <circle class="path" fill="none" stroke-width="6" stroke-linecap="round" cx="33" cy="33" r="30"></circle>
    </svg>
</div>
</body>
</html>