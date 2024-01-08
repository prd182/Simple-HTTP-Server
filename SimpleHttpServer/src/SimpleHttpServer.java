import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single-threaded simple web server implementation
 */
public class SimpleHttpServer {

    private static final Logger logger = Logger.getLogger(SimpleHttpServer.class.getName());

    public static final int DEFAULT_PORT = 8085;

    private ServerSocket serverSocket = null;


    public static void main(String[] args) {

        try {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

            SimpleHttpServer webServer = new SimpleHttpServer();
            webServer.listen(port);

        } catch (NumberFormatException e) {

            System.err.println("Usage: WebServer [PORT]");
            System.exit(1);
        }
    }

    private void listen(int port) {

        try {

            serverSocket = new ServerSocket(port);
            logger.log(Level.INFO, "server bind to " + getAddress(serverSocket));

            serve(serverSocket);

        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + port);
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
    }

    private void serve(ServerSocket serverSocket) {

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "New connection from " + getAddress(clientSocket));

                dispatch(clientSocket);

            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    private void dispatch(Socket clientSocket) {

        try {

            OutputStream outputStream = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n\r\nHello, world!";

            outputStream.write(response.getBytes());

            outputStream.close();
            clientSocket.close();


        } catch (SocketException ex) {

            logger.log(Level.INFO, "client disconnected " + getAddress(clientSocket));

        } catch (IOException e) {

            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private String getAddress(ServerSocket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
    }

    private String getAddress(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
    }
}

