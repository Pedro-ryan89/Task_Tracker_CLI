@echo off
rem Compile Java source files
javac -d out/production/Task_Tracker src/*.java
if %errorlevel% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)
