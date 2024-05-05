package model;

import java.util.Arrays;
import java.util.Random;

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 * @author Dept of Computer Science, UMCP
 */

public class ClearCellGameModel extends GameModel {
	
	/* Include whatever instance variables you think are useful. */
	private Random random;
	private int score = 0;
	/**
	 * Defines a board with empty cells.  It relies on the
	 * super class constructor to define the board.
	 * 
	 * @param rows number of rows in board
	 * @param cols number of columns in board
	 * @param random random number generator to be used during game when
	 * rows are randomly created
	 */
	public ClearCellGameModel(int rows, int cols, Random random) {
		super(rows, cols);
		this.random = random;
	}

	/**
	 * The game is over when the last row (the one with index equal
	 * to board.length-1) contains at least one cell that is not empty.
	 */
	public boolean isGameOver() {
		for (BoardCell b : board[board.length - 1]) {
			if (!(b == BoardCell.EMPTY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the player's score.  The player should be awarded one point
	 * for each cell that is cleared.
	 * 
	 * @return player's score
	 */
	public int getScore() {
		return this.score;
	}

	
	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do 
	 * the following:
	 * 
	 * 1. Shift the existing rows down by one position.
	 * 2. Insert a row of random BoardCell objects at the top
	 * of the board. The row will be filled from left to right with cells 
	 * obtained by calling BoardCell.getNonEmptyRandomBoardCell().  (The Random
	 * number generator passed to the constructor of this class should be
	 * passed as the argument to this method call.)
	 */
	public void nextAnimationStep() {
		if (isGameOver()) {
			return;
		}
		
		for (int i = board.length - 1; i > 0; i--) {
			board[i] = board[i - 1];
		}
		board[0] = new BoardCell[board[0].length];
		for (int i = 0; i < board[0].length; i++) {
			board[0][i] = BoardCell.getNonEmptyRandomBoardCell(random);
		}
		
	}

	/**
	 * This method is called when the user clicks a cell on the board.
	 * If the selected cell is not empty, it will be set to BoardCell.EMPTY, 
	 * along with any adjacent cells that are the same color as this one.  
	 * (This includes the cells above, below, to the left, to the right, and 
	 * all in all four diagonal directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of 
	 * cells then those rows will "collapse", meaning that all non-empty 
	 * rows beneath the collapsing row will shift upward. 
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for 
	 * invalid row or "Invalid column index" for invalid column.  We check 
	 * for row validity first.
	 */
	public void processCell(int rowIndex, int colIndex) {
		if (rowIndex >= board.length) {
			throw new IllegalArgumentException("Invalid row index");
		}
		if (colIndex >= board[0].length) {
			throw new IllegalArgumentException("Invalid column index");
		}
		boolean[][] needsCleared = new boolean[board.length][board[0].length];
		BoardCell b = getBoardCell(rowIndex, colIndex);
		needsCleared[rowIndex][colIndex] = true;
		// Set all values of needsCleared equal to true if they are the same
		// color and adjacent
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (getBoardCell(row, col).getColor() == b.getColor()) {
					if (row == rowIndex - 1) {
						if (col == colIndex - 1 || col == colIndex 
								|| col == colIndex + 1) {
							needsCleared[row][col] = true;
						}
					}
					if (row == rowIndex) {
						if (col == colIndex - 1 || col == colIndex + 1) {
							needsCleared[row][col] = true;
						}
					}
					if (row == rowIndex + 1) {
						if (col == colIndex - 1 || col == colIndex 
								|| col == colIndex + 1) {
							needsCleared[row][col] = true;
						}
					}
				}
			}
		}
		// Clear the spots equal to true and increment the score
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (needsCleared[row][col] == true
						&& getBoardCell(row, col).getColor() == b.getColor()
						&& getBoardCell(row, col) != BoardCell.EMPTY) {
					score++;
					setBoardCell(row, col, BoardCell.EMPTY);
				}
			}
		}
		
		// Check for empty rows
		BoardCell[] empty = new BoardCell[board[0].length];
		for (int i = 0; i < empty.length; i++) {
			empty[i] = BoardCell.EMPTY;
		}
		for (int row = 0; row < board.length; row++) {
			if (Arrays.equals(board[row], empty)) {
				if (row < board.length - 2) {
					for (int row2 = row; row2 < board.length - 1; row2++) {
						board[row2] = board[row2 + 1];
						board[row2 + 1] = empty;
					}
				}
			}
		}
	}
		
}

