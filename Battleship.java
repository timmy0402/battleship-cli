import java.util.Random;
import java.util.Scanner;

public class Battleship {
    // Symbolic representations for different elements in the game
    private char water, ship, hit, miss;

    // Number of ships for both user and computer
    private int numShips, numShipsUser, numShipsCom;

    // Size of the game board
    private int size;

    // Arrays to store coordinates of ships for computer and user
    private int[] xCoorCom;
    private int[] yCoorCom;
    private int[] xCoorUser;
    private int[] yCoorUser;

    /**
     * Constructor for the Battleship class, initializes default game settings.
     */
    public Battleship() {
        water = '~';
        ship = 'S';
        hit = 'X';
        miss = 'O';
        numShips = 2;
        size = 5;
        numShipsUser = numShips;
        numShipsCom = numShips;
    }

    /**
     * Creates and initializes the game board with water symbols.
     * 
     * @return The initialized game board.
     */
    public char[][] CreateGameBoard() {
        char[][] gameboard = new char[size][size];
        for (int i = 0; i < gameboard.length; i++) {
            for (int k = 0; k < gameboard.length; k++) {
                gameboard[i][k] = water;
            }
        }

        numShipsUser = numShips;
        numShipsCom = numShips;
        return gameboard;
    }

    /**
     * Generates random coordinates for computer ships.
     */
    public void createCoordinate() {
        Random rand = new Random();
        xCoorCom = new int[numShips];
        yCoorCom = new int[numShips];

        // Generate random coordinates for each ship, ensuring no overlap
        for (int i = 0; i < numShips; i++) {
            int x = rand.nextInt(size);
            xCoorCom[i] = x;
            int y = rand.nextInt(size);
            yCoorCom[i] = y;
        }

        // Ensure no overlap between computer ships
        for (int i = 0; i < numShips; i++) {
            for (int k = i + 1; k < numShips; k++) {
                while (xCoorCom[i] == xCoorCom[k] && yCoorCom[i] == yCoorCom[k]) {
                    xCoorCom[k] = rand.nextInt(size);
                    yCoorCom[k] = rand.nextInt(size);
                }
            }
        }
    }

    /**
     * Takes user input for ship coordinates and updates the user board accordingly.
     * 
     * @param userInput Scanner object for user input.
     * @param board     The user game board.
     */
    public void userCoor(Scanner userInput, char[][] board) {
        xCoorUser = new int[numShips];
        yCoorUser = new int[numShips];

        // Take user input for ship coordinates and update the board
        for (int i = 0; i < numShips; i++) {
            xCoorUser[i] = userInput.nextInt();
            yCoorUser[i] = userInput.nextInt();
            board[yCoorUser[i]][xCoorUser[i]] = ship;
        }
    }

    /**
     * Handles a user's shot on the computer's board.
     * 
     * @param xUser X-coordinate of the user's shot.
     * @param yUser Y-coordinate of the user's shot.
     * @param board The computer's game board.
     */
    public void shotUser(int xUser, int yUser, char[][] board) {
        int x = xUser;
        int y = yUser;
        boolean shoted = false;

        // Check if the shot hits a computer ship
        for (int i = 0; i < xCoorCom.length; i++) {
            if (x == xCoorCom[i] && y == yCoorCom[i]) {
                shoted = true;
            }
        }

        // Process the shot result and update the board
        if (shoted) {
            System.out.println("Hit");
            board[y][x] = hit;
            numShipsCom -= 1;
        } else {
            System.out.println("Missed");
            board[y][x] = miss;
        }
    }

    /**
     * Handles a computer's shot on the user's board.
     * 
     * @param board The user's game board.
     * @param rand  Random object for generating computer's shot coordinates.
     */
    public void shotComp(char[][] board, Random rand) {
        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        // Ensure the computer does not shoot the same cell twice
        while (board[y][x] == 'X') {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }

        boolean shoted = false;

        // Check if the shot hits a user ship
        for (int i = 0; i < xCoorUser.length; i++) {
            if (x == xCoorUser[i] && y == yCoorUser[i]) {
                shoted = true;
            }
        }

        // Process the shot result and update the board
        if (shoted) {
            System.out.println("Hit");
            board[y][x] = hit;
            numShipsUser -= 1;
        } else {
            System.out.println("Missed");
            board[y][x] = miss;
        }
    }

    /**
     * Checks if the game has ended by verifying if either user or computer has no
     * ships left.
     * 
     * @return True if the game has ended, false otherwise.
     */
    public boolean check() {
        return numShipsUser == 0 || numShipsCom == 0;
    }

    /**
     * Setter for the alls symbol.
     * 
     * @param userWater The new water symbol chosen by the user.
     */
    public void setSymbol(String symbolType, char chosenSymbol){
        switch (symbolType) {
            case "water":
                setWater(chosenSymbol);
                break;
            case "ship":
                setShip(chosenSymbol);
                break;
            case "hit":
                setHit(chosenSymbol);
                break;
            case "miss":
                setMiss(chosenSymbol);
                break;
            default:
                break;
        }
    }

    /**
     * Setter for the water symbol.
     * 
     * @param userWater The new water symbol chosen by the user.
     */
    public void setWater(char userWater) {
        water = userWater;
    }

    /**
     * Setter for the ship symbol.
     * 
     * @param userShip The new ship symbol chosen by the user.
     */
    public void setShip(char userShip) {
        ship = userShip;
    }

    /**
     * Setter for the hit symbol.
     * 
     * @param userHit The new hit symbol chosen by the user.
     */
    public void setHit(char userHit) {
        hit = userHit;
    }

    /**
     * Setter for the miss symbol.
     * 
     * @param userMiss The new miss symbol chosen by the user.
     */
    public void setMiss(char userMiss) {
        miss = userMiss;
    }

    /**
     * Setter for the number of ships in the game.
     * 
     * @param ship The new number of ships chosen by the user.
     */
    public void setShip(int ship) {
        numShips = ship;
        numShipsUser = ship;
        numShipsCom = ship;
    }

    /**
     * Setter for the size of the game board.
     * 
     * @param userSize The new size of the game board chosen by the user.
     */
    public void setSize(int userSize) {
        size = userSize;
    }

    /**
     * Getter for the water symbol.
     * 
     * @return The current water symbol.
     */
    public char getWater() {
        return water;
    }

    /**
     * Getter for the ship symbol.
     * 
     * @return The current ship symbol.
     */
    public char getShip() {
        return ship;
    }

    /**
     * Getter for the miss symbol.
     * 
     * @return The current miss symbol.
     */
    public char getMiss() {
        return miss;
    }

    /**
     * Getter for the hit symbol.
     * 
     * @return The current hit symbol.
     */
    public char getHit() {
        return hit;
    }

    /**
     * Getter for the number of user ships.
     * 
     * @return The current number of user ships.
     */
    public int getShipUser() {
        return numShipsUser;
    }

    /**
     * Getter for the number of computer ships.
     * 
     * @return The current number of computer ships.
     */
    public int getShipCom() {
        return numShipsCom;
    }

    /**
     * Getter for the size of the game board.
     * 
     * @return The current size of the game board.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Getter for the symbol of the selected object.
     * 
     * @return The current symbol of the selected object.
     */
    public char getSymbol(String symbolType){
        switch (symbolType) {
            case "water":
                return water;
            case "ship":
                return ship;
            case "hit":
                return hit;
            case "miss":
                return miss;
            default:
                return 'c';
        }
         
    }
}
