# Futebol Bet

Aplicacao Java desktop para gerenciar apostas em partidas de campeonatos de futebol.
Usuarios se organizam em grupos, fazem previsoes de resultados e acumulam pontos
com base na precisao das apostas.

---

## Tecnologias

| Item              | Versao / Descricao                     |
|-------------------|----------------------------------------|
| Linguagem         | Java 11 ou superior                    |
| Interface grafica | Java Swing                             |
| Banco de dados    | SQLite via sqlite-jdbc 3.47.1.0        |
| Build             | Scripts PowerShell (sem Maven/Gradle)  |

---

## Como executar (Windows — PowerShell)

### Passo 1 — Primeira execucao (so precisa fazer uma vez)

Abra o PowerShell na pasta raiz do projeto e execute:

```powershell
.\setup.ps1
```

Este script:
- Cria a pasta `lib/`
- Baixa o driver SQLite (`sqlite-jdbc-3.47.1.0.jar`) do Maven Central
- Cria a pasta `out/` para os arquivos compilados

> **Requisito:** conexao com a internet para o download do driver.

---

### Passo 2 — Compilar

```powershell
.\compilar.ps1
```

Compila todos os arquivos `.java` da pasta `src/` e gera os `.class` em `out/`.

---

### Passo 3 — Executar

```powershell
.\executar.ps1
```

Inicia a aplicacao. Na primeira execucao, o banco de dados `futebol_bet.db` e criado
automaticamente na raiz do projeto.

> O script `executar.ps1` ja compila automaticamente se necessario.

---

### Atalho (do segundo uso em diante)

Depois de ter feito o setup, basta:

```powershell
.\executar.ps1
```

---

## Login padrao

| Campo  | Valor                    |
|--------|--------------------------|
| Email  | admin@futebolbet.com     |
| Senha  | admin123                 |
| Perfil | Administrador            |

Novos participantes podem se cadastrar pela tela de login.

---

## Fluxo de uso

### Administrador
1. Cadastra os **clubes** (ex.: Flamengo, Palmeiras...)
2. Cria **campeonatos** selecionando entre 2 e 8 clubes
3. **Agenda partidas** informando campeonato, clubes e data/hora
4. Apos a partida, **registra o resultado** (placar final)
5. O sistema calcula e distribui os pontos automaticamente
6. Pode criar **grupos** e visualizar a **classificacao**

### Participante
1. Cria uma conta pela tela de login
2. **Cria ou ingressa** em um grupo (maximo 5 participantes por grupo)
3. Faz **apostas** em partidas com pelo menos 20 minutos de antecedencia
4. Visualiza **partidas** e **classificacao** do seu grupo

---

## Regras de negocio

| Regra                                      | Valor      |
|--------------------------------------------|------------|
| Maximo de clubes por campeonato            | 8          |
| Maximo de participantes por grupo          | 5          |
| Maximo de grupos no sistema                | 5          |
| Prazo minimo para apostar antes da partida | 20 minutos |
| Acertar apenas o resultado (vencedor/empate) | 5 pontos |
| Acertar o placar exato                     | 10 pontos  |
| Errar                                      | 0 pontos   |

Tanto **Administrador** quanto **Participante** podem criar grupos.

---

## Arquitetura MVC

O projeto segue o padrao **Model-View-Controller** com separacao clara entre camadas.

```
View (ui/)
  |
  v
Controller (controller/)       <-- camada de mediacao
  |
  v
Service (service/)             <-- logica de negocio
  |
  v
Repository (repository/)       <-- acesso ao banco de dados
  |
  v
SQLite (futebol_bet.db)        <-- persistencia em disco
```

### Camadas

| Pacote         | Responsabilidade                                              |
|----------------|---------------------------------------------------------------|
| `models/`      | Entidades do dominio (Usuario, Partida, Aposta, Grupo...)    |
| `repository/`  | Acesso ao banco SQLite — carrega dados na inicializacao e persiste cada alteracao |
| `service/`     | Regras de negocio (calculo de pontos, validacoes, prazos...) |
| `controller/`  | Ponte entre as telas e os servicos — as Views so falam com Controllers |
| `ui/`          | Telas Swing (Views)                                          |
| `ui/theme/`    | Paleta de cores, fontes e estilos globais (UiTheme)          |
| `enums/`       | Tipos enumerados (TipoResultado, DashboardAba)               |

---

## Estrutura de diretorios

```
futebol-bet/
├── setup.ps1                        <- baixa o driver SQLite (1a vez)
├── compilar.ps1                     <- compila o projeto
├── executar.ps1                     <- executa o projeto
├── futebol_bet.db                   <- banco SQLite (criado automaticamente)
├── lib/
│   └── sqlite-jdbc-3.47.1.0.jar    <- driver JDBC para SQLite
├── out/                             <- arquivos .class compilados
└── src/br/com/futebolbet/
    ├── Main.java
    ├── enums/
    │   ├── DashboardAba.java
    │   └── TipoResultado.java
    ├── models/
    │   ├── Usuario.java             (classe abstrata)
    │   ├── Administrador.java
    │   ├── Participante.java
    │   ├── Clube.java
    │   ├── Campeonato.java          (max. 8 clubes)
    │   ├── Partida.java
    │   ├── Resultado.java
    │   ├── Aposta.java
    │   └── Grupo.java               (max. 5 participantes)
    ├── repository/
    │   ├── DatabaseConnection.java  (conexao JDBC com SQLite)
    │   ├── DatabaseInitializer.java (cria as tabelas na primeira execucao)
    │   ├── UsuarioRepository.java
    │   ├── ClubeRepository.java
    │   ├── CampeonatoRepository.java
    │   ├── PartidaRepository.java
    │   ├── GrupoRepository.java
    │   └── ApostaRepository.java
    ├── service/
    │   ├── AuthService.java
    │   ├── ApostaService.java
    │   ├── CampeonatoService.java
    │   └── GrupoService.java
    ├── controller/
    │   ├── LoginController.java
    │   ├── ClubeController.java
    │   ├── CampeonatoController.java
    │   ├── PartidaController.java
    │   ├── ResultadoController.java
    │   ├── GrupoController.java
    │   ├── ApostaController.java
    │   └── ClassificacaoController.java
    └── ui/
        ├── theme/
        │   └── UiTheme.java         (paleta, fontes, estilos globais)
        ├── AtualizavelInterface.java
        ├── LoginUi.java
        ├── CadastroUsuarioUI.java
        ├── MenuPrincipalUI.java
        ├── AdminClubeUI.java
        ├── AdminCampeonatoUI.java
        ├── AdminPartidasUI.java
        ├── AdminResultadosUI.java
        ├── AdminGruposUI.java
        ├── ParticipantePartidasUI.java
        ├── ParticipanteGruposUI.java
        ├── ApostasUI.java
        └── ClassificacaoUI.java
```

---

## Banco de dados

O arquivo `futebol_bet.db` e criado automaticamente na primeira execucao.
As tabelas sao criadas pelo `DatabaseInitializer` usando as seguintes entidades:

```
usuarios          — administradores e participantes
clubes            — times de futebol
campeonatos       — torneios criados pelo administrador
campeonato_clube  — relacao N:N entre campeonatos e clubes
partidas          — jogos agendados com data, hora e resultado
grupos            — grupos de apostadores
grupo_participante — relacao N:N entre grupos e participantes
apostas           — previsoes dos participantes com pontuacao
```

Os dados persistem entre execucoes do programa.

---

## Solucao de problemas

**"Driver SQLite nao encontrado"**
```powershell
.\setup.ps1
```

**Erro de compilacao**
Verifique se o Java 11+ esta instalado:
```powershell
java -version
javac -version
```

**Banco corrompido ou dados inconsistentes**
Delete o arquivo `futebol_bet.db` — ele sera recriado vazio na proxima execucao.
O administrador padrao tambem sera recriado automaticamente.
