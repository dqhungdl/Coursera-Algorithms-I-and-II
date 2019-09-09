package puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.Scanner;

public class Solver {

    private class Node implements Comparable<Node> {

        private final Board board;

        private final int moves;

        private final Node prev;

        private final int priority;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(Node o) {
            return this.priority - o.priority;
        }
    }

    private boolean isSolvable;

    private int numMoves;

    private final LinkedList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        Node currentNode = null, twinNode = null;
        MinPQ<Node> minPQ = new MinPQ<>(), twinPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));
        twinPQ.insert(new Node(initial.twin(), 0, null));
        solution = new LinkedList<>();
        while (!minPQ.isEmpty()) {
            // delete node with minimum priority
            currentNode = minPQ.delMin();
            twinNode = twinPQ.delMin();
            // check equal to needed board
            if (currentNode.board.isGoal() || twinNode.board.isGoal())
                break;
            // bfs to neighbors
            for (Board board : currentNode.board.neighbors())
                if (currentNode.prev == null || !board.equals(currentNode.prev.board))
                    minPQ.insert(new Node(board, currentNode.moves + 1, currentNode));
            for (Board board : twinNode.board.neighbors())
                if (twinNode.prev == null || !board.equals(twinNode.prev.board))
                    twinPQ.insert(new Node(board, twinNode.moves + 1, twinNode));
        }
        if (currentNode.board.isGoal()) {
            isSolvable = true;
            numMoves = currentNode.moves;
            while (currentNode != null) {
                solution.addFirst(currentNode.board);
                currentNode = currentNode.prev;
            }
        }
        if (twinNode.board.isGoal()) {
            isSolvable = false;
            numMoves = -1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return numMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable())
            return solution;
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = scanner.nextInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }
}