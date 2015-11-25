package org.benzeneframework.http;

import org.benzeneframework.Benzene;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jeongukjae on 15. 11. 3..
 * @author jeongukjae
 *
 * ListeningThread
 * This thread will be processing just listening and creating {@link AcceptThread}
 */
public class ListeningThread extends Thread {
    private Benzene benzene;
    private ServerSocket serverSocket;

    public ListeningThread(Benzene benzene) {
        this.benzene = benzene;
    }

    @Override
    public void run() {
        Socket client;
        AcceptThread acceptThread;
        try {
            // listening
            while ((client = serverSocket.accept()) != null) {

                // create accept thread
                acceptThread = new AcceptThread(benzene, client);
                acceptThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set server socket
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
}
