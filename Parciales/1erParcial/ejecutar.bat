@echo off
cd /d "C:\Users\jaldi\Desktop\Sistemas Distribuidos\mavenproject1\src\main\java\respuestas"

echo.
echo ════════════════════════════════════════════════════════
echo    SISTEMA DISTRIBUIDO DE SOLICITUD DE BECAS
echo ════════════════════════════════════════════════════════
echo.

echo [*] Compilando archivos Java...
javac -encoding UTF-8 *.java
if errorlevel 1 (
    echo [ERROR] Compilacion fallida
    pause
    exit /b 1
)

echo [OK] Compilacion exitosa
echo.
echo [*] Iniciando servidores...
echo.

echo [1] Iniciando SEGIP TCP (puerto 5001)...
start "SEGIP TCP" cmd /k java ServidorSEGIPTCP
timeout /t 2 /nobreak

echo [2] Iniciando BIENESTAR RMI (puerto 1099)...
start "BIENESTAR RMI" cmd /k java ServidorBienestarRMI
timeout /t 2 /nobreak

echo [3] Iniciando FINANCIERO UDP (puerto 5002)...
start "FINANCIERO UDP" cmd /k java ServidorFinancieroUDP
timeout /t 2 /nobreak

echo [4] Iniciando SERVIDOR UNIVERSITARIO RMI (puerto 1099)...
start "SERVIDOR UNIVERSITARIO" cmd /k java ServidorUniversitario
timeout /t 3 /nobreak

echo.
echo [5] Ejecutando CLIENTE...
echo.

java ClienteUniversitario

echo.
echo ════════════════════════════════════════════════════════
echo Sistema completado. Cierra manualmente las ventanas.
echo ════════════════════════════════════════════════════════
echo.
pause
