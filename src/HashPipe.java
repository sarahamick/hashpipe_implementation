/**
 * @desc A class implementing the hashpipe symbol table data structure.
 * @author Sarah Amick
 */
public class HashPipe {

    private Node[] root;
    private int N;
    private int highestPipe;

    public HashPipe(){
        N = 0;
        highestPipe = 0;
        root = new Node[highestPipe];
    }

    /**
     * @desc A method that takes a key-value pair and inserts it with its corresponding pipe at the right
     * position in the hashpipe.
     * @param key, string
     * @param value, integer
     */
    public void put(String key, Integer value){
        int pipeHeight = getNumberOfTrailingZeros(key);

        Node nodeToAdd = new Node(key, value, -1);
        createNodesPipe(nodeToAdd, pipeHeight);
        N++;

        Node furthestRight;

        if(root.length == 0) resizeRoot(pipeHeight, nodeToAdd); //it's the first element to be inserted

        else {
            if (pipeHeight <= root.length) furthestRight = floor(nodeToAdd, pipeHeight-1, root[root.length - 1]);
            else furthestRight = floor(nodeToAdd, root.length, root[root.length - 1]);

            if (furthestRight.key.compareTo(key) == 0) { //the furthestRight node has the same key as nodeToAdd - just update value
                Node downNode = furthestRight;
                while (downNode.down != null) downNode = downNode.down;
                downNode.value = value;
                N--;
            }
            else { //insert the pipe after the furthest node found
                nodeToAdd.pipe[furthestRight.height].pointer = furthestRight.pointer;
                furthestRight.pointer = nodeToAdd.pipe[furthestRight.height];

                Node next = furthestRight;

                while (next.height > 0) {
                    next = floor(nodeToAdd, next.height - 1, next.down);
                    nodeToAdd.pipe[next.height].pointer = next.pointer;
                    next.pointer = nodeToAdd.pipe[next.height];
                }
            }
            if (pipeHeight > root.length) resizeRoot(pipeHeight, nodeToAdd); //if pipe is heigher than the current root, resize the root
        }
    }

    /**
     * @desc A method that finds the node closest to the inserted node, without being larger than the inserted node, at
     * a specified height in the hashpipe.
     * @param nodeToAdd, node to insert into the hashpipe
     * @param heightOfInsertedElement, the height at which you're inserting the current node in the pipe
     * @param startNode, the node from which you start the search to the right
     * @return a node from which the inserted node can be inserted after
     */
    public Node floor(Node nodeToAdd, int heightOfInsertedElement, Node startNode) {
        int currentHeight = startNode.height;

        Node furthestRight = traverseRight(nodeToAdd, startNode);
        if(furthestRight.down==null) return furthestRight;

        while(currentHeight > heightOfInsertedElement){
            furthestRight = traverseRight(nodeToAdd, furthestRight.down);
            currentHeight--;
        }

        return furthestRight;
    }

    /**
     * @desc A debugging method to check if an element is pointing to the correct following element.
     * @param key, a string key of the node you want to examine
     * @param h, the height of a pipe
     * @return the key of the node the node with the specified key at height h is pointing to
     */
    public String control(String key, int h){
        Node foundBaseNode = traversePipes(new Node(key));
        if(foundBaseNode.pipe.length - 1 < h) return null;
        if(foundBaseNode.pipe[h].pointer == null) return null;

        return foundBaseNode.pipe[h].pointer.key;
    }

    /**
     * @desc A method used to find a node with a specified key in the get() method. It traverses to the right and
     * down the hashpipe alternatively until it finds the node at the bottom of the hashpipe with the specified key.
     * @param node the node you are trying to find
     * @return the found node, or null of element is not in the hashpipe
     */
    private Node traversePipes(Node node){
        if(root.length==0) return null;

        Node furthestNode;

        //start at the highest node in the root pipe
        Node rightNode = traverseRight(node, root[root.length-1]);

        while(true){
            Node downNode = traverseDown(rightNode);
            if(downNode==rightNode) {furthestNode = downNode; break;} //we didn't go down the pipe
            rightNode = traverseRight(node, downNode);
        }

        return furthestNode;
    }

    /**
     * @desc A method to find the node beneath a specified node. Used to traverse the
     * hashpipe in the traversePipe() method
     * @param startNode
     * @return the node below the given node, or null if no such node exists
     */
    private Node traverseDown(Node startNode){
        if(startNode.down == null) return startNode;
        else return startNode.down;
    }

    /**
     * @desc A method to follow a linked list path from a start node to the node furthest right from that
     * node without its key being greater than the key of the nodeToFind
     * @param nodeToFind the node to locate in the hashpipe
     * @param startNode the node to start the search from
     * @return The node furthest right in the hashpipe without being greater than the key of the nodeToFind
     */
    private Node traverseRight(Node nodeToFind, Node startNode) {
        if(startNode.pointer==null) return startNode;

        int compare = (startNode.pointer.key).compareTo(nodeToFind.key);
        if (compare > 0) return startNode;
        else if (compare == 0) return startNode.pointer;
        else return traverseRight(nodeToFind, startNode.pointer);
    }

    /**
     * @desc A method to create a node's pipe with the specified height, filled with nodes containing the
     * specified node's key.
     * @param node
     * @param pipeHeight
     */
    private void createNodesPipe(Node node, int pipeHeight){
        node.pipe = new Node[pipeHeight];

        for(int i = 0; i < node.pipe.length; i++) { //make the new node's pipe and fill it with identical nodes
            if(i == 0){ //down node = the node to add
                Node pipeNode = new Node(node.key, node, 0);
                node.pipe[i] = pipeNode;
            }
            else{ //down node is equal to the previous node in the array
                Node pipeNode = new Node(node.key, node.pipe[i-1], i);
                node.pipe[i] = pipeNode;
            }
        }
    }

    /**
     * @desc A method to resize the root array of nodes as new nodes are added with taller pipes.
     * @param newSize of the array
     * @return the index of the first free array spot in the newly resized root
     */
    private int resizeRoot(int newSize, Node nodeToAdd){
        Node[] aux = new Node[newSize];
        for(int i = 0; i < root.length; i++){
            aux[i] = root[i];
        }
        for(int k = root.length; k < aux.length; k++){
            if(k == 0) aux[k] = new Node(nodeToAdd.pipe[k], null, 0, "-1");
            else aux[k] = new Node(nodeToAdd.pipe[k], aux[k-1], k, "-1");
        }
        int lastIndex = root.length;
        root = aux;
        return lastIndex;
    }

    /**
     * @desc A method to find the integer value associated with a specified key in the hashpipe
     * @param key the key to search for in the hashpipe
     * @return the integer value associated with that key, or -1 if no such key exists in the hashpipe
     */
    public Integer get(String key) {
        Node foundNode = traversePipes(new Node(key));
        if(foundNode == null) return -1;
        else return foundNode.value;
    }

    public int size(){
        return N;
    }

    private int getNumberOfTrailingZeros(String string){
        return Integer.numberOfTrailingZeros(string.hashCode()) + 1;
    }

    /**
     * @desc An inner class defining node objects to be used in the hashpipe.
     */
    private class Node {
        String key;
        int value;
        Node pointer;
        Node[] pipe;
        Node down;
        int height;

        private Node(String key, Node down, int height) { //nodes for the element pipes
            this.key = key;
            this.down = down;
            this.height = height;
        }

        private Node(Node pointer, Node down, int height, String key) { //nodes for the root pipe
            this.pointer = pointer;
            this.down = down;
            this.height = height;
            this.key = key;
        }

        private Node(String key, int value, int height) { //nodes for the elements themselves
            this.key = key;
            this.value = value;
            this.height = height;
        }

        private Node(String key) { //nodes for the get and control methods
            this.key = key;
        }
    }
}