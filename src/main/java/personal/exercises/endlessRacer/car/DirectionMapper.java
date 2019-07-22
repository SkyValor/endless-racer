package personal.exercises.endlessRacer.car;

public class DirectionMapper {

    public static Direction addDirection(Direction curDirection, Direction nextDirection) {

        if (curDirection.equals(nextDirection)) {
            return curDirection;
        }

        if (curDirection.equals(Direction.NONE)) {
            return nextDirection;
        }

        if (nextDirection.equals(Direction.NONE)) {
            return Direction.NONE;
        }

        //List<Direction> directions = new LinkedList<>(Arrays.asList(Direction.values()));

        switch (curDirection) {

            case UP:
                if (nextDirection.equals(Direction.RIGHT)) {
                    return Direction.UPPER_RIGHT;
                }

                if (nextDirection.equals(Direction.LEFT)) {
                    return Direction.UPPER_LEFT;
                }
                break;

            case RIGHT:
                if (nextDirection.equals(Direction.UP)) {
                    return Direction.UPPER_RIGHT;
                }

                if (nextDirection.equals(Direction.DOWN)) {
                    return Direction.LOWER_RIGHT;
                }
                break;

            case DOWN:
                if (nextDirection.equals(Direction.RIGHT)) {
                    return Direction.LOWER_RIGHT;
                }

                if (nextDirection.equals(Direction.LEFT)) {
                    return Direction.LOWER_LEFT;
                }
                break;

            case LEFT:
                if (nextDirection.equals(Direction.UP)) {
                    return Direction.UPPER_LEFT;
                }

                if (nextDirection.equals(Direction.DOWN)) {
                    return Direction.LOWER_LEFT;
                }
        }

        // TODO: if curDirection is UP and nextDirection is DOWN, what is the final result??
        // TODO: what is the priority?
        return curDirection;
    }
}
