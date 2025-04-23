/*  Name: Nur Alia Shazwani binti Mohd Nazri
    Student ID: 1231302985
    Assignment: Kwazam Chess
    File: Ram.java                         */
    
package boardgame;

public class Ram extends Piece {
    private boolean movingForward;

    public Ram(Position position, boolean isBlue) {
        super(position, isBlue);
        this.movingForward = isBlue; // Blue moves "up", Red moves "down"
    }

@Override
public boolean canMove(Position newPosition, Board board) {
    System.out.println("Debug: Checking move for Ram from " + position + " to " + newPosition);

    if (!board.isValidPosition(newPosition)) {
        System.out.println("Debug: Invalid target position - " + newPosition);
        return false;
    }

    int currentRow = position.getRow();
    int currentCol = position.getCol();
    int newRow = newPosition.getRow();
    int newCol = newPosition.getCol();

    // Ram can move forward or change direction at board edges
    int direction = movingForward ? -1 : 1;
    
    // Allow movement in the current direction
    if (newCol == currentCol && (newRow - currentRow) == direction) {
        // Check if the target is empty or can be captured
        Piece targetPiece = board.getPiece(newPosition);
        if (targetPiece == null) {
            return true;
        }
        if (targetPiece.isBlue() != this.isBlue()) {
            // Can capture opponent's piece
            return true;
        }
    }

    // Update direction at board edge
    if (newRow == 0 || newRow == board.getRows() - 1) {
        movingForward = !movingForward;
    }

    return false;
}

    @Override
    public String toString() {
        return isBlue() ? "R" : "r";
    }
}
