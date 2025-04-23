/*  Name: Nur Alia Shazwani binti Mohd Nazri
    Student ID: 1231302985
    Assignment: Kwazam Chess
    File: Piece.java                         */
    
package boardgame;

public abstract class Piece {
    protected Position position;
    private boolean isBlue;

    public Piece(Position position, boolean isBlue) {
        this.position = position;
        this.isBlue = isBlue;
    }

    public abstract boolean canMove(Position newPosition, Board board);

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isBlue() {
        return isBlue;
    }

    public void updatePosition(Position to) {
        this.position = to;
    }

    @Override
    public String toString() {
        return isBlue ? "B" : "R";
    }
}
