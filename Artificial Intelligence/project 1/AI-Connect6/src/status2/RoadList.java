package status2;

import java.util.HashSet;
public class RoadList extends HashSet<Road> {
    //�ù�ϣ����Ӻ��Ƴ���Ϣ������·��
    public void addRoad(Road road){
        add(road);
    }
    public void removeRoad(Road road){
        remove(road);
    }
}
