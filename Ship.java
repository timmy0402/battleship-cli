/**
 * Base class for all ship types in the Battleship game.
 * Each ship has a name, size (number of cells it occupies),
 * and tracks its placement on the board and damage from hits.
 * Subclasses (Carrier, Destroyer, etc.) set their specific name and size.
 */
public class Ship {
    // Display name of the ship type (e.g., "Carrier", "Destroyer")
    private String name;

    // Number of cells this ship occupies on the board
    private int size;

    // Coordinates of each cell the ship occupies: cells[i] = {row, col}
    // Null until the ship is placed on the board
    private int[][] cells;

    // Tracks which cells have been hit: hits[i] corresponds to cells[i]
    private boolean[] hits;

    // True if ship is placed horizontally, false if vertical
    private boolean horizontal;

    /**
     * Constructs a ship with the given name and size.
     * The ship is not placed on the board until place() is called.
     *
     * @param name The display name of the ship type.
     * @param size The number of cells this ship occupies.
     */
    protected Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hits = new boolean[size];
        this.cells = null;
    }

    /**
     * Places the ship on the board starting at the given position.
     * If horizontal, the ship extends to the right (increasing column).
     * If vertical, the ship extends downward (increasing row).
     *
     * @param startRow   The row of the ship's first cell.
     * @param startCol   The column of the ship's first cell.
     * @param horizontal True to place horizontally, false for vertically.
     */
    public void place(int startRow, int startCol, boolean horizontal) {
        this.horizontal = horizontal;
        this.cells = new int[size][2];
        for (int i = 0; i < size; i++) {
            cells[i][0] = horizontal ? startRow : startRow + i;
            cells[i][1] = horizontal ? startCol + i : startCol;
        }
    }

    /**
     * Checks whether this ship occupies the given board cell.
     *
     * @param row The row to check.
     * @param col The column to check.
     * @return True if the ship covers this cell, false otherwise.
     */
    public boolean occupies(int row, int col) {
        if (cells == null) return false;
        for (int[] cell : cells) {
            if (cell[0] == row && cell[1] == col) return true;
        }
        return false;
    }

    /**
     * Attempts to register a hit at the given coordinate.
     * If this ship occupies that cell, marks it as hit and returns true.
     *
     * @param row The row of the shot.
     * @param col The column of the shot.
     * @return True if this ship was hit, false if the shot missed this ship.
     */
    public boolean hit(int row, int col) {
        if (cells == null) return false;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][0] == row && cells[i][1] == col) {
                hits[i] = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether all cells of this ship have been hit.
     *
     * @return True if every cell has been hit (ship is sunk), false otherwise.
     */
    public boolean isSunk() {
        for (boolean h : hits) {
            if (!h) return false;
        }
        return true;
    }

    /**
     * Checks whether this ship has been placed on the board.
     *
     * @return True if place() has been called, false otherwise.
     */
    public boolean isPlaced() {
        return cells != null;
    }

    /**
     * @return The display name of this ship type.
     */
    public String getName() { return name; }

    /**
     * @return The number of cells this ship occupies.
     */
    public int getSize() { return size; }

    /**
     * @return The array of cell coordinates, or null if not yet placed.
     */
    public int[][] getCells() { return cells; }

    /**
     * @return True if the ship was placed horizontally, false if vertically.
     */
    public boolean isHorizontal() { return horizontal; }
}
