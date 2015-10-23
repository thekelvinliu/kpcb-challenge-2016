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
    //HELPER CLASSES
    /**
     * A generic class that represents a node in a binary tree.
     *
     * Each node holds a key and a value, as well as references to a node's
     * parent and left and right children. Because this class is not accessible
     * to the outside world, fields will be accessed and modified directly by
     * FixedSizeHashMap or AVLTree for convenience.
     * @param       <T>     the type of the value to be held by this node.
     */
    class Node<T> {
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
         * The height of this node inside a tree
         */
        public int height;
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
            this.height = 0;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }

        //METHODS
        /**
         * Returns the height of this node's left child.
         * @return      the neight of this node's left child
         */
        public int lHeight() {
            return (this.left != null) ? this.left.height : 0;
        }
        /**
         * Returns the height of this node's right child.
         * @return      the neight of this node's right child
         */
        public int rHeight() {
            return (this.right != null) ? this.right.height : 0;
        }
        /**
         * Returns index, key, and value information about this node.
         * @return      a string with information about this node.
         */
        public String toString() {
            return "Node with key " + this.key + " and value " + this.value + ".";
        }
    }
    /**
     * A binary search tree made of generic nodes.
     *
     * This binary search tree is self-balacing and uses and AVL tree design. It
     * is used as the internal data structure of the fixed-size hash map.
     */
    class AVLTree<T> {
        //INSTANCE VARIABLES
        /**
         * The root of this tree.
         */
        private Node<T> root;
        //CONSTRUCTOR
        /**
         * Creates an empty tree.
         */
        public AVLTree() {
            this.root = null;
        }
        //METHODS
    }

    //INSTANCE VARIALBES
    /**
     * The root of the internal tree for this fixed-size hash map.
     */
    private AVLTree<T> tree;

    /**
     * A stack-like doubly linked list used to store unsued nodes.
     *
     * The node class is recycled to accomplish this doubly linked list. A
     * node's next node is the node's right child. A node's previous node is the
     * node's parent.
     */
    private Node<T> nodeBank;
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
        this.tree = new AVLTree<T>();
        this.nodeBank = new Node<T>(null);
        Node<T> cur = this.nodeBank;
        for (int i = 1; i < size; i++) {
            cur.right = new Node<T>(cur);
            cur = cur.right;
        }
        this.size = size;
        this.items = 0;
    }

    //USER METHODS (PUBLIC)
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
     * Returns the value corresponding to a given key.
     * @param       key     the key used to identify the returned value
     * @return      the value associated with key or null
     */
    public T get(String key) {
        return null;
    }
    /**
     * Returns the value corresponding to a given key after deleting the entry.
     * @param       key     the key used to identify the node to delete
     * @return      the value of the node deleted or null
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

    //NODEBANK UTILITY METHODS (PRIVATE)
    /**
     * Returns (pops) a node from this.nodeBank.
     * @return      a node
     */
    private Node<T> getNode() {
        Node<T> retNode = this.nodeBank;
        this.nodeBank = retNode.right;
        this.nodeBank.parent = retNode.parent;
        retNode.right = null;
        return retNode;
    }
    /**
     * Puts (pushes) a node into this.nodeBank.
     * @param       node    the node to be put into this.nodeBank
     */
    private void putNode(Node<T> node) {
        node.parent = this.nodeBank.parent;
        node.right = this.nodeBank;
        this.nodeBank.parent = node;
        this.nodeBank = node;
    }

    //TREE UTILITY METHODS (PRIVATE)
    /**
     * Updates the height of the given node based on its children.
     * @param       n       the node to be updated
     */
    private void updateHeight (Node<T> n) {
        n.height = (n.lHeight() > n.rHeight()) ? n.lHeight() : n.rHeight();
        n.height++;
    }
    /**
     * Returns whether or not the subtree rooted by the given node is balanced.
     * @param       n       the node to check
     * @return      boolean indicating balanced (true) or not balanced (false)
     */
    private boolean isBalanced(Node<T> n) {
        int balFactor = n.lHeight() - n.rHeight();
        return (-1 <= balFactor && balFactor <= 1) ? true : false;
    }
}
