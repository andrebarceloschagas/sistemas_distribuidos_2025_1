/**
 * ServidorThread - Thread responsável por gerenciar comunicação com cliente individual
 * 
 * Esta classe implementa uma thread que trata comunicação bidirecional com um cliente TCP.
 * Cada instância gerencia uma conexão de socket separada, permitindo que o servidor
 * principal atenda múltiplos clientes simultaneamente.
 * 
 * Funcionalidades:
 * - Recebe mensagens do cliente via socket TCP
 * - Envia confirmações de recebimento
 * - Gerencia encerramento gracioso da conexão
 * - Trata exceções de comunicação de rede
 * 
 * @author Sistema de Comunicação TCP
 * @version 2.0
 * @since 2025-04-22
 */

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Thread para gerenciar comunicação individual com cliente TCP.
 * Implementa protocolo de mensagens com confirmação de recebimento.
 */
public class ServidorThread extends Thread {
    
    // ==================== CONSTANTES ====================
    
    /** Mensagem padrão de confirmação enviada ao cliente */
    private static final String MENSAGEM_CONFIRMACAO = "Confirmação de Mensagem Recebida no Servidor";
    
    /** Comando para encerrar a comunicação */
    private static final String COMANDO_TERMINAR = "terminar";
    
    /** Logger para registrar eventos da thread */
    private static final Logger LOGGER = Logger.getLogger(ServidorThread.class.getName());
    
    // ==================== ATRIBUTOS ====================
    
    /** Socket de comunicação com o cliente */
    private final Socket socketCliente;
    
    /** Identificador único da thread para logs */
    private final String identificadorThread;
    
    // ==================== CONSTRUTORES ====================
    
    /**
     * Construtor da thread do servidor.
     * 
     * @param socketCliente Socket estabelecido com o cliente
     * @throws IllegalArgumentException se o socket for nulo
     */
    public ServidorThread(Socket socketCliente) {
        super();
        
        if (socketCliente == null) {
            throw new IllegalArgumentException("Socket do cliente não pode ser nulo");
        }
        
        this.socketCliente = socketCliente;
        this.identificadorThread = "Thread-" + Thread.currentThread().getId() + 
                                 "-" + socketCliente.getRemoteSocketAddress();
        
        LOGGER.info("Nova thread criada para cliente: " + socketCliente.getRemoteSocketAddress());
    }
    
    // ==================== MÉTODOS PRINCIPAIS ====================
    
    /**
     * Método principal da thread - gerencia comunicação com cliente.
     * 
     * Implementa loop de comunicação que:
     * 1. Recebe mensagens do cliente
     * 2. Processa e registra mensagens
     * 3. Envia confirmação de recebimento
     * 4. Verifica comando de encerramento
     * 5. Fecha recursos adequadamente
     */
    @Override
    public void run() {
        LOGGER.info(identificadorThread + " - Iniciando comunicação com cliente");
        
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        
        try {
            // Inicializar streams de comunicação
            outputStream = new DataOutputStream(socketCliente.getOutputStream());
            inputStream = new DataInputStream(socketCliente.getInputStream());
            
            LOGGER.info(identificadorThread + " - Streams inicializados com sucesso");
            
            // Loop principal de comunicação
            executarLoopComunicacao(inputStream, outputStream);
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, identificadorThread + " - Erro de I/O na comunicação", e);
            System.err.println("Erro de comunicação com cliente " + 
                             socketCliente.getRemoteSocketAddress() + ": " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, identificadorThread + " - Erro inesperado", e);
            System.err.println("Erro inesperado na thread: " + e.getMessage());
        } finally {
            // Garantir fechamento de recursos
            fecharRecursos(outputStream, inputStream);
        }
        
        LOGGER.info(identificadorThread + " - Thread encerrada");
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Executa o loop principal de comunicação com o cliente.
     * 
     * @param inputStream Stream de entrada para receber mensagens
     * @param outputStream Stream de saída para enviar confirmações
     * @throws IOException se houver erro de comunicação
     */
    private void executarLoopComunicacao(DataInputStream inputStream, 
                                       DataOutputStream outputStream) throws IOException {
        
        int contadorMensagens = 0;
        
        while (true) {
            try {
                // Receber mensagem do cliente
                String mensagemRecebida = inputStream.readUTF();
                contadorMensagens++;
                
                // Registrar mensagem recebida
                System.out.println("\n" + identificadorThread + " - Mensagem #" + contadorMensagens + 
                                 " recebida: " + mensagemRecebida);
                LOGGER.info(identificadorThread + " - Mensagem recebida: " + mensagemRecebida);
                
                // Enviar confirmação
                outputStream.writeUTF(MENSAGEM_CONFIRMACAO + " #" + contadorMensagens);
                outputStream.flush();
                
                // Verificar comando de encerramento
                if (COMANDO_TERMINAR.equalsIgnoreCase(mensagemRecebida.trim())) {
                    System.out.println(identificadorThread + " - Comando de encerramento recebido");
                    LOGGER.info(identificadorThread + " - Encerrando comunicação por solicitação do cliente");
                    break;
                }
                
            } catch (IOException e) {
                // Cliente pode ter desconectado abruptamente
                if (e.getMessage().contains("Connection reset") || 
                    e.getMessage().contains("EOF")) {
                    System.out.println(identificadorThread + " - Cliente desconectou");
                    LOGGER.info(identificadorThread + " - Cliente desconectou abruptamente");
                    break;
                } else {
                    throw e; // Re-lançar outros erros de I/O
                }
            }
        }
        
        System.out.println(identificadorThread + " - Total de mensagens processadas: " + contadorMensagens);
    }
    
    /**
     * Fecha todos os recursos de rede de forma segura.
     * 
     * @param outputStream Stream de saída a ser fechado
     * @param inputStream Stream de entrada a ser fechado
     */
    private void fecharRecursos(DataOutputStream outputStream, DataInputStream inputStream) {
        System.out.println(identificadorThread + " - Fechando recursos de rede...");
        
        // Fechar streams
        if (outputStream != null) {
            try {
                outputStream.close();
                LOGGER.fine(identificadorThread + " - OutputStream fechado");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, identificadorThread + " - Erro ao fechar OutputStream", e);
            }
        }
        
        if (inputStream != null) {
            try {
                inputStream.close();
                LOGGER.fine(identificadorThread + " - InputStream fechado");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, identificadorThread + " - Erro ao fechar InputStream", e);
            }
        }
        
        // Fechar socket
        if (socketCliente != null && !socketCliente.isClosed()) {
            try {
                socketCliente.close();
                LOGGER.info(identificadorThread + " - Socket fechado com sucesso");
                System.out.println(identificadorThread + " - Conexão encerrada");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, identificadorThread + " - Erro ao fechar socket", e);
                System.err.println("Erro ao fechar socket: " + e.getMessage());
            }
        }
    }
    
    // ==================== MÉTODOS DE INFORMAÇÃO ====================
    
    /**
     * Retorna informações sobre o cliente conectado.
     * 
     * @return String com endereço do cliente ou "Desconhecido" se socket inválido
     */
    public String getInfoCliente() {
        if (socketCliente != null && !socketCliente.isClosed()) {
            return socketCliente.getRemoteSocketAddress().toString();
        }
        return "Cliente desconhecido";
    }
    
    /**
     * Verifica se a conexão com o cliente está ativa.
     * 
     * @return true se a conexão está ativa, false caso contrário
     */
    public boolean isConexaoAtiva() {
        return socketCliente != null && !socketCliente.isClosed() && socketCliente.isConnected();
    }
}