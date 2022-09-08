/* TP1 - LRSO
 * Ciência da Computação - PUC Minas
 * Professor: Marco Antonio da Silva Barbosa
 * Dupla: 
 *   Ana Laura Fernandes de Oliveira - XXXXXX
 *   Larissa Domingues Gomes         - 650525
 */
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Servidor extends Thread 
{
	// Métodos e atributos do estáticos
	static int PortaServidor = 1000;
	static ServerSocket socktServ;
	static int cg = 0;

	public static void main (String args[])
	{

		try
		{	
			init();
			initConexoes();
		}
		catch(Exception e) //SE OCORRER ALGUMA EXCESS�O, ENT�O DEVE SER TRATADA (AMIGAVELMENTE)
		{
			System.out.println("ERRO: \n" + e.toString());
		}
	}

	// Método para inicializar servidor
	static void init() throws Exception{
		// Inicializando Sevidor
		System.out.println("Connectando... ");
		socktServ = new ServerSocket(PortaServidor);
	}

		/* Método para inicializar conexões de clientes
	 *
	 */
	static void initConexoes()throws Exception {

		while(true){		
			Socket conSer = socktServ.accept(); //RECEBE CONEX�O E CRIA UM NOVO CANAL (p) NO SENTIDO CONTR�RIO (SERVIDOR -> CLIENTE)
			System.out.println(" -S- Conectado ao cliente ->" + conSer.toString());
			Thread thread = new Servidor(conSer);
          	thread.start();
			
		}
	}

	// Métodos e atributos da classe
	int c = 0; // Variável para testar conexao
	Socket conSer;

	// Constructor
  	public Servidor(Socket conSer){
    	this.c = cg++;
    	this.conSer = conSer;
 	}

	public void run(){

		try{
			System.out.println(" -S- Recebendo mensagem..." + c);
			//CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO � CONEX�O (p)
			/*ObjectInputStream sServIn = new ObjectInputStream(getInputStream());
			System.out.println(" -S- Recebendo mensagem...");
			Object msgIn = sServIn.readObject(); //ESPERA (BLOQUEADO) POR UM PACOTE
			System.out.println(" -S- Recebido: " + msgIn.toString());
					
			//CRIA UM PACOTE DE SA�DA PARA ENVIAR MENSAGENS, ASSOCIANDO-O � CONEX�O (p)
			ObjectOutputStream sSerOut = new ObjectOutputStream(getOutputStream());
			sSerOut.writeObject("RETORNO " + msgIn.toString() + " - TCP"); //ESCREVE NO PACOTE
			System.out.println(" -S- Enviando mensagem resposta...");
			sSerOut.flush(); //ENVIA O PACOTE*/
				
			//FINALIZA A CONEX�O
			//socktServ.close();
			conSer.close();
			System.out.println(" -S- Conexao finalizada...");
		} catch(Exception e){

		}
	}
}