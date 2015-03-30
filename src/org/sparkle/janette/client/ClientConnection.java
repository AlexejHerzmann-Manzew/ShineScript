package org.sparkle.janette.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.sparkle.jbind.JBinD;
import org.sparkle.jbind.Reader;
import org.sparkle.jbind.Writer;

/**
 *
 * @author yew_mentzaki
 */
public final class ClientConnection {

    Socket client;
    ClientHandler clienthandler;

    private void send() {
        try {
            JBinD b = clienthandler.out();
            if(b==null)return;
            byte[] bytes = Writer.write(b);
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    Thread get = new Thread() {

        @Override
        public void run() {
            InputStream inputStream = null;
            try {
                inputStream = client.getInputStream();
                DataInputStream dis = new DataInputStream(inputStream);
                while (true) {
                    int length = dis.readInt();
                    if (length <= 0) {
                        continue;
                    }
                    byte[] message = new byte[length];
                    dis.read(message);
                    JBinD bind = Reader.read(message);
                    clienthandler.in(bind);
                }
            } catch (IOException ex) {

            } finally {

            }
        }

    };

    public void disconnect() {
        try {
            timer.stop();
            get.stop();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String server;
    int port;
    Class handler;
    int sendingDelay = 350;

    public ClientConnection(String server, int port, Class handler, int sendingDelay) {
        this.server = server;
        this.port = port;
        this.handler = handler;
        this.sendingDelay = sendingDelay;
    }
    Timer timer = new Timer(sendingDelay, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            send();
        }
    });

    public void connect() throws InstantiationException, IllegalAccessException, IOException {
        try {
            this.client = new Socket(server, port);
            clienthandler = (ClientHandler) handler.newInstance();
            get.start();
            timer.start();
        } catch (InstantiationException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
