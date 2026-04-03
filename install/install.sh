#!/bin/bash

echo "Instalando Task Tracker CLI..."

# Pasta de instalação
INSTALL_DIR="$HOME/.task-tracker"

# Copia o projeto
cp -r . "$INSTALL_DIR"

# Cria pasta de saída se não existir
mkdir -p "$INSTALL_DIR/out/production/Task_Tracker"

# Compila
javac -d "$INSTALL_DIR/out/production/Task_Tracker" "$INSTALL_DIR/src/"*.java

# Cria comando global
mkdir -p "$HOME/.local/bin"

echo '#!/bin/bash
'"$INSTALL_DIR"'scripts/task.sh"$@"' > "$HOME/.local/bin/task"

# Permissão
chmod +x "$HOME/.local/bin/task"

echo "Instalação concluída!"
echo "Use: task add \"Sua tarefa\""