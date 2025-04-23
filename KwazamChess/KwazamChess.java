/*  Name: Haizatul Nazirah Nizam binti Hairunizam
    Student ID: 1231303504
    Assignment: Kwazam Chess
    File: KwazamChess.java                         */

import boardgame.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class KwazamChess extends JFrame {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLS = 5;
    private static final int CELL_SIZE = 60;
    private JPanel boardPanel;
    private JLabel[][] cellLabels;
    private Board gameBoard;
    private boolean isBlueTurn = true;
    private Position selectedPosition = null;
    private int turnCounter = 0;

    // Menu items
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameItem, saveGameItem, loadGameItem, rulesItem, exitItem;
    
    private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }   

    private ImageIcon flipIconVertically(ImageIcon icon) {
        Image img = icon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
    
        // Create a new image with a flipped vertical transformation
        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();
        g2d.drawImage(img, 0, height, width, -height, null);
        g2d.dispose();
    
        return new ImageIcon(flippedImage);
    }

    // Load the piece images
    private ImageIcon blueRamIcon = scaleIcon(new ImageIcon("images/BlueRam.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon blueBizIcon = scaleIcon(new ImageIcon("images/BlueBiz.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon blueTorIcon = scaleIcon(new ImageIcon("images/BlueTor.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon blueXorIcon = scaleIcon(new ImageIcon("images/BlueXor.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon blueSauIcon = scaleIcon(new ImageIcon("images/BlueSau.jpg"), CELL_SIZE, CELL_SIZE);

    private ImageIcon redRamIcon = scaleIcon(new ImageIcon("images/RedRam.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon redBizIcon = scaleIcon(new ImageIcon("images/RedBiz.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon redTorIcon = scaleIcon(new ImageIcon("images/RedTor.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon redXorIcon = scaleIcon(new ImageIcon("images/RedXor.jpg"), CELL_SIZE, CELL_SIZE);
    private ImageIcon redSauIcon = scaleIcon(new ImageIcon("images/RedSau.jpg"), CELL_SIZE, CELL_SIZE);

    // flipped versions of the piece images
    private ImageIcon flippedRedRamIcon = flipIconVertically(redRamIcon);
    private ImageIcon flippedRedBizIcon = flipIconVertically(redBizIcon);
    private ImageIcon flippedRedTorIcon = flipIconVertically(redTorIcon);
    private ImageIcon flippedRedXorIcon = flipIconVertically(redXorIcon);
    private ImageIcon flippedRedSauIcon = flipIconVertically(redSauIcon);

    private ImageIcon flippedBlueRamIcon = flipIconVertically(blueRamIcon);
    private ImageIcon flippedBlueBizIcon = flipIconVertically(blueBizIcon);
    private ImageIcon flippedBlueTorIcon = flipIconVertically(blueTorIcon);
    private ImageIcon flippedBlueXorIcon = flipIconVertically(blueXorIcon);
    private ImageIcon flippedBlueSauIcon = flipIconVertically(blueSauIcon);
    
    public KwazamChess() {
        setTitle("Kwazam Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_COLS * CELL_SIZE + 300, BOARD_ROWS * CELL_SIZE + 200);
        setLocationRelativeTo(null);
       
        createMenuBar();
       
        // Create the game board
        boardPanel = new JPanel(new GridLayout(BOARD_ROWS, BOARD_COLS));
        
        // Initialize the board
        gameBoard = new Board();
        cellLabels = new JLabel[BOARD_ROWS][BOARD_COLS];
        initializeBoard();
        add(boardPanel, BorderLayout.CENTER);
       
       // Make window resizable
       setResizable(true);
       
       setVisible(true);
       
       addMouseListeners();
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(getWidth(), 40));

         // Game Menu
        Font menuFont = new Font("Arial", Font.BOLD, 16);
        gameMenu = new JMenu("Game");
        gameMenu.setFont(menuFont);

        newGameItem = new JMenuItem("New Game");
        saveGameItem = new JMenuItem("Save Game");
        loadGameItem = new JMenuItem("Load Game");
        rulesItem = new JMenuItem("Rules");
        exitItem = new JMenuItem("Exit");
        
        newGameItem.addActionListener(e -> resetGame());
        saveGameItem.addActionListener(e -> saveGame());
        loadGameItem.addActionListener(e -> loadGame());
        rulesItem.addActionListener(e -> showRules());
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        gameMenu.addSeparator();
        gameMenu.add(rulesItem);
        gameMenu.add(exitItem);
        
        // Add menu to menu bar
        menuBar.add(gameMenu);
        
        setJMenuBar(menuBar);
    }

    private void resetGame() {
        gameBoard = new Board();
        isBlueTurn = true;
        turnCounter = 0;
        selectedPosition = null;
    
        boardPanel.removeAll();
        initializeBoard();
    
        // Ensure board is oriented for blue player at start
        if (!isBlueTurn) {
            flipBoardForCurrentPlayer();
        }
    }

    private void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("kwazam_chess_save.txt"))) {
            // Save game state
            writer.println(isBlueTurn ? "Blue" : "Red"); // Current turn
            writer.println(turnCounter); // Turn counter
            
            // Save board state
            for (int row = 0; row < BOARD_ROWS; row++) {
                for (int col = 0; col < BOARD_COLS; col++) {
                    Piece piece = gameBoard.getPiece(new Position(row, col));
                    if (piece != null) {
                        writer.println(row + "," + col + "," + 
                            piece.getClass().getSimpleName() + "," + 
                            (piece.isBlue() ? "Blue" : "Red"));
                    }
                }
            }
            
            JOptionPane.showMessageDialog(this, "Game saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage(), 
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a game file to load");
        
        // Set the current directory
        fileChooser.setCurrentDirectory(new File("."));
        
        // Show the file selection dialog
        int result = fileChooser.showOpenDialog(this);
    
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
    
            try (Scanner scanner = new Scanner(selectedFile)) {
                // Reset the board first
                gameBoard = new Board();
                boardPanel.removeAll();
    
                // Read current turn
                String turnColor = scanner.nextLine();
                isBlueTurn = turnColor.equals("Blue");
    
                // Read turn counter
                turnCounter = Integer.parseInt(scanner.nextLine());
    
                // Clear existing board
                for (int row = 0; row < BOARD_ROWS; row++) {
                    for (int col = 0; col < BOARD_COLS; col++) {
                        gameBoard.setPiece(new Position(row, col), null);
                    }
                }
    
                // Restore board state
                while (scanner.hasNextLine()) {
                    String[] pieceData = scanner.nextLine().split(",");
                    int row = Integer.parseInt(pieceData[0]);
                    int col = Integer.parseInt(pieceData[1]);
                    String pieceType = pieceData[2];
                    boolean isBlue = pieceData[3].equals("Blue");
    
                    Position pos = new Position(row, col);
                    switch (pieceType) {
                        case "Ram":
                            gameBoard.setPiece(pos, new Ram(pos, isBlue));
                            break;
                        case "Biz":
                            gameBoard.setPiece(pos, new Biz(pos, isBlue));
                            break;
                        case "Tor":
                            gameBoard.setPiece(pos, new Tor(pos, isBlue));
                            break;
                        case "Xor":
                            gameBoard.setPiece(pos, new Xor(pos, isBlue));
                            break;
                        case "Sau":
                            gameBoard.setPiece(pos, new Sau(pos, isBlue));
                            break;
                    }
                }
    
                // Update board display and flip view if necessary
                updateBoard();
                flipBoardForCurrentPlayer();
    
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Load Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Game Rules
    private void showRules() {
        String rules = "Kwazam Chess Rules:\n\n" +
            "1. Blue and Red players take turns moving pieces.\n" +
            "2. Goal is to capture the opponent's Sau.\n" +
            "3. Each piece moves differently:\n" +
            "   - Sau: Only move 1 step in any direction.\n" +
            "   - Ram: Only move 1 step forward.\n" +
            "   - Biz: moves in a 3x2 L shape in any orientation,\n" +
            "             can skip over other pieces.\n" +
            "   - Tor: moves orthogonally only but can go any distance.\n" +
            "             Transform into Xor every two turns.\n" +
            "   - Xor: moves diagonally only but can go any distance.\n" +
            "             Transform into Tor every two turns.\n" +
            "4. Capture opponent's pieces by moving to their square.\n" +
            "5. The game ends when the Sau is captured by the other side.";
        
        JOptionPane.showMessageDialog(this, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    //Flip the game board according player's turn
    private void flipBoardForCurrentPlayer() {
        JPanel flippedPanel = new JPanel(new GridLayout(BOARD_ROWS, BOARD_COLS));
    
        for (int row = BOARD_ROWS - 1; row >= 0; row--) {
            for (int col = BOARD_COLS - 1; col >= 0; col--) {
                if (isBlueTurn) {
                    flippedPanel.add(cellLabels[BOARD_ROWS - 1 - row][BOARD_COLS - 1 - col]);
                } else {
                    flippedPanel.add(cellLabels[row][col]);
                }
            }
        }
    
        // Replace the old board panel with the flipped one
        remove(boardPanel);
        boardPanel = flippedPanel;
        add(boardPanel, BorderLayout.CENTER);
    
        // Refresh the display
        revalidate();
        repaint();
    
        addMouseListeners();
    }
    

    private void addMouseListeners() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                final int finalRow = row;
                final int finalCol = col;
    
                // Remove existing listeners before adding a new one
                for (MouseListener listener : cellLabels[row][col].getMouseListeners()) {
                    cellLabels[row][col].removeMouseListener(listener);
                }
    
                cellLabels[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Position clickedPosition = new Position(finalRow, finalCol);
    
                        // Adjust position based on current player's perspective
                        if (!isBlueTurn) {
                            clickedPosition = new Position(BOARD_ROWS - 1 - finalRow, BOARD_COLS - 1 - finalCol);
                        }
    
                        handleCellClick(clickedPosition);
                    }
                });
            }
        }
    }
    
    

    private void handleCellClick(Position clickedPosition) {
        System.out.println("Clicked position: " + clickedPosition);
    
        // Adjust for flipped board
        if (!isBlueTurn) {
            clickedPosition = new Position(BOARD_ROWS - 1 - clickedPosition.getRow(),
                                            BOARD_COLS - 1 - clickedPosition.getCol());
            System.out.println("Adjusted clicked position for red's perspective: " + clickedPosition);
        }
    
        if (selectedPosition != null && selectedPosition.equals(clickedPosition)) {
            System.out.println("Repeated click on the same cell ignored.");
            return;
        }
    
        if (selectedPosition == null) {
            // First click - select a piece
            Piece selectedPiece = gameBoard.getPiece(clickedPosition);
            System.out.println("Selected piece: " + selectedPiece);
    
            if (selectedPiece != null && selectedPiece.isBlue() == isBlueTurn) {
                selectedPosition = clickedPosition;
                System.out.println("Piece selected at: " + selectedPosition);
            }
        } else {
            // Second click - attempt to move the piece
            System.out.println("Attempting to move from " + selectedPosition + " to " + clickedPosition);
    
            if (gameBoard.movePiece(selectedPosition, clickedPosition)) {
                // Successful move
                turnCounter++;
    
                // Transform Tors and Xors every two turns
                if (turnCounter % 2 == 0) {
                    gameBoard.transformTorsAndXors();
                }
    
                // Switch turns
                isBlueTurn = !isBlueTurn;
    
                // Update board display
                updateBoard();
    
                // Flip the board for the other player
                flipBoardForCurrentPlayer();
    
                // Check for game-ending conditions
                checkGameEnd();
            }
    
            // Reset selection
            selectedPosition = null;
        }
    }
    
    

    private void checkGameEnd() {
        // Check if a Sau has been captured
        boolean blueSauExists = false;
        boolean redSauExists = false;
    
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Piece piece = gameBoard.getPiece(new Position(row, col));
                if (piece instanceof Sau) {
                    if (piece.isBlue()) {
                        blueSauExists = true;
                    } else {
                        redSauExists = true;
                    }
                }
            }
        }
    
        if (!blueSauExists) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Red Wins! Do you want to play a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        } else if (!redSauExists) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Blue Wins! Do you want to play a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        }
    }
    

    private void initializeBoard() {
        cellLabels = new JLabel[BOARD_ROWS][BOARD_COLS];
    
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                cellLabels[row][col] = new JLabel();
                cellLabels[row][col].setHorizontalAlignment(JLabel.CENTER);
                cellLabels[row][col].setVerticalAlignment(JLabel.CENTER);
                cellLabels[row][col].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cellLabels[row][col].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
               
                cellLabels[row][col].setOpaque(true);
                cellLabels[row][col].setBackground(Color.WHITE);
        
                Piece piece = gameBoard.getPiece(new Position(row, col));
                updatePieceIcon(row, col, piece);
                
                boardPanel.add(cellLabels[row][col]);
            }
        }
    
        // Red pieces (top rows)
        gameBoard.setPiece(new Position(0, 0), new Tor(new Position(0, 0), false));
        gameBoard.setPiece(new Position(0, 1), new Biz(new Position(0, 1), false));
        gameBoard.setPiece(new Position(0, 2), new Sau(new Position(0, 2), false));
        gameBoard.setPiece(new Position(0, 3), new Biz(new Position(0, 3), false));
        gameBoard.setPiece(new Position(0, 4), new Xor(new Position(0, 4), false));
    
        // Red Rams (row 1)
        for (int col = 0; col < BOARD_COLS; col++) {
            gameBoard.setPiece(new Position(1, col), new Ram(new Position(1, col), false));
        }
    
        // Blue Rams (row 6)
        for (int col = 0; col < BOARD_COLS; col++) {
            gameBoard.setPiece(new Position(6, col), new Ram(new Position(6, col), true));
        }
    
        // Blue pieces (top rows)
        gameBoard.setPiece(new Position(7, 0), new Xor(new Position(7, 0), true));
        gameBoard.setPiece(new Position(7, 1), new Biz(new Position(7, 1), true));
        gameBoard.setPiece(new Position(7, 2), new Sau(new Position(7, 2), true));
        gameBoard.setPiece(new Position(7, 3), new Biz(new Position(7, 3), true));
        gameBoard.setPiece(new Position(7, 4), new Tor(new Position(7, 4), true));
    
        // Update the GUI to reflect the new board setup
        updateBoard();
        addMouseListeners();
    }

    private void updatePieceIcon(int row, int col, Piece piece) {
        if (piece != null) {
            if (piece instanceof Ram) {
                cellLabels[row][col].setIcon(
                    piece.isBlue() ? (isBlueTurn ? blueRamIcon : flippedBlueRamIcon) : (isBlueTurn ? redRamIcon : flippedRedRamIcon)
                );
            } else if (piece instanceof Biz) {
                cellLabels[row][col].setIcon(
                    piece.isBlue() ? (isBlueTurn ? blueBizIcon : flippedBlueBizIcon) : (isBlueTurn ? redBizIcon : flippedRedBizIcon)
                );
            } else if (piece instanceof Tor) {
                cellLabels[row][col].setIcon(
                    piece.isBlue() ? (isBlueTurn ? blueTorIcon : flippedBlueTorIcon) : (isBlueTurn ? redTorIcon : flippedRedTorIcon)
                );
            } else if (piece instanceof Xor) {
                cellLabels[row][col].setIcon(
                    piece.isBlue() ? (isBlueTurn ? blueXorIcon : flippedBlueXorIcon) : (isBlueTurn ? redXorIcon : flippedRedXorIcon)
                );
            } else if (piece instanceof Sau) {
                cellLabels[row][col].setIcon(
                    piece.isBlue() ? (isBlueTurn ? blueSauIcon : flippedBlueSauIcon) : (isBlueTurn ? redSauIcon : flippedRedSauIcon)
                );
            }
        } else {
            cellLabels[row][col].setIcon(null);
        }
    }
    
    

    private void updateBoard() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Piece piece = gameBoard.getPiece(new Position(row, col));
                updatePieceIcon(row, col, piece);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KwazamChess());
    }
}