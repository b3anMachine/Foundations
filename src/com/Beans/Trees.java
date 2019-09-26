package com.Beans;

public class Trees {
    public static void main(String[] args) {
        ReferenceAVLTree trunk = new PracticeAVLTree();

        System.out.println("Add: ");
        System.out.println(trunk.toString().equals(""));
        trunk.add(10);
        trunk.add(8);
        trunk.add(6);
        System.out.println(trunk.toString().equals("8, 6, X, X, 10, X, X, "));
        trunk.add(12);
        trunk.add(14);
        System.out.println(trunk.toString().equals("8, 6, X, X, 12, 10, X, X, 14, X, X, "));
        trunk.add(4);
        trunk.add(5);
        System.out.println(trunk.toString().equals("8, 5, 4, X, X, 6, X, X, 12, 10, X, X, 14, X, X, "));
        trunk.add(16);
        trunk.add(15);
        System.out.println(trunk.toString().equals("8, 5, 4, X, X, 6, X, X, 12, 10, X, X, 15, 14, X, X, 16, X, X, "));
        System.out.println();

        System.out.println("Remove: ");
        trunk.remove(8);
        System.out.println(trunk.toString().equals("10, 5, 4, X, X, 6, X, X, 15, 12, X, 14, X, X, 16, X, X, "));
        trunk.remove(6);
        System.out.println(trunk.toString().equals("10, 5, 4, X, X, X, 15, 12, X, 14, X, X, 16, X, X, "));
        trunk.remove(12);
        System.out.println(trunk.toString().equals("10, 5, 4, X, X, X, 15, 14, X, X, 16, X, X, "));
        trunk.remove(5);
        System.out.println(trunk.toString().equals("10, 4, X, X, 15, 14, X, X, 16, X, X, "));
        System.out.println();

        System.out.println("Get: ");
        System.out.println(trunk.get(-1) == null);
        System.out.println(trunk.get(1).equals(4));
        System.out.println(trunk.get(3).equals(14));
        System.out.println(trunk.get(5).equals(16));
        System.out.println(trunk.get(10) == null);
        System.out.println();

        System.out.println("Contains: ");
        System.out.println(trunk.contains(14));
        System.out.println(trunk.contains(16));
        System.out.println(!trunk.contains(12));
    }
}

class PracticeAVLTree extends ReferenceAVLTree {

}

abstract class AVLTreeTemplate {
    abstract public String serialize();

    abstract public ReferenceAVLTree add(Integer data);

    abstract public ReferenceAVLTree remove(Integer data);

    abstract public Integer get(Integer r);

    abstract public boolean contains(Integer data);

    public String toString() {
        return serialize();
    }

    Node root;

    class Node {
        Integer data;
        Node leftChild;
        Node rightChild;

        Integer height;
        Integer balanceFactor;

        Node(Integer data) {
            this.data = data;
            this.height = 0;
            this.balanceFactor = 0;
        }
    }
}

//---

class ReferenceAVLTree extends AVLTreeTemplate {
    public String serialize() {
        StringBuilder b = new StringBuilder();
        serialize(root, b);
        return b.toString();
    }

    private void serialize(Node n, StringBuilder b) {
        if (n == null)
            return;

        b.append(n.data + ", ");

        if (n.leftChild == null)
            b.append("X, ");
        else
            serialize(n.leftChild, b);

        if (n.rightChild == null)
            b.append("X, ");
        else
            serialize(n.rightChild, b);

        return;
    }

    public ReferenceAVLTree add(Integer data) {
        root = add(data, root);
        return this;
    }

    Node add(Integer data, Node n) {
        if (n == null)
            return new Node(data);

        if (data < n.data)
            n.leftChild = add(data, n.leftChild);
        else if (data > n.data)
            n.rightChild = add(data, n.rightChild);

        update(n);
        return balance(n);
    }

    public ReferenceAVLTree remove(Integer data) {
        root = remove(data, root);
        return this;
    }

    private Node remove(Integer data, Node n) {
        if (n == null)
            return null;

        if (data < n.data)
            n.leftChild = remove(data, n.leftChild);
        else if (data > n.data)
            n.rightChild = remove(data, n.rightChild);
        else if (n.leftChild == null)
            return n.rightChild;
        else if (n.rightChild == null)
            return n.leftChild;
        else {
            Node next = n.rightChild;
            while (next.leftChild != null)
                next = next.leftChild;
            n.data = next.data;
            n.rightChild = remove(next.data, n.rightChild);
        }

        update(n);
        return balance(n);
    }

    public boolean contains(Integer data) {
        return contains(data, root);
    }

    private boolean contains(Integer data, Node n) {
        if (n == null)
            return false;
        if (data < n.data)
            return contains(data, n.leftChild);
        if (data > n.data)
            return contains(data, n.rightChild);
        else
            return true;
    }

    public Integer get(Integer rank) {
        return get(root, rank, new int[1]);
    }

    private Integer get(Node n, Integer rank, int[] counter) {
        if (n == null)
            return null;

        Integer r = get(n.leftChild, rank, counter);

        if (r != null)
            return r;

        counter[0]++;
        if (counter[0] == rank)
            return n.data;

        return get(n.rightChild, rank, counter);
    }

    void update(Node n) {
        int lh = -1, rh = -1;

        if (n.leftChild != null)
            lh = n.leftChild.height;

        if (n.rightChild != null)
            rh = n.rightChild.height;

        n.height = 1 + Math.max(lh, rh);
        n.balanceFactor = rh - lh;
    }

    Node balance(Node n) {
        if (n.balanceFactor == -2)
            if (n.leftChild.balanceFactor <= 0) {
                return rightRotate(n);
            } else {
                n.leftChild = leftRotate(n.leftChild);
                return rightRotate(n);
            }
        else if (n.balanceFactor == +2)
            if (n.rightChild.balanceFactor >= 0) // == 1
                return leftRotate(n);
            else {
                n.rightChild = rightRotate(n.rightChild);
                return leftRotate(n);
            }
        else
            return n;
    }

    private Node rightRotate(Node n) {
        Node pivot = n.leftChild;
        n.leftChild = pivot.rightChild;
        pivot.rightChild = n;
        update(n);
        update(pivot);
        return pivot;
    }

    private Node leftRotate(Node n) {
        Node pivot = n.rightChild;
        n.rightChild = pivot.leftChild;
        pivot.leftChild = n;
        update(n);
        update(pivot);
        return pivot;
    }
}