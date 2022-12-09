package code;

import java.util.Comparator;

public class NodeH1Star  implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return (o1.h1()+o1.getDeaths())-(o2.h1()+o2.getDeaths());
    }
}
