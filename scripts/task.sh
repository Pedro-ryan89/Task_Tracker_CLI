#!/bin/bash
# Executa o Task Tracker

# Caminho do build
OUT_DIR="../out"

# Executa a Main
java -cp $OUT_DIR Main "$@"