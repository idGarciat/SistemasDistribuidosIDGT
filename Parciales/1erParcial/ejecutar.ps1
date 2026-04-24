cd "C:\Users\jaldi\Desktop\Sistemas Distribuidos\mavenproject1\src\main\java\respuestas"

Write-Host "════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "   SISTEMA DISTRIBUIDO DE SOLICITUD DE BECAS" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

Write-Host "[*] Compilando archivos Java..." -ForegroundColor Yellow
javac -encoding UTF-8 *.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilación fallida" -ForegroundColor Red
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host "[OK] Compilación exitosa" -ForegroundColor Green
Write-Host ""
Write-Host "[*] Iniciando servidores..." -ForegroundColor Yellow
Write-Host ""

Write-Host "[1] Iniciando SEGIP TCP (puerto 5001)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$pwd'; java ServidorSEGIPTCP" -WindowStyle Normal

Start-Sleep -Seconds 2

Write-Host "[2] Iniciando BIENESTAR RMI (puerto 1099)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$pwd'; java ServidorBienestarRMI" -WindowStyle Normal

Start-Sleep -Seconds 2

Write-Host "[3] Iniciando FINANCIERO UDP (puerto 5002)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$pwd'; java ServidorFinancieroUDP" -WindowStyle Normal

Start-Sleep -Seconds 2

Write-Host "[4] Iniciando SERVIDOR UNIVERSITARIO RMI (puerto 1099)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$pwd'; java ServidorUniversitario" -WindowStyle Normal

Start-Sleep -Seconds 3

Write-Host ""
Write-Host "[5] Ejecutando CLIENTE..." -ForegroundColor Cyan
Write-Host ""

java ClienteUniversitario

Write-Host ""
Write-Host "════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Sistema completado. Cierra las ventanas de los servidores." -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════" -ForegroundColor Cyan
