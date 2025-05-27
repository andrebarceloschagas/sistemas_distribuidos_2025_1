/**
 * MensagemTeste - Classe para representar mensagens serializáveis
 * 
 * Esta classe implementa a interface Serializable para permitir transmissão
 * de objetos através de streams de comunicação de rede. É utilizada para
 * demonstrar comunicação entre cliente e servidor usando ObjectOutputStream
 * e ObjectInputStream.
 * 
 * @author Sistema de Comunicação por Objetos
 * @version 2.0
 * @since 2025-04-29
 */

import java.io.*;

/**
 * Classe que representa uma mensagem de teste serializável.
 * Implementa Serializable para permitir transmissão via rede.
 */
public class MensagemTeste implements Serializable {
    
    // ==================== CONSTANTES ====================
    
    /** Versão de serialização para compatibilidade */
    private static final long serialVersionUID = 1L;
    
    // ==================== ATRIBUTOS ====================
    
    /** Conteúdo textual da mensagem */
    private String texto;
    
    // ==================== CONSTRUTORES ====================
    
    /**
     * Construtor da mensagem de teste.
     * 
     * @param texto Conteúdo textual da mensagem
     * @throws IllegalArgumentException se o texto for nulo ou vazio
     */
    public MensagemTeste(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Texto da mensagem não pode ser nulo ou vazio");
        }
        this.texto = texto.trim();
    }
    
    // ==================== MÉTODOS PRINCIPAIS ====================
    
    /**
     * Exibe o conteúdo da mensagem no console.
     * Formatação melhorada para melhor visualização.
     */
    public void exibir() {
        System.out.println("\n[MENSAGEM RECEBIDA]: " + texto);
    }
    
    /**
     * Retorna o conteúdo textual da mensagem.
     * 
     * @return String contendo o texto da mensagem
     */
    public String getTexto() {
        return texto;
    }
    
    /**
     * Define novo conteúdo para a mensagem.
     * 
     * @param texto Novo conteúdo textual
     * @throws IllegalArgumentException se o texto for nulo ou vazio
     */
    public void setTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Texto da mensagem não pode ser nulo ou vazio");
        }
        this.texto = texto.trim();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representação em string da mensagem.
     * 
     * @return String formatada com informações da mensagem
     */
    @Override
    public String toString() {
        return "MensagemTeste{" +
               "texto='" + texto + '\'' +
               ", tamanho=" + texto.length() +
               '}';
    }
    
    /**
     * Verifica igualdade entre objetos MensagemTeste.
     * 
     * @param obj Objeto a ser comparado
     * @return true se os objetos são iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MensagemTeste that = (MensagemTeste) obj;
        return texto.equals(that.texto);
    }
    
    /**
     * Calcula hash code do objeto.
     * 
     * @return Valor hash baseado no conteúdo da mensagem
     */
    @Override
    public int hashCode() {
        return texto.hashCode();
    }
}


