import java.net.*;
import java.io.*;

/**
 * Servidor TCP/IP para comunicação com múltiplos clientes.
 * 
 * Este programa implementa um servidor TCP que aceita conexões na porta 4321
 * e envia uma mensagem de boas-vindas para cada cliente conectado.
 * 
 * Funcionalidades:
 * - Aceita múltiplas conexões simultâneas
 * - Envia mensagem em formato UTF-8
 * - Execução contínua até ser interrompido
 * - Log detalhado de conexões
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class Servidor2 {
    
    /** Porta onde o servidor irá escutar */
    private static final int PORTA_SERVIDOR = 4321;
    
    /** Backlog máximo de conexões pendentes */
    private static final int BACKLOG_MAXIMO = 300;
    
    /** Mensagem padrão enviada aos clientes */
    private static final String MENSAGEM_PADRAO = "Mensagem do Servidor - Tutorial de Java!";
    
    /** Contador de clientes conectados */
    private static int contadorClientes = 0;
    
    /**
     * Método principal que inicia o servidor TCP.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Servidor2 servidor = new Servidor2();
        servidor.iniciarServidor();
    }
    
    /**
     * Inicia o servidor e entra no loop principal de aceitação de conexões.
     * 
     * O servidor continuará executando até ser interrompido manualmente (Ctrl+C).
     */
    public void iniciarServidor() {
        ServerSocket serverSocket = null;
        
        try {
            // Cria o socket do servidor na porta especificada
            serverSocket = new ServerSocket(PORTA_SERVIDOR, BACKLOG_MAXIMO);
            
            System.out.println("=================================");
            System.out.println("    SERVIDOR TCP INICIADO    ");
            System.out.println("=================================");
            System.out.println("Porta: " + PORTA_SERVIDOR);
            System.out.println("Backlog máximo: " + BACKLOG_MAXIMO);
            System.out.println("Aguardando conexões...");
            System.out.println("(Pressione Ctrl+C para parar)");
            System.out.println("---------------------------------");
            
            // Loop principal do servidor
            while (true) {
                try {
                    // Aceita uma nova conexão de cliente
                    Socket clienteSocket = serverSocket.accept();
                    contadorClientes++;
                    
                    // Log da conexão estabelecida
                    String enderecoCliente = clienteSocket.getInetAddress().getHostAddress();
                    int portaCliente = clienteSocket.getPort();
                    
                    System.out.println("Cliente #" + contadorClientes + " conectado:");
                    System.out.println("  Endereço: " + enderecoCliente + ":" + portaCliente);
                    System.out.println("  Timestamp: " + java.time.LocalDateTime.now());
                    
                    // Processa o cliente
                    processarCliente(clienteSocket);
                    
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão do cliente: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erro fatal ao iniciar servidor na porta " + PORTA_SERVIDOR);
            System.err.println("Detalhes: " + e.getMessage());
            System.err.println("Verifique se a porta não está sendo usada por outro processo.");
        } finally {
            // Garante o fechamento do servidor
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Servidor encerrado.");
                } catch (IOException e) {
                    System.err.println("Erro ao fechar servidor: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Processa um cliente conectado enviando a mensagem padrão.
     * 
     * @param clienteSocket socket do cliente conectado
     */
    private void processarCliente(Socket clienteSocket) {
        DataOutputStream outputStream = null;
        
        try {
            // Cria stream de saída para enviar dados ao cliente
            outputStream = new DataOutputStream(clienteSocket.getOutputStream());
            
            // Envia mensagem UTF-8 para o cliente
            outputStream.writeUTF(MENSAGEM_PADRAO);
            
            System.out.println("  → Mensagem enviada com sucesso!");
            
        } catch (IOException e) {
            System.err.println("  ✗ Erro ao enviar mensagem para cliente: " + e.getMessage());
        } finally {
            // Fecha recursos do cliente
            fecharRecursosCliente(clienteSocket, outputStream);
        }
    }
    
    /**
     * Fecha os recursos relacionados ao cliente de forma segura.
     * 
     * @param clienteSocket socket do cliente
     * @param outputStream stream de saída do cliente
     */
    private void fecharRecursosCliente(Socket clienteSocket, DataOutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            System.err.println("  ✗ Erro ao fechar stream de saída: " + e.getMessage());
        }
        
        try {
            if (clienteSocket != null && !clienteSocket.isClosed()) {
                clienteSocket.close();
                System.out.println("  ✓ Conexão com cliente encerrada.");
                System.out.println("---------------------------------");
            }
        } catch (IOException e) {
            System.err.println("  ✗ Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
    