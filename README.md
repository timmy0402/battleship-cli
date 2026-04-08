# Battleship Game

A console-based Battleship game implemented in Java where you battle a computer opponent on a 10×10 grid. The game features ship placement, turn-based combat, customizable board symbols, and configurable board size.

## Table of Contents

- [Quick Start](#quick-start)
- [How to Play](#how-to-play)
- [Architecture](#architecture)
- [Menu Options](#menu-options)
- [Game Rules](#game-rules)
- [Known Limitations](#known-limitations)

## Quick Start

### Build

```bash
javac *.java
```

### Run

```bash
java BattleshipGame
```

The game will launch the main menu, allowing you to play, customize settings, or view fleet information.

## How to Play

### Setup Phase

1. Select **Play game (1)** from the main menu
2. Review the initial empty boards for both you and the computer
3. Place your 5 ships by entering:
   - **Starting row and column** (e.g., `3 5`)
   - **Orientation**: `H` for horizontal or `V` for vertical

Ships cannot overlap and must fit within the board. You'll see your board update as each ship is placed successfully.

The computer automatically places its ships randomly (hidden from you).

### Combat Phase

Once both fleets are placed:

1. **Your turn**: Enter target coordinates as `(column row)`. Example: `7 2`
2. **Computer's turn**: Computer automatically shoots at a random unshot cell
3. **Results**: Each turn shows hits (X), misses (O), and remaining ship counts
4. **Win condition**: First player to sink all 5 enemy ships wins

You can enter shots until either fleet is completely sunk. After each turn, both boards are displayed showing:
- Your board with your ships (S), hits (X), and misses (O)
- Computer's board with hits (X) and misses (O) on its ships (not shown)

## Architecture

### Class Hierarchy

```
Ship (abstract base class)
├── Carrier (5 cells)
├── BattleshipShip (4 cells)
├── Destroyer (3 cells)
├── Submarine (3 cells)
└── Frigate (2 cells)
```

### Core Classes

#### Ship.java

The base class for all ship types. Each ship tracks:

- **name**: Display name (e.g., "Carrier", "Destroyer")
- **size**: Number of cells occupied (2-5)
- **cells**: 2D array of [row, col] coordinates where the ship is placed
- **hits**: Boolean array tracking which cells have been hit
- **horizontal**: Orientation flag (true = horizontal, false = vertical)

**Key methods:**

- `place(startRow, startCol, horizontal)` — Places the ship on the board
- `hit(row, col)` — Registers a shot; returns true if ship is hit
- `isSunk()` — Returns true if all cells are hit
- `occupies(row, col)` — Returns true if ship occupies that cell
- `isPlaced()` — Returns true if the ship has been placed

**Ship Subclasses** (Carrier, BattleshipShip, Destroyer, Submarine, Frigate)

Simple subclasses that set their specific name and size via the parent constructor:

```java
public class Carrier extends Ship {
    public Carrier() {
        super("Carrier", 5);
    }
}
```

Note: The class is named `BattleshipShip` (not `Battleship`) to avoid conflict with the game engine class.

#### Battleship.java

The game engine managing all game logic:

- **Fleet management**: Maintains lists of user and computer ships
- **Board creation**: Initializes 10×10 boards filled with water symbols
- **Ship placement**: 
  - `placeComputerShips()` — Randomly places computer ships with collision detection
  - `placeUserShips()` — Interactive placement with bounds and overlap validation
- **Shot processing**:
  - `shotUser()` — Processes player shots on computer board
  - `shotComp()` — Computer takes random shots on player board, avoiding previously shot cells
- **Win detection**:
  - `check()` — Returns true if either fleet is fully sunk
  - `getWinner()` — Returns "User", "Computer", or null
- **Board customization**:
  - Symbol management (water, ship, hit, miss) with getter/setter methods
  - Board size adjustment with validation warnings for undersized boards (< 7×7)

**Fleet composition** (5 ships, 17 total cells):
- Carrier: 5 cells
- Battleship: 4 cells
- Destroyer: 3 cells
- Submarine: 3 cells
- Frigate: 2 cells

#### BattleshipGame.java

The entry point and user interface:

- **main()** — Main menu loop allowing players to start games or adjust settings
- **playGame()** — Orchestrates a full game session:
  1. Creates and displays initial empty boards
  2. Places computer ships (hidden) and user ships (visible)
  3. Runs the game loop alternating user and computer shots
  4. Displays updated boards and ship counts after each turn
  5. Announces winner and returns to main menu
- **Menu handling**:
  - `displayMenu()` — Shows 8 options (play, customize symbols, board size, fleet info, exit)
  - `adjustSymbol()` — Changes board symbols for water, ship, hit, or miss
  - `adjustSize()` — Changes board dimensions
  - `viewFleetInfo()` — Displays the standard fleet composition
- **Board display**:
  - `printboard()` — Renders the board with row/column labels for easy coordinate reference

### Data Flow

1. **Initialization**: `BattleshipGame.main()` creates a `Battleship` instance and enters menu loop
2. **Game Start**: User selects "Play game"; `playGame()` is called
3. **Ship Placement**: 
   - Computer: `Battleship.placeComputerShips()` randomly places ships
   - User: `Battleship.placeUserShips()` prompts for each ship's position
4. **Combat Loop** (in `playGame()`):
   - User enters shot coordinates
   - `Battleship.shotUser()` processes shot on computer board
   - `Battleship.shotComp()` generates computer shot on user board
   - Both boards are displayed with updated state
5. **Win Check**: After each turn, `Battleship.check()` determines if game is over
6. **Result**: Winner is announced via `Battleship.getWinner()`, player returns to menu

## Menu Options

| Option | Function |
|--------|----------|
| **1** | Play game — Start a new game session |
| **2** | Adjust water icon — Change the symbol for water cells (~) |
| **3** | Adjust ship icon — Change the symbol for ship cells (S) |
| **4** | Adjust hit icon — Change the symbol for hit cells (X) |
| **5** | Adjust miss icon — Change the symbol for miss cells (O) |
| **6** | Adjust board size — Change the board from default 10×10 to custom size |
| **7** | View fleet info — Display the standard fleet composition |
| **0** | Exit — Quit the game |

**Default symbols**: ~ (water), S (ship), X (hit), O (miss)

## Game Rules

### Ship Placement

- **User ships**: Placed manually by coordinates (row, column) and orientation (horizontal/vertical)
- **Computer ships**: Placed randomly; their positions are hidden from the player
- **Constraints**:
  - Ships must fit within the board (no wrapping)
  - Ships cannot overlap with each other
  - Ships extend to the right (horizontal) or downward (vertical)
  - Starting row and column must be valid (0 to size-1)

### Combat

- **Turn order**: Player shoots first, then computer responds
- **Shots**: Coordinates are entered as `(column row)`, e.g., `7 2` targets column 7, row 2
- **Hit/Miss**: Board is updated immediately showing results
- **Ship sinking**: When all cells of a ship are hit, it is reported as sunk
- **Repeated shots**: Computer avoids shooting the same cell twice; player can attempt repeated shots (will show previous result)

### Win Condition

The game ends immediately when:
- **Player wins**: All 5 computer ships are sunk
- **Computer wins**: All 5 player ships are sunk

Winner is announced and the player returns to the main menu.

## Known Limitations

### Input Validation

- **Shot coordinates**: No validation for out-of-bounds input. Entering coordinates outside [0, size) will throw `ArrayIndexOutOfBoundsException`. Always enter coordinates in the valid range.
- **Player ship placement**: Input is validated for bounds and overlap; invalid placements prompt for re-entry.

### Computer AI

- **Random strategy**: Computer randomly selects unshot cells. It does not employ any hunt-and-destroy logic or pattern targeting.
- **Performance**: With larger boards, finding unshot cells by random iteration may become slow (will eventually find them).

### Board Size

- **Minimum recommended size**: 7×7 (board size < 7 triggers a warning; the standard 5-ship fleet requires at least ~7 cells on a side to fit comfortably)
- **Custom sizes**: No upper limit enforced; very large boards will consume more memory and take longer to display

### Symbol Customization

- **getSymbol() bug**: The `getSymbol(String symbolType)` method always returns 'c' (a literal string lookup bug). Use the specific getter methods (`getWater()`, `getShip()`, `getHit()`, `getMiss()`) instead.
- **Menu option 7**: Currently displays "View fleet info"; the menu option text refers to "Adjust number of ships icon" but this functionality is not implemented. Option 7 only shows fleet composition, not actual number customization.

## Development Notes

### No External Dependencies

This project uses only the Java Standard Library:
- `java.util.ArrayList`, `java.util.List` — Fleet management
- `java.util.HashMap` — Symbol mapping
- `java.util.Random` — Computer ship placement and shot selection
- `java.util.Scanner` — User input handling

### Compilation and Execution

```bash
# Compile all Java files in the current directory
javac *.java

# Run the game
java BattleshipGame
```

All classes are in the default package (no package declarations).

### Code Quality

- **Documentation**: All classes and public methods include Javadoc comments
- **No input validation for shot coordinates**: See Known Limitations
- **Single-threaded**: All operations execute sequentially in the main thread

## Future Enhancements

Potential improvements for future versions:

- Input validation for shot coordinates to prevent `ArrayIndexOutOfBoundsException`
- Computer AI with hunt-and-destroy logic for more challenging gameplay
- Support for custom fleet configurations
- Game state persistence (save/load games)
- Multiplayer support (local or network)
- GUI using Swing or JavaFX

---

Have fun playing Battleship! For issues or suggestions, feel free to contribute to the code.
