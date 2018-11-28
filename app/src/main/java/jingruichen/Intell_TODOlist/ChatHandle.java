package jingruichen.Intell_TODOlist;
/**
 * the chat handle module to control the client's communication with server
 * `ref from my distributed work
 *
 * @author jingruichen
 * @since 2018-11-12
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

//using observer model to manage text flash
class ChatHandle {
    //send a new message
    public void send(String text) {
        try {
            outputStream.writeUTF(text);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //receive a new message
    public String receive() {
        try {
            String line = inputStream.readUTF();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public void InitSocket() throws IOException {
        InitSocket("qwe.chenassert.xyz", 9549);
    }

    public void InitSocket(String server, int port) throws IOException {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server, port), 10000);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    //Close the socket

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
