package com.thekelvinliu.KPCBChallenge;

/**
 * A generic, homogeneous fixed-size hash map.
 *
 * This hash map uses an implicit AVL Tree to maintain a self-balancing binay
 * search tree. Each node of the tree represents an entry in the hash map. The
 * hash map accepts Strings as keys and internally stores the hashed values of
 * the Strings using {@link java.lang.String#hashCode()}. Because this class is
 * implemented with java generics, the mapped values can be of any type, however
 * each individual instance of this hash map may only hold a single type.
 * <p>
 * Each node of this implicit tree is actually stored in a single array. Nodes
 * have integer fields which hold the indices of the left and right children in
 * the this array. This hash map uses modified implementations of standard AVL
 * Tree operations to achieve O(log n) set, get, and delete time complexity.
 * @author      <a href="mailto:kelvin@thekelvinliu.com">Kelvin Liu</a>
 * @version     1.0.0
 * @param       <T>         the type of value that this hash map will hold
 * @see         com.thekelvinliu.KPCBChallenge.FixedSizeHashMap.Node
 */
public class FixedSizeHashMap<T> {
    //HELPER CLASS
    /**
     * A generic class that represents a node in a binary tree.
     *
     * Each node holds a key and a value, as well as indices for the node's
     * left and right children. Because this class is not accessible to the
     * outside world, fields will be accessed and modified directly by
     * FixedSizeHashMap for convenience.
     * @param       <T>     the type of the value to be held by this node
     */
    class Node<T> {
        //INSTANCE VARIABLES
        //the key held by this node
        private int key;
        //the value held by this node
        private T value;
        //the height of this node
        private int height;
        //the index of this node's left child
        private int left;
        //the index of this node's right child
        private int right;

        //CONSTRUCTOR
        /**
         * Creates an empty node.
         *
         * All integer fields are set to -1, which in the context of this whole
         * class, refers to being null. The value field is actually set to null.
         */
        Node() {
            this.key = -1;
            this.value = null;
            this.height = -1;
            this.left = -1;
            this.right = -1;
        }

        //METHODS
        /**
         * Returns a string representation of this node.
         * @return      a string representation of this node
         */
        public String toString() {
            String retval = "(" + this.key + ", ";
            retval += (this.value != null) ? this.value.toString() + ")" : "NULL)";
            return retval;
        }
    }

    //INSTANCE VARIABLES
    //an array of nodes representing this hash map's implicit AVL Tree
    private Node[] tree;
    //the array index of the root
    private int rootInd;
    //the size of this hash map
    private final int size;
    //the number of items currently in this hash map
    private int items;

    //CONSTRUCTOR
    /**
     * Creates an instance of a fixed-size hash map.
     *
     * At this time, all nodes are created and put into the array. Because there
     * are not yet any entries in the hash map, the root index is set to -1,
     * and the number of items is set to 0.
     * @param       size    the fixed-size of this hash map
     */
    public FixedSizeHashMap(int size) {
        this.tree = new Node[size];
        for (int i = 0; i < size; i++) this.tree[i] = new Node();
        this.rootInd = -1;
        this.size = size;
        this.items = 0;
    }

    //USER METHODS (PUBLIC)
    /**
     * Returns a boolean indicating whether key was associated with value.
     *
     * Success of this operation depends on three primary constrains:<p>
     * (1) there must be at least one available node<p>
     * (2) the value must not be null<p>
     * (3) key must not already be used in this fixed-size hash map
     * @param       key     the key to be associated
     * @param       value   the value to be associated
     * @return      boolean indicating success (true) or failure (false)
     */
    public boolean set(String key, T value) {
        if (this.rootInd == -1) {
            System.out.println("wow");
            this.tree[this.items].key = key.hashCode();
            this.tree[this.items].value = value;
            this.rootInd = this.items++;
            return true;
        } else if (this.items < this.size && value != null) {
            this.tree[this.items].key = key.hashCode();
            this.tree[this.items].value = value;
            try {
                //this will throw an IllegalArgumentException if the key is already used
                this.rootInd = this.insert(this.items, this.rootInd);
                this.items++;
                return true;
            } catch (IllegalArgumentException e) {
                //clean up and return false
                this.cleanNode(this.items);
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * Returns the value associated with a given key.
     *
     * This will fail and return null if there are no entries in this hash map,
     * or if the key is not found.
     * @param       key     the key paired with the returned value
     * @return      the value associated with key or null
     */
    public T get(String key) {
        if (this.items > 0 && this.rootInd != -1) {
            int nodeInd = this.find(key.hashCode(), this.rootInd);
            if (nodeInd != -1) {
                return (T) this.tree[nodeInd].value;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    /**
     * Deletes the entry with the given key and returns the associated value.
     *
     * This will fail and return null if there are no entries in this hash map,
     * or if the key is not found.
     * @param       key     the key paired with the returned value
     * @return      the value associated with key or null
     */
    public T delete(String key) {
        if (this.items > 0 && this.rootInd != -1) {
            int nodeInd = this.find(key.hashCode(), this.rootInd);
            if (nodeInd != -1) {
                //TODO: implement node removal
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    /**
     * Returns the load (ratio of items to size) of this fixed-size hash map.
     * @return      the load of this fixed-size hash map
     */
    public float load() {
        return (float)this.items/this.size;
    }
    /**
     * Returns the number of items currently in this hash map.
     * @return      the number of items currently in this hash map
     */
    public int getItemCount() {
        return this.items;
    }
    /**
     * Returns the maximum number of items that this hash map can hold.
     * @return      the maximum number of items that this hash map can hold
     */
    public int getSize() {
        return this.size;
    }

    //TREE METHODS (PRIVATE)
    /**
     * Inserts node at newInd into the subtree rooted by node at startInd.
     *
     * This method will recursively insert a node into the implicit subtree
     * rooted by the node at startInd. This this causes the tree to be
     * unbalanced, tree rotations will be performed in order to re-balance the
     * implicit tree.
     * @param       newInd      the index of the node to be inserted
     * @param       startInd    the index of the subtree root
     * @return      the index of the (new) subtree root
     * @throws      IllegalArgumentException    if the key of the node at newInd
     *                                          is already used in the hash map
     */
    private int insert(int newInd, int startInd) throws IllegalArgumentException {
        int newStartInd = startInd;
        //insert into left subtree
        if (this.tree[newInd].key < this.tree[startInd].key) {
            if (this.tree[startInd].left == -1) {
                this.tree[startInd].left = newInd;
            } else {
                this.tree[startInd].left = this.insert(newInd, this.tree[startInd].left);
                if (this.balanceFactor(startInd) == 2) {
                    int lInd = this.tree[startInd].left;
                    if (lInd != -1 && this.tree[newInd].key < this.tree[lInd].key) {
                        newStartInd = this.rotateCaseLL(startInd);
                    } else {
                        newStartInd = this.rotatecaseLR(startInd);
                    }
                }
            }
        //insert into right subtree
        } else if (this.tree[newInd].key > this.tree[startInd].key) {
            if (this.tree[startInd].right == -1) {
                this.tree[startInd].right = newInd;
            } else {
                this.tree[startInd].right = this.insert(newInd, this.tree[startInd].right);
                if (this.balanceFactor(startInd) == -2) {
                    int rInd = this.tree[startInd].right;
                    if (rInd != -1 && this.tree[newInd].key < this.tree[rInd].key) {
                        newStartInd = this.rotateCaseRL(startInd);
                    } else {
                        newStartInd = this.rotateCaseRR(startInd);
                    }
                }
            }
        //bad key
        } else {
            throw new IllegalArgumentException("Key already used.");
        }
        //update height if necessary
        this.updateHeight(startInd);
        return newStartInd;
    }
    private int find(int key, int startInd) {
        if (startInd == -1) {
            return startInd;
        } else if (key == this.tree[startInd].key) {
            return startInd;
        } else if (key < this.tree[startInd].key) {
            return this.find(key, this.tree[startInd].left);
        } else {
            return this.find(key, this.tree[startInd].right);
        }
    }

    //TREE ROTATIONS
    private int rotateCaseLL(int startInd) {
        int newStartInd = this.tree[startInd].left;
        this.tree[startInd].left = this.tree[newStartInd].right;
        this.tree[newStartInd].right = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    private int rotateCaseRR(int startInd) {
        int newStartInd = this.tree[startInd].right;
        this.tree[startInd].right = this.tree[newStartInd].left;
        this.tree[newStartInd].left = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    private int rotatecaseLR(int startInd) {
        this.tree[startInd].left = this.rotateCaseRR(this.tree[startInd].left);
        return this.rotateCaseLL(startInd);
    }
    private int rotateCaseRL(int startInd) {
        this.tree[startInd].right = this.rotateCaseLL(this.tree[startInd].right);
        return this.rotateCaseRR(startInd);
    }

    //UTILITIES
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }
    //return true if node at index i is a leaf, false otherwise
    private boolean isLeaf(int i) {
        return (this.tree[i].left == -1 && this.tree[i].right == -1);
    }
    private void cleanNode(int i) {
        this.tree[i].key = 0;
        this.tree[i].value = null;
        this.tree[i].height = 0;
        this.tree[i].left = -1;
        this.tree[i].right = -1;
    }
    //returns balance factor of subtree rooted by node at index i
    private int balanceFactor(int i) {
        return this.height(this.tree[i].left) - this.height(this.tree[i].right);
    }
    //get height of a node at index i
    private int height(int i) {
        return (i != -1) ? this.tree[i].height : -1;
    }
    //update the height of a node at index i
    private void updateHeight(int i) {
        int lInd = this.tree[i].left;
        int rInd = this.tree[i].right;
        if (lInd == -1 && rInd == -1)
            this.tree[i].height = 0;
        else if (lInd != -1 && rInd == -1)
            this.tree[i].height = this.tree[lInd].height + 1;
        else if (lInd == -1 && rInd != -1)
            this.tree[i].height = this.tree[rInd].height + 1;
        else
            this.tree[i].height = this.max(this.height(lInd), this.height(rInd)) + 1;
    }
    //inorder printing of subtree rooted by node at index i
    public void print() {
        this.preorderPrint(this.rootInd, "");
    }
    private void preorderPrint(int i, String indent) {
        if (i >= 0) {
            System.out.println(indent + this.tree[i] + " " + this.height(i));
            this.preorderPrint(this.tree[i].left, indent + "  ");
            // if (i == this.rootInd) System.out.print("* ");
            this.preorderPrint(this.tree[i].right, indent + "  ");
        }
    }
}
