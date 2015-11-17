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
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class Asignar extends Thread {

    Socket socket;
    DataInputStream in;

    public Asignar(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.

        try {

            in = new DataInputStream(socket.getInputStream());

            String nombre = in.readUTF();

            if (!buscarActualizar(nombre)) {

                Jugador jugador = new Jugador();
                jugador.setJugadorOrigen(socket);
                jugador.setNombreJugador(nombre);
                jugador.start();
                Jugador.jugadores.add(jugador);

            }
            for (Jugador j : Jugador.jugadores) {

              System.out.println(Jugador.jugadores.size());

            }
        } catch (IOException ex) {
            Logger.getLogger(ChessSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean buscarActualizar(String nombreJugador) {
        for (Jugador j : Jugador.getJugadores()) {
            if (j.getNombreJugador().equals(nombreJugador)) {
                System.out.println("existe");
                Jugador jugador = new Jugador();
                jugador.setJugadorDestino(j.getJugadorDestino());
                jugador.setTiempo(j.getTiempo());
                jugador.setJugadorOrigen(socket);
                jugador.start();
                j = jugador;

                return true;
            }
        }
        return false;
    }

}
