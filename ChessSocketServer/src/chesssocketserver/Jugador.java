/*
 * Copyright 2015 Carlos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chesssocketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jugador extends Thread {

    Socket jugadorOrigen;
    Socket jugadorDestino;
    String nombreJugador;

    int tiempo;
    boolean jugando;

    DataInputStream in;
    DataOutputStream out;

    static ArrayList<Jugador> jugadores = new ArrayList<>();

    @Override
    public void run() {
        super.run();
        while (true) {

            /**
             * Panel de control*
             */
            try {
                in = new DataInputStream(jugadorOrigen.getInputStream());
                int op = in.readInt();
                System.out.println(op);
                switch (op) {
                    case 1:
                        enviarInvitacion();
                        break;
                    case 2:
                        aceptarInvitacion();
                        break;
                    case 3:
                        enviarMovimiento();
                        break;

                }

            } catch (IOException ex) {
                //Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
                jugadores.remove(this);
            }

        }
    }

    private int enviarInvitacion() {

        String nombre = "";
        int bando;
        try {
            //in = new DataInputStream(jugadorOrigen.getInputStream());
            nombre = in.readUTF();
            //in = new DataInputStream(jugadorOrigen.getInputStream());
            //bando = in.readInt();

            jugadorDestino = getSocketJugador(nombre);
            if (jugadorDestino != null) {
                out = new DataOutputStream(jugadorDestino.getOutputStream());
                out.writeInt(1);
                //out = new DataOutputStream(jugadorDestino.getOutputStream());
                out.writeUTF(nombreJugador);
                //out = new DataOutputStream(jugadorDestino.getOutputStream());
               // out.writeInt(bando);
                
                System.out.println("invitando a: " + jugadorDestino);
                System.out.println("origen " + jugadorOrigen.toString() + " destino :" + jugadorDestino);

                return 1;

            }
        } catch (IOException ex) {
        }

        return 0;
    }

    private void aceptarInvitacion() {
        try {
            in = new DataInputStream(jugadorOrigen.getInputStream());
            int respuesta = in.readInt();
            in = new DataInputStream(jugadorOrigen.getInputStream());
            jugadorDestino = getSocketJugador(in.readUTF());
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeInt(2);
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeInt(respuesta);
            System.out.println(out);

        } catch (IOException ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Socket getSocketJugador(String nombre) {
        Socket socket = null;
        for (Jugador j : jugadores) {
            if (j.getNombreJugador().equals(nombre)) {
                socket = j.getJugadorOrigen();
            }
        }
        return socket;
    }

    private void enviarMovimiento() {
        try {
            String move;
            in = new DataInputStream(jugadorOrigen.getInputStream());
            move = in.readUTF();
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeInt(3);
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeUTF(move);
            System.out.println(move);
        } catch (IOException ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Socket getJugadorOrigen() {
        return jugadorOrigen;
    }

    public void setJugadorOrigen(Socket jugadorOrigen) {
        this.jugadorOrigen = jugadorOrigen;
    }

    public Socket getJugadorDestino() {
        return jugadorDestino;
    }

    public void setJugadorDestino(Socket jugadorDestino) {
        this.jugadorDestino = jugadorDestino;
    }

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

}
