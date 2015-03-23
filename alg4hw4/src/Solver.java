import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Solver.java
 * Mar 19, 2015
 * Igal Israilevich
 */

/**
 * @author Igal Israilevich
 *
 */
public class Solver {
	private Board initial;
	private Board twin;
//	private MinPQ<BoardWrapper> pq;
//	private MinPQ<BoardWrapper> pqTwin;
	private int moves = -1;
	private List<BoardWrapper> boardsTracker;

	public final Comparator<BoardWrapper> MANHATTEN_ORDER = new Comparator<BoardWrapper>() {

		@Override
		public int compare(final BoardWrapper o1, final BoardWrapper o2) {
			int priorManh1 = o1.manhatten*2 + o1.moves;
			int priorManh2 = o2.manhatten*2 + o2.moves;
			if (priorManh1 < priorManh2) {
				return -1;
			} else if (priorManh1 > priorManh2) {
				return 1;
			} else {
				return 0;
			}
		}
	}; //

	/**
	 * find a solution to the initial board (using the A* algorithm)
	 * 
	 * @param initial
	 */
	public Solver(Board initial) {
		if (initial == null)
			throw new NullPointerException();
		this.initial = initial;
		this.twin = initial.twin();
		//this.pq = new MinPQ<BoardWrapper>(MANHATTEN_ORDER);
		//this.boardsTracker = new ArrayList<BoardWrapper>();
		//innerSolution();
	}

	/**
	 * is the initial board solvable?
	 * 
	 * @return
	 */
	public boolean isSolvable() {
		return innerSolution();
		
	}

	/**
	 * min number of moves to solve initial board; -1 if unsolvable
	 * 
	 * @return
	 */
	public int moves() {
		return moves;
	}

	/**
	 * sequence of boards in a shortest solution; null if unsolvable
	 * 
	 * @return
	 */
	public Iterable<Board> solution() {
		Stack<Board> boards = new Stack<>();
		int index = boardsTracker.size()-1;
		
		while(index != -1){
			BoardWrapper curBoardWrapper = boardsTracker.get(index);
			boards.push(curBoardWrapper.board);
			index = curBoardWrapper.parentIndex;
		}
		return boards;
	}

	private boolean innerSolution() {
		int moves = 0;
		List<BoardWrapper> boardsTracker = new ArrayList<>();
		MinPQ<BoardWrapper> pq = new MinPQ<BoardWrapper>(MANHATTEN_ORDER);
		BoardWrapper curBoard = new BoardWrapper(initial, moves, -1);
		curBoard.index = 0;
		pq.insert(curBoard);
		boardsTracker.add(curBoard);
		int trackerIndex = 1;
		// params for twin
		int movesTwin = 0;
		List<BoardWrapper> boardsTwinTracker = new ArrayList<>();
		MinPQ<BoardWrapper> pqTwin = new MinPQ<BoardWrapper>(MANHATTEN_ORDER);
		BoardWrapper curBoardTwin = new BoardWrapper(twin, moves, -1);		
		curBoardTwin.index = 0;
		pqTwin.insert(curBoardTwin);
		boardsTwinTracker.add(curBoardTwin);	
		int trackerTwinIndex = 1;
		boolean isSolvable = false;
		while (true) {
			// getting new curBoard
			curBoard = pq.delMin();
			if (curBoard.board.isGoal()) {
				boardsTracker.add(curBoard);
				isSolvable = true;
				break;
			}			
			// avoid putting initial board twice
			if (initial != curBoard.board) {
				curBoard.index = trackerIndex;
				boardsTracker.add(curBoard);
				trackerIndex++;
			}
			// adding
			moves = curBoard.moves;
			moves++;
			int parentIndex = curBoard.parentIndex;
			Board prevBoard = parentIndex != -1 ? boardsTracker.get(parentIndex).board : null;
			Iterable<Board> neighbors = curBoard.board.neighbors();
			for (Board board : neighbors) {
				if (!board.equals(prevBoard)) {
					pq.insert(new BoardWrapper(board, moves, curBoard.index));
				}
			}
			
			///////////////////////////// solution for twin ///////////////////////////////////////////////
			
			// getting new curBoardTwin
			curBoardTwin = pqTwin.delMin();
			if (curBoardTwin.board.isGoal()) {
				boardsTwinTracker.add(curBoardTwin);
				//isSolvable = false;
				break;
			}			
			// avoid putting initial board twice
			if (twin != curBoardTwin.board) {
				curBoardTwin.index = trackerTwinIndex;
				boardsTwinTracker.add(curBoardTwin);				
				trackerTwinIndex++;
			}
			// adding
			movesTwin = curBoardTwin.moves;
			movesTwin++;
			int parentTwinIndex = curBoardTwin.parentIndex;
			Board prevBoardTwin = parentTwinIndex != -1 ? boardsTwinTracker.get(parentTwinIndex).board : null;
			Iterable<Board> neighborsTwin = curBoardTwin.board.neighbors();
			for (Board board : neighborsTwin) {
				if (!board.equals(prevBoardTwin)) {
					pqTwin.insert(new BoardWrapper(board, movesTwin, curBoardTwin.index));
				}
			}			
			

		}// end while
		if(isSolvable){
			this.moves = curBoard.moves;
			this.boardsTracker = boardsTracker;
		}
		return isSolvable;
	}
	
/*	private boolean innerSolutionForInitial(int trackerIndex){
		// getting new curBoard
		BoardWrapper curBoard = pq.delMin();
		if (curBoard.board.isGoal()) {
			boardsTracker.add(curBoard);
			return true;
		}			
		// avoid putting initial board twice
		if (initial != curBoard.board) {
			curBoard.index = trackerIndex;
			boardsTracker.add(curBoard);
			trackerIndex++;
		}
		// adding
		moves = curBoard.moves;
		moves++;
		int parentIndex = curBoard.parentIndex;
		Board prevBoard = parentIndex != -1 ? boardsTracker.get(parentIndex).board : null;
		Iterable<Board> neighbors = curBoard.board.neighbors();
		for (Board board : neighbors) {
			if (!board.equals(prevBoard)) {
				pq.insert(new BoardWrapper(board, moves, curBoard.index));
			}
		}		
		return false;
	}*/
	
//	private boolean innerSolutionForTwin(){
//		return true;
//	}	

	private class BoardWrapper {
		private int index;
		private final int moves;
		private final int parentIndex;
		private final int manhatten;
		private final Board board;

		public BoardWrapper(Board board, int moves, int parentIndex) {
			this.board = board;
			this.moves = moves;
			this.manhatten = board.manhattan();
			this.parentIndex = parentIndex;
		}
	}

	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
