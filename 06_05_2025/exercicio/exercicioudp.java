// Exemplo de exercício típico de sockets UDP em Java
// Exercício: Crie um cliente e um servidor UDP. O cliente envia uma mensagem para o servidor, que responde com a mesma mensagem em maiúsculas.
// O cliente exibe a resposta recebida.

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ExercicioUDP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite 1 para executar como Servidor ou 2 para Cliente:");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer
        if (opcao == 1) {
            executarServidor();
        } else if (opcao == 2) {
            executarCliente(scanner);
        } else {
            System.out.println("Opção inválida.");
        }
        scanner.close();
    }

    // Função do Servidor UDP
    public static void executarServidor() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(6790); // Porta diferente dos exemplos
            byte[] buffer = new byte[1024];
            System.out.println("Servidor UDP aguardando mensagens na porta 6790...");
            while (true) {
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteRecebido);
                String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
                System.out.println("Mensagem recebida: " + mensagem);
                String resposta = mensagem.toUpperCase();
                byte[] respostaBytes = resposta.getBytes();
                DatagramPacket pacoteResposta = new DatagramPacket(
                    respostaBytes, respostaBytes.length,
                    pacoteRecebido.getAddress(), pacoteRecebido.getPort()
                );
                socket.send(pacoteResposta);
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        } finally {
            if (socket != null) socket.close();
        }
    }

    // Função do Cliente UDP
    public static void executarCliente(Scanner scanner) {
        DatagramSocket socket = null;
        try {
            System.out.print("Digite o IP do servidor (ex: localhost): ");
            String servidor = scanner.nextLine();
            System.out.print("Digite a mensagem a ser enviada: ");
            String mensagem = scanner.nextLine();
            byte[] mensagemBytes = mensagem.getBytes();
            InetAddress endereco = InetAddress.getByName(servidor);
            socket = new DatagramSocket();
            DatagramPacket pacote = new DatagramPacket(mensagemBytes, mensagemBytes.length, endereco, 6790);
            socket.send(pacote);
            byte[] buffer = new byte[1024];
            DatagramPacket resposta = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(5000); // Timeout de 5 segundos
            socket.receive(resposta);
            String respostaStr = new String(resposta.getData(), 0, resposta.getLength());
            System.out.println("Resposta do servidor: " + respostaStr);
        } catch (SocketTimeoutException e) {
            System.out.println("Tempo de resposta esgotado.");
        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        } finally {
            if (socket != null) socket.close();
        }
    }
}