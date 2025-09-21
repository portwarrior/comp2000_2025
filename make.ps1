# Usage: .\make.ps1 clean, .\make.ps1 compile, .\make.ps1 run, or .\make.ps1 (does all)

param(
    [string]$target = "all"
)

$SRC_DIR = "src"
$MAIN_CLASS = "Main"

function Clean {
    Write-Host "Cleaning compiled files..." -ForegroundColor Yellow
    Set-Location $SRC_DIR
    Remove-Item -Recurse -Force *.class -ErrorAction SilentlyContinue
    Get-ChildItem -Recurse -Include *.class | Remove-Item -Force -ErrorAction SilentlyContinue
    if ($?) {
        Write-Host "Class files cleaned" -ForegroundColor Green
    } else {
        Write-Host "No class files to clean" -ForegroundColor Gray
    }
    Set-Location ..
}

function Compile {
    Write-Host "Compiling Java files..." -ForegroundColor Yellow
    Set-Location $SRC_DIR
    
    # Check if Main.java exists
    if (!(Test-Path $MAIN_CLASS.java)) {
        Write-Host "Error: $MAIN_CLASS.java not found!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    
    javac -cp . $MAIN_CLASS.java
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Compilation successful!" -ForegroundColor Green
    } else {
        Write-Host "Compilation failed!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Set-Location ..
}

function Run {
    Write-Host "Running the game..." -ForegroundColor Yellow
    Set-Location $SRC_DIR
    java $MAIN_CLASS
    Set-Location ..
}

switch ($target.ToLower()) {
    "clean" { Clean }
    "compile" { Compile }
    "run" { Run }
    "all" { 
        Clean
        Compile
        Run
    }
    default {
        Write-Host "Usage: .\make.ps1 [clean|compile|run|all]" -ForegroundColor Red
        Write-Host "  clean   - Remove all .class files"
        Write-Host "  compile - Compile Java files"
        Write-Host "  run     - Run the program"
        Write-Host "  all     - Do all steps (default)"
    }
}