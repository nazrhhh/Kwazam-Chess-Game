/*  Name: Nur Iman binti Mohamad Idros
    Student ID: 1231303620
    Assignment: Kwazam Chess
    File: Tor.java                         */
    
package boardgame;

public class Tor extends Piece {
    public Tor(Position position, boolean isBlue) {
        super(position, isBlue);
    }

    @Override
    public boolean canMove(Position newPosition, Board board) {
        int currentRow = position.getRow();
        int currentCol = position.getCol();
        int targetRow = newPosition.getRow();
        int targetCol = newPosition.getCol();

        // Check if the move is orthogonal (same row or same column)
        if (currentRow != targetRow && currentCol != targetCol) {
            System.out.println("Debug: Tor can only move orthogonally.");
            return false;
        }

        // Determine the direction of movement
        int rowDirection = Integer.compare(targetRow, currentRow); // 1, 0, or -1
        int colDirection = Integer.compare(targetCol, currentCol); // 1, 0, or -1

        // Check for pieces blocking the path
        int r = currentRow + rowDirection;
        int c = currentCol + colDirection;
        while (r != targetRow || c != targetCol) {
            if (board.getPiece(new Position(r, c)) != null) {
                System.out.println("Debug: Tor cannot skip over other pieces.");
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
        return isBlue() ? "T" : "t";
    }
}

