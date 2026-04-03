# Futebol Bet - Sistema de Gerenciamento de Apostas

## 📋 Descrição
Aplicação Java para gerenciar apostas de participantes em partidas de campeonatos de futebol. O sistema permite que usuários criem grupos, registrem apostas e recebam pontuação automática com base na precisão de suas previsões.

---

## ✅ Funcionalidades Implementadas (Etapa 1)

### 1. **Modelos de Dados (Models)**
- ✅ `Usuario` - Classe abstrata base para todos os usuários
- ✅ `Administrador` - Herda de Usuario, gerencia o sistema
- ✅ `Participante` - Herda de Usuario, registra apostas e acumula pontos
- ✅ `Clube` - Representa um clube de futebol
- ✅ `Campeonato` - Conjunto de clubes (máx. 8 clubes)
- ✅ `Partida` - Representa uma partida entre dois clubes
- ✅ `Resultado` - Armazena gols marcados por cada time
- ✅ `Aposta` - Nova classe para registrar previsões dos participantes
- ✅ `Grupo` - Nova classe para agrupar participantes (máx. 5 participantes)

### 2. **Repositories em Memória**
O padrão Repository foi implementado com Singleton para armazenar dados em memória:

- ✅ `UsuarioRepository` - Gerencia usuários do sistema
- ✅ `CampeonatoRepository` - Gerencia campeonatos (validando máx. 8 clubes)
- ✅ `GrupoRepository` - Gerencia grupos de apostadores
- ✅ `ApostaRepository` - Gerencia apostas registradas
- ✅ `PartidaRepository` - Gerencia partidas do campeonato

Cada repositório oferece operações CRUD básicas: adicionar, obter, remover e limpar.

### 3. **Interfaces Gráficas (UI) com Swing**

#### 3.1 LoginUI
- ✅ Autenticação de usuários
- Interface simples com campos de email e senha

#### 3.2 ApostasUI (Nova)
- Permite que participantes registrem apostas em partidas
- Campos obrigatórios:
  - Seleção da partida
  - Resultado esperado (Casa, Empate, Fora)
  - Gols esperados para cada time
- Validação: Apostas só podem ser feitas até 20 minutos antes da partida
- Interface com GridBagLayout para melhor organização

#### 3.3 ClassificacaoUI (Nova)
- Exibe ranking dos participantes dentro do grupo
- Mostra posição, nome e pontos acumulados
- Fácil visualização em formato tabular

#### 3.4 AdminResultadosUI (Nova)
- Exclusiva para administradores
- Registra resultado real das partidas
- Calcula automaticamente pontos de todas as apostas dessa partida
- Critérios de pontuação:
  - 10 pontos: Placar exato
  - 5 pontos: Resultado correto (vencedor ou empate)
  - 0 pontos: Erro

---

## 🎯 Regras de Negócio Implementadas

✅   **Cadastro de Campeonato:** Máximo 8 clubes por campeonato
✅ **Grupos de Apostadores:** Máximo 5 participantes por grupo
✅ **Limite de Apostas:** Apostas permitidas até 20 minutos antes da partida
✅ **Cálculo de Pontos:**
   - Acertar apenas o resultado: 5 pontos
   - Acertar resultado e placar exato: 10 pontos
   - Erro: 0 pontos

---

## 📂 Estrutura do Projeto

```
src/br/com/futebolbet/
├── models/
│   ├── Usuario.java
│   ├── Administrador.java
│   ├── Participante.java
│   ├── Clube.java
│   ├── Campeonato.java
│   ├── Partida.java
│   ├── Resultado.java
│   ├── Aposta.java (NOVO)
│   └── Grupo.java (NOVO)
├── repository/
│   ├── UsuarioRepository.java (NOVO)
│   ├── CampeonatoRepository.java (NOVO)
│   ├── GrupoRepository.java (NOVO)
│   ├── ApostaRepository.java (NOVO)
│   └── PartidaRepository.java (NOVO)
├── service/
│   └── AuthService.java
├── ui/
│   ├── LoginUI.java
│   ├── ApostasUI.java (NOVO)
│   ├── ClassificacaoUI.java (NOVO)
│   └── AdminResultadosUI.java (NOVO)
└── Main.java
```

---

## 🔄 Próximas Etapas (Services)

As seguintes funcionalidades serão implementadas na próxima etapa:

- [ ] `CampeonatoService` - Gerenciar campeonatos e clubes
- [ ] `GrupoService` - Criar e gerenciar grupos de apostadores
- [ ] `ApostaService` - Validar e registrar apostas
- [ ] `PartidaService` - Gerenciar partidas e cronograma
- [ ] `PontuacaoService` - Calcular e atualizar pontos
- [ ] Integração das UIs com os Services
- [ ] Menu principal para navegação

---

## 📝 Notas de Desenvolvimento

### Padrões Utilizados
- **Singleton:** Repositórios garantem única instância em memória
- **Herança:** Usuario é superclasse para Administrador e Participante
- **MVC Parcial:** Separação entre Models, Views (UI) e Data Access (Repository)

### Limitações Atuais
⚠️ Dados armazenados apenas em memória (perdidos ao encerrar a aplicação)
⚠️ Sem persistência em banco de dados (próxima fase)
⚠️ UI ainda não integrada com Services

---

## 🚀 Como Compilar e Executar

```bash
# Compilar
javac -d bin/ src/br/com/futebolbet/**/*.java

# Executar
java -cp bin/ br.com.futebolbet.Main
```

---

## 👥 Equipe de Desenvolvimento

**Etapa 1 (Repositories e UI):** Implementada
**Etapa 2 (Services):** A fazer
**Etapa 3 (Finalização):** A fazer
