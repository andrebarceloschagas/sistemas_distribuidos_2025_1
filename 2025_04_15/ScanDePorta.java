import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Scanner de Portas TCP
 * 
 * Este programa implementa um scanner de portas que verifica quais portas
 * TCP estão abertas em um determinado endereço IP dentro de um intervalo
 * especificado pelo usuário.
 * 
 * Funcionalidades:
 * - Scan de intervalo de portas configurável
 * - Timeout configurável para cada tentativa de conexão
 * - Interface interativa para entrada de dados
 * - Relatório detalhado de portas abertas
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class ScanDePorta {
    
    /** Timeout padrão para conexões em milissegundos */
    private static final int TIMEOUT_PADRAO = 50;
    
    /** Scanner para entrada de dados do usuário */
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Método principal que executa o scanner de portas.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     * @throws IOException em caso de erro de E/S
     */
    public static void main(String[] args) throws IOException {
        ScanDePorta scannerPortas = new ScanDePorta();
        scannerPortas.executarScanner();
    }
    
    /**
     * Executa o scanner de portas com interface interativa.
     */
    public void executarScanner() {
        try {
            exibirCabecalho();
            
            // Coleta dados do usuário
            String enderecoIP = obterEnderecoIP();
            int portaInicial = obterPortaInicial();
            int portaFinal = obterPortaFinal(portaInicial);
            
            // Executa o scan
            executarScanPortas(enderecoIP, portaInicial, portaFinal);
            
        } catch (Exception e) {
            System.err.println("Erro durante execução: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Exibe o cabeçalho do programa.
     */
    private void exibirCabecalho() {
        System.out.println("=========================================");
        System.out.println("           SCANNER DE PORTAS TCP        ");
        System.out.println("=========================================");
        System.out.println("Este programa verifica portas abertas");
        System.out.println("em um endereço IP específico.");
        System.out.println("-----------------------------------------");
    }
    
    /**
     * Obtém o endereço IP do usuário com validação básica.
     * 
     * @return endereço IP informado pelo usuário
     */
    private String obterEnderecoIP() {
        String ip;
        do {
            System.out.print("Digite o endereço IP (ex: 192.168.1.1): ");
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
     * Obtém a porta inicial do usuário com validação.
     * 
     * @return porta inicial válida
     */
    private int obterPortaInicial() {
        int porta;
        do {
            System.out.print("Digite a porta inicial (1-65535): ");
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
                scanner.nextLine(); // Limpa entrada inválida
                porta = -1; // Força nova iteração
            }
        } while (true);
        
        return porta;
    }
    
    /**
     * Obtém a porta final do usuário com validação.
     * 
     * @param portaInicial porta inicial para validação de intervalo
     * @return porta final válida
     */
    private int obterPortaFinal(int portaInicial) {
        int porta;
        do {
            System.out.print("Digite a porta final (" + portaInicial + "-65535): ");
            try {
                porta = scanner.nextInt();
                scanner.nextLine(); // Consome quebra de linha
                
                if (porta < portaInicial) {
                    System.out.println("⚠️  Porta final deve ser maior ou igual à inicial!");
                } else if (porta > 65535) {
                    System.out.println("⚠️  Porta deve estar entre " + portaInicial + " e 65535!");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️  Por favor, digite um número válido!");
                scanner.nextLine(); // Limpa entrada inválida
                porta = -1; // Força nova iteração
            }
        } while (true);
        
        return porta;
    }
    
    /**
     * Executa o scan das portas no intervalo especificado.
     * 
     * @param enderecoIP endereço IP a ser escaneado
     * @param portaInicial primeira porta do intervalo
     * @param portaFinal última porta do intervalo
     */
    private void executarScanPortas(String enderecoIP, int portaInicial, int portaFinal) {
        System.out.println("\n📡 Iniciando scan de portas...");
        System.out.println("Alvo: " + enderecoIP);
        System.out.println("Intervalo: " + portaInicial + " - " + portaFinal);
        System.out.println("Timeout: " + TIMEOUT_PADRAO + "ms");
        System.out.println("-----------------------------------------");
        
        int portasAbertas = 0;
        int totalPortas = portaFinal - portaInicial + 1;
        long tempoInicio = System.currentTimeMillis();
        
        // Scan das portas
        for (int porta = portaInicial; porta <= portaFinal; porta++) {
            if (verificarPorta(enderecoIP, porta)) {
                System.out.println("✅ Porta " + porta + " ABERTA");
                portasAbertas++;
            }
            
            // Exibe progresso a cada 100 portas ou na última porta
            if (porta % 100 == 0 || porta == portaFinal) {
                int progresso = (int) (((double) (porta - portaInicial + 1) / totalPortas) * 100);
                System.out.printf("⏳ Progresso: %d%% (%d/%d portas)%n", 
                    progresso, porta - portaInicial + 1, totalPortas);
            }
        }
        
        // Relatório final
        exibirRelatorioFinal(portasAbertas, totalPortas, tempoInicio);
    }
    
    /**
     * Verifica se uma porta específica está aberta.
     * 
     * @param enderecoIP endereço IP a ser testado
     * @param porta porta a ser testada
     * @return true se a porta estiver aberta, false caso contrário
     */
    private boolean verificarPorta(String enderecoIP, int porta) {
        Socket socket = null;
        try {
            socket = new Socket();
            InetSocketAddress endereco = new InetSocketAddress(enderecoIP, porta);
            socket.connect(endereco, TIMEOUT_PADRAO);
            return true; // Conexão bem-sucedida = porta aberta
            
        } catch (Exception e) {
            // Conexão falhou = porta fechada ou filtrada
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignora erros ao fechar socket
                }
            }
        }
    }
    
    /**
     * Exibe o relatório final do scan.
     * 
     * @param portasAbertas número de portas abertas encontradas
     * @param totalPortas total de portas escaneadas
     * @param tempoInicio tempo de início do scan
     */
    private void exibirRelatorioFinal(int portasAbertas, int totalPortas, long tempoInicio) {
        long tempoFinal = System.currentTimeMillis();
        double tempoDecorrido = (tempoFinal - tempoInicio) / 1000.0;
        
        System.out.println("\n=========================================");
        System.out.println("           RELATÓRIO FINAL              ");
        System.out.println("=========================================");
        System.out.println("Portas abertas encontradas: " + portasAbertas);
        System.out.println("Total de portas escaneadas: " + totalPortas);
        System.out.println("Tempo decorrido: " + String.format("%.2f", tempoDecorrido) + " segundos");
        System.out.println("Velocidade: " + String.format("%.2f", totalPortas / tempoDecorrido) + " portas/segundo");
        
        if (portasAbertas == 0) {
            System.out.println("\n🔒 Nenhuma porta aberta foi encontrada.");
        } else {
            System.out.println("\n🔓 Scan concluído! Verifique as portas abertas acima.");
        }
    }
    
    /**
     * Valida o formato básico de um endereço IP.
     * 
     * @param ip endereço IP a ser validado
     * @return true se o formato estiver correto, false caso contrário
     */
    private boolean validarFormatoIP(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
