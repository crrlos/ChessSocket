package chesssocketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessSocketServer {

    ServerSocket serverSocket;
    Socket jugadorSocket;
    DataInputStream in;
    DataOutputStream out;

    public ChessSocketServer() {
        try {
            serverSocket = new ServerSocket(40000);
        } catch (IOException ex) {
            Logger.getLogger(ChessSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() {
        while (true) {
            try {
                jugadorSocket = serverSocket.accept();
                in = new DataInputStream(jugadorSocket.getInputStream());
                
            } catch (IOException ex) {
                Logger.getLogger(ChessSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean buscarActualizar(String nombreJugador) {
        for (Jugador j : Jugador.getJugadores()) {
                
        }
        return false;
    }

    public static void main(String[] args) {
        ChessSocketServer socketServer = new ChessSocketServer();
        socketServer.start();

    }

}
