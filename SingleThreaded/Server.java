import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server{

    public void run() throws IOException, UnknownHostException{
        int port = 8010;
        ServerSocket socket = new ServerSocket(port);
        socket.setSoTimeout(10000);
        while (true) {
            try{
                System.out.println("Server is listening on port "+port);
                Socket acceptedConnection = socket.accept();
                System.out.println("Connection accepted from client "+acceptedConnection.getRemoteSocketAddress()); 
                // getRemoteSocketAddress() --- gives the client's IP and port for visibility


                // buffer reader - bytes ko combine karke text dedega
                // print writer - text ko bytes
                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(),true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
                
                toClient.println("hello from server, PrintWriter is to send info and BufferedReader is to read info"); //text to byte and send

                toClient.close();
                fromClient.close();
                acceptedConnection.close();

            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        try{
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

// Flushing means "send the data now" — without it, Java might keep your message in memory and send it later (or never, if you forget to close).


// https://chatgpt.com/share/6883ae20-6f58-800f-809d-4ff6b10e98f6