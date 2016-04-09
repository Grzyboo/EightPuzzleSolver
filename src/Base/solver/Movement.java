package Base.solver;

public enum Movement {
    UP,
    RIGHT,
    DOWN,
    LEFT,

    NONE;

    static Movement[] values = Movement.values();

    static Movement getValueFromInt(int x) {
        return values[x];
    }

    static Movement getOppositeDirection(Movement direction) {
        switch(direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
        }

        return NONE;
    }
}
