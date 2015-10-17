package com.thekelvinliu.KPCBChallenge;

public class FixedSizeHashMap<T> {
    //HELPER CLASS
    /**
     * A generic structure class to box a key, a value, and array indices.
     *
     * This class is exclusively available to FixedSizeHashMap. As such, the key
     * and value fields will be accessed and modified directly by
     * FixedSizeHashMap.
     * @param       <T>     the type of the value to be held by this node.
     */
    public class Node<T> {
        // INSTANCE VARIABLES
        /**
         * This node's key.
         */
        public int key;
        /**
         * This node's value.
         */
        public T value;
        /**
         * The array index of this node.
         */
        public final int index;
        /**
         * The array index of this node's parent.
         */
        public final int parent;
        /**
         * The array index of this node's left child.
         */
        public final int left;
        /**
         * The array index of this node's right child.
         */
        public final int right;

        // CONSTRUCTOR
        /**
         * Creates a node instance.
         *
         * Does not require key or value upon instantiation, as Nodes will
         * always be pre-allocated upon creation of a FixedSizeHashMap instance.
         * @param       i       this node's index
         * @param       maxSize the size of the FixedSizeHashMap
         */
        public Node(int i, int maxSize) {
            this.index = i;
            this.parent = (i > 0) ? (i - 1)/2 : -1;
            int tmp = 2*i + 1;
            this.left = (tmp < maxSize) ? tmp : -1;
            tmp++;
            this.right = (tmp < maxSize) ? tmp : -1;
        }

        //METHODS
        /**
         * Returns index, key, and value information about this node.
         * @return      a string with information about this node.
         */
        public String toString() {
            return "Node at " + this.index + " with key " + this.key
                   + " and value " + this.value + ".";
        }

        // THESE ARE UNUSED
        public void setKey(int k) {
            this.key = k;
        }

        public void setValue(T v) {
            this.value = v;
        }
    }

    //INSTANCE VARIALBES
    /**
     * The internal structure of this fixed-size hash map is a binary tree
     * stored as a one-dimensional array. For an item with index i in the array,
     * parent has index:        (i - 1)/2
     * left child has index:    2*i + 1
     * right child has index:   2*i +2
     */
    private Node<T>[] tree;
    /**
     * The maximum number of items this fixed-size hash map can hold.
     */
    private int size;
    /**
     * The current number of items in this fixed-size hash map.
     */
    private int items;

    //CONSTRUCTOR

    //METHODS
}
