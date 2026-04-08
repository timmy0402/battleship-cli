import java.util.*;

/**
 * Entry point and UI for the Battleship game.
 * Contains the main menu loop, board printing, and delegates
 * all game logic to the Battleship engine class.
 */
public class BattleshipGame {
    public static void main(String[] args) {
        // Initialization
        System.out.println("Welcome to Battleship!");
        Scanner userInput = new Scanner(System.in);
        Random rand = new Random();
        Battleship game = new Battleship();
        int userChoice;

        do {
            // Display menu options
            displayMenu();

            // User input for menu option
            userChoice = userInput.nextInt();

            // Switch case for menu options
            switch (userChoice) {
                case 0:
                    System.out.println("Exit, See you again!");
                    break;
                case 1:
                    // Play the game
                    playGame(userInput, rand, game);
                    break;
                // Adjust symbol cases
                case 2:
                case 3:
                case 4:
                case 5:
                    // Adjust water, ship, hit, or miss symbol
                    adjustSymbol(userChoice, game, userInput);
                    break;
                case 6:
                    // Adjust board size
                    adjustSize(game, userInput);
                    break;
                case 7:
                    // View fleet information
                    viewFleetInfo(game);
                    break;
                default:
                    System.out.println("Option not exist");
            }

        } while (userChoice != 0);
    }

    /**
     * Runs a full game session: sets up boards, places ships,
     * alternates user and computer shots until one fleet is sunk.
     *
     * @param userInput Scanner for reading user input.
     * @param rand      Random object for computer decisions.
     * @param game      The Battleship game engine instance.
     */
    private static void playGame(Scanner userInput, Random rand, Battleship game) {
        // Initialize game boards
        char[][] userboard = game.CreateGameBoard();
        char[][] comboard = game.CreateGameBoard();

        // Display initial empty boards
        System.out.println("This is your board");
        printboard(userboard);
        System.out.println("This is computer's board");
        printboard(comboard);

        // Place ships: computer randomly, user via input
        game.placeComputerShips(comboard);

        System.out.println("=== Place your ships ===");
        game.placeUserShips(userInput, userboard);

        // Display boards after ship placement
        System.out.println("This is your board");
        printboard(userboard);
        System.out.println("This is computer's board");
        printboard(comboard);

        // Game loop: alternate user and computer shots
        do {
            System.out.println("What coordinate do you want to shoot? (col row)");
            int x = userInput.nextInt();
            int y = userInput.nextInt();

            while (x >= game.getSize() || y >= game.getSize()) {
                System.out.println("Invalid shot coordinates. Try again");
                System.out.println("What coordinate do you want to shoot? (col row)");
                x = userInput.nextInt();
                y = userInput.nextInt();
            }

            System.out.print("Your shot: ");
            game.shotUser(x, y, comboard);
            System.out.print("Computer's shot: ");
            game.shotComp(userboard, rand);

            // Display updated boards after shots
            System.out.println("\nThis is your board");
            printboard(userboard);
            System.out.println("This is computer's board");
            printboard(comboard);

            // Show remaining ship counts
            System.out.printf("Your ships remaining: %d | Computer's ships remaining: %d%n%n",
                    game.getShipUser(), game.getShipCom());
        } while (!game.check());

        // Display game result
        String winner = game.getWinner();
        if ("User".equals(winner)) {
            System.out.println("Congratulations! You sunk the entire enemy fleet!\n");
        } else {
            System.out.println("The computer sunk your entire fleet. Better luck next time!\n");
        }
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMenu() {
        System.out.println("Play game (1)");
        System.out.println("Adjust water icon (2)");
        System.out.println("Adjust ship icon (3)");
        System.out.println("Adjust hit icon (4)");
        System.out.println("Adjust miss icon (5)");
        System.out.println("Adjust board size (6)");
        System.out.println("View fleet info (7)");
        System.out.println("Exit (0)");
        System.out.print("What is your choice? ");
    }

    /**
     * Adjusts one of the board symbols (water, ship, hit, or miss)
     * based on user input.
     *
     * @param userChoice The menu option (2-5) indicating which symbol.
     * @param game       The Battleship game engine instance.
     * @param userInput  Scanner for reading user input.
     */
    private static void adjustSymbol(int userChoice, Battleship game, Scanner userInput) {
        String symbolType = switch (userChoice) {
            case 2 -> "water";
            case 3 -> "ship";
            case 4 -> "hit";
            case 5 -> "miss";
            default -> throw new IllegalStateException("Unexpected value: " + userChoice);
        };

        System.out.printf("Current symbol: %s%n", game.getSymbol(symbolType));
        System.out.print("What symbol do you choose? ");
        char chosenSymbol = userInput.next().charAt(0);
        game.setSymbol(symbolType, chosenSymbol);
        System.out.println();
    }

    /**
     * Adjusts the board size based on user input.
     * Warns if the chosen size is too small for the standard fleet.
     *
     * @param game      The Battleship game engine instance.
     * @param userInput Scanner for reading user input.
     */
    private static void adjustSize(Battleship game, Scanner userInput) {
        System.out.printf("Current size: %s%n", game.getSize());
        System.out.print("What size do you choose? ");
        int size = userInput.nextInt();
        game.setSize(size);
        System.out.println();
    }

    /**
     * Displays the standard fleet composition: each ship's name and size.
     *
     * @param game The Battleship game engine instance.
     */
    private static void viewFleetInfo(Battleship game) {
        System.out.println("\n=== Standard Fleet ===");
        for (Ship s : game.getFleetInfo()) {
            System.out.printf("  %-12s  %d cells%n", s.getName(), s.getSize());
        }
        System.out.printf("  Total fleet:   5 ships, 17 cells%n%n");
    }

    /**
     * Prints the game board with row and column labels.
     * Column numbers are printed across the top, and
     * row numbers are printed along the left side.
     *
     * @param board The game board to print.
     */
    public static void printboard(char[][] board) {
        // Print column header
        System.out.print("  ");
        for (int col = 0; col < board[0].length; col++) {
            System.out.printf("%2d", col);
        }
        System.out.println();

        // Print each row with its row number
        for (int row = 0; row < board.length; row++) {
            System.out.printf("%2d", row);
            for (char cell : board[row]) {
                System.out.printf("%2s", cell);
            }
            System.out.println();
        }
    }
}
