import java.net.*;
import java.io.*;

/**
 * Cliente TCP/IP para comunicação com servidor.
 * 
 * Este programa implementa um cliente TCP que se conecta a um servidor
 * na porta 4321 e recebe uma mensagem do tipo UTF-8.
 * 
 * Funcionalidades:
 * - Conecta ao servidor usando timeout
 * - Recebe mensagem em formato UTF-8
 * - Tratamento adequado de exceções
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class Cliente2 {
    
    /** Endereço do servidor */
    private static final String SERVIDOR_HOST = "localhost";
    
    /** Porta do servidor */
    private static final int SERVIDOR_PORTA = 4321;
    
    /** Timeout de conexão em milissegundos */
    private static final int TIMEOUT_CONEXAO = 1000;
    
    /**
     * Método principal que executa o cliente TCP.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     * @throws IOException em caso de erro de E/S
     */
    public static void main(String[] args) throws IOException {
        Cliente2 cliente = new Cliente2();
        cliente.executarCliente();
    }
    
    /**
     * Executa a lógica principal do cliente.
     * 
     * Estabelece conexão com o servidor, recebe mensagem e encerra a conexão.
     */
    public void executarCliente() {
        Socket socket = null;
        DataInputStream inputStream = null;
        
        try {
            // Cria socket e configura endereço do servidor
            socket = new Socket();
            InetSocketAddress endereco = new InetSocketAddress(SERVIDOR_HOST, SERVIDOR_PORTA);
            
            System.out.println("Conectando ao servidor " + SERVIDOR_HOST + ":" + SERVIDOR_PORTA);
            
            // Estabelece conexão com timeout configurado
            socket.connect(endereco, TIMEOUT_CONEXAO);
            
            System.out.println("Conexão estabelecida com sucesso!");
            
            // Cria stream de entrada para receber dados
            inputStream = new DataInputStream(socket.getInputStream());
            
            // Recebe mensagem UTF-8 do servidor
            String mensagemRecebida = inputStream.readUTF();
            
            // Exibe mensagem recebida
            System.out.println("Mensagem recebida do servidor: " + mensagemRecebida);
            
        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor.");
            System.err.println("Verifique se o servidor está rodando em " + SERVIDOR_HOST + ":" + SERVIDOR_PORTA);
        } catch (SocketTimeoutException e) {
            System.err.println("Erro: Timeout de conexão. Servidor não respondeu em " + TIMEOUT_CONEXAO + "ms");
        } catch (IOException e) {
            System.err.println("Erro de E/S durante a comunicação: " + e.getMessage());
        } finally {
            // Garante o fechamento dos recursos
            fecharRecursos(socket, inputStream);
        }
    }
    
    /**
     * Fecha os recursos de rede de forma segura.
     * 
     * @param socket socket a ser fechado
     * @param inputStream stream de entrada a ser fechada
     */
    private void fecharRecursos(Socket socket, DataInputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
                System.out.println("Stream de entrada fechada.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar stream de entrada: " + e.getMessage());
        }
        
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Conexão encerrada com sucesso.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket: " + e.getMessage());
        }
    }
}
