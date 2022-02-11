import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String textFromClient    =
                "echo \"\"";
        // echo " "
        String pattern = "echo \".*\"";
        System.out.println("Original: " +textFromClient);

        boolean matches = Pattern.matches(pattern, textFromClient);
        if (matches) {
            textFromClient = textFromClient.replace("echo \"","");
            textFromClient = textFromClient.substring(0, textFromClient.length() - 1);
        }
        System.out.println(textFromClient);

    }
}
