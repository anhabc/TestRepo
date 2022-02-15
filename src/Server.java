import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.Objects;
import java.util.regex.Pattern;

public class Server {
    public static void main(String argv[])  {
        final ServerSocket serverSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner scanner = new Scanner(System.in);
        String patternEcho = "echo \".*\"";
        String patternStand = "standardize \".*\"";

        try{
        serverSocket = new ServerSocket(9091);
        clientSocket = serverSocket.accept();

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String clientName = in.readLine();
        out.println("Hello " + clientName);
        System.out.println("connected " +clientName);

        /*response from client to server*/
        Thread receive = new Thread(new Runnable() {
            String message;
            @Override
            public void run() {
                try {
                    message= in.readLine();
                    while (!Objects.equals(message, "exit")){
                        System.out.println("Client: " +message);

                        boolean matchesEcho = Pattern.matches(patternEcho, message);
                        boolean matchesStand = Pattern.matches(patternStand, message);

                        if (matchesEcho || matchesStand){
                            if (matchesEcho) {
                                message = message.replace("echo \"","");
                                message = message.substring(0, message.length() - 1);
                            }
                            if (matchesStand){
                                message = message.replace("standardize \"","");
                                message = message.substring(0, message.length() - 1);
                                message = message.trim().toLowerCase().replaceAll(" +"," ");
                                message = message.substring(0, 1).toUpperCase() + message.substring(1);
                            }
                            out.println(message);
                        } else {
                            out.println("Not found!");
                        }
                        message = in.readLine();
                    }
                    out.println("Goodbye " + clientName);
                    System.out.println("Client disconnected");
                    out.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch(IOException e){
                    e.printStackTrace();
                }

            }
        });
        receive.start();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}