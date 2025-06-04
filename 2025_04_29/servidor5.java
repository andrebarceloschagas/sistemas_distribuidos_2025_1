/**
 * Servidor5 - Servidor TCP para recepção de objetos serializados
 * 
 * Esta aplicação implementa um servidor TCP que recebe objetos serializados
 * de clientes através de ObjectInputStream. O servidor fica em loop contínuo
 * aguardando conexões e processando objetos MensagemTeste recebidos.
 * 
 * Funcionalidades:
 * - Servidor TCP multi-cliente (sequencial)
 * - Deserialização de objetos recebidos
 * - Tratamento robusto de exceções
 * - Logs detalhados de atividade
 * - Configuração flexível de porta e backlog
 * 
 * @author Sistema de Comunicação por Objetos
 * @version 2.0
 * @since 2025-04-29
 */

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servidor TCP que recebe e processa objetos serializados.
 */
public class Servidor5 {
    
    // ==================== CONSTANTES ====================
    
    /** Porta padrão do servidor */
    private static final int PORTA_SERVIDOR = 4321;
    
    /** Backlog para conexões pendentes */
    private static final int BACKLOG_CONEXOES = 50;
    
    /** Logger para registrar eventos do servidor */
    private static final Logger LOGGER = Logger.getLogger(Servidor5.class.getName());
    
    // ==================== ATRIBUTOS ====================
    
    /** Socket do servidor */
    private ServerSocket serverSocket;
    
    /** Contador de clientes atendidos */
    private int contadorClientes;
    
    /** Flag para controlar execução do servidor */
    private volatile boolean executando;
    
    // ==================== MÉTODO PRINCIPAL ====================
    
    /**
     * Método principal do servidor.
     * 
     * Argumentos aceitos:
     * - args[0]: Porta do servidor (opcional, padrão: 4321)
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        int porta = PORTA_SERVIDOR;
        
        // Processar argumentos
        if (args.length > 0) {
            try {
                porta = Integer.parseInt(args[0]);
                if (porta < 1 || porta > 65535) {
                    throw new IllegalArgumentException("Porta deve estar entre 1 e 65535");
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro: Porta deve ser um número válido");
                exibirAjuda();
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.err.println("Erro: " + e.getMessage());
                exibirAjuda();
                System.exit(1);
            }
        }
        
        // Iniciar servidor
        Servidor5 servidor = new Servidor5();
        try {
            servidor.iniciarServidor(porta);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro crítico no servidor", e);
            System.err.println("Erro crítico: " + e.getMessage());
            System.exit(1);
        }
    }
    
    // ==================== MÉTODOS PRINCIPAIS ====================
    
    /**
     * Inicia o servidor na porta especificada.
     * 
     * @param porta Porta para bind do servidor
     * @throws IOException se houver erro na inicialização
     */
    public void iniciarServidor(int porta) throws IOException {
        System.out.println("=== Servidor de Comunicação por Objetos ===");
        System.out.println("Porta: " + porta);
        System.out.println("Backlog: " + BACKLOG_CONEXOES);
        System.out.println();
        
        try {
            // Criar socket do servidor
            serverSocket = new ServerSocket(porta, BACKLOG_CONEXOES);
            executando = true;
            contadorClientes = 0;
            
            System.out.println("✓ Servidor iniciado com sucesso!");
            System.out.println("✓ Aguardando conexões de clientes...");
            System.out.println("✓ Pressione Ctrl+C para parar o servidor");
            System.out.println();
            
            LOGGER.info("Servidor iniciado na porta " + porta);
            
            // Configurar shutdown hook para encerramento gracioso
            configurarShutdownHook();
            
            // Loop principal do servidor
            executarLoopPrincipal();
            
        } catch (IOException e) {
            System.err.println("✗ Erro ao iniciar servidor: " + e.getMessage());
            throw e;
        } finally {
            pararServidor();
        }
    }
    
    /**
     * Executa o loop principal de atendimento a clientes.
     */
    private void executarLoopPrincipal() {
        while (executando) {
            Socket socketCliente = null;
            
            try {
                // Aguardar conexão de cliente
                socketCliente = serverSocket.accept();
                contadorClientes++;
                
                String enderecoCliente = socketCliente.getRemoteSocketAddress().toString();
                System.out.println("[Cliente #" + contadorClientes + "] Conectado: " + enderecoCliente);
                LOGGER.info("Cliente conectado: " + enderecoCliente);
                
                // Processar cliente
                processarCliente(socketCliente);
                
            } catch (SocketException e) {
                if (executando) {
                    System.err.println("Erro no socket do servidor: " + e.getMessage());
                    LOGGER.log(Level.WARNING, "Erro no socket do servidor", e);
                }
                // Se não está executando, é encerramento normal
            } catch (IOException e) {
                System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                LOGGER.log(Level.WARNING, "Erro ao aceitar conexão", e);
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Erro inesperado no loop principal", e);
            } finally {
                // Garantir fechamento do socket do cliente
                if (socketCliente != null && !socketCliente.isClosed()) {
                    try {
                        socketCliente.close();
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Erro ao fechar socket do cliente", e);
                    }
                }
            }
        }
    }
    
    /**
     * Processa comunicação com um cliente específico.
     * 
     * @param socketCliente Socket do cliente conectado
     * @throws IOException se houver erro de comunicação
     * @throws ClassNotFoundException se houver erro na deserialização
     */
    private void processarCliente(Socket socketCliente) throws IOException, ClassNotFoundException {
        String identificadorCliente = "[Cliente #" + contadorClientes + "]";
        ObjectInputStream inputStream = null;
        
        try {
            System.out.println(identificadorCliente + " Criando stream de entrada...");
            inputStream = new ObjectInputStream(socketCliente.getInputStream());
            
            System.out.println(identificadorCliente + " Aguardando objeto...");
            
            // Receber objeto do cliente
            Object objetoRecebido = inputStream.readObject();
            
            if (objetoRecebido instanceof MensagemTeste) {
                MensagemTeste mensagem = (MensagemTeste) objetoRecebido;
                
                System.out.println(identificadorCliente + " Objeto recebido com sucesso!");
                System.out.println(identificadorCliente + " Tipo: " + objetoRecebido.getClass().getSimpleName());
                System.out.println(identificadorCliente + " Conteúdo:");
                
                // Exibir conteúdo da mensagem
                mensagem.exibir();
                
                LOGGER.info("Objeto recebido de cliente: " + mensagem);
                
            } else {
                System.out.println(identificadorCliente + " Objeto de tipo inesperado: " + 
                                 objetoRecebido.getClass().getSimpleName());
                LOGGER.warning("Objeto de tipo inesperado recebido: " + objetoRecebido.getClass());
            }
            
            System.out.println(identificadorCliente + " ✓ Processamento concluído");
            System.out.println();
            
        } catch (ClassNotFoundException e) {
            System.err.println(identificadorCliente + " ✗ Erro: Classe não encontrada");
            throw e;
        } catch (IOException e) {
            System.err.println(identificadorCliente + " ✗ Erro de comunicação: " + e.getMessage());
            throw e;
        } finally {
            // Fechar stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Erro ao fechar ObjectInputStream", e);
                }
            }
        }
    }
    
    /**
     * Para o servidor de forma gracioso.
     */
    public void pararServidor() {
        System.out.println("\nEncerrando servidor...");
        executando = false;
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("✓ Servidor encerrado com sucesso!");
                System.out.println("✓ Total de clientes atendidos: " + contadorClientes);
                LOGGER.info("Servidor encerrado. Clientes atendidos: " + contadorClientes);
            } catch (IOException e) {
                System.err.println("Erro ao fechar servidor: " + e.getMessage());
                LOGGER.log(Level.WARNING, "Erro ao fechar ServerSocket", e);
            }
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Configura hook para encerramento gracioso do servidor.
     */
    private void configurarShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSinal de encerramento recebido...");
            pararServidor();
        }, "ShutdownHook"));
    }
    
    /**
     * Exibe ajuda sobre uso do programa.
     */
    private static void exibirAjuda() {
        System.out.println("\nUso: java Servidor5 [porta]");
        System.out.println("  porta: Porta do servidor (padrão: 4321)");
        System.out.println("\nExemplos:");
        System.out.println("  java Servidor5");
        System.out.println("  java Servidor5 8080");
    }
    
    // ==================== MÉTODOS DE INFORMAÇÃO ====================
    
    /**
     * Retorna o número de clientes atendidos.
     * 
     * @return Número de clientes atendidos
     */
    public int getContadorClientes() {
        return contadorClientes;
    }
    
    /**
     * Verifica se o servidor está executando.
     * 
     * @return true se executando, false caso contrário
     */
    public boolean isExecutando() {
        return executando;
    }
}
