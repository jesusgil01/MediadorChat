/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Cliente extends Colega {


    public Cliente(String nombre, String ip, int puerto) {
        this.puerto = puerto;
        this.nombre = nombre;
        this.ip = ip;
    }

    public void init() {

        try {
            cliente = new Socket(ip, puerto);
            buffSalida = new DataOutputStream(cliente.getOutputStream());
            buffEntrada = new DataInputStream(cliente.getInputStream());
            teclado = new DataInputStream(System.in);
            buffSalida.writeUTF(nombre);
            buffSalida.flush();
            RecibirDatos();
            EscribirDatos();
        } catch (Exception e) {
            System.out.println("no funcino");
            System.exit(0);
        }
    }

    public static void main(String[] args) {

        System.out.println("enviar -m '<Cuerpo del mensaje>' -t '<Nombre del topico>' ");
        System.out.println("crear -t '<Nombre del topico>' ");
        System.out.println("remove -t '<Nombre del topico>' ");
        System.out.println("suscribe -t '<Nombre del topico>' ");
        System.out.println("unsuscribe -t '<Nombre del topico>' ");
        System.out.println("");

        Cliente cliente = new Cliente("Gil", "10.10.152.149", 9500);
        cliente.init();
    }


}
