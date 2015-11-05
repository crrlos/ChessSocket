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

    DataInputStream in;
    DataOutputStream out;

    static ArrayList<Jugador> jugadores = new ArrayList<>();

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                in = new DataInputStream(jugadorOrigen.getInputStream());
                int op = in.readInt();

                switch (op) {
                    case 1:
                        invitarJugador();
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

            out = new DataOutputStream(jugadorOrigen.getOutputStream());
            out.writeInt(enviarInvitacion(nombre));

        } catch (IOException ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int enviarInvitacion(String nombre) {
        for (Jugador j : jugadores) {
            if (j.getNombreJugador().equals(nombre)) {
                try {
                    out = new DataOutputStream(j.getJugadorOrigen().getOutputStream());
                    out.writeUTF(this.nombreJugador);
                    return 1;
                } catch (IOException ex) {
                    Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        return 0;
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

}
