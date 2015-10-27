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
        //the index of this node's parent
        private int parent;
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
            retval += (this.value != null) ? this.value.toString() : "NULL";
            retval += ", " + this.left + ", " + this.right + ")";
            return retval;
        }
    }

    //INSTANCE VARIABLES
    //an array of nodes representing this hash map's implicit AVL Tree
    public Node[] tree;
    //bitmap used to mark which indices in the array hold active nodes.
    public byte[] bitmap;
    //the array index of the root
    public int rootInd;
    //the size of this hash map
    public final int size;
    //the number of items currently in this hash map
    public int items;
    //recently deleted index
    public int delInd;

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
        this.bitmap = new byte[size/8];
        this.rootInd = -1;
        this.size = size;
        this.items = 0;
    }

    // private int getAvailableNode() {
    //     for (int i = 0; i < this.size; i++) {
    //         if (this.bitmap[i] == 0) {
    //             return i;
    //         }
    //     }
    //     return -1;
    // }

    // private void bitFlip(int x) {
    //     this.bitmap[x] = (this.bitmap[x] == 1) ? (byte)0 : (byte)1;
    // }


    // public void pb() {
    //     for (int i = 0; i < this.bitmap.length; i++) {
    //         for (int j = 0; j < 8; j++) {
    //             System.out.print((this.bitmap[i] & (1 << j)) + " ");
    //         }
    //         System.out.print(". ");
    //     }
    //     System.out.println();
    // }

    public int getAvailableNode() {
        int i = 0;
        int j = 0;
        for (; this.bitmap[i] == 0xFF; i++);
        for (; j < 8; j++) {
            if ((this.bitmap[i] & (1 << j)) == 0) {
                return 8*i + j;
            }
        }
        return -1;
    }

    public void bitFlip(int x) {
        int index = x/8;
        int offset = x%8;
        this.bitmap[index] ^= (1 << offset);
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
        if (this.items < this.size && value != null) {
            int newInd = this.getAvailableNode();
            if (key == "e") System.out.println(newInd);
            this.tree[newInd].key = key.hashCode();
            this.tree[newInd].value = value;
            this.tree[newInd].height = 0;
            try {
                //throws an IllegalArgumentException if the key is already used
                this.rootInd = this.insert(newInd, this.rootInd);
                this.bitFlip(newInd);
                this.items++;
                return true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                //clean up and return false
                this.cleanNode(newInd);
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
            //get the index of the node with the given string
            int nodeInd = this.find(key.hashCode(), this.rootInd);
            return (nodeInd != -1) ? (T) this.tree[nodeInd].value : null;
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
            //get the index of the node with the given string
            this.rootInd = this.remove(key.hashCode(), this.rootInd);
            if (this.delInd != -1) {
                T retval = (T) this.tree[delInd].value;
                this.cleanNode(this.delInd);
                this.bitFlip(this.delInd);
                this.delInd = -1;
                this.items--;
                return retval;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * Removes node with key from the subtree rooted by node at startInd.
     *
     * This method recursively traverses the implicit subtree rooted by the node
     * at startInd to find and remove the node with the given key. Depending on
     * the number of children the found node has, it may or may not rebalance
     * the implicit tree using {@link FixedSizeHashMap#rebalance}.
     * @param       key         the key of the node to be removed
     * @param       startInd    the index of the subtree root
     * @return      the index of the (new) subtree root
     */
    private int remove(int key, int startInd) {
        //startInd isn't actually part of the implicit tree
        if (startInd == -1) {
            this.delInd = -1;
            return -1;
        }
        //remove from left subtree
        else if (key < this.tree[startInd].key) {
            this.tree[startInd].left = this.remove(key, this.tree[startInd].left);
            return this.rebalance(startInd);
        }
        //remove from right subtree
        else if (key > this.tree[startInd].key) {
            this.tree[startInd].right = this.remove(key, this.tree[startInd].right);
            return this.rebalance(startInd);
        }
        //startInd is the node to be removed
        else {
            int lInd = this.tree[startInd].left;
            int rInd = this.tree[startInd].right;
            //node is a leaf, simply remove it
            if (lInd == -1 && rInd == -1) {
                this.delInd = startInd;
                return -1;
            }
            //node has a single child, give the node it's grandchildren
            else if (lInd != -1 && rInd == -1) {
                this.delInd = startInd;
                return lInd;
            } else if (lInd == -1 && rInd != -1) {
                this.delInd = startInd;
                return rInd;
            }
            //node has two children, replace the node it's successor (smallest
            //node in right subtree), then remove the successor
            else {
                int smallestInd = this.getSmallest(rInd);
                int tempInd = startInd;
                this.nodeKVSwap(startInd, smallestInd);
                this.tree[startInd].right = this.remove(this.tree[smallestInd].key, rInd);
                this.delInd = smallestInd;
                return this.rebalance(startInd);
            }
        }
    }

        // else {
        //     int curInd = startInd;
        //     int prevInd = curInd;
        //     while (key != this.tree[curInd].key) {
        //         if (key < this.tree[curInd].key) {
        //             prevInd = curInd;
        //             curInd = this.tree[curInd].left;
        //         } else {
        //             prevInd = curInd;
        //             curInd = this.tree[curInd].right;
        //         }
        //     }
        //     //now, curInd is the index of the node that needs removal
        //     //prevInd has the parent of curInd
        //     T tempValue = (T) this.tree[curInd].value;
        //     int lInd = this.tree[curInd].left;
        //     int rInd = this.tree[curInd].right;
        //     //node is a leaf, simply remove it
        //     if (lInd == -1 && rInd == -1) {
        //         this.cleanNode(curInd);
        //         this.delValue = tempValue;
        //         return -1;
        //     }
        //     //node has a single child, give the node it's grandchildren
        //     else if (lInd != -1 && rInd == -1) {
        //         this.cleanNode(curInd);
        //         this.delValue = tempValue;
        //         return lInd;
        //     } else if (lInd == -1 && rInd != -1) {
        //         this.cleanNode(curInd);
        //         this.delValue = tempValue;
        //         return rInd;
        //     }
        //     //node has two children, replace the node it's successor (smallest
        //     //node in right subtree), then remove the successor
        //     else {
        //         int smallestInd = this.getSmallest(rInd);
        //         this.nodeKVSwap(startInd, smallestInd);
        //         this.remove(this.tree[smallestInd].key, rInd);
        //         this.tree[startInd].right = this.remove(this.tree[smallestInd].key, rInd);
        //         return this.rebalance(startInd);
        //     }
        // }
    /**
     * Returns the index of the smallest node in the subtree rooted by startInd.
     * @param       startInd    the index of the subtree root
     * @return      the index of the node with the smallest key
     */
    private int getSmallest(int startInd) {
        if (this.tree[startInd].left != -1) {
            return this.getSmallest(this.tree[startInd].left);
        } else {
            return startInd;
        }
    }
    /**
     * swaps the keys and values of a and b the node at index a with the node at index b.
     *
     * Only overwrites the key and value fields of the nodes. The height and
     * indices of the children are left untouched.
     * @param       a       the index of the node to be overwritten (dst)
     * @param       b       the index of the node with the data to write (src)
     */
    private void nodeKVSwap(int a, int b) {
        int tempKey = this.tree[a].key;
        this.tree[a].key = this.tree[b].key;
        this.tree[b].key = tempKey;
        T tempValue = (T) this.tree[a].value;
        this.tree[a].value = this.tree[b].value;
        this.tree[b].value = tempValue;
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
    /**
     * Prints the implicit tree in a preorder fashion. Only used for testing.
     */
    public void print() {
        this.preorderPrint(this.rootInd, "");
    }

    //TREE METHODS (PRIVATE)
    /**
     * Inserts node at newInd into the subtree rooted by node at startInd.
     *
     * This method recursively traverses the implicit subtree rooted by the node
     * at startInd in order to find the proper place to put the new node. It
     * then calls {@link FixedSizeHashMap#rebalance} to enforce the height
     * invariant for AVL Trees.
     * @param       newInd      the index of the node to be inserted
     * @param       startInd    the index of the subtree root
     * @return      the index of the (new) subtree root
     * @throws      IllegalArgumentException    if the key of the node at newInd
     *                                          is already used in the hash map
     */
    private int insert(int newInd, int startInd) throws IllegalArgumentException {
        //insert at startInd if startInd isn't actually part of the implicit tree
        if (startInd == -1) {
            return newInd;
        }
        //insert into left subtree
        else if (this.tree[newInd].key < this.tree[startInd].key) {
            this.tree[startInd].left = this.insert(newInd, this.tree[startInd].left);
        }
        //insert into right subtree
        else if (this.tree[newInd].key > this.tree[startInd].key) {
            this.tree[startInd].right = this.insert(newInd, this.tree[startInd].right);
        }
        //duplicate key
        else {
            throw new IllegalArgumentException("Key already used.");
        }
        //rebalance tree and return
        return this.rebalance(startInd);
    }
    /**
     * Rebalances the subtree rooted by the node at index startInd.
     *
     * Based on the balance factor of the given node, this method will apply the
     * appropriate tree rotation. This method is NOT recursive and will only
     * apply the rotation to the node at index startInd.
     * @param       startInd    the index of the subree root
     * @return      the index of the (new) subtree root
     * @see         FixedSizeHashMap#balanceFactor
     */
    private int rebalance(int startInd) {
        int newStartInd;
        //left subtree heavy
        if (this.balanceFactor(startInd) == 2) {
            int lInd = this.tree[startInd].left;
            if (lInd == -1) {
                newStartInd = this.rotateCaseLL(startInd);
            } else {
                newStartInd = this.rotatecaseLR(startInd);
            }
        }
        //right subtree heavy
        else if (this.balanceFactor(startInd) == -2) {
            int rInd = this.tree[startInd].right;
            if (rInd == -1) {
                newStartInd = this.rotateCaseRL(startInd);
            } else {
                newStartInd = this.rotateCaseRR(startInd);
            }
        }
        //no rebalancing needed
        else {
            newStartInd = startInd;
        }
        //update height if necessary
        this.updateHeight(startInd);
        return newStartInd;
    }
    /**
     * Returns the index of the node containing key.
     *
     * This method recursively traverses the tree, beginning at the node at
     * index startInd. If startInd is not actually a node in the tree, -1 will
     * be returned, indicating that no node was found with the given key.
     * @param       key         the key to search for
     * @param       startInd    the index of the subtree root
     */
    private int find(int key, int startInd) {
        if (startInd == -1) {
            return -1;
        } else if (key == this.tree[startInd].key) {
            return startInd;
        } else if (key < this.tree[startInd].key) {
            return this.find(key, this.tree[startInd].left);
        } else {
            return this.find(key, this.tree[startInd].right);
        }
    }

    //TREE ROTATIONS
    /**
     * Performs a tree rotation for the left left case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     * @return      the new start index after rotation
     */
    private int rotateCaseLL(int startInd) {
        int newStartInd = this.tree[startInd].left;
        this.tree[startInd].left = this.tree[newStartInd].right;
        this.tree[newStartInd].right = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    /**
     * Performs a tree rotation for the right right case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     * @return      the new start index after rotation
     */
    private int rotateCaseRR(int startInd) {
        int newStartInd = this.tree[startInd].right;
        this.tree[startInd].right = this.tree[newStartInd].left;
        this.tree[newStartInd].left = startInd;
        //update heights
        this.updateHeight(startInd);
        this.updateHeight(newStartInd);
        return newStartInd;
    }
    /**
     * Performs a tree rotation for the left right case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     * @return      the new start index after rotation
     */
    private int rotatecaseLR(int startInd) {
        this.tree[startInd].left = this.rotateCaseRR(this.tree[startInd].left);
        return this.rotateCaseLL(startInd);
    }
    /**
     * Performs a tree rotation for the right left case at startInd.
     *
     * See <a href="https://en.wikipedia.org/wiki/AVL_tree#Insertion">this</a>
     * Wikipedia article for examples of the cases used.
     * @return      the new start index after rotation
     */
    private int rotateCaseRL(int startInd) {
        this.tree[startInd].right = this.rotateCaseLL(this.tree[startInd].right);
        return this.rotateCaseRR(startInd);
    }

    //UTILITIES
    /**
     * Returns the max of two integers
     * @param       a       the first integer
     * @param       b       the second integer
     * @return      the larger of the two given integers
     */
    private static int max(int a, int b) {
        return (a > b) ? a : b;
    }
    /**
     * Cleans the node at index i by setting its fields to -1 or null.
     * @param       i       the index of the node to be cleaned
     */
    private void cleanNode(int i) {
        this.tree[i].key = -1;
        this.tree[i].value = null;
        this.tree[i].height = -1;
        this.tree[i].left = -1;
        this.tree[i].right = -1;
    }
    /**
     * Returns the balance factor of the subtree rooted by the node at index i.
     *
     * Balance factor is defined as the difference between a node's left and
     * right subtrees.
     * @return      the balance factor of the specified node
     */
    private int balanceFactor(int i) {
        return this.height(this.tree[i].left) - this.height(this.tree[i].right);
    }
    /**
     * Returns the height of the node at index i or -1 if i is not active
     * @return      the height of the node at index i or -1
     */
    private int height(int i) {
        return (i != -1) ? this.tree[i].height : -1;
    }
    /**
     * Updates the height of the node at index i
     */
    private void updateHeight(int i) {
        if (i != -1) {
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
    }
    /**
     * Preorder prints this hash map's implicit tree
     * @param       i       the root of the subtree to print
     * @param       indent  the string indentation to print
     */
    private void preorderPrint(int i, String indent) {
        if (i >= 0) {
            System.out.println(indent + this.tree[i] + " " + this.height(i));
            this.preorderPrint(this.tree[i].left, indent + "  ");
            // if (i == this.rootInd) System.out.print("* ");
            this.preorderPrint(this.tree[i].right, indent + "  ");
        }
    }
}
