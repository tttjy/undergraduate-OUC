package status3;

import java.util.ArrayList;
import java.util.HashSet;
public class RoadList extends ArrayList<Road> {
    //用哈希将添加和移除信息操作到路表
    public void addRoad(Road road){
        add(road);
    }
    public void removeRoad(Road road){
        remove(road);
    }
}
