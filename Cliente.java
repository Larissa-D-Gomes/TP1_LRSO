/* TP1 - LRSO
 * Ciência da Computação - PUC Minas
 * Professor: Marco Antonio da Silva Barbosa
 * Dupla: 
 *   Ana Laura Fernandes de Oliveira - XXXXXX
 *   Larissa Domingues Gomes         - 650525
 */

import java.net.Socket;
import java.io.*;

class Cliente {
   public static void main(String[] args) {
      try
      {
         String IPServ = "127.0.0.1";
         int PortaServ = 1000;
            
         System.out.println(" -C- Conectando ao servidor ->" +  IPServ + ":" +PortaServ);
         Socket socktCli = new Socket ( IPServ,PortaServ);
        /* System.out.println(" -C- Detalhes conexao :" + socktCli.toString()); //DETALHAMENTO (EXTRA)
            
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