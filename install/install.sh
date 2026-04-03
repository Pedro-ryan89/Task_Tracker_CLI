#!/bin/bash

echo "Instalando Task Tracker CLI..."

# Diretórios
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC_DIR="$PROJECT_DIR/src"
OUT_DIR="$PROJECT_DIR/out"
BIN_DIR="$HOME/.local/bin"
TASK_WRAPPER="$BIN_DIR/task"

# Cria diretórios se não existirem
mkdir -p "$OUT_DIR"
mkdir -p "$BIN_DIR"

# Compila todo o código Java
echo "Compilando código Java..."
javac -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
if [ $? -ne 0 ]; then
    echo "[ERROR] Falha na compilação."
    exit 1
fi

# Cria script wrapper
echo "Criando script de execução..."
cat > "$TASK_WRAPPER" << EOF
#!/bin/bash
java -cp "$OUT_DIR" Main "\$@"
EOF

# Dá permissão de execução automaticamente
chmod +x "$TASK_WRAPPER"

echo "Instalação concluída!"
echo "Use: task add \"Sua tarefa\""