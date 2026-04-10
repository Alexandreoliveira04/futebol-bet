# Futebol Bet - Sistema de Gerenciamento de Apostas

## Atualizacoes Recentes (Abr/2026)

- Participante agora pode criar grupos na propria aba `Grupos` (alem de ingressar em grupos existentes).
- Ajustes de textos da UI para evitar caracteres corrompidos (acentuacao).
- Padronizacao dos arquivos Java para UTF-8 sem BOM.

### Regra de Negocio Atualizada

- Criacao de grupos: permitida para **Administrador** e **Participante**.
- Limite de grupos no sistema permanece em **5**.
- Limite de participantes por grupo permanece em **5**.

### Compilacao Recomendada (Windows/PowerShell)

```powershell
$files = Get-ChildItem -Recurse -Filter *.java .\src | ForEach-Object FullName
javac -encoding UTF-8 -d .\bin $files
java -cp .\bin br.com.futebolbet.Main
```
## ðŸ“‹ DescriÃ§Ã£o
AplicaÃ§Ã£o Java para gerenciar apostas de participantes em partidas de campeonatos de futebol. O sistema permite que usuÃ¡rios criem grupos, registrem apostas e recebam pontuaÃ§Ã£o automÃ¡tica com base na precisÃ£o de suas previsÃµes.

---

## âœ… Funcionalidades Implementadas (Etapa 1)

### 1. **Modelos de Dados (Models)**
- âœ… `Usuario` - Classe abstrata base para todos os usuÃ¡rios
- âœ… `Administrador` - Herda de Usuario, gerencia o sistema
- âœ… `Participante` - Herda de Usuario, registra apostas e acumula pontos
- âœ… `Clube` - Representa um clube de futebol
- âœ… `Campeonato` - Conjunto de clubes (mÃ¡x. 8 clubes)
- âœ… `Partida` - Representa uma partida entre dois clubes
- âœ… `Resultado` - Armazena gols marcados por cada time
- âœ… `Aposta` - Nova classe para registrar previsÃµes dos participantes
- âœ… `Grupo` - Nova classe para agrupar participantes (mÃ¡x. 5 participantes)

### 2. **Repositories em MemÃ³ria**
O padrÃ£o Repository foi implementado com Singleton para armazenar dados em memÃ³ria:

- âœ… `UsuarioRepository` - Gerencia usuÃ¡rios do sistema
- âœ… `CampeonatoRepository` - Gerencia campeonatos (validando mÃ¡x. 8 clubes)
- âœ… `GrupoRepository` - Gerencia grupos de apostadores
- âœ… `ApostaRepository` - Gerencia apostas registradas
- âœ… `PartidaRepository` - Gerencia partidas do campeonato

Cada repositÃ³rio oferece operaÃ§Ãµes CRUD bÃ¡sicas: adicionar, obter, remover e limpar.

### 3. **Interfaces GrÃ¡ficas (UI) com Swing**

#### 3.1 LoginUI
- âœ… AutenticaÃ§Ã£o de usuÃ¡rios
- Interface simples com campos de email e senha

#### 3.2 ApostasUI (Nova)
- Permite que participantes registrem apostas em partidas
- Campos obrigatÃ³rios:
  - SeleÃ§Ã£o da partida
  - Resultado esperado (Casa, Empate, Fora)
  - Gols esperados para cada time
- ValidaÃ§Ã£o: Apostas sÃ³ podem ser feitas atÃ© 20 minutos antes da partida
- Interface com GridBagLayout para melhor organizaÃ§Ã£o

#### 3.3 ClassificacaoUI (Nova)
- Exibe ranking dos participantes dentro do grupo
- Mostra posiÃ§Ã£o, nome e pontos acumulados
- FÃ¡cil visualizaÃ§Ã£o em formato tabular

#### 3.4 AdminResultadosUI (Nova)
- Exclusiva para administradores
- Registra resultado real das partidas
- Calcula automaticamente pontos de todas as apostas dessa partida
- CritÃ©rios de pontuaÃ§Ã£o:
  - 10 pontos: Placar exato
  - 5 pontos: Resultado correto (vencedor ou empate)
  - 0 pontos: Erro

---

## ðŸŽ¯ Regras de NegÃ³cio Implementadas

âœ…   **Cadastro de Campeonato:** MÃ¡ximo 8 clubes por campeonato
âœ… **Grupos de Apostadores:** MÃ¡ximo 5 participantes por grupo
âœ… **Limite de Apostas:** Apostas permitidas atÃ© 20 minutos antes da partida
âœ… **CÃ¡lculo de Pontos:**
   - Acertar apenas o resultado: 5 pontos
   - Acertar resultado e placar exato: 10 pontos
   - Erro: 0 pontos

---

## ðŸ“‚ Estrutura do Projeto

```
src/br/com/futebolbet/
â”œâ”€â”€ models/
â”‚   â”œâ”€â”€ Usuario.java
â”‚   â”œâ”€â”€ Administrador.java
â”‚   â”œâ”€â”€ Participante.java
â”‚   â”œâ”€â”€ Clube.java
â”‚   â”œâ”€â”€ Campeonato.java
â”‚   â”œâ”€â”€ Partida.java
â”‚   â”œâ”€â”€ Resultado.java
â”‚   â”œâ”€â”€ Aposta.java (NOVO)
â”‚   â””â”€â”€ Grupo.java (NOVO)
â”œâ”€â”€ repository/
â”‚   â”œâ”€â”€ UsuarioRepository.java (NOVO)
â”‚   â”œâ”€â”€ CampeonatoRepository.java (NOVO)
â”‚   â”œâ”€â”€ GrupoRepository.java (NOVO)
â”‚   â”œâ”€â”€ ApostaRepository.java (NOVO)
â”‚   â””â”€â”€ PartidaRepository.java (NOVO)
â”œâ”€â”€ service/
â”‚   â””â”€â”€ AuthService.java
â”œâ”€â”€ ui/
â”‚   â”œâ”€â”€ LoginUI.java
â”‚   â”œâ”€â”€ ApostasUI.java (NOVO)
â”‚   â”œâ”€â”€ ClassificacaoUI.java (NOVO)
â”‚   â””â”€â”€ AdminResultadosUI.java (NOVO)
â””â”€â”€ Main.java
```

---

## ðŸ”„ PrÃ³ximas Etapas (Services)

As seguintes funcionalidades serÃ£o implementadas na prÃ³xima etapa:

- [ ] `CampeonatoService` - Gerenciar campeonatos e clubes
- [ ] `GrupoService` - Criar e gerenciar grupos de apostadores
- [ ] `ApostaService` - Validar e registrar apostas
- [ ] `PartidaService` - Gerenciar partidas e cronograma
- [ ] `PontuacaoService` - Calcular e atualizar pontos
- [ ] IntegraÃ§Ã£o das UIs com os Services
- [ ] Menu principal para navegaÃ§Ã£o

---

## ðŸ“ Notas de Desenvolvimento

### PadrÃµes Utilizados
- **Singleton:** RepositÃ³rios garantem Ãºnica instÃ¢ncia em memÃ³ria
- **HeranÃ§a:** Usuario Ã© superclasse para Administrador e Participante
- **MVC Parcial:** SeparaÃ§Ã£o entre Models, Views (UI) e Data Access (Repository)

### LimitaÃ§Ãµes Atuais
âš ï¸ Dados armazenados apenas em memÃ³ria (perdidos ao encerrar a aplicaÃ§Ã£o)
âš ï¸ Sem persistÃªncia em banco de dados (prÃ³xima fase)
âš ï¸ UI ainda nÃ£o integrada com Services

---

## ðŸš€ Como Compilar e Executar

```bash
# Compilar
javac -d bin/ src/br/com/futebolbet/**/*.java

# Executar
java -cp bin/ br.com.futebolbet.Main
```

---

## ðŸ‘¥ Equipe de Desenvolvimento

**Etapa 1 (Repositories e UI):** Implementada
**Etapa 2 (Services):** A fazer
**Etapa 3 (FinalizaÃ§Ã£o):** A fazer
