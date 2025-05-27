# Sistemas Distribuídos 2025.1

**Acadêmico:** [Antonio André Barcelos Chagas](https://github.com/andrebarceloschagas)

**Disciplina:** Sistemas Distribuídos 2025.1  

**Professor:** Marcelo Lisboa  

**Instituição:** CCOMP/UFT

## Visão Geral

Este repositório reúne exemplos práticos e exercícios do curso de Sistemas Distribuídos, implementados em Java e Python. Os códigos demonstram conceitos fundamentais como comunicação TCP/UDP, programação concorrente, serialização de objetos e computação paralela utilizando MPI.

## Estrutura do Projeto

```
sistemas_distribuidos_2025_1/
│
├── 2025_04_15/          # Comunicação TCP/IP Básica
│   ├── Cliente2.java
│   ├── Servidor2.java
│   └── ScanDePorta.java
│
├── 2025_04_22/          # Programação Concorrente
│   ├── TesteThreads.java
│   ├── CorridaTreads.java
│   ├── CorridaThreadsHeranca.java
│   └── cliente_servidor/
│       ├── Cliente4.java
│       ├── Servidor4.java
│       └── ServidorThread.java
│
├── 2025_04_29/          # Serialização de Objetos
│   ├── MensagemTeste.java
│   ├── Cliente5.java
│   └── Servidor5.java
│
├── 2025_05_06/          # Comunicação UDP
│   ├── ClienteUDP.java
│   ├── ServidorUDP.java
│   └── exercicio/
│       ├── ExercicioUDP.java
│       ├── UDPServerConcorrente.java
│       └── UDPServerInverter.java
│
├── 2025_05_13/          # Computação Paralela (Python MPI)
│   ├── mpi.py
│   ├── teste01.py
│   ├── teste03.py
│   ├── teste04.py
│   └── teste07.py
│
└── 2025_05_27/          # Projetos Avançados
    └── soma_paralela_mpi.py
```

## Tecnologias Utilizadas

### Java
- JDK 17 ou superior (compatível com versões anteriores)
- Sockets TCP/UDP para comunicação de rede
- Threads para programação concorrente
- ObjectInputStream/ObjectOutputStream para serialização
- Logging para rastreamento e depuração

### Python
- Python 3.8 ou superior
- mpi4py para computação paralela
- Conformidade com PEP 8
- Type hints e docstrings

## Pré-requisitos

### Java
```bash
java -version
javac -version
sudo apt update
sudo apt install default-jdk
```

### Python
```bash
python3 --version
sudo apt install mpich python3-pip
pip3 install mpi4py
```

## Como Executar

### Comunicação TCP/UDP (Java)

#### Cliente-Servidor TCP Básico
```bash
cd 2025_04_15/
javac Servidor2.java && java Servidor2   # Servidor
javac Cliente2.java && java Cliente2     # Cliente
```

#### Scanner de Portas
```bash
cd 2025_04_15/
javac ScanDePorta.java && java ScanDePorta
```

#### Cliente-Servidor TCP Concorrente
```bash
cd 2025_04_22/cliente_servidor/
javac ServidorThread.java Servidor4.java && java Servidor4   # Servidor
javac Cliente4.java && java Cliente4                         # Cliente
```

#### Comunicação UDP
```bash
cd 2025_05_06/
javac ServidorUDP.java && java ServidorUDP
javac ClienteUDP.java && java ClienteUDP "mensagem teste" localhost 6789
```

### Programação Concorrente (Java)

#### Simulação de Corrida com Threads
```bash
cd 2025_04_22/
javac TesteThreads.java CorridaTreads.java && java CorridaTreads
```

#### Threads por Herança
```bash
cd 2025_04_22/
javac CorridaThreadsHeranca.java && java CorridaThreadsHeranca
```

### Serialização de Objetos (Java)
```bash
cd 2025_04_29/
javac MensagemTeste.java Servidor5.java && java Servidor5   # Servidor
javac Cliente5.java && java Cliente5                        # Cliente
```

### Computação Paralela (Python MPI)

#### Testes Básicos MPI
```bash
cd 2025_05_13/
mpirun -np 4 python3 teste01.py
mpirun -np 4 python3 teste03.py
mpirun -np 4 python3 teste04.py
mpirun -np 4 python3 teste07.py
```

#### Somatório Paralelo Avançado
```bash
cd 2025_05_27/
mpirun -np 4 python3 soma_paralela_mpi.py
mpirun -np 8 python3 soma_paralela_mpi.py
```

## Conceitos Demonstrados

- Comunicação de rede (TCP, UDP, sockets, timeouts)
- Programação concorrente (threads, sincronização, thread safety)
- Serialização de objetos (ObjectInputStream/ObjectOutputStream, Serializable)
- Computação paralela (MPI, paralelização, comunicação entre processos)

## Padrões e Boas Práticas

### Java
- Nomenclatura em CamelCase para classes e camelCase para métodos
- JavaDoc em todas as classes e métodos
- Tratamento robusto de exceções
- Uso de try-with-resources e finally para gerenciamento de recursos
- Uso de constantes (static final)
- Logging estruturado

### Python
- Conformidade com PEP 8
- Anotações de tipo (type hints)
- Docstrings em funções e classes
- Tratamento adequado de exceções
- Decomposição em funções menores
- Uso de constantes em UPPER_CASE

## Ferramentas de Desenvolvimento

### Compilação e Execução
```bash
find . -name "*.java" -exec javac {} \;         # Compilar todos os arquivos Java
python3 -m py_compile arquivo.py                # Verificar sintaxe Python
find . -name "*.class" -delete                  # Limpar arquivos compilados Java
rm -rf __pycache__/                             # Limpar caches Python
```

### Verificação de Estilo
```bash
pip3 install flake8
flake8 arquivo.py                               # Python
# Para Java, recomenda-se o uso de IDEs como IntelliJ ou Eclipse
```

## Referências

- Oracle Java Networking Tutorial: https://docs.oracle.com/javase/tutorial/networking/
- Oracle Concurrency Tutorial: https://docs.oracle.com/javase/tutorial/essential/concurrency/
- mpi4py Documentation: https://mpi4py.readthedocs.io/en/stable/
- UDP Socket Programming Guide: https://docs.oracle.com/javase/tutorial/networking/datagrams/

## Contribuição

Para contribuir com este projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua funcionalidade (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Licença

Este projeto é destinado exclusivamente para fins educacionais no contexto do curso de Sistemas Distribuídos 2025.1.

---

*Última atualização: 27 de maio de 2025*
