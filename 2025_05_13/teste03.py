#!/usr/bin/env python3
"""
Programa MPI - Comunicação Mestre-Escravo

Este programa demonstra comunicação básica entre processos usando MPI.
O processo mestre (rank 0) envia uma mensagem para todos os escravos,
que a transformam em maiúscula e retornam para o mestre.

Funcionalidades:
1. Mestre envia mensagem "Hello, world" para todos os escravos
2. Escravos transformam a mensagem para maiúscula
3. Escravos enviam mensagem alterada de volta para o mestre
4. Mestre recebe e exibe as mensagens dos escravos

Para executar:
    mpiexec -n 4 python teste03.py

Autor: Andre
Data: 27 de maio de 2025
"""

from mpi4py import MPI


def main():
    """
    Função principal que coordena a comunicação mestre-escravo.
    
    O processo com rank 0 atua como mestre, enviando mensagens
    e recebendo respostas. Os demais processos atuam como escravos.
    """
    # Comunicador básico que envolve todos os processos
    comm = MPI.COMM_WORLD
    
    # Obtém o número total de processos
    size = comm.Get_size()
    
    # Obtém o rank (ID) do processo atual
    rank = comm.Get_rank()
    
    if rank == 0:
        # Processo mestre (rank 0)
        processo_mestre(comm, size)
    else:
        # Processos escravos (rank > 0)
        processo_escravo(comm, rank)


def processo_mestre(comm, size):
    """
    Executa as tarefas do processo mestre.
    
    Args:
        comm: Comunicador MPI
        size: Número total de processos
    """
    # Mensagem a ser enviada para os escravos
    msg = "Hello, world"
    
    # Envia mensagem para todos os processos escravos
    for i in range(1, size):
        comm.send(msg, dest=i, tag=1)
        print(f"Mestre enviou mensagem para escravo {i}")
    
    # Recebe respostas de todos os escravos
    for i in range(1, size):
        # Recebe dados do escravo i com tag 2
        resposta = comm.recv(source=i, tag=2)
        print(f"Msg recebida do escravo {i} = {resposta}")


def processo_escravo(comm, rank):
    """
    Executa as tarefas dos processos escravos.
    
    Args:
        comm: Comunicador MPI
        rank: Rank do processo escravo atual
    """
    # Recebe mensagem do mestre (fonte 0) com tag 1
    mensagem_recebida = comm.recv(source=0, tag=1)
    
    # Mostra o rank e a mensagem recebida
    print(f"Escravo de rank {rank}: {mensagem_recebida}")
    
    # Transforma a mensagem para maiúscula
    mensagem_maiuscula = mensagem_recebida.upper()
    
    # Envia mensagem transformada de volta para o mestre
    comm.send(mensagem_maiuscula, dest=0, tag=2)
    print(f"Escravo {rank} enviou resposta: {mensagem_maiuscula}")


if __name__ == "__main__":
    main()
