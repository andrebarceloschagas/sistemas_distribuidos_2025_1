from mpi4py import MPI

comm = MPI.COMM_WORLD  # comunicador básico que envolve todos os processos sempre deve existir
size = comm.Get_size()  # pega o número/quantidade de processos
rank = comm.Get_rank()  # pega o rank (ID) do processo

if rank == 0:  # sou o mestre
    msg = "Hello, world"

    for i in range(1, size):
        comm.send(
            msg, dest=i, tag=1)  # envia dado msg para destino (dest) indicado pelo processo i com tag 1

    for i in range(1, size):
        s = comm.recv(source=i, tag=2)

    print("Msg recebida do escravo %d = %s" % (i, s))  # mostra o rank e o dado recebido pelo escravo

else:  # sou escravo
    s = comm.recv(source=0, tag=1)  # recebe dado s da fonte (source) 0 (mestre) com tag 1

    print("Escravo de rank %d: %s" % (rank, s))  # mostra o rank e o dado recebido pelo escravo

    s = s.upper()

    comm.send(s, dest=0, tag=2)

'''
atividade
1)ESCRAVO TRANSFORMAR A MENSAGEM RECEBIDA DO MESTRE PARA MAIÚSCULA
2)ESCRAVO ENVIA MENSAGEM ALTERA PARA O MESTRE
3)MESTRE RECEBE MENSAGEM DE CADA ESCRAVO E APRESENTA NA TELA DA
SEGUINTE MANEIRA: MSG RECEBIDA DO ESCRAVO xxx=xxxx
'''
