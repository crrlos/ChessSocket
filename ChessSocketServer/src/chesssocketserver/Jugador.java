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

    DataInputStream in;
    DataOutputStream out;

    boolean jugando;

    static ArrayList<Jugador> jugadores = new ArrayList<>();

    @Override
    public void run() {
        super.run();
        while (true) {
            /**
             * pausa del hilo
             */
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }
            /**
             * fin pausa del hilo
             */

            try {
                in = new DataInputStream(jugadorOrigen.getInputStream());
                int op = in.readInt();
                System.out.println(op);
                switch (op) {
                    case 1:
                        invitarJugador();
                        break;
                    case 2:
                        aceptarInvitacion();
                        break;
                    case 3:
                        enviarMovimiento();
                        break;

                }

            } catch (IOException ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void invitarJugador() {
        try {
            String nombre;
            in = new DataInputStream(jugadorOrigen.getInputStream());
            nombre = in.readUTF();
            enviarInvitacion(nombre);

        } catch (IOException ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int enviarInvitacion(String nombre) {
        jugadorDestino = getSocketJugador(nombre);
        if (jugadorDestino != null) {
            try {
                out = new DataOutputStream(jugadorDestino.getOutputStream());
                out.writeUTF(nombreJugador);
                System.out.println("invitando a: " + jugadorDestino);
                System.out.println("origen " + jugadorOrigen.toString() + " destino :" + jugadorDestino);

                return 1;
            } catch (IOException ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    private void aceptarInvitacion() {
        try {
            in = new DataInputStream(jugadorOrigen.getInputStream());
            jugadorDestino = getSocketJugador(in.readUTF());
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeInt(10);
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
            in = new DataInputStream(jugadorOrigen.getInputStream());
            out = new DataOutputStream(jugadorDestino.getOutputStream());
            out.writeUTF(in.readUTF());
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
