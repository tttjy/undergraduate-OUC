package status3;

import java.util.ArrayList;
import java.util.HashSet;
public class RoadList extends ArrayList<Road> {
    //�ù�ϣ����Ӻ��Ƴ���Ϣ������·��
    public void addRoad(Road road){
        add(road);
    }
    public void removeRoad(Road road){
        remove(road);
    }
}
