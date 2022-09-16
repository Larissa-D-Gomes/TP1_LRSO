/* TP1 - LRSO
 * Ciência da Computação - PUC Minas
 * Professor: Marco Antonio da Silva Barbosa
 * Dupla: 
 *   Ana Laura Fernandes de Oliveira - 680715
 *   Larissa Domingues Gomes         - 650525
 * Venda de ingressos
 */

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.*;

public class Servidor 
{
	// Métodos e atributos do estáticos
	static int PortServ = 1000;
	static ServerSocket socktServ;
	static int qtyId = 0; // Quantidade de ids no servidor

	// Quantidade de ingressos para determinados shows
	static int ticketsColdplay = 50000;
	static int ticketsTaylor = 50000;
	static int ticketsHarry = 50000;


	public static void main (String args[])
	{
		try
		{	
			init(); // Inicializando servidor 
			initConexoes();
		}
		catch(Exception e) // Tratamento de erros
		{
			System.out.println("ERRO: \n" + e.toString());
		}
	}

	// Método para inicializar servidor
	static void init() throws Exception{
		// Inicializando Sevidor
		System.out.println("Conectando... ");
		socktServ = new ServerSocket(PortServ);
	}

	// Método para inicializar conexões de clientes no servidor
	static void initConexoes()throws Exception {

		while(true){		
			Socket conSer = socktServ.accept(); // Aceitando conexão do cliente
			System.out.println("Conectado ao cliente:" + conSer.toString());
			Thread thread = new Service(conSer); // Criando uma thread para novo cliente conectado
          	thread.start(); // Exceutando run da thread
		}
	}

	
	// Classe para executar serviço para cada cliente conectado por meio de threads
	static class Service extends Thread{
		// Dados  do cliente
		int clientId;  
		String clientName = "";
		String clientDocument = "";
		String clientEmail = "";
		int clientTicketsColdplay = 0;
		int clientTicketsTaylor = 0;
		int clientTicketsHarry = 0;

		// Leitores do socket
		ObjectInputStream reader;   
		ObjectOutputStream writer;
		// Socket do cliente
		Socket conSer;

		// Construtors
		public Service(Socket conSer) throws Exception {
			this.clientId = ++qtyId;
			this.conSer = conSer;
			this.reader = new ObjectInputStream(this.conSer.getInputStream());
			this.writer = new ObjectOutputStream(this.conSer.getOutputStream());

			// Receber confirmação de conexão do cliente
			reader.readObject(); 
		}

		public void run(){

			try{
				menu();

				this.writer.writeObject("END CONNECTION");
				conSer.close();
				System.out.println("Conexao com cliente [" + clientId + "] finalizada.");
			} catch(Exception e){

			}
		}

		public void menu() throws Exception{
			int option;

			String menu = "--------- Menu ---------\n" +
						"1) Cadastrar\n" +
						"2) Comprar ingressos\n" +
						"3) Listar ingressos comprados\n" +
						"0) Sair\n" +
						"-----------------------------------\n" +
						"Digite o código da açãoao que você deseja: ";

			do {
				// Escrevendo Menu
				writer.flush();
				this.writer.writeObject(menu);

				// Lendo resposta de usuário
				String opInt = reader.readObject().toString(); 
				option = Integer.parseInt(opInt);

				switch(option) {
					case 0:
						break;
					case 1:
						cadastre();
						break;
					case 2:
						orderMenu();
						break;
					case 3:
						listTickets();
						break;
					default:
						this.writer.writeObject("Codigo nao encontrado! Aperte enter para continua...");
				}

				// Finalizar loop de menu para encerrar conexão
				if(option == 0){
					break;
				}
				reader.readObject().toString();
			} while(true);
		}

		/* Método para executar cadastro de cliente */
		public void cadastre() throws Exception{
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

		/* Método para executar compra de cliente */
		public void orderMenu() throws Exception{
			if(this.clientName.equals("")) {
				this.writer.writeObject("Faça o cadastro primeiro.");
				writer.flush();
				return;
			}
			String ticketsMenu = "--------- Menu de compra ---------\n" + "1) Coldplay \n" + "2) Taylor Swift \n" + "3) Harry Styles \n" + "----------------------------------- \n" + "Digite o codigo do show que voce deseja comprar o ingresso: ";
			// Enviando menu para cliente
			this.writer.writeObject(ticketsMenu);

			// Aguardando resposta de usuário
			String ticketInt = reader.readObject().toString(); 
			// Solicitando o ingresso desejado pelo cliente
			int ticketId = Integer.parseInt(ticketInt);
			
			// Perguntando quantidade para o cliente
			this.writer.writeObject("\nDigite quantos ingressos voce deseja comprar:");
			// Lendo quantidade desejada pelo cliente
			String qt = reader.readObject().toString();
			int quantidade = Integer.parseInt(qt);
			
			// Verificando se há ingressos suficientes para o pedido
			String response = updateTicketQty(ticketId, quantidade);

			// Caso não ingressos suficientes, alertar erro
			if(!response.equals("OK")){
				response += "\nAperte enter para continuar...";
				this.writer.writeObject(response);
				return;
			}

			response = "\n" + quantidade + " Ingresso(s) adquirido(s) pelo cliente " + clientId + " do show:\n";
			
			// Atualizando ingressos do cliente
			switch(ticketId) {
				case 1:
					clientTicketsColdplay += quantidade;
					response = response + "Coldplay";

					break;
				case 2:
					clientTicketsTaylor += quantidade;
					response = response + "Taylor Swift";

					break;
				case 3:
					clientTicketsHarry += quantidade;
					response = response + "Harry Styles";				

					break;
				default:
					response = "Codigo do show nao encontrado! Tente novamente.";
					
			}
			response += "\nAperte enter para continuar...";
			this.writer.writeObject(response);

		}

		/* Método para listar tickets comprado pelo cliente */
		public void listTickets() throws Exception{
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
				total += "\nIngressos Harry Styles: " + clientTicketsHarry;
				writer.flush();
			}

			if(this.clientTicketsTaylor != 0) {
				total += "\nIngressos Taylor Swift: " + clientTicketsTaylor;	
			}

			if(total.equals("")) {
				total += "Você não tem nenhum ingresso. :(";
			}

			total += "\nAperte enter para continuar...";
			this.writer.writeObject(total);
		}
		
		/* Método para atualizar quantidade de ingressos total após compras */
		static synchronized String updateTicketQty(int ticketId, int qty) {
			switch(ticketId) {
				case 1:
					if(qty > ticketsColdplay) 
						return "Não há ingressos suficientes...";
					ticketsColdplay -= qty;
					break;
				case 2:
					if(qty > ticketsTaylor) 
						return "Não há ingressos suficientes...";

					ticketsTaylor -= qty;
					break;
				case 3:
					if(qty > ticketsHarry) 
						return "Não há ingressos suficientes...";
					ticketsHarry -= qty;		
					break;
				}
			return "OK";
		}

		
	}
}