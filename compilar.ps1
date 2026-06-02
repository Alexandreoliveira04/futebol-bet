# compilar.ps1 — Compila todos os fontes Java com o driver SQLite no classpath
$ErrorActionPreference = "Stop"

$jar = ".\lib\sqlite-jdbc-3.47.1.0.jar"

if (-not (Test-Path $jar)) {
    Write-Host "[ERRO] Driver SQLite nao encontrado. Execute .\setup.ps1 primeiro." -ForegroundColor Red
    exit 1
}

if (-not (Test-Path ".\out")) {
    New-Item -ItemType Directory -Path ".\out" | Out-Null
}

Write-Host "Compilando..." -ForegroundColor Cyan
$files = Get-ChildItem -Recurse -Filter *.java .\src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp $jar -d .\out $files

if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] Compilacao concluida com sucesso." -ForegroundColor Green
} else {
    Write-Host "[ERRO] Falha na compilacao." -ForegroundColor Red
    exit 1
}
