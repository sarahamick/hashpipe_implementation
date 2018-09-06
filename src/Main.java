public class Main {
    public static void main(String[] args) {
        String[] perfect = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R","S","T", "U", "V", "W", "X", "Y", "Z"};
        String[] searchExample = new String[]{"S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"};
        String[] worstCase = new String[]{"A", "C", "E", "G", "I", "K", "L", "M", "O", "Q", "S", "U", "W", "Y"};

        HashPipe hashpipe = new HashPipe();
        for(int i = 0; i < searchExample.length; i++){
            hashpipe.put(searchExample[i], i);
        }

        for(int i = 0; i < perfect.length; i++){
            System.out.println(hashpipe.get(perfect[i]));
        }

        for(int i = 0; i < hashpipe.size(); i++){
            System.out.println(hashpipe.control(perfect[i], i));
        }
    }
}
