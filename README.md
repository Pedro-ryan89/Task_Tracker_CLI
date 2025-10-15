# Task Tracker CLI

A simple command-line task tracker to manage your to-dos.

## Installation Guide (For Users)

This is the easiest way to install and use the Task Tracker CLI.

### Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 8 or higher installed.

### Steps

1. **Download the Package:**
   - Download the latest `.zip` package from the [Releases page](https://github.com/your-username/Task-Tracker-CLI/releases) (replace with your actual release link).

2. **Extract the Files:**
   - Unzip the archive to a permanent location on your computer (e.g., `C:\Users\YourUser\Applications\Task-Tracker`).

3. **Run:**
   - Navigate to the directory you extracted and use `task.bat` to run commands.
   ```bash
   cd C:\Users\YourUser\Applications\Task-Tracker
   task add "My first task"
   ```

4. **(Optional) Add to PATH:**
   - To run `task` from anywhere, add the `Task-Tracker` directory to your system's PATH environment variable.

## For Developers

If you want to modify the source code:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/Task-Tracker-CLI.git
   cd Task-Tracker-CLI/Task_Tracker
   ```

2. **Compile the code:**
   Run the `build.bat` script from the `Task_Tracker` directory to compile the source files:
   ```bash
   build.bat
   ```

## Usage

Use the `task` command followed by one of the following sub-commands:

- `add <description>`: Adds a new task.
  ```bash
  task add "Buy milk"
  ```

- `list`: Lists all active tasks.
  ```bash
  task list
  ```

- `done <id>`: Marks a task as done.
  ```bash
  task done 3
  ```

- `update <id> <status>`: Updates a task's status. Available statuses are `todo`, `in_progress`, `done`.
  ```bash
  task update 3 in_progress
  ```

- `delete <id>`: Moves a task to the trash.
  ```bash
  task delete 3
  ```

- `deleted`: Lists tasks in the trash.
  ```bash
  task deleted
  ```

- `help`: Shows this help message.
  ```bash
  task help
  ```
