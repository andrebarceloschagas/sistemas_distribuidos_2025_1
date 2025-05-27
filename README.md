# 🌐 Sistemas Distribuídos 2025.1

## 📝 Visão Geral

Este repositório contém exemplos práticos e exercícios de **Sistemas Distribuídos**, implementados em **Java** e **Python**, demonstrando conceitos fundamentais como comunicação TCP/UDP, programação concorrente, serialização de objetos e computação paralela com MPI.

## 🗂️ Estrutura do Projeto

```
sistemas_distribuidos_2025_1/
│
├── 2025_04_15/          # 🌐 Comunicação TCP/IP Básica
│   ├── Cliente2.java    # Cliente TCP simples
│   ├── Servidor2.java   # Servidor TCP básico
│   └── ScanDePorta.java # Scanner de portas
│
├── 2025_04_22/          # 🧵 Programação Concorrente
│   ├── TesteThreads.java           # Thread trabalhadora básica
│   ├── CorridaTreads.java          # Simulador de corrida com threads
│   ├── CorridaThreadsHeranca.java  # Herança de threads
│   └── cliente_servidor/
│       ├── Cliente4.java           # Cliente TCP interativo
│       ├── Servidor4.java          # Servidor TCP concorrente
│       └── ServidorThread.java     # Thread de tratamento de cliente
│
├── 2025_04_29/          # 📦 Serialização de Objetos
│   ├── MensagemTeste.java   # Classe serializável de teste
│   ├── Cliente5.java       # Cliente com envio de objetos
│   └── Servidor5.java      # Servidor com recepção de objetos
│
├── 2025_05_06/          # 📡 Comunicação UDP
│   ├── ClienteUDP.java     # Cliente UDP básico
│   ├── ServidorUDP.java    # Servidor UDP echo
│   └── exercicio/
│       ├── ExercicioUDP.java          # Cliente/Servidor UDP integrado
│       ├── UDPServerConcorrente.java  # Servidor UDP multi-thread
│       └── UDPServerInverter.java     # Servidor UDP com inversão
│
├── 2025_05_13/          # 🐍 Computação Paralela (Python MPI)
│   ├── mpi.py           # Utilitários MPI básicos
│   ├── teste01.py       # Hello World MPI
│   ├── teste03.py       # Comunicação point-to-point
│   ├── teste04.py       # Broadcast e scatter
│   └── teste07.py       # Reduce e gather
│
└── 2025_05_27/          # 🚀 Projetos Avançados
    └── soma_paralela_mpi.py  # Somatório paralelo com MPI
```

## 🛠️ Tecnologias Utilizadas

### ☕ Java
- **JDK 17+** (compatível com versões anteriores)
- **Sockets TCP/UDP** para comunicação de rede
- **Threads** para programação concorrente
- **ObjectInputStream/ObjectOutputStream** para serialização
- **Logging** para rastreamento e depuração

### 🐍 Python
- **Python 3.8+**
- **mpi4py** para computação paralela
- **Conformidade PEP 8** para estilo de código
- **Type hints** para melhor documentação
- **Docstrings** para documentação de funções

## 📋 Pré-requisitos

### Para Java:
```bash
# Verificar versão do Java
java -version
javac -version

# Instalar OpenJDK (se necessário)
sudo apt update
sudo apt install default-jdk
```

### Para Python:
```bash
# Verificar versão do Python
python3 --version

# Instalar MPI e mpi4py
sudo apt install mpich python3-pip
pip3 install mpi4py
```

## 🚀 Como Executar

### 📡 Comunicação TCP/UDP (Java)

#### Cliente-Servidor TCP Básico:
```bash
cd 2025_04_15/

# Terminal 1: Servidor
javac Servidor2.java && java Servidor2

# Terminal 2: Cliente
javac Cliente2.java && java Cliente2
```

#### Scanner de Portas:
```bash
cd 2025_04_15/
javac ScanDePorta.java && java ScanDePorta
```

#### Cliente-Servidor TCP Concorrente:
```bash
cd 2025_04_22/cliente_servidor/

# Terminal 1: Servidor
javac ServidorThread.java Servidor4.java && java Servidor4

# Terminal 2: Cliente
javac Cliente4.java && java Cliente4
```

#### Comunicação UDP:
```bash
cd 2025_05_06/

# Terminal 1: Servidor UDP
javac ServidorUDP.java && java ServidorUDP

# Terminal 2: Cliente UDP
javac ClienteUDP.java && java ClienteUDP "mensagem teste" localhost 6789
```

### 🧵 Programação Concorrente (Java)

#### Simulação de Corrida com Threads:
```bash
cd 2025_04_22/
javac TesteThreads.java CorridaTreads.java && java CorridaTreads
```

#### Threads por Herança:
```bash
cd 2025_04_22/
javac CorridaThreadsHeranca.java && java CorridaThreadsHeranca
```

### 📦 Serialização de Objetos (Java)

```bash
cd 2025_04_29/

# Terminal 1: Servidor
javac MensagemTeste.java Servidor5.java && java Servidor5

# Terminal 2: Cliente
javac Cliente5.java && java Cliente5
```

### 🐍 Computação Paralela (Python MPI)

#### Testes Básicos MPI:
```bash
cd 2025_05_13/

# Hello World MPI
mpirun -np 4 python3 teste01.py

# Comunicação Point-to-Point
mpirun -np 4 python3 teste03.py

# Operações Coletivas
mpirun -np 4 python3 teste04.py
mpirun -np 4 python3 teste07.py
```

#### Somatório Paralelo Avançado:
```bash
cd 2025_05_27/

# Executar com 4 processos
mpirun -np 4 python3 soma_paralela_mpi.py

# Executar com 8 processos
mpirun -np 8 python3 soma_paralela_mpi.py
```

## 📚 Conceitos Demonstrados

### 🌐 Comunicação de Rede
- **TCP**: Conexões confiáveis e orientadas a conexão
- **UDP**: Comunicação rápida sem garantias
- **Sockets**: Interface de programação para rede
- **Timeouts**: Controle de tempo limite de conexão

### 🧵 Concorrência
- **Threads Java**: Programação multi-threaded
- **Sincronização**: Coordenação entre threads
- **Thread Safety**: Programação segura para threads
- **Pools de Threads**: Gerenciamento eficiente de recursos

### 📦 Serialização
- **ObjectInputStream/ObjectOutputStream**: Serialização Java nativa
- **Serializable**: Interface para objetos serializáveis
- **Versionamento**: Controle de versão de objetos

### 🐍 Computação Paralela
- **MPI**: Message Passing Interface
- **Paralelização**: Distribuição de carga de trabalho
- **Comunicação de Processos**: Point-to-point e coletiva
- **Sincronização Distribuída**: Coordenação entre processos

## 🎯 Padrões e Boas Práticas

### ☕ Java
- ✅ **Nomenclatura**: CamelCase para classes, camelCase para métodos
- ✅ **JavaDoc**: Documentação completa de classes e métodos
- ✅ **Exception Handling**: Tratamento robusto de exceções
- ✅ **Resource Management**: try-with-resources e finally blocks
- ✅ **Constants**: Uso de static final para valores constantes
- ✅ **Logging**: Uso do framework de logging Java

### 🐍 Python
- ✅ **PEP 8**: Conformidade com o guia de estilo Python
- ✅ **Type Hints**: Anotações de tipo para melhor legibilidade
- ✅ **Docstrings**: Documentação de funções e classes
- ✅ **Error Handling**: Tratamento adequado de exceções
- ✅ **Function Decomposition**: Quebra de código em funções menores
- ✅ **Constants**: Uso de UPPER_CASE para constantes

## 🔧 Ferramentas de Desenvolvimento

### Compilação e Execução:
```bash
# Compilar todos os arquivos Java
find . -name "*.java" -exec javac {} \;

# Verificar sintaxe Python
python3 -m py_compile arquivo.py

# Limpar arquivos compilados
find . -name "*.class" -delete
rm -rf __pycache__/
```

### Verificação de Estilo:
```bash
# Para Python (instalar flake8)
pip3 install flake8
flake8 arquivo.py

# Para Java (usar IDEs como IntelliJ ou Eclipse)
```

## 📖 Referências e Recursos

- **Java Networking**: [Oracle Java Networking Tutorial](https://docs.oracle.com/javase/tutorial/networking/)
- **Java Concurrency**: [Oracle Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- **Python MPI**: [mpi4py Documentation](https://mpi4py.readtimeout/en/stable/)
- **UDP Programming**: [UDP Socket Programming Guide](https://docs.oracle.com/javase/tutorial/networking/datagrams/)

## 🤝 Contribuição

Para contribuir com este projeto:

1. 🍴 Faça um fork do repositório
2. 🌟 Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. 📝 Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. 📤 Push para a branch (`git push origin feature/nova-funcionalidade`)
5. 🔀 Abra um Pull Request

## 📄 Licença

Este projeto é desenvolvido para fins educacionais no curso de **Sistemas Distribuídos 2025.1**.

---

**Desenvolvido com ❤️ para aprendizado de Sistemas Distribuídos**

*Última atualização: 27 de maio de 2025*
