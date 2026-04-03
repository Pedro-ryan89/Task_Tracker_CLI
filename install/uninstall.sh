#!/bin/bash
# uninstall.sh

echo "Removendo binário..."
rm -f ~/.local/bin/task

echo "Removendo build antigo..."
rm -rf ~/.task-tracker/out

echo "Removendo arquivos de dados..."
rm -f ~/.task-tracker/tasks.json
rm -f ~/.task-tracker/deleted_tasks.json

echo "Desinstalação concluída!"