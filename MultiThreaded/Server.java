import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    
    public Consumer<Socket> getConsumer(){
        return (clientSocket) -> {
            // try-with-resources
            try  (
                BufferedReader fromSocket = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)
            ){
                String clientMsg = fromSocket.readLine();
                System.out.println("Received: " + clientMsg);
                toSocket.println("Hello from server " + clientSocket.getInetAddress());
            } catch (IOException ex){
                ex.printStackTrace();
            }
        };
    }
    public static void main(String[] args) {
        int port = 8010;
        // object creation as we did write static (dont think server is something related to sockets in this case)
        Server server = new Server();

        try{
            // this creates a listening socket on the server (remember the diagramatic explaination )
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
            System.out.println("Server is listening on the port "+port);
            while (true) {
                try {
                    // control waits(blocks) until it gets TCP connection request from a client.
                //  here multiple clients get to have sockets and are identified using
                //  client and server port and ip (as u dont see this keyword)
                Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from: " + clientSocket.getInetAddress());

                Thread thread = new Thread(()-> server.getConsumer().accept(clientSocket));
                thread.start();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}


/*
    "Object references in Java are unique, and whenever an object is created, it is stored in memory and can be uniquely identified by its reference.
    In the case of a Socket, the client IP, client port, server IP, and server port together define a unique TCP connection. 
    This is what allows Java (and the OS) to distinguish between multiple client connections, even though the server code may not manually 
    assign unique variable names to each connection."
*/ 


/*
    public Consumer<Socket> getConsumer() {
    // This returns an object of a class implementing Consumer<Socket>
    return new Consumer<Socket>() {
        @Override
        public void accept(Socket clientSocket) {
            try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
                toSocket.println("Hello from server " + clientSocket.getInetAddress());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };
}

*/