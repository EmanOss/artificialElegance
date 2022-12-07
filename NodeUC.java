import java.util.Comparator;

public class NodeUC  implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o1.getDeaths()==o2.getDeaths()?(o1.getBlackBoxesDamaged()-o2.getBlackBoxesDamaged()): (o1.getDeaths()-o2.getDeaths());
    }
}