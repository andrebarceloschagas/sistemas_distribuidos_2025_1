import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UDPServerInverter {

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(6789);
            byte[] buffer = new byte[1000];

            System.out.println("*** Servidor de Inversão UDP aguardando requests...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Recebido de " + clientAddress + ":" + clientPort + ": " + receivedMessage);

                // Inverte a string recebida
                String reversedMessage = new StringBuilder(receivedMessage).reverse().toString();
                byte[] sendBuffer = reversedMessage.getBytes();

                // Envia a resposta invertida para o cliente
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
            // serverSocket.close(); // Nunca chegará aqui neste loop infinito
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}