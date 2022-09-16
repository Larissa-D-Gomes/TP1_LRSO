/* TP1 - LRSO
 * Ciência da Computação - PUC Minas
 * Professor: Marco Antonio da Silva Barbosa
 * Dupla: 
 *   Ana Laura Fernandes de Oliveira - 680715
 *   Larissa Domingues Gomes         - 650525
 */
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.*;

public class Servidor extends Thread 
{
	// Métodos e atributos do estáticos
	static int PortaServidor = 1000;
	static int ticketsColdplay = 50000;
	static int ticketsTaylor = 50000;
	static int ticketsHarry = 50000;
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
		System.out.println("Conectando... ");
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
	int clientId = 0; // Variável para testar conexao
	List<String> ticketsList = new ArrayList<>();
	String clientName = "";
	String clientDocument = "";
	String clientEmail = "";
	int clientTicketsColdplay = 0;
	int clientTicketsTaylor = 0;
	int clientTicketsHarry = 0;
	ObjectInputStream reader;   
	ObjectOutputStream writer;

	Socket conSer;

	// Constructor
  	public Servidor(Socket conSer) throws Exception {
    	this.clientId = ++cg;
    	this.conSer = conSer;
		this.reader = new ObjectInputStream(conSer.getInputStream());
		this.writer = new ObjectOutputStream(this.conSer.getOutputStream());

		// Receber confirmação de conexão do cliente
		reader.readObject(); 
 	}

	public void run(){

		try{
			System.out.println(" -S- Recebendo mensagem..." + clientId);
			System.out.println(" -S- Cliente " + clientId + " na fila para adquirir ingresso:");

			menu();

			conSer.close();
			System.out.println(" -S- Conexao finalizada...");
		} catch(Exception e){

		}
	}

	public void menu() throws Exception{
		int opcao;

		String menu = "--------- Menu ---------\n" +
					  "1) Cadastrar\n" +
					  "2) Comprar ingressos\n" +
					  "3) Listar ingressos comprados\n" +
					  "0) Sair\n" +
					  "-----------------------------------\n" +
					  "Digite o codigo da acao que voce deseja: ";

		do {
			// Escrevendo Menu
			writer.flush();
			this.writer.writeObject(menu);

			// Aguardando resposta de usuário
			String opInt = reader.readObject().toString(); 
			System.out.println("Op: " + opInt);
			opcao = Integer.parseInt(opInt);

			switch(opcao) {
				case 1:
					cadastrar();
					break;
				case 2:
					menuDeCompras();
					break;
				case 3:
					listarIngressos();
					break;
				default:
					System.out.println("Codigo nao encontrado! Tente novamente.");
			}
			reader.readObject().toString();
		} while(opcao != 0);
	}

	public void cadastrar() throws Exception{
		this.writer.writeObject("Digite o seu nome:");
		clientName = reader.readObject().toString();
		this.writer.writeObject("Digite o seu CPF ou RG:");
		clientDocument = reader.readObject().toString();
		this.writer.writeObject("Digite o seu email:");
		clientEmail = reader.readObject().toString();

		String successMessage = "Cadastro finalizado! \n " +
								"\nNome: " + clientName + 
								"\nDocumento: " + clientDocument +
								"\nE-mail: " + clientEmail +
								"\nAperte enter para continuar...";

		this.writer.writeObject(successMessage);

	}

	public void menuDeCompras() {
		Scanner in = new Scanner(System.in);
		int ingressoId;

		System.out.println("--------- Menu de compra ---------");
		System.out.println("1) Coldplay");
		System.out.println("2) Taylor Swift");
		System.out.println("3) Harry Styles");
		System.out.println("-----------------------------------");
		System.out.println("Digite o codigo do show que voce deseja comprar o ingresso:");
		
		ingressoId = in.nextInt();
		
		System.out.println("Digite quantos ingressos voce deseja comprar:");
		int quantidade;
		quantidade = in.nextInt();
		
		System.out.println(quantidade + "Ingresso(s) adquirido(s) pelo cliente " + clientId + " do show:");
		
		switch(ingressoId) {
			case 1:
				clientTicketsColdplay += quantidade;
				System.out.println("Coldplay");
				break;
			case 2:
				clientTicketsTaylor += quantidade;
				System.out.println("Taylor Swift");
				break;
			case 3:
				clientTicketsHarry += quantidade;
				System.out.println("Harry Styles");
				break;
			default:
				System.out.println("Codigo do show nao encontrado! Tente novamente.");
		}
	}

	public void listarIngressos() throws Exception{
		System.out.println("listar");
		if(this.clientName.equals("")) {
			this.writer.writeObject("Faça o cadastro primeiro.");
			writer.flush();
			return;
		}

		String total = "";

		if(this.clientTicketsColdplay != 0) {
			total += "\nIngressos Coldplay: " + clientTicketsColdplay;
		}

		if(this.clientTicketsHarry != 0) {
			total += "\nIngressos Harry Styles: " + clientTicketsColdplay;
			writer.flush();
		}

		if(this.clientTicketsTaylor != 0) {
			total += "\nIngressos Taylor Swift: " + clientTicketsColdplay;	
		}

		if(total.equals("")) {
			total += "Você não tem nenhum ingresso. :(";
		}

		total += "\nAperte enter para continuar...";
		this.writer.writeObject(total);
	}
}