import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Board.java
 * Mar 19, 2015
 * Igal Israilevich
 */

/**
 * @author Igal Israilevich
 *
 */
public class Board {
	private int N;
	private int[][] blocks;
	private int[][] goalBlocks;

	/**
	 * construct a board from an N-by-N array of blocks (where blocks[i][j] =
	 * block in row i, column j)
	 * 
	 * @param blocks
	 */
	public Board(int[][] blocks) {
		this.blocks = blocks;
		this.N = blocks.length;
		// build goal
		goalBlocks = initGoalBoard(N);

	}

	/**
	 * board dimension N
	 * 
	 * @return
	 */
	public int dimension() {
		return N;
	}

	/**
	 * number of blocks out of place
	 * 
	 * @return
	 */
	public int hamming() {
		int priority = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0)
					continue;
				if (blocks[i][j] != goalBlocks[i][j]) {
					priority++;
				}
			}
		}
		return priority;
	}

	/**
	 * sum of Manhattan distances between blocks and goal
	 * 
	 * @return
	 */
	public int manhattan() {
		int priority = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				int val = blocks[i][j];
				if (val == 0)
					continue;
				// find val into goal blocks array
				int[] goalCoord = findGoalCoord(val);
				int valPriority = calcManhPriority(i, j, goalCoord[0], goalCoord[1]);
				priority = priority + valPriority;
			}
		}
		return priority;
	}

	private int[] findGoalCoord(int val) {
		int[] retCoord = new int[2];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (goalBlocks[i][j] == val) {
					retCoord[0] = i;
					retCoord[1] = j;
					break;
				}
			}
		}
		return retCoord;

	}

	private int[] findZeroCoord() {
		int[] retCoord = new int[2];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0) {
					retCoord[0] = i;
					retCoord[1] = j;
					break;
				}
			}
		}
		return retCoord;
	}

	private int calcManhPriority(int cRow, int cCol, int gRow, int gCol) {
		return Math.abs(cRow - gRow) + Math.abs(cCol - gCol);
	}

	/**
	 * is this board the goal board?
	 * 
	 * @return
	 */
	public boolean isGoal() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (goalBlocks[i][j] != blocks[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * a board that is obtained by exchanging two adjacent blocks in the same
	 * row
	 * 
	 * @return
	 */
	public Board twin() {
		int[][] twinArr;
		if (blocks[0][0] != 0) {
			if (blocks[0][1] != 0) {
				twinArr = swap(0, 0, 0, 1);
			} else {
				twinArr = swap(0, 0, 0, 2);
			}
		} else {
			twinArr = swap(0, 1, 0, 2);
		}
		return new Board(twinArr);
	}

	/**
	 * does this board equal y?
	 */
	public boolean equals(Object y) {
		if (this == y) {
			return true;
		}
		if (y == null || y.getClass() != getClass()) {
			return false;
		}
		Board yBoard = (Board) y;
		int[][] yBlocks = yBoard.getBlocks();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (yBlocks[i][j] != blocks[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * all neighboring boards
	 * 
	 * @return
	 */
	public Iterable<Board> neighbors() {
		List<Board> boards = new ArrayList<>();
		// find block with value 0
		int[] zeroCoord = findZeroCoord();
		// find all possible variants to move 0
		int row = zeroCoord[0];
		int col = zeroCoord[1];
		if (row - 1 >= 0) {
			boards.add(new Board(swap(row, col, row - 1, col)));
		}
		if (row + 1 < N) {
			boards.add(new Board(swap(row, col, row + 1, col)));
		}
		if (col - 1 >= 0) {
			boards.add(new Board(swap(row, col, row, col - 1)));
		}
		if (col + 1 < N) {
			boards.add(new Board(swap(row, col, row, col + 1)));
		}

		return boards;
	}

	private int[][] swap(int row1, int col1, int row2, int col2) {
		int[][] copy = copyBlocks();
		int t = copy[row1][col1];
		copy[row1][col1] = copy[row2][col2];
		copy[row2][col2] = t;
		return copy;
	}

	private int[][] copyBlocks() {
		int[][] copy = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				copy[i][j] = blocks[i][j];
			}
		}
		return copy;
	}

	/**
	 * string representation of this board (in the output format specified
	 * below)
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < N; i++) {
			if (i == 0) {
				buf.append(N);
			}
			buf.append("\n");
			for (int j = 0; j < N; j++) {
				buf.append(blocks[i][j] + " ");
			}
		}
		return buf.toString();
	}

	private static int[][] initGoalBoard(int N) {
		int[][] g = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				g[i][j] = i * N + j + 1;
			}
		}
		g[N - 1][N - 1] = 0;
		return g;
	}

	public int[][] getBlocks() {
		return blocks;
	}

	public static void main(String[] args) {
		int[][] blocks = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
		Board board = new Board(blocks);
		System.out.println(board);
		Board boardGoal = new Board(initGoalBoard(3));
		System.out.println(boardGoal);
		int manhatten = board.manhattan();
		int hamming = board.hamming();
		System.out.println(manhatten);
		System.out.println(hamming);
		Board boardTwin = board.twin();
		System.out.println(boardTwin);
		System.out.println(board);
	}

}
