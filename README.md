# Futebol Bet

Aplicacao Java desktop para gerenciar apostas em partidas de campeonatos de futebol. Usuarios se organizam em grupos, fazem previsoes de resultados e acumulam pontos com base na precisao das apostas.

---

## Arquitetura

O projeto segue uma arquitetura em camadas inspirada em MVC, com separacao clara entre modelos, acesso a dados, logica de negocio e interface grafica.

```
Main
 └── inicia o repositorio de usuarios e abre a tela de login

models      → entidades do dominio (Usuario, Partida, Aposta...)
repository  → acesso a dados em memoria (padrao Singleton)
service     → logica de negocio (autenticacao, apostas, campeonatos)
ui          → telas Swing
ui/theme    → estilos e cores reutilizaveis (UiTheme)
enums       → tipos enumerados (TipoResultado, DashboardAba)
```

**Padroes utilizados:**
- **Singleton** — cada repositorio tem uma unica instancia compartilhada
- **Heranca** — `Administrador` e `Participante` estendem `Usuario`
- **Camadas** — models / repository / service / ui sem acoplamento cruzado

---

## Estrutura de diretorios

```
futebol-bet/
└── src/br/com/futebolbet/
    ├── Main.java
    ├── enums/
    │   ├── DashboardAba.java
    │   └── TipoResultado.java
    ├── models/
    │   ├── Usuario.java          (classe abstrata base)
    │   ├── Administrador.java
    │   ├── Participante.java
    │   ├── Clube.java
    │   ├── Campeonato.java       (max. 8 clubes)
    │   ├── Partida.java
    │   ├── Resultado.java
    │   ├── Aposta.java
    │   └── Grupo.java            (max. 5 participantes)
    ├── repository/
    │   ├── UsuarioRepository.java
    │   ├── CampeonatoRepository.java
    │   ├── ClubeRepository.java
    │   ├── GrupoRepository.java
    │   ├── ApostaRepository.java
    │   └── PartidaRepository.java
    ├── service/
    │   ├── AuthService.java
    │   ├── ApostaService.java
    │   ├── CampeonatoService.java
    │   └── GrupoService.java
    └── ui/
        ├── theme/
        │   └── UiTheme.java      (paleta de cores e estilos globais)
        ├── AtualizavelInterface.java
        ├── LoginUi.java
        ├── CadastroUsuarioUI.java
        ├── MenuPrincipalUI.java
        ├── ApostasUI.java
        ├── ClassificacaoUI.java
        ├── ParticipantePartidasUI.java
        ├── ParticipanteGruposUI.java
        ├── AdminCampeonatoUI.java
        ├── AdminClubeUI.java
        ├── AdminGruposUI.java
        ├── AdminPartidasUI.java
        └── AdminResultadosUI.java
```

---

## Regras de negocio

| Regra                                 | Valor      |
|---------------------------------------|------------|
| Max. clubes por campeonato            | 8          |
| Max. participantes por grupo          | 5          |
| Max. grupos no sistema                | 5          |
| Prazo para apostar antes da partida   | 20 minutos |
| Acertar o resultado (vencedor/empate) | 5 pontos   |
| Acertar o placar exato                | 10 pontos  |
| Errar                                 | 0 pontos   |

Tanto o **Administrador** quanto o **Participante** podem criar grupos.

---

## Como executar

**Pre-requisito:** Java 11 ou superior instalado.

### Windows (PowerShell)

```powershell
$files = Get-ChildItem -Recurse -Filter *.java .\src | ForEach-Object FullName
javac -encoding UTF-8 -d .\out $files
java -cp .\out br.com.futebolbet.Main
```

### Linux / macOS

```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out br.com.futebolbet.Main
```

**Login padrao (administrador):**
- Email: `admin@futebolbet.com`
- Senha: `admin123`

---

## Limitacoes atuais

- Dados armazenados apenas em memoria — perdidos ao encerrar a aplicacao.
- Sem persistencia em banco de dados.