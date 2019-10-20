/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pedro
 */
public abstract class Mediator {

    public ServerSocket server;
    public int puerto = 9500;
    //Conexion[] cliente = new Conexion[11];
    public List<Conexion> conexiones = new ArrayList<Conexion>();

    public void init() {
        Socket socket;
        try {
            server = new ServerSocket(puerto);
            System.out.println("Esperando peticiones por el puerto " + puerto);
            while (true) {
                socket = server.accept();
                DataInputStream buffEntrada = new DataInputStream(socket.getInputStream());
                DataOutputStream buffSalida = new DataOutputStream(socket.getOutputStream());

                Conexion conexion = new Conexion(socket, buffEntrada, buffSalida);
                conexion.start();
                conexiones.add(conexion);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

}
