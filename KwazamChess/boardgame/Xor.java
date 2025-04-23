/*  Name: Nur Iman binti Mohamad Idros
    Student ID: 1231303620
    Assignment: Kwazam Chess
    File: Xor.java                         */
    
package boardgame;

public class Xor extends Piece {
    public Xor(Position position, boolean isBlue) {
        super(position, isBlue);
    }

    @Override
    public boolean canMove(Position newPosition, Board board) {
        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getCol() - position.getCol());

        // Check if the move is diagonal (same row and column difference)
        if (rowDiff != colDiff) {
            System.out.println("Debug: Xor can only move diagonally.");
            return false;
        }

        // Ensure the destination is not blocked by another piece
        int rowDirection = (newPosition.getRow() > position.getRow()) ? 1 : -1;
        int colDirection = (newPosition.getCol() > position.getCol()) ? 1 : -1;

        int r = position.getRow() + rowDirection;
        int c = position.getCol() + colDirection;

        while (r != newPosition.getRow() && c != newPosition.getCol()) {
            Piece pieceAtDestination = board.getPiece(new Position(r, c));
            if (pieceAtDestination != null) {
                System.out.println("Debug: Xor cannot skip over other pieces.");
                return false;
            }
            r += rowDirection;
            c += colDirection;
        }

        // Move is valid if no pieces are in the way
        return true;
    }

    @Override
    public String toString() {
        return isBlue() ? "X" : "x";
    }
}
