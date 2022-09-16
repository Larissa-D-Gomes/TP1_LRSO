/* TP1 - LRSO
 * Ciência da Computação - PUC Minas
 * Professor: Marco Antonio da Silva Barbosa
 * Dupla: 
 *   Ana Laura Fernandes de Oliveira - 680715
 *   Larissa Domingues Gomes         - 650525
 */

import java.net.Socket;
import java.io.*;
import java.util.*;

class Cliente {
   public static void main(String[] args) {
      try
      {
         String IPServ = "127.0.0.1";
         int portServ = 1000;
            
         Socket socktCli = new Socket (IPServ, portServ);

          // Inicializando writer
         ObjectOutputStream writer = new ObjectOutputStream(socktCli.getOutputStream());
         // Enviando pacote de conexão
         writer.writeObject("Cliente Conectado!");
			writer.flush(); 

         // Inicializando reader
         ObjectInputStream reader = new ObjectInputStream (socktCli.getInputStream());
         Scanner in = new Scanner(System.in);

         while(true) {
            // Recebendo mensagem do servidor
            String msg = reader.readObject().toString();

            // Finalizar conexão ao receber flag para encerrar
            if(msg.equals("END CONNECTION")){
               break;
            }
            System.out.println(msg);
            // Respondendo mensagem ao servidor
            String clientIn = in.nextLine();
            writer.writeObject(clientIn);
         }

         // Finalizando conexão do cliente no servidor
         socktCli.close();
         System.out.println("Conexão finalizada.");
      }
      catch(Exception e) // Tratamento de erro
      {
         System.out.println("Erro: " + e.toString());
      }


   }
}