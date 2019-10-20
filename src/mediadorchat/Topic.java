/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static mediadorchat.Conexion.clientesConectados;


public class Topic {

    public static Vector<Conexion> usuarios = new Vector();
    public String topicTitle;

    public static Vector<Conexion> getUserList() {
        return usuarios;
    }

    public static void setUserList(Vector<Conexion> conexion) {
        Topic.usuarios = usuarios;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void Publish(String msg) {

        for (int i = 0; i < usuarios.size(); i++) {
            if (i != usuarios.indexOf(this)) {
                usuarios.get(i).EnviarMensaje(msg);
            }
        }


    }


}
