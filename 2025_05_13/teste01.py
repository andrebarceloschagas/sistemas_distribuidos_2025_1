#!/usr/bin/env python3
"""
Programa MPI - Hello World Básico

Versão alternativa do programa básico de MPI que demonstra
a inicialização e identificação de processos paralelos.

Para executar:
    mpiexec -n 4 python teste01.py

Autor: Andre
Data: 27 de maio de 2025
"""

from mpi4py import MPI


def main():
    """
    Função principal que executa o programa MPI básico.
    
    Inicializa o ambiente MPI e faz cada processo imprimir
    sua identificação única (rank).
    """
    # Inicializa o comunicador MPI que gerencia todos os processos
    comm = MPI.COMM_WORLD
    
    # Obtém o rank (identificador único) do processo atual
    rank = comm.Get_rank()
    
    # Imprime mensagem de identificação do processo
    print(f"Hello world from process {rank}")


if __name__ == "__main__":
    main()