package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkIO {
    private InputStream is;
    private BufferedInputStream bis;
    private DataInputStream dis;

    private OutputStream os;
    private BufferedOutputStream bos;
    private DataOutputStream dos;

    private String request;

    public NetworkIO(Socket socket) throws IOException {
        this.is = socket.getInputStream();
        this.bis = new BufferedInputStream(is);
        this.dis = new DataInputStream(bis);

        this.os = socket.getOutputStream();
        this.bos = new BufferedOutputStream(os);
        this.dos = new DataOutputStream(bos);
    }

    public String readRequest() throws IOException {
        return this.dis.readUTF();
    }

    public void writeResult(String reply) throws IOException {
        this.dos.writeUTF(reply); //to add later
        this.dos.flush();
    }
    
    public void closeConnection() throws IOException {
        this.is.close();
        this.bis.close();
        this.dis.close();

        this.os.close();
        this.bos.close();
        this.dos.close();
    }

}
