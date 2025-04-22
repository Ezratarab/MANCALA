# MANCALA - The Ancient Board Game

<div align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Mancala_players_in_Tanzania.jpg/600px-Mancala_players_in_Tanzania.jpg" alt="Mancala gameplay" width="400">
  
  [![Java](https://img.shields.io/badge/Java-17+-red?logo=java&logoColor=white)](https://java.com)
  [![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)
  [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen)](CONTRIBUTING.md)
</div>

## 🎮 About the Game
Mancala is one of the oldest known board games with ancient African origins. This Java implementation features:

- Classic Kalah variant rules
- Console-based interface
- 1-player vs AI or 2-player mode
- Score tracking and game history

## 🛠️ Tech Stack
- **Core**: Java 17
- **Build**: Maven
- **Testing**: JUnit 5
- **Packaging**: Executable JAR

## 📥 Installation & Running
```bash
# Clone the repository
git clone https://github.com/Ezratarab/MANCALA.git
cd MANCALA

# Build with Maven
mvn clean package

# Run the game
java -jar target/mancala-1.0.jar
## 🧠 AI Implementation

The computer opponent uses:

Minimax algorithm with alpha-beta pruning

Configurable difficulty levels

Heuristic evaluation of board states
