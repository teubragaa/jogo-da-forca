# Jogo da Forca Multiplayer em Tempo Real (Java RMI)

### Trabalho desenvolvido para a disciplina de Sistemas Distribuídos 2026.2.
#### Adaptado e evoluído a partir da base do projeto-chat, ucsal 2026.2

Aplicação de jogo multiplayer em grupo desenvolvida em **Java** utilizando a arquitetura de **RMI (Remote Method Invocation)** e o padrão de projeto **Remote**. O projeto implementa comunicação bidirecional em tempo real (*Broadcast* e *Callbacks*) para gerenciar uma partida colaborativa do Jogo da Forca em rede local (LAN) ou `localhost`.

---

##  Origem do Projeto e Reutilização de Arquitetura

Este projeto foi construído como evolução direta do **`projeto-chat`** (aplicação de chat em tempo real via Java RMI).

A infraestrutura de comunicação em rede, o gerenciamento de concorrência com `ConcurrentHashMap` e o mecanismo de *Callback* assíncrono desenvolvidos no `projeto-chat` foram totalmente reutilizados. A camada de servidor foi estendida para atuar como o **gerenciador de estado e autoridade das regras do jogo**, substituindo as mensagens de texto livre por validação de palpites, controle de vidas e sincronização da palavra secreta.

---

##  Funcionalidades

- **Conexão Dinâmica via LAN:** Suporte à configuração do endereço IP do servidor para execuções na mesma rede local ou em `localhost`.
- **Modo Colaborativo Multiplayer:** Todos os participantes conectados na sala jogam juntos na mesma partida.
- **Validação de Jogadas no Servidor:** O servidor valida se a letra já foi tentada, se está correta ou errada e atualiza o estado geral.
- **Broadcast em Tempo Real:** Qualquer palpite enviado por um jogador atualiza instantaneamente a tela (desenho ASCII da forca, palavra oculta e vidas) de todos os clientes conectados.
- **Notificações do Sistema:** Informa a entrada de novos jogadores, acertos, erros e telas de vitória ou término de jogo (*Game Over*).

---

## Tecnologias Utilizadas

- **Linguagem:** Java (JDK 17+)
- **Comunicação Distribuída:** Java RMI (`java.rmi.*`)
- **Gerenciamento de Dependências / Build:** Maven

---

##  Arquitetura e Estrutura de Classes

O projeto foi estruturado seguindo uma arquitetura em camadas simples (MVC/Layered) sobre o protocolo **Java RMI (Remote Method Invocation)**, separando a lógica de negócio, a infraestrutura de rede e a apresentação no terminal.

###  Estrutura de Pacotes

```text
src/main/java/org/example/
├── model/
│   └── JogoForca.java          # Regras de negócio, turnos, palavra e vidas
├── rmi/
│   ├── JogoForcaClientInterface.java # Contrato RMI para callbacks no cliente
│   ├── JogoForcaClientImpl.java      # Implementação do callback de recebimento
│   ├── JogoForcaServerInterface.java # Contrato RMI para ações do servidor
│   └── JogoForcaServerImpl.java      # Servidor RMI e orquestrador de broadcast
├── client/
│   ├── ClientMain.java         # Ponto de entrada CLI do jogador
│   └── ViewConsole.java        # Formatação e desenho ASCII da forca
└── server/
    └── ServerMain.java         # Ponto de entrada e registro RMI Registry (1099)