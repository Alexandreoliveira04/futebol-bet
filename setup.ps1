# setup.ps1 -- Baixa o driver SQLite e prepara o ambiente do projeto
$ErrorActionPreference = "Stop"

$libDir   = ".\lib"
$jarName  = "sqlite-jdbc-3.47.1.0.jar"
$jarPath  = "$libDir\$jarName"
$jarUrl   = "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.47.1.0/$jarName"

Write-Host "=== Futebol Bet -- Setup ===" -ForegroundColor Cyan

if (-not (Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir | Out-Null
    Write-Host "[OK] Diretorio lib/ criado." -ForegroundColor Green
}

if (Test-Path $jarPath) {
    Write-Host "[OK] Driver SQLite ja existe: $jarPath" -ForegroundColor Green
} else {
    Write-Host "Baixando SQLite JDBC de Maven Central..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $jarUrl -OutFile $jarPath -UseBasicParsing
    Write-Host "[OK] Download concluido: $jarPath" -ForegroundColor Green
}

if (-not (Test-Path ".\out")) {
    New-Item -ItemType Directory -Path ".\out" | Out-Null
}

Write-Host ""
Write-Host "Setup concluido! Execute:" -ForegroundColor Cyan
Write-Host "  .\compilar.ps1   para compilar o projeto" -ForegroundColor White
Write-Host "  .\executar.ps1   para rodar o projeto"    -ForegroundColor White
