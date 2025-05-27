#!/usr/bin/env python3
"""
Programa MPI - Cálculo Paralelo de Somatório

Este programa demonstra o cálculo paralelo de um somatório usando MPI.
O trabalho é dividido entre múltiplos processos para acelerar o cálculo.

Funcionalidades:
- Processo mestre (rank 0) coordena o trabalho e coleta resultados
- Processos escravos calculam somas parciais de intervalos específicos
- Medição de tempo de execução para avaliar performance

Para executar:
    mpiexec -n 4 python teste04.py

Autor: Andre
Data: 27 de maio de 2025
"""

from mpi4py import MPI
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
    Função principal que coordena o cálculo paralelo do somatório.
    """
    # Inicializa o ambiente MPI
    comm = MPI.COMM_WORLD
    size = comm.Get_size()
    rank = comm.Get_rank()
    
    if rank == 0:
        # Processo mestre
        processo_mestre(comm, size)
    else:
        # Processos escravos
        processo_escravo(comm, size, rank)


def processo_mestre(comm, size):
    """
    Executa as tarefas do processo mestre.
    
    Args:
        comm: Comunicador MPI
        size: Número total de processos
    """
    rank = 0  # O mestre sempre tem rank 0
    
    # Solicita o número para calcular o somatório
    print("Digite o número para calcular o somatório:")
    num = int(input())
    
    # Inicia medição de tempo
    tempo_inicio_mpi = MPI.Wtime()
    tempo_inicio_datetime = datetime.datetime.now()
    
    # Envia o número para todos os processos escravos
    for i in range(1, size):
        comm.send(num, dest=i, tag=1)
    
    # Calcula a parte do mestre
    parte_por_processo = int(num / size)
    inicio_mestre = parte_por_processo * rank + 1
    fim_mestre = parte_por_processo * (rank + 1)
    
    # Se é o último processo, ajusta para incluir números restantes
    if rank == size - 1:
        fim_mestre = num
    
    soma_total = somatorio(inicio_mestre, fim_mestre)
    print(f"Mestre calculou intervalo [{inicio_mestre}, {fim_mestre}]")
    
    # Recebe e soma os resultados dos escravos
    for i in range(1, size):
        info = MPI.Status()
        soma_parcial = comm.recv(source=i, tag=2, status=info)
        print(f"Recebido do escravo {info.Get_source()}: {soma_parcial}")
        soma_total += soma_parcial
    
    # Finaliza medição de tempo
    tempo_final_mpi = MPI.Wtime() - tempo_inicio_mpi
    tempo_final_datetime = datetime.datetime.now()
    
    diferenca_tempo = tempo_final_datetime - tempo_inicio_datetime
    tempo_execucao = diferenca_tempo.total_seconds()
    
    # Exibe resultados
    print(f"Resultado final do somatório = {soma_total}")
    print(f"Tempo de execução MPI (segundos): {tempo_final_mpi:.6f}")
    print(f"Tempo de execução datetime (segundos): {tempo_execucao:.6f}")


def processo_escravo(comm, size, rank):
    """
    Executa as tarefas dos processos escravos.
    
    Args:
        comm: Comunicador MPI
        size: Número total de processos
        rank: Rank do processo escravo atual
    """
    # Recebe o número do mestre
    num = comm.recv(source=0, tag=1)
    print(f"Escravo de rank {rank} recebeu número: {num}")
    
    # Calcula o intervalo que este escravo deve processar
    parte_por_processo = int(num / size)
    inicio = parte_por_processo * rank + 1
    fim = parte_por_processo * (rank + 1)
    
    # Se é o último processo, ajusta para incluir números restantes
    if rank == size - 1:
        fim = num
    
    # Calcula o somatório do intervalo designado
    soma_parcial = somatorio(inicio, fim)
    print(f"Escravo {rank} calculou intervalo [{inicio}, {fim}] = {soma_parcial}")
    
    # Envia o resultado de volta para o mestre
    comm.send(soma_parcial, dest=0, tag=2)


if __name__ == "__main__":
    main()
