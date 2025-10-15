@echo off
rem Change directory to the script's location to ensure tasks.json is found
cd /d "%~dp0"
rem Run the Java application, passing all arguments
java -cp "out/production/Task_Tracker" Main %*
