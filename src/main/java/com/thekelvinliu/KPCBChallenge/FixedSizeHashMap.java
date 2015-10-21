package com.thekelvinliu.KPCBChallenge;

/**
 * A generic (homogeneous) fixed-size hash map.
 *
 * Creates an AVL tree to maintain a self-balancing binary search tree of nodes.
 * Nodes hold the hashed value of a String as keys and objects of type T as
 * values. An AVL tree is used to achieve O(log n) set, get, and delete time
 * complexity. If more information was given about the intended use for this
 * fixed-size hash map, a different type of self-balancing tree could be used.
 * For example, if just a few values held in the fixed-size hash map were
 * frequently accessed, a splay tree would be preferred.
 * @param       <T>     the type of value that will be handled by this hash map.
 */
public class FixedSizeHashMap<T> {
    //HELPER CLASS
    /**
     * A generic class that represents a node in a binary tree.
     *
     * Each node holds a key and a value, as well as references to a node's
     * parent and left and right children. This class is exclusively available
     * to FixedSizeHashMap. As such, any fields of any node will be accessed
     * and modified directly by FixedSizeHashMap.
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
         * This node's parent.
         */
        public Node<T> parent;
        /**
         * This node's left child.
         */
        public Node<T> left;
        /**
         * This node's right child.
         */
        public Node<T> right;

        // CONSTRUCTOR
        /**
         * Creates a node instance.
         *
         * Does not require key or value upon instantiation, as Nodes will
         * always be pre-allocated upon creation of a FixedSizeHashMap instance.
         * @param       parent       the parent of the newly created node
         */
        public Node(Node<T> parent) {
            this.key = 0;
            this.value = null;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }

        /**
         * Creates a node instance without a parent parameter.
         */
        public Node() {
            this(null);
        }

        //METHODS
        /**
         * Returns index, key, and value information about this node.
         * @return      a string with information about this node.
         */
        public String toString() {
            return "Node with key " + this.key + " and value " + this.value + ".";
        }
    }

    //INSTANCE VARIALBES
    /**
     * The root of the internal tree for this fixed-size hash map.
     */
    private Node<T> tree;
    /**
     * A doubly linked list of nodes not currently active in the tree.
     *
     * The head of the list is the first element of the list. A node's next node
     * is the node's right child. A node's previous node is the node's parent.
     */
    private Node<T> nodeBank;
    /**
     * A cursor used for traversing a tree or list of nodes.
     */
    private Node<T> cursor;
    /**
     * The maximum number of items this fixed-size hash map can hold.
     */
    private int size;
    /**
     * The current number of items in this fixed-size hash map.
     */
    private int items;

    //CONSTRUCTOR
    /**
     * Creates an instance of a fixed-size hash map.
     *
     * Pre-allocates nodes based on size arguement.
     * @param       size    the max size of this hash map
     */
    public FixedSizeHashMap(int size) {
        this.tree = null;
        this.nodeBank = new Node<T>(null);
        this.cursor = this.nodeBank;
        for (int i = 1; i < size; i++) {
            this.cursor.right = new Node<T>(this.cursor);
            this.cursor = this.cursor.right;
        }
        this.size = size;
        this.items = 0;
    }

    //METHODS
    /**
     * Returns a boolean indicating whether key was associated with value.
     *
     * Success of this operation depends on three primary constrains:
     * (1) there must be at least one available node
     * (2) key must not already be used in this fixed-size hash map
     * (3) value must not be null
     * @param       key     the key to be associated
     * @param       value   the value to be associated
     * @return      boolean indicating success (true) or failure (false)
     */
    public boolean set(String key, T value) {
        if (this.items < this.size && value != null) {

            return true;
        } else {
            return false;
        }
    }
    /**
     *
     */
    public T get(String key) {
        return null;
    }
    /**
     *
     */
    public T delete(String key) {
        return null;
    }

    /**
     * Returns the load (ratio of items to size) of this fixed-size hash map.
     * @return      the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.items/this.size;
    }
}
