/*  Name: Nur Alia Shazwani binti Mohd Nazri
    Student ID: 1231302985
    Assignment: Kwazam Chess
    File: Board.java                         */
    
package boardgame;

public class Board {
    private static final int ROWS = 8; // Board rows
    private static final int COLS = 5; // Board columns
    private Piece[][] squares;        // 2D array for the board

    public Board() {
        squares = new Piece[ROWS][COLS];
    }

    // Validate if a position is within the board's boundaries
    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < ROWS &&
               position.getCol() >= 0 && position.getCol() < COLS;
    }

    // Check if a position is occupied
    public boolean isOccupied(Position position) {
        return isValidPosition(position) && squares[position.getRow()][position.getCol()] != null;
    }

    // Get the piece at a specific position
    public Piece getPiece(Position position) {
        if (!isValidPosition(position)) return null;
        return squares[position.getRow()][position.getCol()];
    }

    // Set a piece at a specific position
    public void setPiece(Position position, Piece piece) {
        if (isValidPosition(position)) {
            squares[position.getRow()][position.getCol()] = piece;
        }
    }

    // Move a piece from one position to another
    public boolean movePiece(Position from, Position to) {
        if (!isValidPosition(from) || !isValidPosition(to)) {
            System.out.println("Debug: Invalid positions - " + from + " or " + to);
            return false;
        }

        Piece movingPiece = getPiece(from);
        if (movingPiece == null) {
            System.out.println("Debug: No piece at " + from);
            return false;
        }

        Piece targetPiece = getPiece(to);

        // Check if the move is valid for the piece
        if (movingPiece.canMove(to, this)) {
            // Handle captures
            if (targetPiece != null) {
                if (movingPiece.isBlue() != targetPiece.isBlue()) {
                    System.out.println(movingPiece + " captures " + targetPiece + " at " + to);
                    if (targetPiece instanceof Sau) {
                        System.out.println("Game Over! " + (movingPiece.isBlue() ? "Blue" : "Red") + " wins!");
                    }
                } else {
                    System.out.println("Debug: Cannot capture friendly piece.");
                    return false;
                }
            }

            // Move the piece
            setPiece(to, movingPiece);
            setPiece(from, null);
            movingPiece.updatePosition(to);

            return true;
        } else {
            System.out.println("Debug: Invalid move for " + movingPiece + " to " + to);
        }

        return false;
    }

    // Transform all Tor ↔ Xor pieces
    public void transformTorsAndXors() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                Piece piece = getPiece(new Position(row, col));
                if (piece instanceof Tor) {
                    setPiece(new Position(row, col), new Xor(piece.getPosition(), piece.isBlue()));
                } else if (piece instanceof Xor) {
                    setPiece(new Position(row, col), new Tor(piece.getPosition(), piece.isBlue()));
                }
            }
        }
    }

    // Get the number of rows
    public int getRows() {
        return ROWS;
    }

    // **Get the number of columns**
    public int getCols() {
        return COLS;
    }

    // Print the board
    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (squares[row][col] != null) {
                    System.out.print(squares[row][col] + " ");
                } else {
                    System.out.print(". "); // Empty square
                }
            }
            System.out.println();
        }
    }
}

