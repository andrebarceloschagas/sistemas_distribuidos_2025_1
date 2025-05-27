#!/usr/bin/env python3
"""
MPI Parallel Sum Calculator - Distributed Computing Example

This module demonstrates parallel computation using MPI (Message Passing Interface)
to calculate the sum of integers from 1 to N using multiple processes.

The master process (rank 0) distributes the workload among worker processes,
collects partial results, and computes the final sum. Worker processes
calculate partial sums for their assigned ranges.

Features:
- Distributed workload across multiple MPI processes
- Performance measurement with execution time tracking
- Efficient load balancing with remainder handling
- Detailed logging of process communication

Author: Andre
Version: 2.0
Date: 27/05/2025
"""

from mpi4py import MPI
import datetime
import sys
from typing import Tuple


def calculate_partial_sum(start: int, end: int) -> int:
    """
    Calculate the sum of integers in a given range [start, end].
    
    Args:
        start (int): Starting number (inclusive)
        end (int): Ending number (inclusive)
        
    Returns:
        int: Sum of integers from start to end
        
    Example:
        >>> calculate_partial_sum(1, 5)
        15
    """
    if start > end:
        return 0
    
    return sum(range(start, end + 1))


def get_work_range(rank: int, total_processes: int, total_number: int) -> Tuple[int, int]:
    """
    Calculate the work range for a specific worker process.
    
    Args:
        rank (int): Current process rank (1-based for workers)
        total_processes (int): Total number of processes
        total_number (int): The maximum number to sum up to
        
    Returns:
        Tuple[int, int]: Start and end numbers for this process
    """
    worker_count = total_processes - 1  # Exclude master process
    chunk_size = total_number // worker_count
    
    start = chunk_size * (rank - 1) + 1
    end = chunk_size * rank
    
    # Last worker handles any remainder
    if rank == total_processes - 1:
        end = total_number
        
    return start, end


def master_process(comm, size: int) -> None:
    """
    Execute master process logic: coordinate work distribution and collect results.
    
    Args:
        comm: MPI communicator object
        size (int): Total number of processes
    """
    print("🔢 MPI Parallel Sum Calculator")
    print("=" * 50)
    
    try:
        number = int(input("Digite o número para calcular o somatório (1 até N): "))
        if number < 1:
            print("❌ Erro: O número deve ser maior que 0")
            sys.exit(1)
    except ValueError:
        print("❌ Erro: Por favor, digite um número válido")
        sys.exit(1)
    
    print(f"📊 Calculando somatório de 1 até {number:,}")
    print(f"🖥️  Usando {size} processos ({size-1} workers)")
    print("-" * 50)
    
    # Start timing
    start_wall_time = MPI.Wtime()
    start_time = datetime.datetime.now()
    
    # Distribute work to all worker processes
    print("📤 Distribuindo trabalho para os processos...")
    for worker_rank in range(1, size):
        comm.send(number, dest=worker_rank, tag=1)
    
    # Collect results from all workers
    total_sum = 0
    print("📥 Coletando resultados dos workers...")
    
    for worker_rank in range(1, size):
        status = MPI.Status()
        partial_sum = comm.recv(source=worker_rank, tag=2, status=status)
        source_rank = status.Get_source()
        
        print(f"   ✅ Recebido de worker {source_rank}: {partial_sum:,}")
        total_sum += partial_sum
    
    # Calculate execution time
    end_wall_time = MPI.Wtime()
    end_time = datetime.datetime.now()
    
    wall_time = end_wall_time - start_wall_time
    execution_time = (end_time - start_time).total_seconds()
    
    # Display results
    print("=" * 50)
    print("📊 RESULTADOS FINAIS")
    print("=" * 50)
    print(f"🎯 Somatório final: {total_sum:,}")
    print(f"⏱️  Tempo de execução (MPI Wall Time): {wall_time:.6f} segundos")
    print(f"⏱️  Tempo de execução (Python Time): {execution_time:.6f} segundos")
    print(f"🔧 Processos utilizados: {size}")
    
    # Calculate theoretical sum for verification
    expected_sum = number * (number + 1) // 2
    if total_sum == expected_sum:
        print("✅ Verificação: Resultado correto!")
    else:
        print(f"❌ Erro: Resultado esperado {expected_sum:,}, obtido {total_sum:,}")


def worker_process(comm, rank: int, size: int) -> None:
    """
    Execute worker process logic: receive work assignment and compute partial sum.
    
    Args:
        comm: MPI communicator object
        rank (int): Current process rank
        size (int): Total number of processes
    """
    # Receive work assignment from master
    number = comm.recv(source=0, tag=1)
    
    # Calculate work range
    start, end = get_work_range(rank, size, number)
    
    print(f"🔨 Worker {rank}: Calculando soma de {start:,} até {end:,}")
    
    # Calculate partial sum
    partial_sum = calculate_partial_sum(start, end)
    
    print(f"✅ Worker {rank}: Soma parcial = {partial_sum:,}")
    
    # Send result back to master
    comm.send(partial_sum, dest=0, tag=2)


def main():
    """
    Main function that initializes MPI and coordinates the parallel sum calculation.
    """
    # Initialize MPI communication
    comm = MPI.COMM_WORLD
    size = comm.Get_size()
    rank = comm.Get_rank()
    
    # Ensure we have at least 2 processes (1 master + 1 worker)
    if size < 2:
        if rank == 0:
            print("❌ Erro: Este programa requer pelo menos 2 processos MPI")
            print("   Execute com: mpirun -np <N> python3 soma_paralela_mpi.py")
            print("   Onde <N> >= 2")
        sys.exit(1)
    
    try:
        if rank == 0:
            master_process(comm, size)
        else:
            worker_process(comm, rank, size)
    except KeyboardInterrupt:
        print(f"\n⚠️  Processo {rank} interrompido pelo usuário")
    except Exception as e:
        print(f"❌ Erro no processo {rank}: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
  
   
  
