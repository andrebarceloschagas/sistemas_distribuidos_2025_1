
/**
 * ServidorUDP - Servidor de eco UDP
 * 
 * Esta aplicação implementa um servidor UDP que funciona como um eco,
 * devolvendo as mensagens recebidas de volta aos clientes. O servidor
 * fica em loop contínuo aguardando datagramas e respondendo imediatamente.
 * 
 * Funcionalidades:
 * - Servidor UDP de eco (echo server)
 * - Atendimento contínuo de clientes
 * - Logs detalhados de atividade
 * - Tratamento robusto de exceções
 * - Configuração flexível de porta
 * - Estatísticas de atendimento
 * 
 * @author Sistema de Comunicação UDP
 * @version 2.0
 * @since 2025-05-06
 */

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servidor UDP que implementa funcionalidade de eco.
 */
public class ServidorUDP {

    // ==================== CONSTANTES ====================

    /** Porta padrão do servidor */
    private static final int PORTA_PADRAO = 6789;

    /** Tamanho do buffer para recepção de dados */
    private static final int TAMANHO_BUFFER = 1024;

    /** Logger para registrar eventos do servidor */
    private static final Logger LOGGER = Logger.getLogger(ServidorUDP.class.getName());

    // ==================== ATRIBUTOS ====================

    /** Socket do servidor UDP */
    private DatagramSocket socket;

    /** Contador de mensagens processadas */
    private int contadorMensagens;

    /** Flag para controlar execução do servidor */
    private volatile boolean executando;

    // ==================== MÉTODO PRINCIPAL ====================

    /**
     * Método principal do servidor UDP.
     * 
     * Argumentos aceitos:
     * - args[0]: Porta do servidor (opcional, padrão: 6789)
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        int porta = PORTA_PADRAO;

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
        ServidorUDP servidor = new ServidorUDP();
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
     * Inicia o servidor UDP na porta especificada.
     * 
     * @param porta Porta para bind do servidor
     * @throws SocketException se houver erro na criação do socket
     */
    public void iniciarServidor(int porta) throws SocketException {
        System.out.println("=== Servidor UDP de Eco ===");
        System.out.println("Porta: " + porta);
        System.out.println("Buffer: " + TAMANHO_BUFFER + " bytes");
        System.out.println();

        try {
            // Criar socket UDP
            socket = new DatagramSocket(porta);
            executando = true;
            contadorMensagens = 0;

            System.out.println("✓ Servidor UDP iniciado com sucesso!");
            System.out.println("✓ Aguardando datagramas de clientes...");
            System.out.println("✓ Pressione Ctrl+C para parar o servidor");
            System.out.println();

            LOGGER.info("Servidor UDP iniciado na porta " + porta);

            // Configurar shutdown hook
            configurarShutdownHook();

            // Loop principal do servidor
            executarLoopPrincipal();

        } catch (SocketException e) {
            System.err.println("✗ Erro ao criar socket UDP: " + e.getMessage());
            throw e;
        } finally {
            pararServidor();
        }
    }

    /**
     * Executa o loop principal de atendimento de clientes.
     */
    private void executarLoopPrincipal() {
        byte[] buffer = new byte[TAMANHO_BUFFER];

        while (executando) {
            try {
                // Preparar datagrama para recepção
                DatagramPacket pacoteRequest = new DatagramPacket(buffer, buffer.length);

                System.out.println("[Aguardando] Próximo datagrama...");

                // Receber datagrama (bloqueante)
                socket.receive(pacoteRequest);
                contadorMensagens++;

                // Processar e responder
                processarRequest(pacoteRequest);

            } catch (SocketException e) {
                if (executando) {
                    System.err.println("Erro no socket UDP: " + e.getMessage());
                    LOGGER.log(Level.WARNING, "Erro no socket UDP", e);
                }
                // Se não está executando, é encerramento normal
            } catch (IOException e) {
                System.err.println("Erro de I/O: " + e.getMessage());
                LOGGER.log(Level.WARNING, "Erro de I/O na comunicação", e);
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Erro inesperado no loop principal", e);
            }
        }
    }

    /**
     * Processa request de cliente e envia resposta (eco).
     * 
     * @param pacoteRequest Datagrama recebido do cliente
     * @throws IOException se houver erro no envio da resposta
     */
    private void processarRequest(DatagramPacket pacoteRequest) throws IOException {
        // Extrair informações do request
        InetAddress enderecoCliente = pacoteRequest.getAddress();
        int portaCliente = pacoteRequest.getPort();
        int tamanhoMensagem = pacoteRequest.getLength();

        String identificador = "[#" + contadorMensagens + "]";

        System.out.println(identificador + " Request recebido:");
        System.out.println("  - Cliente: " + enderecoCliente.getHostAddress() + ":" + portaCliente);
        System.out.println("  - Tamanho: " + tamanhoMensagem + " bytes");

        try {
            // Extrair conteúdo da mensagem
            String mensagemRecebida = new String(
                    pacoteRequest.getData(),
                    0,
                    tamanhoMensagem,
                    "UTF-8");

            System.out.println("  - Conteúdo: \"" + mensagemRecebida + "\"");

            // Preparar resposta (eco)
            DatagramPacket pacoteResposta = new DatagramPacket(
                    pacoteRequest.getData(),
                    tamanhoMensagem,
                    enderecoCliente,
                    portaCliente);

            // Enviar resposta
            System.out.print(identificador + " Enviando eco...");
            socket.send(pacoteResposta);
            System.out.println(" ✓ Enviado!");

            LOGGER.info("Eco enviado para " + enderecoCliente + ":" + portaCliente +
                    " - " + mensagemRecebida);

        } catch (IOException e) {
            System.out.println(" ✗ Erro no envio!");
            throw new IOException("Erro ao enviar resposta: " + e.getMessage(), e);
        }

        System.out.println(identificador + " ✓ Processamento concluído");
        System.out.println();
    }

    /**
     * Para o servidor de forma gracioso.
     */
    public void pararServidor() {
        System.out.println("\nEncerrando servidor UDP...");
        executando = false;

        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("✓ Servidor UDP encerrado com sucesso!");
            System.out.println("✓ Total de mensagens processadas: " + contadorMensagens);
            LOGGER.info("Servidor UDP encerrado. Mensagens processadas: " + contadorMensagens);
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
        }, "ShutdownHook-UDP"));
    }

    /**
     * Exibe ajuda sobre uso do programa.
     */
    private static void exibirAjuda() {
        System.out.println("\nUso: java ServidorUDP [porta]");
        System.out.println("  porta: Porta do servidor (padrão: 6789)");
        System.out.println("\nExemplos:");
        System.out.println("  java ServidorUDP");
        System.out.println("  java ServidorUDP 8080");
    }

    // ==================== MÉTODOS DE INFORMAÇÃO ====================

    /**
     * Retorna o número de mensagens processadas.
     * 
     * @return Número de mensagens processadas
     */
    public int getContadorMensagens() {
        return contadorMensagens;
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
