/*  Name: Vinoshnee A/P Alagiri
    Student ID: 1211108662
    Assignment: Kwazam Chess
    File: Biz.java                         */
    
package boardgame;

public class Biz extends Piece {
    public Biz(Position position, boolean isBlue) {
        super(position, isBlue);
    }

    @Override
    public boolean canMove(Position newPosition, Board board) {
        int[][] moves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] move : moves) {
            int newRow = position.getRow() + move[0];
            int newCol = position.getCol() + move[1];
            if (newRow == newPosition.getRow() && newCol == newPosition.getCol() &&
                board.isValidPosition(newPosition)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return isBlue() ? "B" : "b";
    }
}
