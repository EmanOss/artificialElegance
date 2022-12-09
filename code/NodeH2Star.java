package code;

import java.util.Comparator;

public class NodeH2Star implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return (o1.h2()+o1.getDeaths())-(o2.h2()+o2.getDeaths());
    }
}
