package chesssocketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CertificateFactory;
import java.util.Scanner;
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

                String nombre = in.readUTF();

                if (!buscarActualizar(nombre)) {

                    Jugador jugador = new Jugador();
                    jugador.setJugadorOrigen(jugadorSocket);
                    jugador.setNombreJugador(nombre);
                    jugador.start();
                    Jugador.jugadores.add(jugador);

                }
            } catch (IOException ex) {
                Logger.getLogger(ChessSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (Jugador j : Jugador.jugadores) {

                System.out.println(j.getNombreJugador());

            }
            System.out.println(jugadorSocket);
            //break;

        }

    }

    private boolean buscarActualizar(String nombreJugador) {
        for (Jugador j : Jugador.getJugadores()) {
            if (j.getNombreJugador().equals(nombreJugador)) {
                
                Jugador jugador = new Jugador();
                jugador.setJugadorDestino(j.getJugadorDestino());
                jugador.setTiempo(j.getTiempo());
                jugador.setJugadorOrigen(jugadorSocket);
                jugador.start();
                j = jugador;
                
                return true;
            }
        }
        return false;
    }
    
    public void test(String movimiento){
        try {
            DataOutputStream out = new DataOutputStream(jugadorSocket.getOutputStream());
            out.writeUTF(movimiento);
        } catch (IOException ex) {
            Logger.getLogger(ChessSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        ChessSocketServer socketServer = new ChessSocketServer();
        socketServer.start();
        while(true){
            socketServer.test(s.nextLine());
        }

    }

}
