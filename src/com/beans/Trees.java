package com.beans;

public class Trees {
    public static void main(String... args) {
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
    abstract public String toString();

    abstract public ReferenceAVLTree add(Integer data);

    abstract public ReferenceAVLTree remove(Integer data);

    abstract public Integer get(Integer r);

    abstract public Boolean contains(Integer data);

    Node root;

    class Node {
        private Integer data;
        private Node leftChild;
        private Node rightChild;
        private Integer height;
        private Integer balanceFactor;

        Node(Integer data) {
            this.setData(data);
            this.setHeight(0);
            this.setBalanceFactor(0);
        }

        public Integer getData() {
            return data;
        }

        public void setData(Integer data) {
            this.data = data;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getBalanceFactor() {
            return balanceFactor;
        }

        public void setBalanceFactor(Integer balanceFactor) {
            this.balanceFactor = balanceFactor;
        }
    }
}

//---

class ReferenceAVLTree extends AVLTreeTemplate {
    public String toString() {
        StringBuilder b = new StringBuilder();
        toStringInner(root, b);
        return b.toString();
    }

    private void toStringInner(Node n, StringBuilder b) {
        if (n == null)
            return;

        b.append(n.getData() + ", ");

        if (n.getLeftChild() == null)
            b.append("X, ");
        else
            toStringInner(n.getLeftChild(), b);

        if (n.getRightChild() == null)
            b.append("X, ");
        else
            toStringInner(n.getRightChild(), b);

        return;
    }

    public ReferenceAVLTree add(Integer data) {
        root = add(data, root);
        return this;
    }

    Node add(Integer data, Node n) {
        if (n == null)
            return new Node(data);

        if (data < n.getData())
            n.setLeftChild(add(data, n.getLeftChild()));
        else if (data > n.getData())
            n.setRightChild(add(data, n.getRightChild()));

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

        if (data < n.getData())
            n.setLeftChild(remove(data, n.getLeftChild()));
        else if (data > n.getData())
            n.setRightChild(remove(data, n.getRightChild()));
        else if (n.getLeftChild() == null)
            return n.getRightChild();
        else if (n.getRightChild() == null)
            return n.getLeftChild();
        else {
            Node next = n.getRightChild();
            while (next.getLeftChild() != null)
                next = next.getLeftChild();
            n.setData(next.getData());
            n.setRightChild(remove(next.getData(), n.getRightChild()));
        }

        update(n);
        return balance(n);
    }

    public Boolean contains(Integer data) {
        return contains(data, root);
    }

    private boolean contains(Integer data, Node n) {
        if (n == null)
            return false;
        if (data < n.getData())
            return contains(data, n.getLeftChild());
        if (data > n.getData())
            return contains(data, n.getRightChild());
        else
            return true;
    }

    public Integer get(Integer rank) {
        return get(root, rank, new int[1]);
    }

    private Integer get(Node n, Integer rank, int[] counter) {
        if (n == null)
            return null;

        Integer r = get(n.getLeftChild(), rank, counter);

        if (r != null)
            return r;

        counter[0]++;
        if (counter[0] == rank)
            return n.getData();

        return get(n.getRightChild(), rank, counter);
    }

    void update(Node n) {
        int lh = -1, rh = -1;

        if (n.getLeftChild() != null)
            lh = n.getLeftChild().getHeight();

        if (n.getRightChild() != null)
            rh = n.getRightChild().getHeight();

        n.setHeight(1 + Math.max(lh, rh));
        n.setBalanceFactor(rh - lh);
    }

    Node balance(Node n) {
        if (n.getBalanceFactor() == -2)
            if (n.getLeftChild().getBalanceFactor() <= 0) {
                return rightRotate(n);
            } else {
                n.setLeftChild(leftRotate(n.getLeftChild()));
                return rightRotate(n);
            }
        else if (n.getBalanceFactor() == +2)
            if (n.getRightChild().getBalanceFactor() >= 0) // == 1
                return leftRotate(n);
            else {
                n.setRightChild(rightRotate(n.getRightChild()));
                return leftRotate(n);
            }
        else
            return n;
    }

    private Node rightRotate(Node n) {
        Node pivot = n.getLeftChild();
        n.setLeftChild(pivot.getRightChild());
        pivot.setRightChild(n);
        update(n);
        update(pivot);
        return pivot;
    }

    private Node leftRotate(Node n) {
        Node pivot = n.getRightChild();
        n.setRightChild(pivot.getLeftChild());
        pivot.setLeftChild(n);
        update(n);
        update(pivot);
        return pivot;
    }
}