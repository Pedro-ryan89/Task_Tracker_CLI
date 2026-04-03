#!/bin/bash
echo "Instalando Task Tracker CLI..."

# cria pasta
mkdir -p ~/.task-tracker

# copia arquivos do src e scripts, ignorando .git
rsync -av --exclude='.git' ./ ~/.task-tracker/

@echo off

mkdir out\production\Task_Tracker 2>nul

dir /s /b src\*.java > sources.txt

#!/bin/bash
set -e  # Para o script se algum comando falhar

echo "Instalando Task Tracker CLI..."

# --- Limpa build anterior ---
rm -rf ~/.task-tracker/out
mkdir -p ~/.task-tracker/out

# --- Compila todos os arquivos .java ---
javac -d ~/.task-tracker/out \
    ~/.task-tracker/src/model/*.java \
    ~/.task-tracker/src/repository/*.java \
    ~/.task-tracker/src/service/*.java \
    ~/.task-tracker/src/TaskManager.java \
    ~/.task-tracker/src/Main.java

# --- Cria link simbólico para execução ---
mkdir -p ~/.local/bin
ln -sf ~/.task-tracker/out/Main ~/.local/bin/task

echo "Instalação concluída!"
echo "Use: task add \"Sua tarefa\""