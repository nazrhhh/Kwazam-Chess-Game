/*  Name: Vinoshnee A/P Alagiri
    Student ID: 1211108662
    Assignment: Kwazam Chess
    File: Sau.java                         */
    
package boardgame;

public class Sau extends Piece {
    public Sau(Position position, boolean isBlue) {
        super(position, isBlue);
    }

    @Override
    public boolean canMove(Position newPosition, Board board) {
        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getCol() - position.getCol());
        return rowDiff <= 1 && colDiff <= 1 && board.isValidPosition(newPosition);
    }

    @Override
    public String toString() {
        return isBlue() ? "S" : "s";
    }
}
