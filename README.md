# Task Tracker CLI

Task Tracker CLI é uma ferramenta simples de linha de comando para gerenciar suas tarefas, com suporte a **tarefas ativas**, **tarefas concluídas**, **lixeira** e funcionalidades de manutenção automática.

---

## Índice

- [Recursos](#recursos)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
  - [Linux / macOS](#linux--macos)
  - [Windows](#windows)
- [uso basico](#uso-basico)
- [Para Desenvolvedores](#para-desenvolvedores)
- [Notas de Implementação](#notas-de-implementação)

---

## Recursos

- Adicionar, listar, atualizar, concluir e deletar tarefas.  
- Suporte à **lixeira**, com opção de esvaziar permanentemente.  
- IDs automáticos para tarefas.  
- Persistência em arquivos JSON (`tasks.json` e `deleted_tasks.json`).  
- Limpeza automática de tarefas **DONE** após 24h e exclusão definitiva da lixeira após 7 dias.  
- Funciona em **Windows e Linux**.

---

## Pré-requisitos

- **Java JDK 8+** ([Download JDK](https://www.oracle.com/java/technologies/downloads/))  
- **(Opcional) Git**, caso queira clonar o repositório  
- **Terminal ou CMD/Powershell** para executar comandos

---

## Instalação

### Linux 

1. Clone ou baixe o repositório:  
   ```bash
   git clone https://github.com/Pedro-ryan89/Task_Tracker_CLI.git
   cd Task_Tracker_CLI
   ```
2. O script irá:
   - Compilar o código Java
   - Criar um diretório ~/.task-tracker/out com os .class compilados
   - Criar um link simbólico ~/.local/bin/task para rodar o CLI de qualquer lugar

3. teste a instalação
  ```bash
  task help
  ```

### Windows

1. Baixe ou clone o repositorio:
 ```Bash
 git clone https://github.com/Pedro-ryan89/Task_Tracker_CLI.git
 cd Task_Tracker_CLI
```
2. Execute o script de instalação
```Bash
 install\install.bat
```

(em ambos os casos o instalador ja posibilita a utilização em qualquer diretorio)

### uso basico
Após instalar, você pode usar o comando ```task``` seguido de sub-comandos:   

| Comando                | Descrição                                         | Exemplo                     |
| ---------------------- | ------------------------------------------------- | --------------------------- |
| `add <descrição>`      | Adiciona uma nova tarefa                          | `task add "Estudar Java"`   |
| `list`                 | Lista todas tarefas ativas                        | `task list`                 |
| `done <id>`            | Marca a tarefa como concluída                     | `task done 3`               |
| `update <id> <status>` | Atualiza o status (`todo`, `in_progress`, `done`) | `task update 3 in_progress` |
| `delete <id>`          | Move a tarefa para a lixeira                      | `task delete 3`             |
| `deleted`              | Lista tarefas na lixeira                          | `task deleted`              |
| ` trash-clear`         | Esvazia a lixeira permanentemente                 | `task trash-clear`          |
| `help`                 | Mostra a ajuda completa                           | `task help`                 |

# Para Desenvolvedores

1. Clone o repositorio:
 ```Bash
git clone https://github.com/Pedro-ryan89/Task_Tracker_CLI.git
cd Task_Tracker_CLI
```
3. Compile o Codigo:
```Bash
 ./scripts/build.sh  # Linux/macOS
build.bat           # Windows
```
5. Execute localmente:
```Bash
./task           # Linux/macOS
task.bat         # Windows
```
6. Faça alterações em ```src/``` e teste antes de commitar.

## Notas de Implementação
- Persistência: as tarefas são salvas em ```tasks.json``` (ativas) e ```deleted_tasks.json``` (excluídas).
- IDs: cada tarefa recebe um ID incremental único; IDs antigos são liberados apenas ao esvaziar a lixeira.
#### Manutenção automática:
   - Tarefas DONE são movidas para a lixeira automaticamente após 24 horas
   - Tarefas na lixeira são removidas definitivamente após 7 dias
