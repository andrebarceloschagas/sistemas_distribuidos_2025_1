import java.net.*;
import java.io.*;

/**
 * Servidor TCP Concorrente
 * 
 * Este programa implementa um servidor TCP que aceita múltiplas conexões
 * simultâneas, criando uma thread dedicada para cada cliente conectado.
 * Permite comunicação bidirecional contínua com cada cliente.
 * 
 * Funcionalidades:
 * - Servidor multi-threaded para múltiplos clientes simultâneos
 * - Thread dedicada para cada cliente (usando ServidorThread)
 * - Escuta contínua na porta 4321
 * - Logs detalhados de conexões
 * - Tratamento robusto de exceções
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class Servidor4 {

    /** Porta padrão onde o servidor irá escutar */
    private static final int PORTA_SERVIDOR = 4321;

    /** Backlog máximo de conexões pendentes */
    private static final int BACKLOG_MAXIMO = 300;

    /** Contador de clientes conectados */
    private static int contadorClientes = 0;

    /**
     * Método principal que inicia o servidor TCP concorrente.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Servidor4 servidor = new Servidor4();
        servidor.iniciarServidor();
    }

    /**
     * Inicia o servidor e entra no loop principal de aceitação de conexões.
     */
    public void iniciarServidor() {
        ServerSocket serverSocket = null;

        try {
            // Cria socket do servidor
            serverSocket = new ServerSocket(PORTA_SERVIDOR, BACKLOG_MAXIMO);

            exibirInformacaoInicializacao();

            // Loop principal do servidor
            while (true) {
                try {
                    // Aguarda nova conexão de cliente
                    Socket clienteSocket = serverSocket.accept();
                    contadorClientes++;

                    // Log da nova conexão
                    logNovaConexao(clienteSocket);

                    // Cria e inicia thread dedicada para o cliente
                    criarThreadCliente(clienteSocket);

                } catch (IOException e) {
                    System.err.println("❌ Erro ao aceitar conexão: " + e.getMessage());
                    // Continua executando mesmo com erro em conexão específica
                }
            }

        } catch (IOException e) {
            System.err.println("💥 Erro fatal ao iniciar servidor!");
            System.err.println("Detalhes: " + e.getMessage());
            System.err.println("Verifique se a porta " + PORTA_SERVIDOR + " não está em uso.");
        } finally {
            // Garante fechamento do servidor
            fecharServidor(serverSocket);
        }
    }

    /**
     * Exibe informações de inicialização do servidor.
     */
    private void exibirInformacaoInicializacao() {
        System.out.println("=========================================");
        System.out.println("    SERVIDOR TCP CONCORRENTE ATIVO     ");
        System.out.println("=========================================");
        System.out.println("🌐 Porta: " + PORTA_SERVIDOR);
        System.out.println("👥 Backlog máximo: " + BACKLOG_MAXIMO);
        System.out.println("🔄 Modo: Multi-threaded");
        System.out.println("⏰ Iniciado em: " + java.time.LocalDateTime.now());
        System.out.println("=========================================");
        System.out.println("🔊 Servidor aguardando conexões...");
        System.out.println("   (Pressione Ctrl+C para parar)");
        System.out.println("-----------------------------------------");
    }

    /**
     * Registra informações sobre nova conexão de cliente.
     * 
     * @param clienteSocket socket do cliente conectado
     */
    private void logNovaConexao(Socket clienteSocket) {
        String enderecoCliente = clienteSocket.getInetAddress().getHostAddress();
        int portaCliente = clienteSocket.getPort();
        String timestamp = java.time.LocalDateTime.now().toString();

        System.out.println("\n🆕 NOVA CONEXÃO #" + contadorClientes);
        System.out.println("   📍 Cliente: " + enderecoCliente + ":" + portaCliente);
        System.out.println("   ⏰ Timestamp: " + timestamp);
        System.out.println("   👥 Total de clientes ativos: " + contadorClientes);
    }

    /**
     * Cria e inicia uma thread dedicada para atender o cliente.
     * 
     * @param clienteSocket socket do cliente a ser atendido
     */
    private void criarThreadCliente(Socket clienteSocket) {
        try { // Cria thread dedicada para o cliente
            ServidorThread threadCliente = new ServidorThread(clienteSocket);

            // Inicia a thread
            threadCliente.start();

            System.out.println("   🚀 Thread #" + contadorClientes + " iniciada para o cliente");
            System.out.println("-----------------------------------------");

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar thread para cliente: " + e.getMessage());

            // Fecha socket do cliente se não foi possível criar thread
            try {
                clienteSocket.close();
                contadorClientes--; // Decrementa contador
            } catch (IOException ex) {
                System.err.println("⚠️  Erro ao fechar socket do cliente: " + ex.getMessage());
            }
        }
    }

    /**
     * Fecha o servidor de forma segura.
     * 
     * @param serverSocket socket do servidor a ser fechado
     */
    private void fecharServidor(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("\n🛑 Servidor encerrado.");
                System.out.println("📊 Total de clientes atendidos: " + contadorClientes);
            } catch (IOException e) {
                System.err.println("⚠️  Erro ao fechar servidor: " + e.getMessage());
            }
        }
    }

    /**
     * Método para obter estatísticas do servidor.
     * 
     * @return número total de clientes conectados
     */
    public static int getTotalClientesConectados() {
        return contadorClientes;
    }
}
