package Base.solver;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {
    int[] startBoard;
    int[] goalBoard;

    static int BOARD_SIZE = 9;

    public Solver(int[] start, int[] goal) {
        setBoards(start, goal);
    }

    public final void setBoards(int[] start, int[] goal) {
        startBoard = start;
        goalBoard = goal;
    }

    private boolean matchesGoal(int[] board) {
        for(int i=0; i<BOARD_SIZE; ++i) {
            if(board[i] != goalBoard[i])
                return false;
        }
        return true;
    }

    private static Movement[][] legalMovements = {
            {Movement.RIGHT, Movement.DOWN},
            {Movement.RIGHT, Movement.DOWN, Movement.LEFT},
            {Movement.DOWN, Movement.LEFT},
            {Movement.UP, Movement.RIGHT, Movement.DOWN},
            {Movement.UP, Movement.RIGHT, Movement.DOWN, Movement.LEFT},
            {Movement.UP, Movement.DOWN, Movement.LEFT},
            {Movement.UP, Movement.RIGHT},
            {Movement.UP, Movement.RIGHT, Movement.LEFT},
            {Movement.UP, Movement.LEFT}
    };

    private boolean canMove(int zeroPos, Movement direction) {
        for(int i=0; i<legalMovements[zeroPos].length; ++i) {
            if(legalMovements[zeroPos][i] == direction)
                return true;
        }

        return false;
    }

    private void move(int[] state, Movement direction) {
        int zeroPos = 0;
        for(int i=0; i<BOARD_SIZE; ++i) {
            if(state[i] == 0) {
                zeroPos = i;
                break;
            }
        }

        int newZeroPos = zeroPos;

        switch(direction) {
            case UP: {
                newZeroPos -= 3;
                break;
            }
            case DOWN: {
                newZeroPos += 3;
                break;
            }
            case RIGHT: {
                ++newZeroPos;
                break;
            }
            case LEFT: {
                --newZeroPos;
                break;
            }
        }

        state[zeroPos] = state[newZeroPos];
        state[newZeroPos] = 0;
    }

    private int getManhattanScore(int[] state) {
        int sum = 0;

        for(int i=0; i<BOARD_SIZE; ++i) {

            if(state[i] == 0)
                continue;

            for(int j=0; j<BOARD_SIZE; ++j) {

                if(state[i] != goalBoard[j])
                    continue;

                int xA = i % 3;
                int yA = i / 3;
                int xB = j % 3;
                int yB = j / 3;

                int difference = Math.abs(xA - xB) + Math.abs(yA - yB);
                sum += difference;
            }
        }

        return sum;
    }

    public LinkedList<Movement> getSolution() {
        PriorityQueue<MovementNode> openList = new PriorityQueue<>();
        LinkedList<MovementNode> closedList = new LinkedList<>();

        openList.add(new MovementNode(startBoard, 0, 0, new LinkedList<>(), Movement.NONE));

        while(!openList.isEmpty()) {
            MovementNode lowestScoreNode = openList.poll();
            if(matchesGoal(lowestScoreNode.getState())) {
                return new LinkedList<>(lowestScoreNode.getMovesList());
            }

            closedList.add(lowestScoreNode);

            for(int direction=0; direction<4; ++direction) {
                Movement directionValue = Movement.getValueFromInt(direction);

                if(!canMove(lowestScoreNode.getZero(), directionValue) || lowestScoreNode.getBlockedMovement() == directionValue)
                    continue;

                int[] movedState = new int[BOARD_SIZE];
                System.arraycopy(lowestScoreNode.getState(), 0, movedState, 0, BOARD_SIZE);

                move(movedState, directionValue);

                int gScore = lowestScoreNode.getMoves() + 1;
                int hScore = getManhattanScore(movedState);
                LinkedList<Movement> movementHistory = new LinkedList<>(lowestScoreNode.getMovesList());
                movementHistory.addLast(directionValue);
                MovementNode neighbourNode = new MovementNode(movedState, gScore, hScore, movementHistory, Movement.getOppositeDirection(directionValue));

                if(closedList.contains(neighbourNode))
                    continue;

                if(!openList.contains(neighbourNode)) {
                    openList.add(neighbourNode);
                }
            }
        }

        return null;
    }

    public String getSolutionString() {
        LinkedList<Movement> listOfMoves = getSolution();

        StringBuilder result = new StringBuilder();
        for(Movement move : listOfMoves) {
            switch(move) {
                case UP:
                    result.append("U");
                    break;
                case DOWN:
                    result.append("D");
                    break;
                case RIGHT:
                    result.append("R");
                    break;
                case LEFT:
                    result.append("L");
                    break;
            }
        }

        return result.toString();
    }
}
