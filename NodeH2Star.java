import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class NodeH2Star implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return (o1.h2()+o1.getBlackBoxesDamaged())-(o2.h2()+o2.getBlackBoxesDamaged());
    }
}
