
/**
 * ClienteUDP - Cliente para comunicação UDP com eco
 * 
 * Esta aplicação implementa um cliente UDP que envia mensagens para um servidor
 * e aguarda resposta (eco). Demonstra comunicação UDP básica com timeout,
 * tratamento de exceções e configuração flexível de parâmetros.
 * 
 * Funcionalidades:
 * - Envio de datagramas UDP
 * - Recepção de resposta com timeout
 * - Configuração via argumentos de linha de comando
 * - Tratamento robusto de exceções de rede
 * - Logs detalhados de atividade
 * 
 * Argumentos: <servidor> <porta> <mensagem>
 * Exemplo: java ClienteUDP localhost 6789 "mensagem teste"
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
 * Cliente UDP para comunicação com servidor de eco.
 */
public class ClienteUDP {

    // ==================== CONSTANTES ====================

    /** Servidor padrão */
    private static final String SERVIDOR_PADRAO = "localhost";

    /** Porta padrão do servidor */
    private static final int PORTA_PADRAO = 6789;

    /** Mensagem padrão */
    private static final String MENSAGEM_PADRAO = "MENSAGEM TESTE UDP";

    /** Timeout padrão para recepção em milissegundos */
    private static final int TIMEOUT_RECEPCAO = 10000;

    /** Tamanho do buffer para resposta */
    private static final int TAMANHO_BUFFER = 1024;

    /** Logger para registrar eventos do cliente */
    private static final Logger LOGGER = Logger.getLogger(ClienteUDP.class.getName());

    // ==================== MÉTODO PRINCIPAL ====================

    /**
     * Método principal do cliente UDP.
     * 
     * Argumentos aceitos:
     * - args[0]: Endereço do servidor (opcional, padrão: localhost)
     * - args[1]: Porta do servidor (opcional, padrão: 6789)
     * - args[2]: Mensagem a enviar (opcional, padrão: MENSAGEM TESTE UDP)
     * 
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        ClienteUDP cliente = new ClienteUDP();

        try {
            // Processar argumentos
            ConfiguracaoCliente config = processarArgumentos(args);

            // Executar comunicação UDP
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
     * Executa a comunicação UDP com o servidor.
     * 
     * @param config Configuração do cliente
     */
    private void executarComunicacao(ConfiguracaoCliente config) {
        System.out.println("=== Cliente UDP ===");
        System.out.println("Servidor: " + config.servidor + ":" + config.porta);
        System.out.println("Mensagem: \"" + config.mensagem + "\"");
        System.out.println("Timeout: " + TIMEOUT_RECEPCAO + "ms");
        System.out.println();

        DatagramSocket socket = null;

        try {
            // Criar socket UDP
            socket = criarSocket();

            // Preparar e enviar datagrama
            enviarMensagem(socket, config);

            // Aguardar e processar resposta
            receberResposta(socket);

            System.out.println("✓ Comunicação UDP concluída com sucesso!");

        } catch (SocketTimeoutException e) {
            System.err.println("✗ Timeout: Servidor não respondeu em " + TIMEOUT_RECEPCAO + "ms");
            LOGGER.warning("Timeout aguardando resposta do servidor");
        } catch (SocketException e) {
            System.err.println("✗ Erro no socket UDP: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro no socket UDP", e);
        } catch (IOException e) {
            System.err.println("✗ Erro de I/O: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro de I/O na comunicação", e);
        } finally {
            // Fechar socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket UDP fechado");
                LOGGER.info("Socket UDP fechado");
            }
        }
    }

    /**
     * Cria e configura socket UDP.
     * 
     * @return Socket UDP configurado
     * @throws SocketException se houver erro na criação
     */
    private DatagramSocket criarSocket() throws SocketException {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT_RECEPCAO);

        System.out.println("✓ Socket UDP criado na porta local: " + socket.getLocalPort());
        LOGGER.info("Socket UDP criado na porta " + socket.getLocalPort());

        return socket;
    }

    /**
     * Envia mensagem para o servidor via UDP.
     * 
     * @param socket Socket UDP para envio
     * @param config Configuração com dados do servidor e mensagem
     * @throws IOException se houver erro no envio
     */
    private void enviarMensagem(DatagramSocket socket, ConfiguracaoCliente config)
            throws IOException {

        System.out.print("Enviando datagrama...");

        try {
            // Converter mensagem para bytes
            byte[] dadosMensagem = config.mensagem.getBytes("UTF-8");

            // Resolver endereço do servidor
            InetAddress enderecoServidor = InetAddress.getByName(config.servidor);

            // Criar datagrama
            DatagramPacket pacoteEnvio = new DatagramPacket(
                    dadosMensagem,
                    dadosMensagem.length,
                    enderecoServidor,
                    config.porta);

            // Enviar datagrama
            socket.send(pacoteEnvio);

            System.out.println(" ✓ Enviado!");
            System.out.println("  - Tamanho: " + dadosMensagem.length + " bytes");
            System.out.println("  - Destino: " + enderecoServidor.getHostAddress() + ":" + config.porta);

            LOGGER.info("Datagrama enviado para " + enderecoServidor + ":" + config.porta +
                    " (" + dadosMensagem.length + " bytes)");

        } catch (UnknownHostException e) {
            System.out.println(" ✗ Erro!");
            throw new IOException("Servidor não encontrado: " + config.servidor, e);
        } catch (IOException e) {
            System.out.println(" ✗ Erro!");
            throw new IOException("Erro ao enviar datagrama: " + e.getMessage(), e);
        }
    }

    /**
     * Recebe e processa resposta do servidor.
     * 
     * @param socket Socket UDP para recepção
     * @throws IOException se houver erro na recepção
     */
    private void receberResposta(DatagramSocket socket) throws IOException {
        System.out.print("Aguardando resposta...");

        try {
            // Preparar buffer para resposta
            byte[] buffer = new byte[TAMANHO_BUFFER];
            DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);

            // Receber resposta (bloqueante com timeout)
            socket.receive(pacoteResposta);

            System.out.println(" ✓ Recebido!");

            // Processar resposta
            String resposta = new String(
                    pacoteResposta.getData(),
                    0,
                    pacoteResposta.getLength(),
                    "UTF-8");

            // Exibir informações da resposta
            System.out.println("\n=== Resposta do Servidor ===");
            System.out.println("Conteúdo: \"" + resposta + "\"");
            System.out.println("Tamanho: " + pacoteResposta.getLength() + " bytes");
            System.out.println("Origem: " + pacoteResposta.getAddress().getHostAddress() +
                    ":" + pacoteResposta.getPort());

            LOGGER.info("Resposta recebida de " + pacoteResposta.getAddress() + ":" +
                    pacoteResposta.getPort() + " - " + resposta);

        } catch (SocketTimeoutException e) {
            System.out.println(" ✗ Timeout!");
            throw e;
        } catch (IOException e) {
            System.out.println(" ✗ Erro!");
            throw new IOException("Erro ao receber resposta: " + e.getMessage(), e);
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
        String servidor = SERVIDOR_PADRAO;
        int porta = PORTA_PADRAO;
        String mensagem = MENSAGEM_PADRAO;

        try {
            if (args.length > 0 && !args[0].trim().isEmpty()) {
                servidor = args[0].trim();
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

        return new ConfiguracaoCliente(servidor, porta, mensagem);
    }

    /**
     * Exibe ajuda sobre uso do programa.
     */
    private static void exibirAjuda() {
        System.out.println("\nUso: java ClienteUDP [servidor] [porta] [mensagem]");
        System.out.println("  servidor: Endereço do servidor (padrão: localhost)");
        System.out.println("  porta:    Porta do servidor (padrão: 6789)");
        System.out.println("  mensagem: Mensagem a enviar (padrão: MENSAGEM TESTE UDP)");
        System.out.println("\nExemplos:");
        System.out.println("  java ClienteUDP");
        System.out.println("  java ClienteUDP 192.168.1.100");
        System.out.println("  java ClienteUDP localhost 8080");
        System.out.println("  java ClienteUDP servidor.com 6789 \"Olá servidor UDP!\"");
    }

    // ==================== CLASSES AUXILIARES ====================

    /**
     * Classe para armazenar configuração do cliente.
     */
    private static class ConfiguracaoCliente {
        final String servidor;
        final int porta;
        final String mensagem;

        ConfiguracaoCliente(String servidor, int porta, String mensagem) {
            this.servidor = servidor;
            this.porta = porta;
            this.mensagem = mensagem;
        }
    }
}
