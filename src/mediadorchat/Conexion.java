/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediadorchat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import sun.security.krb5.internal.crypto.Des;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Conexion extends Thread {

    Socket cliente1 = null;
    DataInputStream buffEntrada;
    DataOutputStream buffSalida;
    DataInputStream teclado;
    public static Vector<Conexion> clientesConectados = new Vector();
    public static List<Topic> topics = new ArrayList<Topic>();
    Comandos comandos = new Comandos();
    Topic topic_a = new Topic();
    CommandLineParser parser = new DefaultParser();


    public Conexion(Socket cliente, DataInputStream buffEntrada, DataOutputStream buffSalida) {
        cliente1 = cliente;
        this.buffEntrada = buffEntrada;
        this.buffSalida = buffSalida;
        clientesConectados.add(this);
        Topic topic = topics.stream().
                filter(current -> "BroadCasts".equals(current.getTopicTitle()))
                .findAny()
                .orElse(null);
        topic.getUserList().add(this);
    }

    public void run() {

        Topic Desarrollo4 = new Topic();
        Desarrollo4.setTopicTitle("Desarrollo4");
        topics.add(Desarrollo4);

        Topic Pruebas = new Topic();
        Pruebas.setTopicTitle("Pruebas");
        topics.add(Pruebas);

        try {

            System.out.println("Num: " + clientesConectados.size());

            while (true) {

                ArrayList<String> args = new ArrayList<String>();
                String mensaje = buffEntrada.readUTF();
                //System.out.println(mensaje);
                Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
                Matcher regexMatcher = regex.matcher(mensaje);
                // regexMatcher.
                int index = 0;
                while (regexMatcher.find()) {
                    if (regexMatcher.group(1) != null)
                        args.add(regexMatcher.group(1));
                    else if (regexMatcher.group(2) != null)
                        args.add(regexMatcher.group(2));
                    else
                        args.add(regexMatcher.group());
                }

                if (args.get(0).equals("enviar")) {
                    // CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));
                    if (commandLine != null) {
                        if (commandLine.hasOption("m")) {
                            String messageBody = commandLine.getOptionValue("m").trim();
                            if (commandLine.hasOption("t")) {
                                String topicName = commandLine.getOptionValue("t").trim();
                                if (args.get(4).equals("BroadCasts")) {
                                    topic_a = new Topic();
                                    topic_a.Publish(messageBody);

                                }
                            }
                        }
                    }
                } else if (args.get(0).equals("crear")) {

                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

                    if (commandLine != null) {

                        if (commandLine.hasOption("t")) {


                            String topicName = commandLine.getOptionValue("t").trim();

                            if (FindTopic(topicName) == false) {
                                topic_a.setTopicTitle(topicName);
                                topics.add(topic_a);

                                Display(topics);

                                EnviarMensaje("Se creo el topico " + topicName);


                            } else {

                                EnviarMensaje("El topico ya existe");

                            }

                        }

                    }

                } else if (args.get(0).equals("remove")) {

                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

                    if (commandLine != null) {

                        if (commandLine.hasOption("t")) {

                            String topicName = commandLine.getOptionValue("t").trim();

                            topic_a = GetTopic(topicName);

                            if (topic_a != null) {
                                topics.remove(topic_a);
                                Display(topics);

                                EnviarMensaje("El topico " + topicName + " fue removido");

                            } else if (topic_a == null) {
                                EnviarMensaje("El topico " + topicName + " no existe");
                            }


                        }


                    }

                } else if (args.get(0).equals("suscribe")) {

                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

                    if (commandLine != null) {

                        if (commandLine.hasOption("t")) {

                            String topicName = commandLine.getOptionValue("t").trim();
                            topic_a = GetTopic(topicName);

                            if (topic_a != null) {
                                topic_a.setUserList(clientesConectados);
                                DisplayUsers(topic_a.getUserList());

                                EnviarMensaje("Suscrito al topico" + topicName);

                            } else {
                                EnviarMensaje("No se encontro el topico " + topicName);
                            }

                        }
                    }
                } else if (args.get(0).equals("unsuscribe")) {

                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

                    if (commandLine != null) {

                        if (commandLine.hasOption("t")) {

                            String topicName = commandLine.getOptionValue("t").trim();
                            topic_a = GetTopic(topicName);

                            if (topic_a != null) {
                                clientesConectados.remove(this);

                                topic_a.setUserList(clientesConectados);
                                DisplayUsers(topic_a.getUserList());

                                EnviarMensaje("Dado de baja del topico " + topicName);

                            } else {
                                EnviarMensaje("No se encontro el topico" + topicName);

                            }

                        }
                    }
                } else if (args.get(0).equals("topic")) {

                    CommandLine commandLine = comandos.parse(args.toArray(new String[args.size()]));

                    if (commandLine != null) {

                        if (commandLine.hasOption("l")) {
                            Display(topics);
                            EnviarMensaje("Debug prueba topics");

                        }

                    }

                }
                System.out.println("*   --- --- --- --- ---    *");
            }
        } catch (Exception exception) {
        }
    }

    ;


    public void EnviarMensaje(String mensaje) {
        try {
            buffSalida.writeUTF(mensaje);
            buffSalida.close();
        } catch (Exception e) {
        }

    }

    public void Display(List<Topic> topics) {
        for (Topic i : topics) {
            System.out.println("Topic -> " + i.getTopicTitle());
        }
    }

    public String DisplayClient(List<Topic> topics) {
        String line = "";
        for (Topic i : topics) {
            line = line + i.getTopicTitle() + "\n";
        }
        return line;
    }

    public Topic GetTopic(String topicName) {
        for (Topic topic : topics) {
            if (topic.getTopicTitle().equals(topicName)) {
                return topic;
            }
        }
        return null;
    }

    public void DisplayUsers(Vector<Conexion> usuarios) {
        for (Conexion e :
                usuarios) {
            System.out.println(e.getName());
        }
    }

    public boolean FindTopic(String name) {
        for (Topic i : topics) {
            if (i.getTopicTitle() == name) {
                return true;
            }
        }
        return false;
    }


}
