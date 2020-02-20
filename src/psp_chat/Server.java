/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_chat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dam2
 */
public class Server extends Thread {

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private DataInputStream entradaCli = null;

    public Server(Socket socket) {
        clientSocket = socket;
    }

    public void run() {

        try {
            //Marcamos que el cliente está conectado
            boolean conectado = true;
            System.out.println("Arrancando hilo");

            //Abrimos la entrada para recibir los mensajes del cliente
            entradaCli = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            //Recogemos el nombre de usuario y indicamos que se ha conectado
            String usuario = entradaCli.readUTF();
            System.out.println(usuario + " se ha conectado.");
            
            while (conectado) {
                //Mientras esté el cliente el servidor recibirá el texto
                String mensaje = entradaCli.readUTF();

                if (!mensaje.equals("/bye")) {
                    //Si el texto no es /bye se muestra el mensaje junto con el nombre de usuario
                    System.out.println(usuario + ": " + mensaje);
                } else {
                    //si es /bye el boolean se vuelve false y desconectamos
                    conectado = false;
                }
            }
            //Indicamos que el usuario se desconecta y cerramos la entrada.
            System.out.println(usuario + " se ha desconectado");
            entradaCli.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        try {
            //realizamos la iniciación del serviodr
            System.out.println("Creando socket servidor");

            ServerSocket serverSocket = new ServerSocket();

            System.out.println("Realizando el bind");

            //pedimos por teclado la ip y el puerto a establecer
            String ip = JOptionPane.showInputDialog("Indique IP donde alojar su servidor");
            int port = selecPuerto();

            InetSocketAddress addr = new InetSocketAddress(ip, port);
            serverSocket.bind(addr);
            System.out.println("Inicializando servicio en: " + ip);
            System.out.println("Inicializando servicio en: " + port);

            System.out.println("Aceptando conexiones");

            while (serverSocket != null) {
                
                //empezamos la conexión con el cliente y empezamos un hilo
                Socket newSocket = serverSocket.accept();
                System.out.println("Conexión recibida");
                System.out.println("cliente: " + newSocket);

                Server hilo = new Server(newSocket);
                hilo.start();
            }
            System.out.println("Conexion recibida");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Método para escribir el puerto sin que sea posible escribir letras y con una longitud mayor a 2
    public static int selecPuerto() {
        String puerto = JOptionPane.showInputDialog("Indique puerto donde alojar su servidor");

        if (puerto.length() >= 2 && puerto.matches("[0-9]+")) {
            int port = Integer.parseInt(puerto);
            return port;
        } else {
            selecPuerto();
            return 0;
        }
    }
}
