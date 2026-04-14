import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Game engine for Battleship. Manages board state, ship fleets,
 * shot processing, and win condition checking for both user and computer.
 */
public class Battleship {
    // Symbolic representations for different elements on the board
    private HashMap<String, Character> symbolMap = new HashMap<>();

    // Size of the game board (size x size grid)
    private int size;

    // Fleets of Ship objects for each player
    private List<Ship> userShips;
    private List<Ship> comShips;

    /**
     * Constructor for the Battleship class.
     * Initializes default game settings and creates both fleets.
     */
    public Battleship() {
        symbolMap.put("water", '~');
        symbolMap.put("ship", 'S');
        symbolMap.put("hit", 'X');
        symbolMap.put("miss", 'O');
        size = 10;
        userShips = createFleet();
        comShips = createFleet();
    }

    /**
     * Creates a standard Battleship fleet containing one of each ship type.
     * Fleet: Carrier(5), Battleship(4), Destroyer(3), Submarine(3), Frigate(2).
     *
     * @return A new list of Ship objects representing the full fleet.
     */
    private List<Ship> createFleet() {
        List<Ship> fleet = new ArrayList<>();
        fleet.add(new Carrier());
        fleet.add(new BattleshipShip());
        fleet.add(new Destroyer());
        fleet.add(new Submarine());
        fleet.add(new Frigate());
        return fleet;
    }

    /**
     * Creates and initializes a game board filled with the water symbol.
     *
     * @return The initialized game board as a 2D char array.
     */
    public char[][] CreateGameBoard() {
        char water = symbolMap.get("water");
        char[][] gameboard = new char[size][size];
        for (int i = 0; i < gameboard.length; i++) {
            for (int k = 0; k < gameboard[i].length; k++) {
                gameboard[i][k] = water;
            }
        }
        return gameboard;
    }

    /**
     * Randomly places all computer ships on the board.
     * For each ship, picks a random orientation and starting position,
     * then validates that the ship fits within bounds and does not
     * overlap with any previously placed ship. Retries on conflict.
     *
     * @param board The computer's game board (ships are NOT marked on it
     *              since they should be hidden from the user).
     */
    public void placeComputerShips(char[][] board) {
        Random rand = new Random();

        for (Ship s : comShips) {
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 1000) {
                attempts++;
                boolean horizontal = rand.nextBoolean();

                // Pick a start position that keeps the ship within bounds
                int startRow, startCol;
                if (horizontal) {
                    startRow = rand.nextInt(size);
                    startCol = rand.nextInt(size - s.getSize() + 1);
                } else {
                    startRow = rand.nextInt(size - s.getSize() + 1);
                    startCol = rand.nextInt(size);
                }

                // Check for overlap with already-placed ships
                boolean overlap = false;
                for (int i = 0; i < s.getSize(); i++) {
                    int row = horizontal ? startRow : startRow + i;
                    int col = horizontal ? startCol + i : startCol;
                    for (Ship other : comShips) {
                        if (other.isPlaced() && other.occupies(row, col)) {
                            overlap = true;
                            break;
                        }
                    }
                    if (overlap) break;
                }

                if (!overlap) {
                    s.place(startRow, startCol, horizontal);
                    placed = true;
                }
            }
        }
    }

    /**
     * Prompts the user to place each ship in their fleet on the board.
     * For each ship, asks for a starting row, column, and orientation (H/V).
     * Validates that the ship fits within bounds and does not overlap
     * with previously placed ships. Re-prompts on invalid input.
     *
     * @param userInput Scanner object for reading user input.
     * @param board     The user's game board (ships are marked with the ship symbol).
     */
    public void placeUserShips(Scanner userInput, char[][] board) {
        for (Ship s : userShips) {
            boolean placed = false;

            while (!placed) {
                System.out.printf(Color.ANSI_CYAN + "Place your %s (size %d)" + Color.ANSI_RESET + "%n", s.getName(), s.getSize());
                System.out.print(Color.ANSI_YELLOW + "Enter starting row and column (row col): " + Color.ANSI_RESET);
                int startRow = userInput.nextInt();
                int startCol = userInput.nextInt();

                System.out.print(Color.ANSI_YELLOW + "Horizontal or Vertical? (H/V): " + Color.ANSI_RESET);
                String orient = userInput.next().toUpperCase();
                boolean horizontal = orient.equals("H");

                // Validate bounds
                if (horizontal && startCol + s.getSize() > size) {
                    System.out.println(Color.ANSI_RED + "Ship goes out of bounds horizontally. Try again." + Color.ANSI_RESET);
                    continue;
                }
                if (!horizontal && startRow + s.getSize() > size) {
                    System.out.println(Color.ANSI_RED + "Ship goes out of bounds vertically. Try again." + Color.ANSI_RESET);
                    continue;
                }
                if (startRow < 0 || startRow >= size || startCol < 0 || startCol >= size) {
                    System.out.println(Color.ANSI_RED + "Starting position is out of bounds. Try again." + Color.ANSI_RESET);
                    continue;
                }

                // Check for overlap with already-placed user ships
                boolean overlap = false;
                for (int i = 0; i < s.getSize(); i++) {
                    int row = horizontal ? startRow : startRow + i;
                    int col = horizontal ? startCol + i : startCol;
                    for (Ship other : userShips) {
                        if (other.isPlaced() && other.occupies(row, col)) {
                            overlap = true;
                            break;
                        }
                    }
                    if (overlap) break;
                }

                if (overlap) {
                    System.out.println(Color.ANSI_RED + "Overlaps with another ship. Try again." + Color.ANSI_RESET);
                    continue;
                }

                // Place the ship and mark its cells on the board
                s.place(startRow, startCol, horizontal);
                for (int[] cell : s.getCells()) {
                    board[cell[0]][cell[1]] = symbolMap.get("ship");
                }
                placed = true;

                System.out.printf(Color.ANSI_GREEN + "%s placed successfully!" + Color.ANSI_RESET + "%n", s.getName());

                // Print the board so the user can see where their ships are
                printBoard(board);
                System.out.println();
            }
        }
    }

    /**
     * Prints the game board with row and column labels.
     *
     * @param board The game board to print.
     */
    public void printBoard(char[][] board) {
        System.out.print("  ");
        for (int col = 0; col < board[0].length; col++) {
            System.out.printf("%2d", col);
        }
        System.out.println();
        for (int row = 0; row < board.length; row++) {
            System.out.printf("%2d", row);
            for (char cell : board[row]) {
                System.out.print(" " + colorCell(cell));
            }
            System.out.println();
        }
    }

    /**
     * Returns a color-coded string for a board cell character.
     *
     * @param cell The cell character to colorize.
     * @return The cell wrapped in the appropriate ANSI color code.
     */
    private String colorCell(char cell) {
        char water = symbolMap.get("water");
        char ship = symbolMap.get("ship");
        char hit = symbolMap.get("hit");
        char miss = symbolMap.get("miss");

        if (cell == water) return Color.ANSI_CYAN + cell + Color.ANSI_RESET;
        if (cell == ship) return Color.ANSI_GREEN + cell + Color.ANSI_RESET;
        if (cell == hit) return Color.ANSI_RED + cell + Color.ANSI_RESET;
        if (cell == miss) return Color.ANSI_YELLOW + cell + Color.ANSI_RESET;
        return String.valueOf(cell);
    }

    /**
     * Handles a user's shot on the computer's board.
     * Checks each computer ship to see if the shot hits one of its cells.
     * If a ship is hit and all its cells are now damaged, reports it as sunk.
     *
     * @param xUser X-coordinate (column) of the user's shot.
     * @param yUser Y-coordinate (row) of the user's shot.
     * @param board The computer's game board to update.
     */
    public void shotUser(int xUser, int yUser, char[][] board) {
        int row = yUser;
        int col = xUser;

        for (Ship s : comShips) {
            if (s.hit(row, col)) {
                board[row][col] = symbolMap.get("hit");
                System.out.println(Color.ANSI_RED + "Hit!" + Color.ANSI_RESET);
                if (s.isSunk()) {
                    System.out.printf(Color.ANSI_GREEN + "You sunk the computer's %s!" + Color.ANSI_RESET + "%n", s.getName());
                }
                return;
            }
        }

        // No ship was hit
        board[row][col] = symbolMap.get("miss");
        System.out.println(Color.ANSI_YELLOW + "Missed" + Color.ANSI_RESET);
    }

    /**
     * Handles a computer's shot on the user's board.
     * Randomly selects coordinates, avoiding cells that have already been shot.
     * Checks each user ship to see if the shot hits one of its cells.
     *
     * @param board The user's game board to update.
     * @param rand  Random object for generating shot coordinates.
     */
    public void shotComp(char[][] board, Random rand) {
        char hit = symbolMap.get("hit");
        char miss = symbolMap.get("miss");
        int col = rand.nextInt(size);
        int row = rand.nextInt(size);

        // Avoid shooting cells that have already been shot (hit or miss)
        while (board[row][col] == hit || board[row][col] == miss) {
            col = rand.nextInt(size);
            row = rand.nextInt(size);
        }

        System.out.printf(Color.ANSI_PURPLE + "Computer shoots (%d, %d): " + Color.ANSI_RESET, col, row);

        for (Ship s : userShips) {
            if (s.hit(row, col)) {
                board[row][col] = hit;
                System.out.println(Color.ANSI_RED + "Hit!" + Color.ANSI_RESET);
                if (s.isSunk()) {
                    System.out.printf(Color.ANSI_RED + "Computer sunk your %s!" + Color.ANSI_RESET + "%n", s.getName());
                }
                return;
            }
        }

        // No ship was hit
        board[row][col] = miss;
        System.out.println(Color.ANSI_YELLOW + "Missed" + Color.ANSI_RESET);
    }

    /**
     * Checks if the game has ended by verifying if either player's
     * entire fleet has been sunk.
     *
     * @return True if the game has ended, false otherwise.
     */
    public boolean check() {
        return allSunk(comShips) || allSunk(userShips);
    }

    /**
     * Determines the winner of the game.
     *
     * @return "User" if the computer's fleet is sunk,
     *         "Computer" if the user's fleet is sunk,
     *         or null if the game is still in progress.
     */
    public String getWinner() {
        if (allSunk(comShips)) return "User";
        if (allSunk(userShips)) return "Computer";
        return null;
    }

    /**
     * Checks whether all ships in a fleet have been sunk.
     *
     * @param fleet The list of ships to check.
     * @return True if every ship in the fleet is sunk.
     */
    private boolean allSunk(List<Ship> fleet) {
        for (Ship s : fleet) {
            if (!s.isSunk()) return false;
        }
        return true;
    }

    /**
     * Sets a board symbol based on the symbol type name.
     *
     * @param symbolType  The type to change: "water", "ship", "hit", or "miss".
     * @param chosenSymbol The new character symbol.
     */
    public void setSymbol(String symbolType, char chosenSymbol) {
        symbolMap.put(symbolType, chosenSymbol);
    }

    /**
     * Gets the current character for a given symbol type.
     *
     * @param symbolType The type to query: "water", "ship", "hit", or "miss".
     * @return The current character for that symbol type, or 'c' if unknown.
     */
    public char getSymbol(String symbolType) {
        return symbolMap.get(symbolType);
    }

    // --- Symbol setters ---

    /** @param userWater The new water symbol. */
    public void setWater(char userWater) { symbolMap.put("water", userWater); }

    /** @param userShip The new ship symbol. */
    public void setShip(char userShip) { symbolMap.put("ship", userShip); }

    /** @param userHit The new hit symbol. */
    public void setHit(char userHit) { symbolMap.put("hit", userHit); }

    /** @param userMiss The new miss symbol. */
    public void setMiss(char userMiss) { symbolMap.put("miss", userMiss); }

    // --- Symbol getters ---

    /** @return The current water symbol. */
    public char getWater() { return symbolMap.get("water"); }

    /** @return The current ship symbol. */
    public char getShip() { return symbolMap.get("ship"); }

    /** @return The current hit symbol. */
    public char getHit() { return symbolMap.get("hit"); }

    /** @return The current miss symbol. */
    public char getMiss() { return symbolMap.get("miss"); }

    // --- Board size ---

    /**
     * Sets the board size. Warns if too small for the standard fleet.
     *
     * @param userSize The new board dimension (userSize x userSize).
     */
    public void setSize(int userSize) {
        if (userSize < 7) {
            System.out.println(Color.ANSI_YELLOW + "Warning: board size below 7 may be too small for the standard fleet." + Color.ANSI_RESET);
        }
        size = userSize;
    }

    /** @return The current board size. */
    public int getSize() { return size; }

    // --- Ship count queries ---

    /**
     * Gets the number of user ships that have not yet been sunk.
     *
     * @return The count of surviving user ships.
     */
    public int getShipUser() {
        int count = 0;
        for (Ship s : userShips) {
            if (!s.isSunk()) count++;
        }
        return count;
    }

    /**
     * Gets the number of computer ships that have not yet been sunk.
     *
     * @return The count of surviving computer ships.
     */
    public int getShipCom() {
        int count = 0;
        for (Ship s : comShips) {
            if (!s.isSunk()) count++;
        }
        return count;
    }

    /**
     * Returns the list of ship types in the fleet for display purposes.
     *
     * @return A list of ships representing the standard fleet composition.
     */
    public List<Ship> getFleetInfo() {
        return createFleet();
    }
}
