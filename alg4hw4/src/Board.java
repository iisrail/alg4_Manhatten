import java.util.ArrayList;
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
	 * construct a board from an N-by-N array of blocks
	 * (where blocks[i][j] = block in row i, column j)
	 * @param blocks
	 */
    public Board(int[][] blocks)  {
    	this.blocks = blocks;
    	this.N = blocks.length;
    	//build goal
    	goalBlocks = initGoalBoard(N);
    	
    }
    /**
     * board dimension N
     * @return
     */
	public int dimension(){
		return N; 
	}
	/**
	 * number of blocks out of place
	 * @return
	 */
	public int hamming() {
		return 0; 
	}
	/**
	 * sum of Manhattan distances between blocks and goal
	 * @return
	 */
	public int manhattan(){
		return 0; 
	}
	/**
	 * is this board the goal board?
	 * @return
	 */
	public boolean isGoal(){
		return false; 
	}
	/**
	 * a board that is obtained by exchanging two adjacent blocks in the same row
	 * @return
	 */
	public Board twin() {
		return new Board(blocks); 
	}
	/**
	 * does this board equal y?
	 */
	public boolean equals(Object y) {
		return false;
	}
	/**
	 * all neighboring boards
	 * @return
	 */
	public Iterable<Board> neighbors(){
    	List<Board> boards = new ArrayList<>();
    	return boards;
	}
	/**
	 * string representation of this board (in the output format specified below)
	 */
	public String toString(){
		StringBuilder buf = new StringBuilder();
		
		for(int i=0;i<N;i++){
			if(i == 0){
				buf.append(N);
			}
			buf.append("\n");
			for(int j=0;j<N;j++){
				buf.append(blocks[i][j] + " ");
			}
		}
		return buf.toString();
	}
	
	private static int[][] initGoalBoard(int N){
    	int[][] g = new int[N][N];
    	for(int i=0;i<N;i++){
    		for(int j=0;j<N;j++){
    			g[i][j]= i*N+j+1;
    		}
    	}
    	g[N-1][N-1]=0;		
		return g;
	}
	
	public static void main(String[] args) {
		int[][] blocks = {{1,2,3},{4,5,0},{7,8,6}};
		Board board = new Board(blocks);
		System.out.println(board);
		Board boardGoal = new Board(initGoalBoard(3));
		System.out.println(boardGoal);
		
	}
}
