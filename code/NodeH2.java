package code;

import java.util.Comparator;

public class NodeH2 implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o1.h2()-o2.h2();
    }
}
