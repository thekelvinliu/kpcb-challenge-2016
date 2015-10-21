package com.thekelvinliu.KPCBChallenge;

/**
 * A generic (homogeneous) fixed-size hash map.
 *
 * Uses a one-dimensional array to hold the nodes of a binary search tree. Nodes
 * hold the hashed value of a String as keys. All values held by this fixed-size
 * hash map must be of type T.
 *
 * TODO: add time/space complexity stuff
 *
 * @param       <T>     the type of value that will be handled by this hash map.
 */
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
     * The internal structure of this fixed-size hash map.
     *
     * For an item with index i in the array,
     * parent has index:        (i - 1)/2
     * left child has index:    2*i + 1
     * right child has index:   2*i +2
     */
    private Node[] tree;
    /**
     * The maximum number of items this fixed-size hash map can hold.
     */
    private int size;
    /**
     * The current number of items in this fixed-size hash map.
     */
    public int items;

    //CONSTRUCTOR
    /**
     * Creates an instance of a fixed-size hash map.
     *
     * Pre-allocates nodes based on size arguement.
     * @param       size    the max size of this hash map
     */
    public FixedSizeHashMap(int size) {
        tree = new Node[size];
        for (int i = 0; i < size; i++) tree[i] = new Node<T>(i, size);
        this.size = size;
        this.items = 0;
    }

    //METHODS
    /**
     * Naive way of setting stuff for now...
     */
    public boolean set(String key, T value) {
        //impose fixed size constraint
        if (this.items < this.size) {
            //ensure that key is not already being used
            int h = key.hashCode();
            int node_index = this.find(h);
            //if it is, do not allow setting
            if (h == this.tree[node_index].key) return false;
            //otherwise, go for it
            else {
                Node<T> n = this.tree[this.items++];
                n.key = h;
                n.value = value;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     *
     */
    public T get(String key) {
        int node_index = this.find(key.hashCode());
        if (node_index != -1) return (T) this.tree[node_index];
        else return null;
    }

    /**
     *
     */
    // public T delete(String key) {}

    /**
     * Returns the index of the node containing the given key.
     *
     * Does a binary search on the tree for the hashed value of key. If a match
     * is found, the index of the matching node is returned. If the whole tree
     * is traversed without a match, -1 is returned.
     * @param       hashed_key  the hashed value of a string key
     * @return      the index of the matching node
     */
    private int find(int hashed_key) {
        int node_index = 0;
        while (node_index != -1) {
            //stop searching if theres a match
            if (hashed_key == this.tree[node_index].key) {
                break;
            } else if (hashed_key < this.tree[node_index].key) {
                node_index = this.tree[node_index].left;
            } else {
                node_index = this.tree[node_index].right;
            }
        }
        //either the value of a matching node or -1
        return node_index;
    }

    /**
     * Swaps the the keys and values of the nodes at index first and second
     * @param       first   index of first node to swap
     * @param       second  index of second node to swap
     */
    private void swap(int first, int second) {
        int tKey = tree[first].key;
        T tValue = (T) tree[first].value;
        tree[first].key = tree[second].key;
        tree[first].value = tree[second].value;
        tree[second].key = tKey;
        tree[second].value = tValue;
    }

    /**
     * Returns the load (ratio of items to size) of this fixed-size hash map.
     * @return      the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.items/this.size;
    }

    /**
     * Returns a string representation of this fixed-size hash map
     * @return      string representation of this fixed-size hash map
     */
    public String toString() {
        String retval = "FSHM:";
        for (int i = 0; i < this.size; i++) {
            retval += (" " + i + "[" + this.tree[i].key + ":");
            if (this.tree[i].value == null) {
                retval += ("NULL]");
            } else {
                retval += (this.tree[i].value.toString() + "]");
            }
        }
        return retval;
    }
}
