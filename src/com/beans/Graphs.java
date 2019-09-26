package com.beans;

import java.util.*;

public class Graphs {
    public static void main(String... args) {
        GraphsReference gs = new GraphsPractice();

        gs.add('A', new Character[]{'B', 'J', 'G'}, new Integer[]{1, 3, 6});
        gs.add('B', 'D', 1);
        gs.add('J', new Character[]{'D', 'G'}, new Integer[]{2, 2});
        gs.add('G', new Character[]{'F', 'E'}, new Integer[]{1, 2});
        gs.add('D', 'H', 9);
        gs.add('H', new Character[]{'C', 'I', 'F'}, new Integer[]{3, 1, 5});
        gs.add('F', new Character[]{'I', 'E'}, new Integer[]{2, 8});
        gs.add('E', 'I', 1);
        gs.add('Z');

        System.out.println("depthFirstTraversal: ");
        System.out.println(gs.depthFirstTraversal('Z').toString().equals("[Z]"));
        System.out.println(gs.depthFirstTraversal('A').toString().equals("[A, J, D, H, I, E, F, C, G, B]"));
        System.out.println();

        System.out.println("breadthFirstTraversal: ");
        System.out.println(gs.breadthFirstTraversal('Z').toString().equals("[Z]"));
        System.out.println(gs.breadthFirstTraversal('A').toString().equals("[A, B, G, J, D, E, F, H, I, C]"));
        System.out.println();

        System.out.println("Dijkstra: ");
        System.out.println(gs.dijkstraShortestPath('A', 'Z').toString().equals("[]"));
        System.out.println(gs.dijkstraShortestPath('A', 'C').toString().equals("[A, J, G, F, I, H, C]"));
    }
}

class GraphsPractice extends GraphsReference {

}

abstract class GraphsTemplate {
    abstract List<Character> depthFirstTraversal(Character c);

    abstract List<Character> breadthFirstTraversal(Character c);

    abstract List<Character> dijkstraShortestPath(Character a, Character b);

    Map<Character, Set<Edge>> graph = new HashMap<>();

    //Note: this class has a natural ordering that is inconsistent with equals.
    class Edge implements Comparable<Edge> {
        Character dest;
        Integer dist;

        Edge(Character dest, Integer dist) {
            this.dest = dest;
            this.dist = dist;
        }

        public int compareTo(Edge other) {
            return Integer.compare(dest, other.dest);
        }

        public String toString() {
            return "(" + dest + ", " + dist + ")";
        }

    }

    public void add(Character base, Character[] dests, Integer[] dists) {
        if (dests.length != dists.length)
            return;

        for (int i = 0; i < dests.length; i++)
            add(base, dests[i], dists[i]);
    }

    public void add(Character base, Character dest, Integer dist) {
        add(base);
        add(dest);

        graph.get(base).add(new Edge(dest, dist));
        graph.get(dest).add(new Edge(base, dist));
    }

    public void add(Character node) {
        if (graph.containsKey(node))
            return;
        graph.put(node, new TreeSet<>());
    }
}

//---

class GraphsReference extends GraphsTemplate {
    List<Character> depthFirstTraversal(Character c) {
        List<Character> order = new ArrayList<>();

        Set<Character> visited = new HashSet<>();
        visited.add(c);

        ArrayDeque<Character> stack = new ArrayDeque<>();
        stack.push(c);

        while (!stack.isEmpty()) {
            Character cur = stack.pop();

            order.add(cur);

            for (Edge edge : graph.get(cur))
                if (visited.add(edge.dest))
                    stack.push(edge.dest);
        }

        return order;
    }

    List<Character> breadthFirstTraversal(Character c) {
        List<Character> order = new ArrayList<>();

        Set<Character> visited = new HashSet<>();
        visited.add(c);

        ArrayDeque<Character> queue = new ArrayDeque<>();
        queue.offer(c);

        while (!queue.isEmpty()) {
            Character cur = queue.poll();

            order.add(cur);

            for (Edge edge : graph.get(cur))
                if (visited.add(edge.dest))
                    queue.offer(edge.dest);
        }

        return order;
    }

    public List<Character> dijkstraShortestPath(Character a, Character b) {
        List<Character> path = new ArrayList<>();

        Map<Character, Integer> distances = new HashMap<>();
        for (Character c : graph.keySet())
            distances.put(c, Integer.MAX_VALUE);
        distances.put(a, 0);

        Map<Character, Character> backSteps = new HashMap<>();
        for (Character c : graph.keySet())
            backSteps.put(c, null);

        PriorityQueue<Edge> priQueue = new PriorityQueue<>(new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                return Integer.compare(e1.dist, e2.dist);
            }
        });
        priQueue.offer(new Edge(a, 0));

        while (!priQueue.isEmpty() && !priQueue.peek().equals(b)) {
            Edge from = priQueue.poll();

            if (distances.get(from.dest) < from.dist) //Priority queue optimization
                continue;

            for (Edge to : graph.get(from.dest)) {
                Integer oldDist = distances.get(to.dest);
                Integer newDist = distances.get(from.dest) + to.dist;

                if (newDist < oldDist) {
                    distances.put(to.dest, newDist);
                    backSteps.put(to.dest, from.dest);
                    priQueue.add(new Edge(to.dest, newDist));
                }
            }
        }

        if (distances.get(b).equals(Integer.MAX_VALUE))
            return path;

        for (Character step = b; step != null; step = backSteps.get(step))
            path.add(step);

        Collections.reverse(path);

        return path;
    }
}