import java.net.*;
import java.io.*;

public class UDPServerConcorrente {

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6789);
            System.out.println("*** Servidor de Inversão UDP Concorrente aguardando requests...");

            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                // Cria uma nova thread para processar a requisição
                ClientHandler handler = new ClientHandler(serverSocket, receivePacket);
                new Thread(handler).start();
            }
            // serverSocket.close(); // Nunca chegará aqui neste loop infinito
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final DatagramSocket serverSocket;
        private final DatagramPacket receivePacket;

        public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
        }

        @Override
        public void run() {
            try {
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();

                System.out.println("Thread " + Thread.currentThread().getName() + " processando: " + receivedMessage + " de " + clientAddress + ":" + clientPort);

                // Inverte a string recebida
                String reversedMessage = new StringBuilder(receivedMessage).reverse().toString();
                byte[] sendBuffer = reversedMessage.getBytes();

                // Envia a resposta invertida para o cliente
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("Thread " + Thread.currentThread().getName() + " respondeu para " + clientAddress + ":" + clientPort + ": " + reversedMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}