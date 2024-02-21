package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1. get commands from client
 * 2. run logic
 * 3. keep users info
 * additional: add logout
 */
public class ServerApp 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Server App running..." );
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        int port = Integer.parseInt(args[0]);
        ServerSocket server= new ServerSocket(port);
        int deckNum = Integer.parseInt(args[1]);

        BacarratEngine gameEngine = new BacarratEngine(deckNum);
        

        while(true) {
            System.out.println("Waiting for client connection");
            Socket socket = server.accept();
            System.out.println("Connected ...");
            GameClientHandler clientHandler = new GameClientHandler(socket, gameEngine);
            threadPool.submit(clientHandler);
            System.out.println("Submitted to threadpool");
            
        }
    }
}
