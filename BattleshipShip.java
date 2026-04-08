/**
 * Battleship ship type — a heavy warship.
 * Occupies 4 cells on the board.
 * Named BattleshipShip to avoid conflict with the Battleship game engine class.
 */
public class BattleshipShip extends Ship {
    public BattleshipShip() {
        super("Battleship", 4);
    }
}
