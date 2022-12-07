import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class NodeH1 implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o1.h1()-o2.h1();
    }
}
