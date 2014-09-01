public class HexToBase {


public static String toBase64(String s) {

  byte[] bytes = s.getBytes();

  for(int i = 0; i < bytes.length; i++) {
    System.out.println(bytes[i]);
  }

  return s;
}


public static void main(String[] args) {

  if (args.length != 1) {
    System.out.println("Missing parameter.");
    System.exit(-1);
  }

  String input = args[0];
  
  System.out.println(toBase64("aaB"));//input));
  }
}
