import java.util.*;

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
                // would be on the adjust function
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
                    // Adjust number of ships
                    adjustNumberOfShips(game, userInput);
                    break;
                default:
                    System.out.println("Option not exist");
            }

        } while (userChoice != 0);
    }

    private static void playGame(Scanner userInput, Random rand, Battleship game) {
        // Initialize game boards
        char[][] userboard = game.CreateGameBoard();
        char[][] comboard = game.CreateGameBoard();

        // Display initial boards
        System.out.println("This is your board");
        printboard(userboard);
        System.out.println("This is computer's board");
        printboard(comboard);

        // Setup user's ships
        game.createCoordinate();
        System.out.println("Enter your ship(s) coordinate (start at 0) in the form of (x y x y ...)");
        game.userCoor(userInput, userboard);

        // Display updated boards with user's ships
        System.out.println("This is your board");
        printboard(userboard);
        System.out.println("This is computer's board");
        printboard(comboard);

        // Game loop for user and computer shots
        do {
            System.out.println("What coordinate do you want to shoot? (x y)");
            int x = userInput.nextInt();
            int y = userInput.nextInt();

            System.out.print("Your shot: ");
            game.shotUser(x, y, comboard);
            System.out.print("Computer's shot: ");
            game.shotComp(userboard, rand);

            // Display updated boards after shots
            System.out.println("This is your board");
            printboard(userboard);
            System.out.println("This is computer's board");
            printboard(comboard);
            System.out.println();
        } while (!game.check());

        // Display game end message
        System.out.println("The game has ended\n");
    }

    private static void displayMenu() {
        // Display menu options
        System.out.println("Play game (1)");
        System.out.println("Adjust water icon (2)");
        System.out.println("Adjust ship icon (3)");
        System.out.println("Adjust hit icon (4)");
        System.out.println("Adjust miss icon (5)");
        System.out.println("Adjust board size icon (6)");
        System.out.println("Adjust number of ships icon (7)");
        System.out.println("Exit (0)");
        System.out.print("What is your choice? ");
    }

    private static void adjustSymbol(int userChoice, Battleship game, Scanner userInput) {
        // Adjust water, ship, hit, or miss symbol
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

    private static void adjustSize(Battleship game, Scanner userInput) {
        // Adjust board size
        System.out.printf("Current size: %s%n", game.getSize());
        System.out.print("What size do you choose? ");
        int size = userInput.nextInt();
        game.setSize(size);
        System.out.println();
    }

    private static void adjustNumberOfShips(Battleship game, Scanner userInput) {
        // Adjust number of ships
        System.out.printf("Current number: %s%n", game.getShipCom());
        System.out.print("How many ships? ");
        int ship = userInput.nextInt();
        game.setShip(ship);
        System.out.println();
    }

    public static void printboard(char[][] board) {
        // Print the game board
        for (char[] row : board) {
            for (char cell : row) {
                System.out.printf("%2s", cell);
            }
            System.out.println();
        }
    }
}
