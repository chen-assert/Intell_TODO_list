package src;
/**
 * server thread to handle single user
 *
 * @author jingruichen
 * @since 2018-11-08
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

import static src.CoreServer.*;


// one client correspond to one thread
public class ServerThread implements Runnable {
    private String clientName = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private Socket clientSocket;
    private int clientCount;

    public ServerThread(Socket clientSocket, int clientCount) {
        this.clientSocket = clientSocket;
        this.clientCount = clientCount;
    }

    public void send(String text) throws Exception {
        synchronized (this) {
            os.writeUTF(text);
            os.flush();
        }
        System.out.println("send:" + text);
    }

    public String receive() throws Exception {
        String line;
        synchronized (this) {
            line = is.readUTF();
        }
        System.out.println("receive:" + line);
        return line;
    }

    /**
     * @text the broadcast text
     * @exclude 0--broadcast all,1--exclude self
     */
    public void broadcast(String text, int exclude) throws Exception {
        //show the log in server
        System.out.println(text);
        for (ServerThread aThreadpools2 : threadpool) {
            if (exclude == 1 && aThreadpools2 == this) {
            } else {
                aThreadpools2.send(text);
            }
        }
    }

    public boolean startWithIgnoreCase(String src, String obj) {
        if (obj.length() > src.length()) {
            return false;
        }
        return src.substring(0, obj.length()).equalsIgnoreCase(obj);
    }


    public void run() {
        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
            String a = receive();
            if (a.startsWith("@login@")) {
                String name = receive();
                String pass = receive();
                Account account = accountMap.get(name);
                if (account != null && account.password.equals(pass)) {
                    send("@successful@");
                    send(String.valueOf(account.cookie));
                } else {
                    send("@fail@");
                }
            } else if (a.startsWith("@download@")) {
                int cookie = Integer.parseInt(receive());
                send(save.get(cookie));
            } else if (a.startsWith("@upload@")) {
                int cookie = Integer.parseInt(receive());
                String content = receive();
                save.put(cookie, content);
            } else if (a.startsWith("@register@")) {
                String name = receive();
                String pass = receive();
                if (accountMap.get(name) == null) {
                    Account account = new Account(pass, new Random().nextInt());
                    accountMap.put(name, account);
                    send("@successful@");
                    send(String.valueOf(account.cookie));
                } else {
                    send("@fail@");
                }
            }
            /*
            if(a.startsWith("@test@")){
                CoreServer.save=a;
            }else if(a.startsWith("@request@")){
                send(save);
            }
            */
            synchronized (this) {
                threadpool.remove(this);
            }
            is.close();
            os.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            //avoid potential problem
            threadpool.remove(this);
        }
    }
}
