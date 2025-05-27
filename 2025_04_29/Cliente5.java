/**
 * Cliente5 - Cliente TCP para comunicação com objetos serializados
 * 
 * Esta aplicação demonstra comunicação cliente-servidor usando serialização
 * de objetos Java. O cliente estabelece conexão TCP com o servidor e envia
 * um objeto MensagemTeste serializado através de ObjectOutputStream.
 * 
 * Funcionalidades:
 * - Conexão TCP com timeout configurável
 * - Serialização e envio de objetos
 * - Tratamento de exceções de rede
 * - Configuração de endereço e porta do servidor
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
 * Cliente TCP que envia objetos serializados para o servidor.
 */
public class Cliente5 {
    
    // ==================== CONSTANTES ====================
    
    /** Endereço padrão do servidor */
    private static final String ENDERECO_SERVIDOR_PADRAO = "localhost";
    
    /** Porta padrão do servidor */
    private static final int PORTA_SERVIDOR_PADRAO = 4321;
    
    /** Timeout para conexão em milissegundos */
    private static final int TIMEOUT_CONEXAO = 5000;
    
    /** Mensagem padrão a ser enviada */
    private static final String MENSAGEM_PADRAO = "Mensagem de teste enviada pelo Cliente5";
    
    /** Logger para registrar eventos do cliente */
    private static final Logger LOGGER = Logger.getLogger(Cliente5.class.getName());
    
    // ==================== MÉTODO PRINCIPAL ====================
    
    /**
     * Método principal do cliente.
     * 
     * Argumentos aceitos:
     * - args[0]: Endereço do servidor (opcional, padrão: localhost)
     * - args[1]: Porta do servidor (opcional, padrão: 4321)
     * - args[2]: Mensagem a enviar (opcional, padrão: mensagem teste)
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        Cliente5 cliente = new Cliente5();
        
        try {
            // Processar argumentos da linha de comando
            ConfiguracaoCliente config = processarArgumentos(args);
            
            // Executar comunicação com servidor
            cliente.executarComunicacao(config);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Erro nos argumentos: " + e.getMessage());
            exibirAjuda();
            System.exit(1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro inesperado no cliente", e);
            System.err.println("Erro inesperado: " + e.getMessage());
            System.exit(1);
        }
    }
    
    // ==================== MÉTODOS PRINCIPAIS ====================
    
    /**
     * Executa a comunicação com o servidor.
     * 
     * @param config Configuração do cliente
     * @throws IOException se houver erro de comunicação
     */
    private void executarComunicacao(ConfiguracaoCliente config) throws IOException {
        System.out.println("=== Cliente de Comunicação por Objetos ===");
        System.out.println("Servidor: " + config.endereco + ":" + config.porta);
        System.out.println("Mensagem: " + config.mensagem);
        System.out.println("Timeout: " + TIMEOUT_CONEXAO + "ms");
        System.out.println();
        
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        
        try {
            // Estabelecer conexão com o servidor
            socket = estabelecerConexao(config.endereco, config.porta);
            
            // Criar stream de saída para objetos
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            LOGGER.info("ObjectOutputStream criado com sucesso");
            
            // Criar e enviar objeto mensagem
            MensagemTeste mensagem = new MensagemTeste(config.mensagem);
            enviarMensagem(outputStream, mensagem);
            
            System.out.println("✓ Objeto enviado com sucesso!");
            System.out.println("✓ Comunicação concluída");
            
        } finally {
            // Fechar recursos
            fecharRecursos(outputStream, socket);
        }
    }
    
    /**
     * Estabelece conexão TCP com o servidor.
     * 
     * @param endereco Endereço do servidor
     * @param porta Porta do servidor
     * @return Socket conectado
     * @throws IOException se houver erro na conexão
     */
    private Socket estabelecerConexao(String endereco, int porta) throws IOException {
        System.out.print("Conectando ao servidor " + endereco + ":" + porta + "...");
        
        Socket socket = new Socket();
        InetSocketAddress enderecoServidor = new InetSocketAddress(endereco, porta);
        
        try {
            socket.connect(enderecoServidor, TIMEOUT_CONEXAO);
            System.out.println(" ✓ Conectado!");
            
            LOGGER.info("Conexão estabelecida com " + enderecoServidor);
            return socket;
            
        } catch (SocketTimeoutException e) {
            System.out.println(" ✗ Timeout!");
            throw new IOException("Timeout ao conectar com servidor: " + e.getMessage(), e);
        } catch (ConnectException e) {
            System.out.println(" ✗ Falhou!");
            throw new IOException("Não foi possível conectar ao servidor: " + e.getMessage(), e);
        }
    }
    
    /**
     * Envia objeto mensagem para o servidor.
     * 
     * @param outputStream Stream de saída para objetos
     * @param mensagem Objeto mensagem a ser enviado
     * @throws IOException se houver erro no envio
     */
    private void enviarMensagem(ObjectOutputStream outputStream, MensagemTeste mensagem) 
            throws IOException {
        System.out.print("Enviando objeto mensagem...");
        
        try {
            outputStream.writeObject(mensagem);
            outputStream.flush();
            System.out.println(" ✓ Enviado!");
            
            LOGGER.info("Objeto enviado: " + mensagem);
            
        } catch (IOException e) {
            System.out.println(" ✗ Erro!");
            throw new IOException("Erro ao enviar objeto: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fecha todos os recursos de rede de forma segura.
     * 
     * @param outputStream Stream de saída a ser fechado
     * @param socket Socket a ser fechado
     */
    private void fecharRecursos(ObjectOutputStream outputStream, Socket socket) {
        System.out.print("Fechando conexão...");
        
        // Fechar stream
        if (outputStream != null) {
            try {
                outputStream.close();
                LOGGER.fine("ObjectOutputStream fechado");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar ObjectOutputStream", e);
            }
        }
        
        // Fechar socket
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println(" ✓ Conexão fechada!");
                LOGGER.info("Socket fechado com sucesso");
            } catch (IOException e) {
                System.out.println(" ✗ Erro ao fechar!");
                LOGGER.log(Level.WARNING, "Erro ao fechar socket", e);
            }
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Processa argumentos da linha de comando.
     * 
     * @param args Argumentos recebidos
     * @return Configuração do cliente
     * @throws IllegalArgumentException se argumentos inválidos
     */
    private static ConfiguracaoCliente processarArgumentos(String[] args) {
        String endereco = ENDERECO_SERVIDOR_PADRAO;
        int porta = PORTA_SERVIDOR_PADRAO;
        String mensagem = MENSAGEM_PADRAO;
        
        try {
            if (args.length > 0 && !args[0].trim().isEmpty()) {
                endereco = args[0].trim();
            }
            
            if (args.length > 1) {
                porta = Integer.parseInt(args[1]);
                if (porta < 1 || porta > 65535) {
                    throw new IllegalArgumentException("Porta deve estar entre 1 e 65535");
                }
            }
            
            if (args.length > 2 && !args[2].trim().isEmpty()) {
                mensagem = args[2].trim();
            }
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Porta deve ser um número válido");
        }
        
        return new ConfiguracaoCliente(endereco, porta, mensagem);
    }
    
    /**
     * Exibe ajuda sobre uso do programa.
     */
    private static void exibirAjuda() {
        System.out.println("\nUso: java Cliente5 [endereço] [porta] [mensagem]");
        System.out.println("  endereço: Endereço do servidor (padrão: localhost)");
        System.out.println("  porta:    Porta do servidor (padrão: 4321)");
        System.out.println("  mensagem: Mensagem a enviar (padrão: mensagem teste)");
        System.out.println("\nExemplos:");
        System.out.println("  java Cliente5");
        System.out.println("  java Cliente5 192.168.1.100");
        System.out.println("  java Cliente5 localhost 8080");
        System.out.println("  java Cliente5 servidor.com 4321 \"Olá servidor!\"");
    }
    
    // ==================== CLASSES AUXILIARES ====================
    
    /**
     * Classe para armazenar configuração do cliente.
     */
    private static class ConfiguracaoCliente {
        final String endereco;
        final int porta;
        final String mensagem;
        
        ConfiguracaoCliente(String endereco, int porta, String mensagem) {
            this.endereco = endereco;
            this.porta = porta;
            this.mensagem = mensagem;
        }
    }
}