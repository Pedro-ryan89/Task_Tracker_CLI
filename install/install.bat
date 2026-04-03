@echo off
echo Instalando Task Tracker CLI...

set INSTALL_DIR=%USERPROFILE%\.task-tracker

REM Copiar arquivos
xcopy /E /I /Y . %INSTALL_DIR%

REM Criar pasta de saída
mkdir %INSTALL_DIR%\out\production\Task_Tracker

REM Compilar
javac -d %INSTALL_DIR%\out\production\Task_Tracker %INSTALL_DIR%\src\*.java

REM Criar comando global
echo @echo off > %INSTALL_DIR%\task.cmd
echo java -cp "%INSTALL_DIR%\out\production\Task_Tracker" Main %%* >> %INSTALL_DIR%\task.cmd

REM Adicionar ao PATH (usuário)
setx PATH "%PATH%;%INSTALL_DIR%"

echo.
echo Instalacao concluida!
echo Feche e abra o terminal novamente.
echo Depois use: task add "Sua tarefa"