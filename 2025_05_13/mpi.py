#!/usr/bin/env python3
"""
Programa básico MPI - Hello World

Este programa demonstra o uso básico do MPI (Message Passing Interface)
onde cada processo imprime uma mensagem com seu rank único.

Para executar:
    mpiexec -n 4 python mpi.py

Autor: Andre
Data: 27 de maio de 2025
"""

from mpi4py import MPI


def main():
    """
    Função principal que executa o programa MPI básico.
    
    Cada processo obtém seu rank único e imprime uma mensagem
    de identificação no terminal.
    """
    # Obtém o comunicador global que inclui todos os processos MPI
    comm = MPI.COMM_WORLD
    
    # Obtém o identificador único (rank) deste processo
    # Os ranks vão de 0 até (número_de_processos - 1)
    rank = comm.Get_rank()
    
    # Imprime mensagem identificando o processo atual
    print(f"Hello world from process {rank}")


if __name__ == "__main__":
    main()
