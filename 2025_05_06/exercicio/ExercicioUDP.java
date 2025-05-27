/**
 * ExercicioUDP - Aplicação integrada de Cliente e Servidor UDP
 * 
 * Esta aplicação demonstra comunicação UDP bidirecional em uma única classe,
 * permitindo ao usuário escolher executar como cliente ou servidor. O servidor
 * converte mensagens recebidas para maiúsculas e as retorna aos clientes.
 * 
 * Funcionalidades:
 * - Modo servidor: recebe mensagens e retorna em maiúsculas
 * - Modo cliente: envia mensagens e exibe respostas
 * - Interface interativa para escolha de modo
 * - Configuração flexível de servidor
 * - Tratamento robusto de exceções e timeouts
 * 
 * Exercício: Cliente e servidor UDP onde o servidor responde com mensagem em maiúsculas
 * 
 * @author Sistema de Exercícios UDP
 * @version 2.0
 * @since 2025-05-06
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Classe principal que implementa cliente e servidor UDP em uma aplicação.
 */
public class ExercicioUDP {
    
    // ==================== CONSTANTES ====================
    
    /** Porta do servidor UDP */
    private static final int PORTA_SERVIDOR = 6790;
    
    /** Tamanho do buffer para comunicação */
    private static final int TAMANHO_BUFFER = 1024;
    
    /** Timeout para resposta do cliente em milissegundos */
    private static final int TIMEOUT_CLIENTE = 5000;
    
    /** Logger para registrar eventos da aplicação */
    private static final Logger LOGGER = Logger.getLogger(ExercicioUDP.class.getName());
    
    // ==================== MÉTODO PRINCIPAL ====================
    
    /**
     * Método principal da aplicação.
     * Permite ao usuário escolher entre executar como servidor ou cliente.
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== Exercício UDP - Cliente/Servidor ===");
        System.out.println("Funcionalidade: Servidor converte mensagens para maiúsculas");
        System.out.println("Porta do servidor: " + PORTA_SERVIDOR);
        System.out.println();
        
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Solicitar modo de operação
            int opcao = solicitarModoOperacao(scanner);
            
            // Executar modo selecionado
            switch (opcao) {
                case 1:
                    System.out.println("Iniciando modo SERVIDOR...\n");
                    executarServidor();
                    break;
                case 2:
                    System.out.println("Iniciando modo CLIENTE...\n");
                    executarCliente(scanner);
                    break;
                default:
                    System.err.println("Opção inválida. Use 1 para Servidor ou 2 para Cliente.");
                    System.exit(1);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na execução da aplicação", e);
            System.err.println("Erro inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    // ==================== MÉTODOS DE INTERFACE ====================
    
    /**
     * Solicita ao usuário o modo de operação.
     * 
     * @param scanner Scanner para entrada do usuário
     * @return 1 para servidor, 2 para cliente
     */
    private static int solicitarModoOperacao(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Escolha o modo de operação:");
                System.out.println("  1 - Servidor UDP (recebe e converte para maiúsculas)");
                System.out.println("  2 - Cliente UDP (envia mensagens)");
                System.out.print("Digite sua opção (1 ou 2): ");
                
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer
                
                if (opcao == 1 || opcao == 2) {
                    return opcao;
                }
                
                System.err.println("Erro: Digite apenas 1 ou 2\n");
                
            } catch (Exception e) {
                System.err.println("Erro: Entrada inválida. Digite apenas números.\n");
                scanner.nextLine(); // Limpar buffer inválido
            }
        }
    }
    
    // ==================== IMPLEMENTAÇÃO DO SERVIDOR ====================
    
    /**
     * Executa o servidor UDP que converte mensagens para maiúsculas.
     * O servidor fica em loop contínuo aguardando mensagens de clientes.
     */
    public static void executarServidor() {
        System.out.println("=== Modo Servidor UDP ===");
        System.out.println("Porta: " + PORTA_SERVIDOR);
        System.out.println("Funcionalidade: Converte mensagens para MAIÚSCULAS");
        System.out.println("Pressione Ctrl+C para parar o servidor\n");
        
        DatagramSocket socket = null;
        int contadorMensagens = 0;
        
        try {
            // Criar socket do servidor
            socket = new DatagramSocket(PORTA_SERVIDOR);
            System.out.println("✓ Servidor iniciado com sucesso!");
            System.out.println("✓ Aguardando mensagens de clientes...\n");
            
            LOGGER.info("Servidor UDP iniciado na porta " + PORTA_SERVIDOR);
            
            // Buffer para recepção
            byte[] buffer = new byte[TAMANHO_BUFFER];
            
            // Loop principal do servidor
            while (true) {
                try {
                    // Preparar datagrama para recepção
                    DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                    
                    System.out.println("[Aguardando] Próxima mensagem...");
                    
                    // Receber mensagem (bloqueante)
                    socket.receive(pacoteRecebido);
                    contadorMensagens++;
                    
                    // Processar mensagem
                    processarMensagemServidor(socket, pacoteRecebido, contadorMensagens);
                    
                } catch (IOException e) {
                    System.err.println("Erro ao processar mensagem: " + e.getMessage());
                    LOGGER.log(Level.WARNING, "Erro no processamento de mensagem", e);
                }
            }
            
        } catch (SocketException e) {
            System.err.println("✗ Erro ao criar socket do servidor: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao criar socket do servidor", e);
        } finally {
            // Fechar recursos
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("\n✓ Servidor encerrado");
                System.out.println("✓ Total de mensagens processadas: " + contadorMensagens);
                LOGGER.info("Servidor encerrado. Mensagens processadas: " + contadorMensagens);
            }
        }
    }
    
    /**
     * Processa uma mensagem recebida pelo servidor.
     * 
     * @param socket Socket do servidor
     * @param pacoteRecebido Datagrama recebido do cliente
     * @param numeroMensagem Número sequencial da mensagem
     * @throws IOException se houver erro no processamento
     */
    private static void processarMensagemServidor(DatagramSocket socket, 
                                                DatagramPacket pacoteRecebido, 
                                                int numeroMensagem) throws IOException {
        
        // Extrair informações do cliente
        InetAddress enderecoCliente = pacoteRecebido.getAddress();
        int portaCliente = pacoteRecebido.getPort();
        
        // Converter mensagem recebida
        String mensagemOriginal = new String(pacoteRecebido.getData(), 0, 
                                           pacoteRecebido.getLength(), "UTF-8");
        
        String identificador = "[#" + numeroMensagem + "]";
        
        System.out.println(identificador + " Mensagem recebida:");
        System.out.println("  - Cliente: " + enderecoCliente.getHostAddress() + ":" + portaCliente);
        System.out.println("  - Original: \"" + mensagemOriginal + "\"");
        
        // Converter para maiúsculas
        String resposta = mensagemOriginal.toUpperCase();
        System.out.println("  - Convertida: \"" + resposta + "\"");
        
        try {
            // Preparar e enviar resposta
            byte[] respostaBytes = resposta.getBytes("UTF-8");
            DatagramPacket pacoteResposta = new DatagramPacket(
                respostaBytes, 
                respostaBytes.length,
                enderecoCliente, 
                portaCliente
            );
            
            System.out.print(identificador + " Enviando resposta...");
            socket.send(pacoteResposta);
            System.out.println(" ✓ Enviado!");
            
            LOGGER.info("Resposta enviada para " + enderecoCliente + ":" + portaCliente + 
                       " - " + resposta);
            
        } catch (IOException e) {
            System.out.println(" ✗ Erro no envio!");
            throw new IOException("Erro ao enviar resposta: " + e.getMessage(), e);
        }
        
        System.out.println(identificador + " ✓ Processamento concluído\n");
    }
    
    // ==================== IMPLEMENTAÇÃO DO CLIENTE ====================
    
    /**
     * Executa o cliente UDP que envia mensagens para o servidor.
     * 
     * @param scanner Scanner para entrada do usuário
     */
    public static void executarCliente(Scanner scanner) {
        System.out.println("=== Modo Cliente UDP ===");
        System.out.println("Servidor: porta " + PORTA_SERVIDOR);
        System.out.println("Timeout: " + TIMEOUT_CLIENTE + "ms");
        System.out.println();
        
        DatagramSocket socket = null;
        
        try {
            // Obter configurações do usuário
            String enderecoServidor = obterEnderecoServidor(scanner);
            String mensagem = obterMensagem(scanner);
            
            // Criar socket do cliente
            socket = new DatagramSocket();
            System.out.println("✓ Socket cliente criado na porta: " + socket.getLocalPort());
            
            // Enviar mensagem e receber resposta
            enviarMensagemCliente(socket, enderecoServidor, mensagem);
            
        } catch (SocketTimeoutException e) {
            System.err.println("✗ Timeout: Servidor não respondeu em " + TIMEOUT_CLIENTE + "ms");
            System.err.println("  Verifique se o servidor está executando e acessível");
            LOGGER.warning("Timeout aguardando resposta do servidor");
        } catch (IOException e) {
            System.err.println("✗ Erro no cliente: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro no cliente UDP", e);
        } finally {
            // Fechar socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket cliente fechado");
                LOGGER.info("Socket cliente fechado");
            }
        }
    }
    
    /**
     * Obtém o endereço do servidor do usuário.
     * 
     * @param scanner Scanner para entrada
     * @return Endereço do servidor
     */
    private static String obterEnderecoServidor(Scanner scanner) {
        System.out.print("Digite o endereço do servidor (ex: localhost, 192.168.1.100): ");
        String endereco = scanner.nextLine().trim();
        
        if (endereco.isEmpty()) {
            endereco = "localhost";
            System.out.println("Usando endereço padrão: localhost");
        }
        
        return endereco;
    }
    
    /**
     * Obtém a mensagem a ser enviada do usuário.
     * 
     * @param scanner Scanner para entrada
     * @return Mensagem a ser enviada
     */
    private static String obterMensagem(Scanner scanner) {
        System.out.print("Digite a mensagem a ser enviada: ");
        String mensagem = scanner.nextLine().trim();
        
        if (mensagem.isEmpty()) {
            mensagem = "Mensagem de teste";
            System.out.println("Usando mensagem padrão: " + mensagem);
        }
        
        return mensagem;
    }
    
    /**
     * Envia mensagem para o servidor e processa resposta.
     * 
     * @param socket Socket do cliente
     * @param enderecoServidor Endereço do servidor
     * @param mensagem Mensagem a ser enviada
     * @throws IOException se houver erro na comunicação
     */
    private static void enviarMensagemCliente(DatagramSocket socket, 
                                            String enderecoServidor, 
                                            String mensagem) throws IOException {
        
        System.out.println("\n=== Enviando Mensagem ===");
        System.out.println("Destino: " + enderecoServidor + ":" + PORTA_SERVIDOR);
        System.out.println("Mensagem: \"" + mensagem + "\"");
        System.out.println();
        
        try {
            // Preparar dados para envio
            byte[] mensagemBytes = mensagem.getBytes("UTF-8");
            InetAddress endereco = InetAddress.getByName(enderecoServidor);
            
            // Criar e enviar datagrama
            DatagramPacket pacote = new DatagramPacket(
                mensagemBytes, 
                mensagemBytes.length, 
                endereco, 
                PORTA_SERVIDOR
            );
            
            System.out.print("Enviando datagrama...");
            socket.send(pacote);
            System.out.println(" ✓ Enviado!");
            
            // Configurar timeout e aguardar resposta
            socket.setSoTimeout(TIMEOUT_CLIENTE);
            receberRespostaCliente(socket);
            
            System.out.println("\n✓ Comunicação concluída com sucesso!");
            
        } catch (UnknownHostException e) {
            throw new IOException("Servidor não encontrado: " + enderecoServidor, e);
        }
    }
    
    /**
     * Recebe e processa resposta do servidor.
     * 
     * @param socket Socket do cliente
     * @throws IOException se houver erro na recepção
     */
    private static void receberRespostaCliente(DatagramSocket socket) throws IOException {
        System.out.print("Aguardando resposta...");
        
        // Preparar buffer para resposta
        byte[] buffer = new byte[TAMANHO_BUFFER];
        DatagramPacket resposta = new DatagramPacket(buffer, buffer.length);
        
        // Receber resposta (com timeout)
        socket.receive(resposta);
        System.out.println(" ✓ Recebido!");
        
        // Processar resposta
        String respostaStr = new String(resposta.getData(), 0, resposta.getLength(), "UTF-8");
        
        System.out.println("\n=== Resposta do Servidor ===");
        System.out.println("Conteúdo: \"" + respostaStr + "\"");
        System.out.println("Tamanho: " + resposta.getLength() + " bytes");
        System.out.println("Origem: " + resposta.getAddress().getHostAddress() + 
                         ":" + resposta.getPort());
        
        LOGGER.info("Resposta recebida: " + respostaStr);
    }
}