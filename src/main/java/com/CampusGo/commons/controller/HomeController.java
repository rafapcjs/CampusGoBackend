package com.CampusGo.commons.controller;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Campus GO - Gestión Académica 📚</title>
                <script src="https://cdn.tailwindcss.com"></script>
            </head>
            <body class="bg-gray-100 flex items-center justify-center h-screen">

                <div class="max-w-3xl mx-auto bg-white p-8 shadow-lg rounded-lg text-center">
                    <h1 class="text-4xl font-bold text-blue-600 mb-4">📚 Campus GO</h1>
                    <p class="text-gray-700 text-lg">La plataforma integral para administrar:</p>

                    <div class="grid grid-cols-2 gap-4 mt-6">
                        <div class="bg-blue-100 p-4 rounded-lg shadow">
                            <h2 class="text-xl font-semibold text-blue-700">📖 Materias</h2>
                        </div>
                        <div class="bg-green-100 p-4 rounded-lg shadow">
                            <h2 class="text-xl font-semibold text-green-700">🎓 Estudiantes</h2>
                        </div>
                        <div class="bg-yellow-100 p-4 rounded-lg shadow">
                            <h2 class="text-xl font-semibold text-yellow-700">📑 Matrículas</h2>
                        </div>
                        <div class="bg-red-100 p-4 rounded-lg shadow">
                            <h2 class="text-xl font-semibold text-red-700">👨‍🏫 Docentes</h2>
                        </div>
                    </div>

                    <div class="mt-6 space-y-4">
                        <a href="#" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">📌 Ver Notas</a>
                        <a href="#" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded ml-2">📋 Gestionar Estudiantes</a>
                        <a href="https://campusgobackend.onrender.com/swagger-ui/index.html" target="_blank" class="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded block mx-auto">📜 API Docs (Swagger)</a>
                    </div>
                </div>

            </body>
            </html>
            """;
    }}