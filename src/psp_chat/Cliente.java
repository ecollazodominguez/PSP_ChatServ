/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static psp_chat.Server.selecPuerto;

/**
 *
 * @author Mirroriced
 */
public class Cliente {

    private static DataInputStream entradaServ = null;
    private static DataOutputStream salidaServ = null;
    private Socket clienteSocket = null;

    public static void main(String args[]) {

     Server serv = new Server();
     
        try {
            System.out.println("Creando socket cliente");
            Socket clienteSocket = new Socket();
            System.out.println("Estableciendo la conexion");

            String ip = JOptionPane.showInputDialog("Indique IP donde alojar su servidor");
            int port = selecPuerto();

            InetSocketAddress addr = new InetSocketAddress(ip, port);
            clienteSocket.connect(addr);

            String usuario = JOptionPane.showInputDialog("Indique nombre de usuario");
            boolean conectado = true;
            entradaServ = new DataInputStream(clienteSocket.getInputStream());
            salidaServ = new DataOutputStream(clienteSocket.getOutputStream());

            salidaServ.writeUTF(usuario);
            salidaServ.flush();
            String mensaje = JOptionPane.showInputDialog("");
           while(conectado){
            while (!mensaje.equals("/bye")) {
                try {
                    salidaServ.writeUTF(mensaje);
                    salidaServ.flush();

                    String mensaje2 = entradaServ.readUTF();
                    System.out.println(mensaje2);

                
                    mensaje = JOptionPane.showInputDialog("");
                } catch (IOException ioe) {
                    System.out.println("Enviando error: " + ioe.getMessage());
                }
            }
            conectado = false;
           }
            salidaServ.writeUTF(mensaje);
            salidaServ.flush();
            clienteSocket.close();
            entradaServ.close();
            salidaServ.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
