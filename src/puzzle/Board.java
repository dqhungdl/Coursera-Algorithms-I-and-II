package puzzle;

import java.util.ArrayList;

public class Board {

    private class Coordinate {
        int x, y;
    }

    private static final int dX[] = {0, 0, -1, 1};
    private static final int dY[] = {-1, 1, 0, 0};

    private final int dimension;

    private short[][] board;

    private final Coordinate blankPos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        dimension = tiles.length;
        board = new short[dimension][dimension];
        blankPos = new Coordinate();
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                board[i][j] = (short) tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankPos.x = i;
                    blankPos.y = j;
                }
            }
    }

    // string representation of this board
    public String toString() {
        String rs = "";
        rs += dimension() + "\n";
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++)
                rs += board[i][j] + " ";
            rs += "\n";
        }
        return rs;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int dist = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (board[i][j] != 0 && board[i][j] != i * dimension() + j + 1)
                    dist++;
        return dist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                int u = (board[i][j] - 1) / dimension();
                int v = (board[i][j] - 1) % dimension();
                if (board[i][j] != 0)
                    dist += Math.abs(i - u) + Math.abs(j - v);
            }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;
        Board other = (Board) y;
        if (dimension() != other.dimension)
            return false;
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++)
                if (this.board[i][j] != other.board[i][j])
                    return false;
        return true;
    }

    private int[][] copyBoard() {
        int[][] boardCopy = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                boardCopy[i][j] = board[i][j];
        return boardCopy;
    }

    private void swap(int u1, int v1, int u2, int v2) {
        int temp = board[u1][v1];
        board[u1][v1] = board[u2][v2];
        board[u2][v2] = (short) temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>();
        for (int direction = 0; direction < 4; direction++) {
            int u = blankPos.x + dX[direction];
            int v = blankPos.y + dY[direction];
            if (u < 0 || u >= dimension() || v < 0 || v >= dimension())
                continue;
            Board tempBoard = new Board(copyBoard());
            tempBoard.swap(blankPos.x, blankPos.y, u, v);
            tempBoard.blankPos.x = u;
            tempBoard.blankPos.y = v;
            boards.add(tempBoard);
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(copyBoard());
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension() - 1; j++)
                if (board[i][j] != 0 && board[i][j + 1] != 0) {
                    twinBoard.swap(i, j, i, j + 1);
                    return twinBoard;
                }
        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int n = 3;
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = i * n + j + 1;
        tiles[n - 1][n - 1] = 0;
        Board board = new Board(tiles);
        System.out.println("Is goal: " + board.isGoal());
        System.out.println("Check neighbors");
        for (Board tempBoard : board.neighbors())
            System.out.println(tempBoard.toString());
    }
}
