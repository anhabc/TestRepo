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
        final Scanner sc = new Scanner(System.in);
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

        //response
        Thread receive = new Thread(new Runnable() {
            String msg;
            @Override
            public void run() {
                try {
                    msg = in.readLine();
                    while (!Objects.equals(msg, "exit")){
                        System.out.println("Client: " +msg);

                        boolean matchesEcho = Pattern.matches(patternEcho, msg);
                        boolean matchesStand = Pattern.matches(patternStand, msg);

                        if (matchesEcho || matchesStand){
                            if (matchesEcho) {
                                msg = msg.replace("echo \"","");
                                msg = msg.substring(0, msg.length() - 1);
                            }
                            if (matchesStand){
                                msg = msg.replace("standardize \"","");
                                msg = msg.substring(0, msg.length() - 1);
                                msg = msg.trim().toLowerCase().replaceAll(" +"," ");
                                msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
                            }
                            out.println(msg);
                        } else {
                            out.println("Not found!");
                        }
                        msg = in.readLine();
                    }
                    System.out.println();
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