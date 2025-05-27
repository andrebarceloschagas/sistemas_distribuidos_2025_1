
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Cliente TCP Interativo
 * 
 * Este programa implementa um cliente TCP que permite comunicação
 * interativa e contínua com um servidor. O cliente pode enviar
 * múltiplas mensagens e receber confirmações até decidir encerrar.
 * 
 * Funcionalidades:
 * - Conexão configurável por IP e porta
 * - Comunicação bidirecional contínua
 * - Interface interativa para envio de mensagens
 * - Comando "terminar" para encerrar graciosamente
 * - Tratamento robusto de exceções de rede
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class Cliente4 {
    
    /** Scanner para entrada de dados do usuário */
    private static final Scanner scanner = new Scanner(System.in);
    
    /** Timeout de conexão em milissegundos */
    private static final int TIMEOUT_CONEXAO = 1000;
    
    /** Comando para encerrar a sessão */
    private static final String COMANDO_TERMINAR = "terminar";
    
    /**
     * Método principal que executa o cliente TCP interativo.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     * @throws IOException em caso de erro de E/S
     */
    public static void main(String[] args) throws IOException {
        Cliente4 cliente = new Cliente4();
        cliente.executarCliente();
    }
    
    /**
     * Executa a lógica principal do cliente interativo.
     */
    public void executarCliente() {
        Socket socket = null;
        DataInputStream inputStream = null;
        DataOutputStream outputStream = null;
        
        try {
            exibirCabecalho();
            
            // Obtém configurações de conexão do usuário
            String enderecoIP = obterEnderecoIP();
            int porta = obterPorta();
            
            // Estabelece conexão
            socket = conectarServidor(enderecoIP, porta);
            
            // Configura streams de comunicação
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            
            // Inicia loop de comunicação
            executarComunicacao(inputStream, outputStream);
            
        } catch (Exception e) {
            System.err.println("❌ Erro durante execução: " + e.getMessage());
        } finally {
            // Fecha recursos
            fecharRecursos(socket, inputStream, outputStream);
            scanner.close();
        }
    }
    
    /**
     * Exibe o cabeçalho do programa.
     */
    private void exibirCabecalho() {
        System.out.println("=========================================");
        System.out.println("        CLIENTE TCP INTERATIVO          ");
        System.out.println("=========================================");
        System.out.println("Cliente para comunicação contínua com");
        System.out.println("servidor TCP. Digite 'terminar' para sair.");
        System.out.println("-----------------------------------------");
    }
    
    /**
     * Obtém o endereço IP do servidor do usuário.
     * 
     * @return endereço IP válido
     */
    private String obterEnderecoIP() {
        String ip;
        do {
            System.out.print("Digite o IP do servidor (ex: 127.0.0.1): ");
            ip = scanner.nextLine().trim();
            
            if (ip.isEmpty()) {
                System.out.println("⚠️  Endereço IP não pode estar vazio!");
            } else if (!validarFormatoIP(ip)) {
                System.out.println("⚠️  Formato de IP inválido!");
            } else {
                break;
            }
        } while (true);
        
        return ip;
    }
    
    /**
     * Obtém a porta do servidor do usuário.
     * 
     * @return porta válida
     */
    private int obterPorta() {
        int porta;
        do {
            System.out.print("Digite a porta do servidor (1-65535): ");
            try {
                porta = scanner.nextInt();
                scanner.nextLine(); // Consome quebra de linha
                
                if (porta < 1 || porta > 65535) {
                    System.out.println("⚠️  Porta deve estar entre 1 e 65535!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️  Por favor, digite um número válido!");
                scanner.nextLine();
                porta = -1;
            }
        } while (true);
        
        return porta;
    }
    
    /**
     * Estabelece conexão com o servidor.
     * 
     * @param enderecoIP endereço IP do servidor
     * @param porta porta do servidor
     * @return socket conectado
     * @throws IOException em caso de erro de conexão
     */
    private Socket conectarServidor(String enderecoIP, int porta) throws IOException {
        System.out.println("\n📡 Conectando ao servidor " + enderecoIP + ":" + porta + "...");
        
        Socket socket = new Socket();
        InetSocketAddress endereco = new InetSocketAddress(enderecoIP, porta);
        
        try {
            socket.connect(endereco, TIMEOUT_CONEXAO);
            System.out.println("✅ Conexão estabelecida com sucesso!");
            return socket;
            
        } catch (ConnectException e) {
            throw new IOException("Falha ao conectar: Servidor não encontrado em " + 
                                enderecoIP + ":" + porta);
        } catch (SocketTimeoutException e) {
            throw new IOException("Timeout de conexão: Servidor não respondeu em " + 
                                TIMEOUT_CONEXAO + "ms");
        }
    }
    
    /**
     * Executa o loop principal de comunicação com o servidor.
     * 
     * @param inputStream stream de entrada para receber dados
     * @param outputStream stream de saída para enviar dados
     * @throws IOException em caso de erro de comunicação
     */
    private void executarComunicacao(DataInputStream inputStream, 
                                   DataOutputStream outputStream) throws IOException {
        System.out.println("\n💬 Sessão de comunicação iniciada!");
        System.out.println("Digite suas mensagens ('" + COMANDO_TERMINAR + "' para sair):");
        System.out.println("-----------------------------------------");
        
        int contadorMensagens = 0;
        
        while (true) {
            try {
                // Solicita mensagem do usuário
                System.out.print("\n[" + (++contadorMensagens) + "] Sua mensagem: ");
                String mensagem = scanner.nextLine().trim();
                
                if (mensagem.isEmpty()) {
                    System.out.println("⚠️  Mensagem não pode estar vazia!");
                    contadorMensagens--; // Não conta mensagem vazia
                    continue;
                }
                
                // Envia mensagem para o servidor
                outputStream.writeUTF(mensagem);
                System.out.println("📤 Mensagem enviada para o servidor");
                
                // Recebe confirmação do servidor
                String resposta = inputStream.readUTF();
                System.out.println("📥 Resposta do servidor: " + resposta);
                
                // Verifica comando de terminar
                if (mensagem.equalsIgnoreCase(COMANDO_TERMINAR)) {
                    System.out.println("\n👋 Encerrando sessão...");
                    break;
                }
                
            } catch (IOException e) {
                System.err.println("❌ Erro de comunicação: " + e.getMessage());
                break;
            }
        }
        
        System.out.println("📊 Total de mensagens enviadas: " + (contadorMensagens - 1));
    }
    
    /**
     * Valida o formato básico de um endereço IP.
     * 
     * @param ip endereço IP a ser validado
     * @return true se o formato estiver correto
     */
    private boolean validarFormatoIP(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Fecha todos os recursos de rede de forma segura.
     * 
     * @param socket socket a ser fechado
     * @param inputStream stream de entrada a ser fechada
     * @param outputStream stream de saída a ser fechada
     */
    private void fecharRecursos(Socket socket, DataInputStream inputStream, 
                               DataOutputStream outputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            System.err.println("⚠️  Erro ao fechar stream de entrada: " + e.getMessage());
        }
        
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            System.err.println("⚠️  Erro ao fechar stream de saída: " + e.getMessage());
        }
        
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("🔌 Conexão encerrada.");
            }
        } catch (IOException e) {
            System.err.println("⚠️  Erro ao fechar socket: " + e.getMessage());
        }
    }
}