@echo off
echo Removendo binário...
del /f /q "%USERPROFILE%\.local\bin\task.exe"

echo Removendo build antigo...
rmdir /s /q "%USERPROFILE%\.task-tracker\out"

echo Removendo arquivos de dados...
del /f /q "%USERPROFILE%\.task-tracker\tasks.json"
del /f /q "%USERPROFILE%\.task-tracker\deleted_tasks.json"

echo Desinstalação concluída!
pause