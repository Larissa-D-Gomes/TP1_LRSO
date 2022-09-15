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
         int PortaServ = 1000;
            
         Socket socktCli = new Socket ( IPServ,PortaServ);

         // Reader
         ObjectOutputStream sCliOut = new ObjectOutputStream(socktCli.getOutputStream());
         sCliOut.writeObject("Cliente Conectado!");//ESCREVE NO PACOTE
			sCliOut.flush(); //ENVIA O PACOTE

         ObjectInputStream reader = new ObjectInputStream (socktCli.getInputStream());
         Scanner in = new Scanner(System.in);

         boolean cont = true;

         while(cont) {
            String msg = reader.readObject().toString();
            System.out.println(msg);

            String clientIn = in.nextLine();
            sCliOut.writeObject(clientIn);
         }
        
         /*System.out.println(" -C- Detalhes conexao :" + socktCli.toString()); //DETALHAMENTO (EXTRA)
            
         //CRIA UM PACOTE DE SA DA PARA ENVIAR MENSAGENS, ASSOCIANDO-O   CONEX O (c)
         ObjectOutputStream sCliOut = new ObjectOutputStream(socktCli.getOutputStream());
         sCliOut.writeObject("MENSAGEM TESTE");//ESCREVE NO PACOTE
         System.out.println(" -C- Enviando mensagem...");
         sCliOut.flush(); //ENVIA O PACOTE

         //CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO   CONEX O (c)
         ObjectInputStream sCliIn = new ObjectInputStream (socktCli.getInputStream());
         System.out.println(" -C- Recebendo mensagem...");
         String strMsg = sCliIn.readObject().toString(); */

         socktCli.close();
         System.out.println(" -C- Conexao finalizada...");
      }
      catch(Exception e) // Tratamento de erro
      {
         System.out.println("Erro: " + e.toString());
      }


   }
}