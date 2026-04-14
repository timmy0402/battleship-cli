import java.util.*;

/**
 * Entry point and UI for the Battleship game.
 * Contains the main menu loop, board printing, and delegates
 * all game logic to the Battleship engine class.
 */
public class BattleshipGame {
    public static void main(String[] args) {
        // Initialization
        System.out.println(Color.ANSI_BLUE + "██████╗  █████╗ ████████╗████████╗██╗     ███████╗███████╗██╗  ██╗██╗██████╗ ");
        System.out.println("██╔══██╗██╔══██╗╚══██╔══╝╚══██╔══╝██║     ██╔════╝██╔════╝██║  ██║██║██╔══██╗");
        System.out.println("██████╔╝███████║   ██║      ██║   ██║     █████╗  ███████╗███████║██║██████╔╝");
        System.out.println("██╔══██╗██╔══██║   ██║      ██║   ██║     ██╔══╝  ╚════██║██╔══██║██║██╔═══╝ ");
        System.out.println("██████╔╝██║  ██║   ██║      ██║   ███████╗███████╗███████║██║  ██║██║██║");
        System.out.println("╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝╚═╝" + Color.ANSI_RESET);

        System.out.println(Color.ANSI_CYAN + "Welcome to Battleship!" + Color.ANSI_RESET);

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
                    System.out.println(Color.ANSI_CYAN + "Exit, See you again!" + Color.ANSI_RESET);
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
                    System.out.println(Color.ANSI_RED + "Option not exist" + Color.ANSI_RESET);
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
        System.out.println(Color.ANSI_GREEN + "This is your board" + Color.ANSI_RESET);
        printboard(userboard);
        System.out.println(Color.ANSI_RED + "This is computer's board" + Color.ANSI_RESET);
        printboard(comboard);

        // Place ships: computer randomly, user via input
        game.placeComputerShips(comboard);

        System.out.println(Color.ANSI_PURPLE + "=== Place your ships ===" + Color.ANSI_RESET);
        game.placeUserShips(userInput, userboard);

        // Display boards after ship placement
        System.out.println(Color.ANSI_GREEN + "This is your board" + Color.ANSI_RESET);
        printboard(userboard);
        System.out.println(Color.ANSI_RED + "This is computer's board" + Color.ANSI_RESET);
        printboard(comboard);

        // Game loop: alternate user and computer shots
        do {
            System.out.println(Color.ANSI_YELLOW + "What coordinate do you want to shoot? (col row)" + Color.ANSI_RESET);
            int x = userInput.nextInt();
            int y = userInput.nextInt();

            while (x >= game.getSize() || y >= game.getSize() || x < 0 || y < 0) {
                System.out.println(Color.ANSI_RED + "Invalid shot coordinates. Try again" + Color.ANSI_RESET);
                System.out.println(Color.ANSI_YELLOW + "What coordinate do you want to shoot? (col row)" + Color.ANSI_RESET);
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
            System.out.println(Color.ANSI_RED + "This is computer's board" + Color.ANSI_RESET);
            printboard(comboard);

            // Show remaining ship counts
            System.out.printf(Color.ANSI_CYAN + "Your ships remaining: %d" + Color.ANSI_RESET
                    + " | " + Color.ANSI_RED + "Computer's ships remaining: %d" + Color.ANSI_RESET + "%n%n",
                    game.getShipUser(), game.getShipCom());
        } while (!game.check());

        // Display game result
        String winner = game.getWinner();
        if ("User".equals(winner)) {
            System.out.println(Color.ANSI_GREEN + "Congratulations! You sunk the entire enemy fleet!" + Color.ANSI_RESET + "\n");
        } else {
            System.out.println(Color.ANSI_RED + "The computer sunk your entire fleet. Better luck next time!" + Color.ANSI_RESET + "\n");
        }
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMenu() {
        System.out.println(Color.ANSI_GREEN + "Play game (1)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "Adjust water icon (2)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "Adjust ship icon (3)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "Adjust hit icon (4)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "Adjust miss icon (5)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "Adjust board size (6)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_CYAN + "View fleet info (7)" + Color.ANSI_RESET);
        System.out.println(Color.ANSI_RED + "Exit (0)" + Color.ANSI_RESET);
        System.out.print(Color.ANSI_YELLOW + "What is your choice? " + Color.ANSI_RESET);
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
        System.out.println("\n" + Color.ANSI_PURPLE + "=== Standard Fleet ===" + Color.ANSI_RESET);
        for (Ship s : game.getFleetInfo()) {
            System.out.printf("  " + Color.ANSI_GREEN + "%-12s" + Color.ANSI_RESET + "  %d cells%n", s.getName(), s.getSize());
        }
        System.out.printf(Color.ANSI_CYAN + "  Total fleet:   5 ships, 17 cells" + Color.ANSI_RESET + "%n%n");
    }

    /**
     * Prints the game board with row and column labels.
     * Column numbers are printed across the top, and
     * row numbers are printed along the left side.
     * Cells are color-coded: cyan for water, green for ships,
     * red for hits, yellow for misses.
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
    private static String colorCell(char cell) {
        return switch (cell) {
            case '~' -> Color.ANSI_CYAN + cell + Color.ANSI_RESET;
            case 'S' -> Color.ANSI_GREEN + cell + Color.ANSI_RESET;
            case 'X' -> Color.ANSI_RED + cell + Color.ANSI_RESET;
            case 'O' -> Color.ANSI_YELLOW + cell + Color.ANSI_RESET;
            default -> String.valueOf(cell);
        };
    }
}
