package src;
/**
 * core server module to allocate server thread to every user
 *
 * @author jingruichen
 * @since 2018-11-08
 */
import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class CoreServer {
    public static Map<String,Account> accountMap=new HashMap<>();
    public static Map<Integer,String> save=new HashMap<>();
    private static ServerSocket serverSocket = null;
    //Client limits
    protected static final int maxClientsCount = 100;
    protected static Vector<ServerThread> threadpool = new Vector();

    public static void main(String args[]) {
        try {
            JFrame frame = new JFrame();
            frame.setSize(800, 600);
            frame.setTitle("server");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);
            frame.setVisible(true);
        } catch (Exception e) {
            //make the problem can running in headless service
            //window is unnecessary
            System.out.println("running in headless serviece");
        }
        Random random = new Random();
        accountMap.put("user1",new Account("pass1",random.nextInt()));
        accountMap.put("user2",new Account("pass2",random.nextInt()));
        accountMap.put("user3",new Account("pass3",random.nextInt()));
        int portNumber;
        if (args.length >= 1) {
            portNumber = Integer.valueOf(args[0]);
        } else {
            portNumber = 9549;
        }
        System.out.println("Server start in port " + portNumber);
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //user count
        int count = 1;
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket, count++);
                Thread t = new Thread(serverThread);
                t.start();
                //use thread pool to save all linked client
                threadpool.add(serverThread);
                if (threadpool.size() >= maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("ERROR:Server's connection reach to the limit");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}