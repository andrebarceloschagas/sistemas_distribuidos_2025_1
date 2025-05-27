#!/usr/bin/env python3
"""
Calculadora de Soma Paralela com MPI - Exemplo de Computação Distribuída

Este módulo demonstra o cálculo paralelo do somatório de 1 até N utilizando MPI (Message Passing Interface).

O processo mestre (rank 0) distribui o trabalho entre os processos trabalhadores,
coleta os resultados parciais e calcula o somatório final. Os processos trabalhadores
calculam somas parciais para seus intervalos atribuídos.

Características:
- Distribuição de carga entre múltiplos processos MPI
- Medição de desempenho com tempo de execução
- Balanceamento eficiente de carga com tratamento de resto
- Relatórios detalhados de comunicação entre processos

Autor: Andre
Versão: 2.0
Data: 27/05/2025
"""

from mpi4py import MPI
import datetime
import sys
from typing import Tuple


def calculate_partial_sum(start: int, end: int) -> int:
    """
    Calcula a soma dos inteiros em um intervalo [start, end].
    
    Args:
        start (int): Número inicial (inclusivo)
        end (int): Número final (inclusivo)
        
    Returns:
        int: Soma dos inteiros de start até end
        
    Exemplo:
        >>> calculate_partial_sum(1, 5)
        15
    """
    if start > end:
        return 0
    
    return sum(range(start, end + 1))


def get_work_range(rank: int, total_processes: int, total_number: int) -> Tuple[int, int]:
    """
    Calcula o intervalo de trabalho para um processo trabalhador específico.
    
    Args:
        rank (int): Rank do processo atual (começa em 1 para workers)
        total_processes (int): Número total de processos
        total_number (int): Número máximo a ser somado
        
    Returns:
        Tuple[int, int]: Início e fim do intervalo para este processo
    """
    worker_count = total_processes - 1  # Exclui o processo mestre
    chunk_size = total_number // worker_count
    
    start = chunk_size * (rank - 1) + 1
    end = chunk_size * rank
    
    # O último worker trata o resto
    if rank == total_processes - 1:
        end = total_number
        
    return start, end


def master_process(comm, size: int) -> None:
    """
    Executa a lógica do processo mestre: coordena a distribuição do trabalho e coleta os resultados.
    
    Args:
        comm: Objeto comunicador MPI
        size (int): Número total de processos
    """
    print("Calculadora de Soma Paralela com MPI")
    print("=" * 50)
    
    try:
        number = int(input("Digite o número para calcular o somatório (1 até N): "))
        if number < 1:
            print("Erro: O número deve ser maior que 0")
            sys.exit(1)
    except ValueError:
        print("Erro: Por favor, digite um número válido")
        sys.exit(1)
    
    print(f"Calculando somatório de 1 até {number:,}")
    print(f"Utilizando {size} processos ({size-1} trabalhadores)")
    print("-" * 50)
    
    # Inicia a contagem de tempo
    start_wall_time = MPI.Wtime()
    start_time = datetime.datetime.now()
    
    # Distribui o trabalho para todos os processos trabalhadores
    print("Distribuindo trabalho para os processos...")
    for worker_rank in range(1, size):
        comm.send(number, dest=worker_rank, tag=1)
    
    # Coleta os resultados dos trabalhadores
    total_sum = 0
    print("Coletando resultados dos trabalhadores...")
    
    for worker_rank in range(1, size):
        status = MPI.Status()
        partial_sum = comm.recv(source=worker_rank, tag=2, status=status)
        source_rank = status.Get_source()
        
        print(f"   Recebido do trabalhador {source_rank}: {partial_sum:,}")
        total_sum += partial_sum
    
    # Calcula o tempo de execução
    end_wall_time = MPI.Wtime()
    end_time = datetime.datetime.now()
    
    wall_time = end_wall_time - start_wall_time
    execution_time = (end_time - start_time).total_seconds()
    
    # Exibe os resultados
    print("=" * 50)
    print("RESULTADOS FINAIS")
    print("=" * 50)
    print(f"Somatório final: {total_sum:,}")
    print(f"Tempo de execução (MPI Wall Time): {wall_time:.6f} segundos")
    print(f"Tempo de execução (Python Time): {execution_time:.6f} segundos")
    print(f"Processos utilizados: {size}")
    
    # Calcula o somatório teórico para verificação
    expected_sum = number * (number + 1) // 2
    if total_sum == expected_sum:
        print("Verificação: Resultado correto!")
    else:
        print(f"Erro: Resultado esperado {expected_sum:,}, obtido {total_sum:,}")


def worker_process(comm, rank: int, size: int) -> None:
    """
    Executa a lógica do processo trabalhador: recebe o intervalo e calcula a soma parcial.
    
    Args:
        comm: Objeto comunicador MPI
        rank (int): Rank do processo atual
        size (int): Número total de processos
    """
    # Recebe o trabalho do mestre
    number = comm.recv(source=0, tag=1)
    
    # Calcula o intervalo de trabalho
    start, end = get_work_range(rank, size, number)
    
    print(f"Trabalhador {rank}: Calculando soma de {start:,} até {end:,}")
    
    # Calcula a soma parcial
    partial_sum = calculate_partial_sum(start, end)
    
    print(f"Trabalhador {rank}: Soma parcial = {partial_sum:,}")
    
    # Envia o resultado de volta ao mestre
    comm.send(partial_sum, dest=0, tag=2)


def main():
    """
    Função principal que inicializa o MPI e coordena o cálculo paralelo do somatório.
    """
    # Inicializa a comunicação MPI
    comm = MPI.COMM_WORLD
    size = comm.Get_size()
    rank = comm.Get_rank()
    
    # Garante pelo menos 2 processos (1 mestre + 1 trabalhador)
    if size < 2:
        if rank == 0:
            print("Erro: Este programa requer pelo menos 2 processos MPI")
            print("   Execute com: mpirun -np <N> python3 soma_paralela_mpi.py")
            print("   Onde <N> >= 2")
        sys.exit(1)
    
    try:
        if rank == 0:
            master_process(comm, size)
        else:
            worker_process(comm, rank, size)
    except KeyboardInterrupt:
        print(f"\nProcesso {rank} interrompido pelo usuário")
    except Exception as e:
        print(f"Erro no processo {rank}: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()



