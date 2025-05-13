import datetime


def somatorio(inicio, fim):
    s = 0
    for i in range(inicio, fim + 1):
        s = s + i
    return s


print("Digite o número para calcular o somatório:")
num = int(input())

start_time = datetime.datetime.now()

soma = somatorio(1, num)

end_time = datetime.datetime.now()

time_diff = end_time - start_time
execution_time = time_diff.total_seconds()

print("Resultado final do somatório=", soma)

print("Tempo de execução02 em segundos de relógio: %.20lf " % (execution_time))
