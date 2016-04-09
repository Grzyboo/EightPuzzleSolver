package Base.solver;

import java.util.LinkedList;

public class MovementNode implements Comparable<MovementNode> {
    private int[] state = new int[Solver.BOARD_SIZE];
    private int moves; // gScore
    private int score; // hScore
    private Movement blockedMovement;

    LinkedList<Movement> movesList;

    MovementNode(int[] state, int gScore, int hScore, LinkedList<Movement> movesHistory, Movement directionToBlock) {
        System.arraycopy(state, 0, this.state, 0, Solver.BOARD_SIZE);
        moves = gScore;
        score = hScore;

        movesList = movesHistory;

        blockedMovement = directionToBlock;
    }

    int getMoves() {
        return moves;
    }

    int getScore() {
        return moves + score;
    }

    int[] getState() {
        return state;
    }

    int getZero() {
        for(int i=0; i<Solver.BOARD_SIZE; ++i) {
            if(state[i] == 0)
                return i;
        }

        return -1;
    }

    Movement getBlockedMovement() {
        return blockedMovement;
    }

    LinkedList<Movement> getMovesList() {
        return movesList;
    }

    public boolean equals(MovementNode node) {
        return moves == node.moves;
    }

    public int compareTo(MovementNode node) {
        if(equals(node))
            return 0;
        else if(moves > node.moves)
            return 1;

        return -1;
    }
}
