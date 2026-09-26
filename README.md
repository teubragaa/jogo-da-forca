# Jogo da Forca Multiplayer em Tempo Real (Java RMI)

### Trabalho desenvolvido para a disciplina de Sistemas Distribuídos 2026.2.
#### Adaptado e evoluído a partir da base do projeto-chat, ucsal 2026.2

Aplicação de jogo multiplayer em grupo desenvolvida em **Java** utilizando a arquitetura de **RMI (Remote Method Invocation)** e o padrão de projeto **Remote**. O projeto implementa comunicação bidirecional em tempo real (*Broadcast* e *Callbacks*) para gerenciar uma partida colaborativa do Jogo da Forca em rede local (LAN) ou `localhost`.

---

## 📌 Origem do Projeto e Reutilização de Arquitetura

Este projeto foi construído como evolução direta do **`projeto-chat`** (aplicação de chat em tempo real via Java RMI).

A infraestrutura de comunicação em rede, o gerenciamento de concorrência com `ConcurrentHashMap` e o mecanismo de *Callback* assíncrono desenvolvidos no `projeto-chat` foram totalmente reutilizados. A camada de servidor foi estendida para atuar como o **gerenciador de estado e autoridade das regras do jogo**, substituindo as mensagens de texto livre por validação de palpites, controle de vidas e sincronização da palavra secreta.

---

## 🚀 Funcionalidades

- **Conexão Dinâmica via LAN:** Suporte à configuração do endereço IP do servidor para execuções na mesma rede local ou em `localhost`.
- **Modo Colaborativo Multiplayer:** Todos os participantes conectados na sala jogam juntos na mesma partida.
- **Validação de Jogadas no Servidor:** O servidor valida se a letra já foi tentada, se está correta ou errada e atualiza o estado geral.
- **Broadcast em Tempo Real:** Qualquer palpite enviado por um jogador atualiza instantaneamente a tela (desenho ASCII da forca, palavra oculta e vidas) de todos os clientes conectados.
- **Notificações do Sistema:** Informa a entrada de novos jogadores, acertos, erros e telas de vitória ou término de jogo (*Game Over*).

---

## 📐 Arquitetura e Estrutura de Classes

A arquitetura mantém o padrão RMI bidirecional, derivado do `projeto-chat`:

| Classe do `projeto-chat` | Nova Classe (`jogo-forca`) | Função na Arquitetura |
| :--- | :--- | :--- |
| `ChatServerInterface.java` | `JogoForcaServerInterface.java` | Contrato remoto do servidor (`registrarJogador`, `enviarPalpite`). |
| `ChatServerImpl.java` | `JogoForcaServerImpl.java` | Gerencia conexões e contém a lógica do jogo (vidas, palavra e broadcast). |
| `ChatClientInterface.java` | `JogoForcaClientInterface.java` | Contrato remoto de Callback no cliente (`receberAtualizacaoJogo`). |
| `ChatClientImpl.java` | `JogoForcaClientImpl.java` | Recebe as atualizações do estado do jogo e renderiza no terminal. |
| `Main.java` | `MainServer.java` | Inicializa o registro RMI (`LocateRegistry`) na porta `1099`. |
| `ClientMain.java` | `MainClient.java` | Interface CLI do jogador para conectar ao IP e enviar palpites. |

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java (JDK 17+)
- **Comunicação Distribuída:** Java RMI (`java.rmi.*`)
- **Gerenciamento de Dependências / Build:** Maven

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- **Java Development Kit (JDK 17 ou superior)** instalado.
- Dispositivos conectados na mesma rede local (LAN).

---

### Passo 1: Executar o Servidor (`MainServer.java`)

1. Execute a classe `MainServer.java`.
2. Informe o **IP da máquina do servidor na rede local** (ou aperte `Enter` para `localhost`).
3. O serviço RMI será publicado na porta **1099**.

> ⚠️ **Nota:** Certifique-se de que a porta `1099` esteja liberada no Firewall do sistema operacional da máquina do servidor.

---

### Passo 2: Executar os Clientes (`MainClient.java`)

Nas máquinas dos jogadores:

1. Execute a classe `MainClient.java`.
2. Digite o **IP do servidor** (ex: `10.18.5.XX` ou `localhost`).
3. Digite o seu **nome de jogador**.
4. Envie palpites digitando uma letra por vez no terminal e acompanhe a evolução do jogo em tempo real!

---

## 📂 Estrutura do Repositório

```text
src/main/java/org/example/
├── JogoForcaClientImpl.java       # Implementação do Callback / Exibição ASCII
├── JogoForcaClientInterface.java  # Interface Remota do Cliente (Callback)
├── JogoForcaServerImpl.java       # Servidor RMI + Regras do Jogo da Forca
├── JogoForcaServerInterface.java  # Interface Remota do Servidor
├── MainClient.java                # Ponto de Entrada CLI do Jogador
└── MainServer.java                # Ponto de Entrada e Registro RMI