package killer;

import java.util.HashSet;

public class RoadList extends HashSet<Road> {
    public void addRoad(Road road){
        add(road);
    }
    public void removeRoad(Road road){
        remove(road);
    }
}
