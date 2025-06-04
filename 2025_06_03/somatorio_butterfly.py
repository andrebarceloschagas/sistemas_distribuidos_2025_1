from mpi4py import MPI
import datetime

def somatorio(inicio, fim):
    s = 0
    for i in range(inicio, fim + 1):
        s = s + i
    return s   

comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

# Inicialização das variáveis
soma_local = 0
soma_total = 0

# Fase 1: Distribuição do número e cálculo local
if rank == 0:
    print("Digite o numero para calcular o somatorio:")
    num = int(input())
    
    wt = MPI.Wtime()
    start_time = datetime.datetime.now()
    
    # Envia o número para todos os outros processos
    for i in range(1, size):
        comm.send(num, dest=i, tag=1)
else:
    # Recebe o número do processo 0
    num = comm.recv(source=0, tag=1)
    print("Processo rank %d recebeu número: %d" % (rank, num))

# Cada processo calcula sua parcela do somatório
parcela = int(num / size)
inicio = parcela * rank + 1
fim = parcela * (rank + 1)

# O último processo pega o resto da divisão
if rank == size - 1:
    fim = num

# Calcula o somatório local
soma_local = somatorio(inicio, fim)
print("Processo %d: somatorio(%d, %d) = %d" % (rank, inicio, fim, soma_local))

# Fase 2: Redução Butterfly
metade = size
soma_total = soma_local

while rank < metade and metade > 1:
    metade = metade // 2  # Divisão inteira
    
    if rank >= metade:
        # Processos da metade superior enviam seus resultados
        destino = rank - metade
        comm.send(soma_total, dest=destino, tag=2)
        print("Processo %d enviou soma %d para processo %d" % (rank, soma_total, destino))
        break  # Processo sai do loop após enviar
    else:
        # Processos da metade inferior recebem e somam
        if rank + metade < size:  # Verifica se existe o processo parceiro
            origem = rank + metade
            soma_recebida = comm.recv(source=origem, tag=2)
            soma_total = soma_total + soma_recebida
            print("Processo %d recebeu soma %d do processo %d, total = %d" % 
                  (rank, soma_recebida, origem, soma_total))

# Fase 3: Resultado final
if rank == 0:
    wt = MPI.Wtime() - wt
    end_time = datetime.datetime.now()
    time_diff = (end_time - start_time)
    execution_time = time_diff.total_seconds()
    
    print("\n=== RESULTADO FINAL ===")
    print("Somatorio de 1 ate %d = %d" % (num, soma_total))
    print("Tempo de execucao MPI: %.6f segundos" % wt)
    print("Tempo de execucao total: %.6f segundos" % execution_time)