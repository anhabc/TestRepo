import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;



public class Client {
    public static void main(String argv[]) {
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        try {
            clientSocket = new Socket("localhost",9091);
            System.out.println("Enter clientName: ");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while (true){
                        msg = sc.nextLine();
                        out.println(msg);

                    }
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (!Objects.equals(msg, "exit")){
                            if (msg == null){
                                break;
                            }
                            System.out.println("Server: "+msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server disconnected");
                        out.close();
                        clientSocket.close();

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}



