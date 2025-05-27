#!/usr/bin/env python3
"""
Programa Sequencial - Cálculo de Somatório

Este programa calcula o somatório de números inteiros de forma sequencial,
servindo como referência para comparação de performance com a versão paralela.

Funcionalidades:
- Calcula somatório de 1 até N de forma sequencial
- Mede tempo de execução para comparação com versão paralela
- Serve como baseline para avaliar ganhos de performance do MPI

Para executar:
    python teste07.py

Autor: Andre
Data: 27 de maio de 2025
"""

import datetime


def somatorio(inicio, fim):
    """
    Calcula o somatório de números inteiros em um intervalo.
    
    Args:
        inicio (int): Número inicial do intervalo (inclusivo)
        fim (int): Número final do intervalo (inclusivo)
    
    Returns:
        int: Soma de todos os números no intervalo [inicio, fim]
    
    Example:
        >>> somatorio(1, 5)
        15  # 1 + 2 + 3 + 4 + 5
    """
    soma = 0
    for i in range(inicio, fim + 1):
        soma += i
    return soma


def main():
    """
    Função principal que executa o cálculo sequencial do somatório.
    
    Solicita um número do usuário, calcula o somatório de 1 até esse número
    e mede o tempo de execução para fins de comparação com versão paralela.
    """
    # Solicita o número para calcular o somatório
    print("Digite o número para calcular o somatório:")
    try:
        num = int(input())
        
        if num < 1:
            print("Por favor, digite um número positivo.")
            return
            
    except ValueError:
        print("Por favor, digite um número válido.")
        return
    
    # Inicia medição de tempo
    tempo_inicio = datetime.datetime.now()
    
    # Calcula o somatório de forma sequencial
    resultado = somatorio(1, num)
    
    # Finaliza medição de tempo
    tempo_final = datetime.datetime.now()
    
    # Calcula tempo de execução
    diferenca_tempo = tempo_final - tempo_inicio
    tempo_execucao = diferenca_tempo.total_seconds()
    
    # Exibe resultados
    print(f"Resultado final do somatório = {resultado}")
    print(f"Tempo de execução (segundos): {tempo_execucao:.20f}")
    
    # Informações adicionais para análise
    print(f"Números somados: 1 até {num}")
    print(f"Total de operações: {num}")


if __name__ == "__main__":
    main()
