public class Test02 {
    public static void main(String[] args) {
        // Test
        int i=0;
        int n = 0;
        n=100_000;

        HashPipe H = new HashPipe();

        for(int j=0;j<n;j++)
            H.put(""+j, j);

    }
}