'''
CCOMP/UFT - Sistemas Distribuídos 2025/1
Discente: Antonio André Barcelos Chagas

Algoritmo de balanceamento de cargas com MPI

'''

from mpi4py import MPI
import datetime

def somatorio(inicio, fim):
   s=0
   for i in range(inicio, fim+1):
     s=s+i
   
   return s   

comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()

if rank == 0:
  print("Digite o número para calcular o somatório:")
  num=int(input())
  
  wt = MPI.Wtime()
  start_time = datetime.datetime.now()
  
  # Cria pool de tarefas pequenas para balanceamento dinâmico
  tamanho_tarefa = max(1, num // (size * 4))  # Tarefas menores
  tarefas = []
  inicio_tarefa = 1
  while inicio_tarefa <= num:
    fim_tarefa = min(inicio_tarefa + tamanho_tarefa - 1, num)
    tarefas.append((inicio_tarefa, fim_tarefa))
    inicio_tarefa = fim_tarefa + 1
  
  soma = 0
  tarefas_enviadas = 0
  resultados_recebidos = 0
  
  # Envia tarefa inicial para cada escravo
  for i in range(1, size):
    if tarefas_enviadas < len(tarefas):
      inicio, fim = tarefas[tarefas_enviadas]
      comm.send(inicio, dest=i, tag=1)
      comm.send(fim, dest=i, tag=2)
      tarefas_enviadas += 1
    else:
      comm.send(-1, dest=i, tag=1)  # Sinal de fim
      comm.send(-1, dest=i, tag=2)
  
  # Loop de balanceamento dinâmico
  while resultados_recebidos < len(tarefas):
    info = MPI.Status()
    s = comm.recv(source=MPI.ANY_SOURCE, tag=3, status=info)
    print("rank do escravo do qual recebeu=", info.Get_source())
    soma += s
    resultados_recebidos += 1
    
    # Envia nova tarefa para o escravo que terminou
    escravo_id = info.Get_source()
    if tarefas_enviadas < len(tarefas):
      inicio, fim = tarefas[tarefas_enviadas]
      comm.send(inicio, dest=escravo_id, tag=1)
      comm.send(fim, dest=escravo_id, tag=2)
      tarefas_enviadas += 1
    else:
      comm.send(-1, dest=escravo_id, tag=1)  # Sinal de fim
      comm.send(-1, dest=escravo_id, tag=2)
  
  wt = MPI.Wtime() - wt
  end_time = datetime.datetime.now()
  time_diff = (end_time - start_time)
  execution_time = time_diff.total_seconds() 

  print("Resultado final do somatório=", soma)
  print("Tempo de execução em segundos de relógio: ", wt)
  print("Tempo de execução02 em segundos de relógio: ", execution_time)
  
  
else:
  while True:
    inicio = comm.recv(source=0, tag=1)
    fim = comm.recv(source=0, tag=2)
    
    if inicio == -1 and fim == -1:  # Sinal de fim
      print("Escravo de rank %d finalizando" % rank)
      break
    
    print("Escravo de rank %d: processando %d a %d" % (rank, inicio, fim))
    soma = somatorio(inicio, fim)
    comm.send(soma, dest=0, tag=3)
