# executar.ps1 — Executa o Futebol Bet com o driver SQLite no classpath
$jar = ".\lib\sqlite-jdbc-3.47.1.0.jar"

if (-not (Test-Path $jar)) {
    Write-Host "[ERRO] Driver SQLite nao encontrado. Execute .\setup.ps1 primeiro." -ForegroundColor Red
    exit 1
}

if (-not (Test-Path ".\out\br\com\futebolbet\Main.class")) {
    Write-Host "[AVISO] Projeto nao compilado. Executando compilar.ps1..." -ForegroundColor Yellow
    & .\compilar.ps1
    if ($LASTEXITCODE -ne 0) { exit 1 }
}

Write-Host "Iniciando Futebol Bet..." -ForegroundColor Cyan
java -cp ".\out;$jar" br.com.futebolbet.Main
