@echo off
REM --- Limpa build anterior ---
rmdir /s /q out\production\Task_Tracker
mkdir out\production\Task_Tracker

REM --- Lista todos os arquivos .java ---
dir /s /b src\*.java > sources.txt

REM --- Compila ---
javac -d out\production\Task_Tracker @sources.txt

del sources.txt

IF %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
) ELSE (
    echo Compilation failed!
)
pause