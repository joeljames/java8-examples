package examples;

public class BytesToString {
    public static void main(String[] args) {
        //bytes to string
        byte[] byteArr = new byte[] {87, 79, 87, 46, 46, 46};
        String value = new String(byteArr);
        System.out.println("The byte value in string is: " + value);
    }
}
