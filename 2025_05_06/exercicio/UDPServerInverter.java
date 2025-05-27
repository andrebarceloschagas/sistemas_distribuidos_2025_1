
/**
 * UDPServerInverter - Servidor UDP para Inversão de Strings
 * 
 * Esta aplicação implementa um servidor UDP que recebe strings de clientes
 * e retorna as mesmas strings invertidas. Demonstra comunicação UDP básica
 * com processamento sequencial de requisições.
 * 
 * Funcionalidades:
 * - Servidor UDP de inversão de strings
 * - Processamento sequencial de clientes
 * - Logs detalhados de atividade
 * - Tratamento robusto de exceções
 * - Estatísticas de processamento
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
 * Servidor UDP que inverte strings recebidas de clientes.
 */
public class UDPServerInverter {

    // ==================== CONSTANTES ====================

    /** Porta padrão do servidor */
    private static final int PORTA_SERVIDOR = 6789;

    /** Tamanho do buffer para recepção */
    private static final int TAMANHO_BUFFER = 1000;

    /** Logger para registrar eventos do servidor */
    private static final Logger LOGGER = Logger.getLogger(UDPServerInverter.class.getName());

    // ==================== ATRIBUTOS ====================

    /** Socket do servidor UDP */
    private DatagramSocket serverSocket;

    /** Contador de mensagens processadas */
    private int contadorMensagens;

    /** Flag para controlar execução do servidor */
    private volatile boolean executando;

    // ==================== MÉTODO PRINCIPAL ====================

    /**
     * Método principal do servidor.
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        UDPServerInverter servidor = new UDPServerInverter();

        try {
            servidor.iniciarServidor();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro crítico no servidor", e);
            System.err.println("Erro crítico: " + e.getMessage());
            System.exit(1);
        }
    }

    // ==================== MÉTODOS PRINCIPAIS ====================

    /**
     * Inicia o servidor UDP de inversão.
     * 
     * @throws SocketException se houver erro na criação do socket
     */
    public void iniciarServidor() throws SocketException {
        System.out.println("=== Servidor UDP de Inversão ===");
        System.out.println("Porta: " + PORTA_SERVIDOR);
        System.out.println("Funcionalidade: Inversão de strings");
        System.out.println("Pressione Ctrl+C para parar o servidor\n");

        try {
            // Criar socket do servidor
            serverSocket = new DatagramSocket(PORTA_SERVIDOR);
            executando = true;
            contadorMensagens = 0;

            System.out.println("✓ Servidor iniciado com sucesso!");
            System.out.println("✓ Aguardando requisições de inversão...\n");

            LOGGER.info("Servidor UDP de inversão iniciado na porta " + PORTA_SERVIDOR);

            // Configurar shutdown hook
            configurarShutdownHook();

            // Loop principal do servidor
            executarLoopPrincipal();

        } catch (SocketException e) {
            System.err.println("✗ Erro ao criar socket do servidor: " + e.getMessage());
            throw e;
        } finally {
            pararServidor();
        }
    }

    /**
     * Executa o loop principal de processamento de requisições.
     */
    private void executarLoopPrincipal() {
        byte[] buffer = new byte[TAMANHO_BUFFER];

        while (executando && !serverSocket.isClosed()) {
            try {
                // Preparar datagrama para recepção
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                System.out.println("[Aguardando] Próxima requisição de inversão...");

                // Receber requisição (bloqueante)
                serverSocket.receive(receivePacket);
                contadorMensagens++;

                // Processar requisição
                processarRequisicaoInversao(receivePacket);

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
                LOGGER.log(Level.SEVERE, "Erro inesperado no processamento", e);
            }
        }
    }

    /**
     * Processa uma requisição de inversão de string.
     * 
     * @param receivePacket Datagrama recebido do cliente
     * @throws IOException se houver erro na comunicação
     */
    private void processarRequisicaoInversao(DatagramPacket receivePacket) throws IOException {
        // Extrair informações do cliente
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        // Extrair mensagem original
        String receivedMessage = new String(
                receivePacket.getData(),
                0,
                receivePacket.getLength(),
                "UTF-8");

        String identificador = "[#" + contadorMensagens + "]";

        System.out.println(identificador + " Requisição de inversão recebida:");
        System.out.println("  - Cliente: " + clientAddress.getHostAddress() + ":" + clientPort);
        System.out.println("  - Mensagem original: \"" + receivedMessage + "\"");
        System.out.println("  - Tamanho: " + receivedMessage.length() + " caracteres");

        try {
            // Inverter a string recebida
            String reversedMessage = new StringBuilder(receivedMessage).reverse().toString();
            System.out.println("  - Mensagem invertida: \"" + reversedMessage + "\"");

            // Preparar resposta
            byte[] sendBuffer = reversedMessage.getBytes("UTF-8");
            DatagramPacket sendPacket = new DatagramPacket(
                    sendBuffer,
                    sendBuffer.length,
                    clientAddress,
                    clientPort);

            // Enviar resposta
            System.out.print(identificador + " Enviando string invertida...");
            serverSocket.send(sendPacket);
            System.out.println(" ✓ Enviado!");

            LOGGER.info("String invertida enviada para " + clientAddress + ":" + clientPort +
                    " - Original: '" + receivedMessage + "' -> Invertida: '" + reversedMessage + "'");

        } catch (IOException e) {
            System.out.println(" ✗ Erro no envio!");
            throw new IOException("Erro ao enviar string invertida: " + e.getMessage(), e);
        }

        System.out.println(identificador + " ✓ Inversão processada com sucesso\n");
    }

    /**
     * Para o servidor de forma gracioso.
     */
    public void pararServidor() {
        System.out.println("\nEncerrando servidor de inversão...");
        executando = false;

        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            System.out.println("✓ Servidor de inversão encerrado com sucesso!");
            System.out.println("✓ Total de strings invertidas: " + contadorMensagens);
            LOGGER.info("Servidor de inversão encerrado. Strings processadas: " + contadorMensagens);
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
        }, "ShutdownHook-UDP-Inverter"));
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

    /**
     * Retorna estatísticas do servidor.
     * 
     * @return String com estatísticas formatadas
     */
    public String getEstatisticas() {
        return String.format("Servidor UDP Inverter - Mensagens processadas: %d, Status: %s",
                contadorMensagens,
                executando ? "Executando" : "Parado");
    }
}