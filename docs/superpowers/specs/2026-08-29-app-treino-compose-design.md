# App de treino — front-end Android em Jetpack Compose

**Data:** 2026-08-29
**Status:** aprovado para planejamento
**Origem:** `prototipo-app-treino.html` (protótipo clicável em HTML/CSS/JS)

## 1. Objetivo

Portar as 6 telas do protótipo HTML para um app Android nativo em Kotlin +
Jetpack Compose, com fidelidade visual alta e os mesmos fluxos interativos.
Somente front-end: dados vêm de um repositório fake em memória.

## 2. Escopo

### Dentro

| Tela | Origem no HTML |
|---|---|
| Início | `#screen-home` |
| Meus treinos | `#screen-workouts` |
| Treino ativo + overlay de descanso | `#screen-active`, `.rest-overlay` |
| Progresso (lista) | `#screen-progress` → `#progressList` |
| Progresso (detalhe) | `#screen-progress` → `#progressDetail` |
| Novo treino | `#screen-create` |
| Perfil | `#screen-profile` |

Mais: barra de navegação inferior de 4 abas, tema dark completo com os tokens
do CSS, e as três fontes do protótipo empacotadas no APK.

### Fora

- **Cenografia do protótipo:** `.lab-header`, `.quick-nav`, `.device`,
  `.notch`, `.statusbar`. Eram moldura de navegador para simular um celular.
- **Tema claro.** Não existe no protótipo; inventar um é escopo não pedido.
- **Persistência, rede, autenticação, notificações.** O botão "Sair da conta"
  e os toggles de notificação são visuais e não disparam efeito real.
- **Testes automatizados.** Entrega é front-end visual; a verificação é o
  build e a inspeção nos previews do Android Studio.

## 3. Stack e decisões

| Decisão | Escolha | Porquê |
|---|---|---|
| UI | Jetpack Compose + Material3 | Mapeamento direto do HTML declarativo |
| Estado | ViewModel por tela, `StateFlow<UiState>` | Sobrevive a rotação; Composables ficam puros e previewáveis |
| Dados | `FakeRepository` singleton em memória | Mantém os fluxos vivos (salvar treino aparece na lista) sem banco |
| Navegação | Navigation Compose | Back button do Android de graça |
| Fontes | TTFs baixados para `res/font/` | Funciona offline e sem Google Play Services, ao contrário de downloadable fonts |
| Build | AGP 8.7, Kotlin 2.0 (+ plugin compose-compiler), compileSdk 35, minSdk 24 | Versões atuais e estáveis |
| Pacote | `com.example.gymmobile` | Placeholder; trocável numa linha |

## 4. Design tokens

Tradução literal do bloco `:root` do CSS. Nenhum valor é "adaptado" ao
Material 3 — as cores são as do protótipo.

### Cores (`ui/theme/Color.kt`)

| Token CSS | Kotlin | Valor |
|---|---|---|
| `--bg` | `Bg` | `#0E0F13` |
| `--surface` | `Surface` | `#1C1F26` |
| `--surface-2` | `Surface2` | `#242832` |
| `--raised` | `Raised` | `#2A2E38` (definido no CSS, sem uso — mantido por paridade) |
| `--border` | `BorderColor` | `#31353F` |
| `--text-primary` | `TextPrimary` | `#F2F1ED` |
| `--text-secondary` | `TextSecondary` | `#9A9CA8` |
| `--text-muted` | `TextMuted` | `#5F626D` |
| `--accent` | `Accent` | `#FF6A39` |
| `--accent-dim` | `AccentDim` | `Accent.copy(alpha = 0.14f)` |
| `--accent-text` | `AccentText` | `#FF8B5F` |
| `--success` | `Success` | `#7FB069` |
| `--success-dim` | `SuccessDim` | `Success.copy(alpha = 0.16f)` |
| `--chalk` | `Chalk` | `#E9DCC0` |
| (texto sobre `.btn-primary`) | `OnAccent` | `#1A0A04` |

Bordas do protótipo são `0.5px`; em Compose viram `0.5.dp`.

### Tipografia (`ui/theme/Type.kt`)

Três famílias em `res/font/`: **Oswald** (display, pesos 400/500/600/700),
**Inter** (body, 400/500/600), **JetBrains Mono** (mono, 400/500).

Estilos nomeados pela função no protótipo, não pelos nomes genéricos do
Material (`bodyLarge` etc.), para que o mapeamento com o CSS continue óbvio:

| Estilo | Uso no HTML | Spec |
|---|---|---|
| `display22` | `.greeting-name` | Oswald 500, 22sp |
| `display24` | `.stat-card .value` | Oswald 600, 24sp |
| `display19` | `.today-card h3`, `.topbar h2` | Oswald 500, 19sp |
| `display16` | `.template-card h4`, `.exercise-card h4` | Oswald 500, 16sp |
| `display44` | `.detail-hero .num` | Oswald 600, 44sp |
| `display40` | `.ring-time` | Oswald 600, 40sp |
| `sectionLabel` | `.section-label` | Mono 400, 12sp, uppercase, letterSpacing 0.04em |
| `mono12` | `.muted`, `.count`, `.pr` | Mono 400, 12sp |
| `mono13` | inputs de série, `.rest-controls` | Mono 400, 13sp |
| `body14` / `body13` / `body12` | textos corridos e legendas | Inter 400/500 |

`Theme.kt` expõe `GymTheme { }` — dark-only, sem `dynamicColor`.

## 5. Estrutura de arquivos

```
app/src/main/java/com/example/gymmobile/
  MainActivity.kt
  ui/theme/        Color.kt  Type.kt  Shape.kt  Theme.kt
  ui/components/   StatCard.kt  SectionLabel.kt  PrimaryButton.kt
                   GhostButton.kt  SquareIconButton.kt  HistoryRow.kt
                   SettingsRow.kt  ToggleSwitch.kt  SelectableChip.kt
  navigation/      Routes.kt  GymNavHost.kt  GymBottomBar.kt
  data/            Models.kt  FakeRepository.kt
  feature/home/      HomeScreen.kt      HomeViewModel.kt
  feature/workouts/  WorkoutsScreen.kt  WorkoutsViewModel.kt
  feature/active/    ActiveWorkoutScreen.kt  ActiveWorkoutViewModel.kt
                     RestOverlay.kt
  feature/progress/  ProgressScreen.kt  ProgressDetailScreen.kt
                     ProgressViewModel.kt  ProgressDetailViewModel.kt
                     Sparkline.kt  BarChart.kt
  feature/create/    CreateWorkoutScreen.kt  CreateWorkoutViewModel.kt
  feature/profile/   ProfileScreen.kt   ProfileViewModel.kt
app/src/main/res/font/   oswald_*.ttf  inter_*.ttf  jetbrains_mono_*.ttf
```

Cada arquivo de tela contém o Composable da tela e seus filhos privados.
Componentes vão para `ui/components/` somente quando usados por 2+ telas.

## 6. Modelos e dados iniciais

`data/Models.kt`:

```kotlin
data class WorkoutTemplate(val id: String, val name: String, val tag: String, val exerciseCount: Int)
data class ExerciseSet(val weight: Double, val reps: Int, val done: Boolean = false)
data class ActiveExercise(val name: String, val sets: List<ExerciseSet>)
data class HistoryEntry(val date: String, val value: String)
data class ProgressExercise(val id: String, val name: String, val pr: String,
                            val bars: List<Int>, val history: List<HistoryEntry>)
data class WorkoutSummary(val name: String, val day: String, val duration: String)
```

`FakeRepository` (objeto singleton, estado mutável em memória) inicia com
exatamente os dados do protótipo:

- **Templates:** Push A / `Peito · Ombro · Tríceps` / 5 · Pull B /
  `Costas · Bíceps` / 6 · Legs / `Pernas · Glúteos` / 6 · Full body /
  `Corpo inteiro` / 8
- **Exercícios do treino ativo:** Supino reto `[60kg×10 ✓, 60kg×8]` ·
  Desenvolvimento halteres `[18kg×12, 18kg×10]` · Elevação lateral `[8kg×15]`
- **Progresso:** Supino reto PR 80kg bars `[40,55,60,80,95,100]` ·
  Agachamento livre PR 110kg bars `[50,60,70,85,90,100]` ·
  Levantamento terra PR 140kg bars `[60,65,75,80,92,100]`, cada um com o
  histórico do protótipo
- **Catálogo para o criador:** os 12 nomes de `exerciseOptions`
- **Últimos treinos:** Pull B `seg · 48min` · Legs `sáb · 55min` ·
  Push A `qui · 51min`

`FakeRepository` expõe os templates como `MutableStateFlow<List<WorkoutTemplate>>`
para que um treino salvo apareça imediatamente na lista.

Os exercícios do treino ativo são uma lista única, devolvida para qualquer
`templateId` — o protótipo também não tinha exercícios por template. O que
varia por template é o nome exibido no rótulo da tela.

## 7. Navegação

`Routes.kt`:

```
"home"  "workouts"  "progress"  "profile"      → abas
"progress/{exerciseId}"                        → detalhe
"active/{templateId}"  "create"                → full-screen
```

`MainActivity` monta um `Scaffold`; a `GymBottomBar` é renderizada apenas
quando a rota atual é uma das 4 abas — equivalente ao `.bottom-nav.hidden`
do JS. Aba ativa recebe `AccentText`; as demais, `TextMuted`.

Divergência deliberada do protótipo: o detalhe de Progresso é uma **rota
própria**, não uma troca de `innerHTML` na mesma tela. Isso faz o botão
voltar do Android funcionar sem código extra.

`active/{templateId}` é alcançável por dois caminhos, como no HTML: o botão
"Iniciar treino" da Home e o botão play de cada card em Meus treinos.

## 8. Telas

### 8.1 Início

`Bom treino,` (12sp, `TextMuted`) sobre `Lucas` (`display22`). Grade de dois
`StatCard`: **Sequência** `4 dias`, **Volume da semana** `12,4 t` — o número
em `display24`, a unidade em Inter 13sp `TextSecondary`.

Rótulo `Treino de hoje` e o card em destaque: fundo com gradiente linear a
155° de `AccentDim` até transparente em 60%, sobre `Surface`, borda 0.5dp,
canto 16dp. Dentro: `Push · dia 3` em mono `AccentText`, `Peito, ombro e
tríceps` em `display19`, `5 exercícios · ~50 min`, e o botão primário
`Iniciar treino` com ícone de play.

Rótulo `Últimos treinos` e três `HistoryRow` (nome à esquerda, `seg · 48min`
em mono `TextMuted` à direita, divisor 0.5dp entre linhas, nenhum no último).

### 8.2 Meus treinos

Título `Meus treinos`. Um card por template: nome em `display16`, tag em
13sp `TextSecondary`, `N exercícios` em mono 11sp `TextMuted`. À direita,
dois botões quadrados de 32dp — lápis (abre `create`) e play em `Accent`
(abre `active/{id}`). Abaixo da lista, botão ghost `Novo treino` com ícone +.

### 8.3 Treino ativo

Barra superior de três elementos: botão voltar, cronômetro da sessão em mono
`AccentText`, botão fechar. A seta volta na pilha de navegação (para Início ou
Meus treinos, conforme a origem); o X encerra e vai para `home`. No protótipo
os dois faziam a mesma coisa; separá-los é o comportamento que um usuário de
Android espera de uma seta.

Abaixo, o rótulo de contexto. Sendo um mock, o repositório devolve a **mesma
lista de três exercícios** para qualquer `templateId` — mas o rótulo é
derivado, não escrito à mão: `{nome do template} · exercício {k} de {total}`,
onde `total` é o tamanho real da lista e `k` é o índice (1-based) do primeiro
exercício com série pendente, limitado a `total`. Assim o contador avança
conforme as séries são marcadas, em vez de exibir um `2 de 5` que não
corresponde à tela.

Um card por exercício: nome em `display16` e a tabela de séries em grade de
4 colunas `26dp | 1fr | 1fr | 32dp`, cabeçalho `Série · Kg · Reps · —` em
mono 10sp uppercase. Cada linha traz o índice, dois campos numéricos
(`Surface2`, borda 0.5dp, foco muda a borda para `Accent`, texto mono
centralizado) e um botão de check 32dp. Check marcado usa fundo `SuccessDim`
e borda + ícone `Success`.

`+ Adicionar série` copia peso e reps da última série, com `done = false` —
igual ao `addSet` do JS.

**Marcar** uma série (não-feita → feita) dispara o descanso de 60s.
**Desmarcar** apenas limpa o estado, sem timer.

Ao fim, botão primário `Finalizar treino` → volta para `home`.

### 8.4 Overlay de descanso

Sobreposição de tela cheia com fundo `#0A0B0E` a 92% de opacidade,
consumindo toques (a tela de baixo não recebe cliques).

Rótulo `DESCANSO` em mono uppercase; anel de 180dp desenhado com `Canvas` +
`drawArc` (stroke 10dp, `StrokeCap.Round`, começando às 12 horas), trilha em
`#31353F` e progresso em `Accent`. É o equivalente ao
`stroke-dasharray/dashoffset` do SVG. No centro, os segundos restantes em
`display40`.

Controles: `-15s` e `+15s` (piso de 5s; `+15s` também estende o total, para
que o anel nunca ultrapasse a volta completa) e o link `Pular descanso`.
O timer chegando a zero fecha o overlay sozinho.

### 8.5 Progresso — lista

Título `Progresso`. Uma linha por exercício: sparkline à esquerda, nome ao
centro, PR em mono `TextSecondary` à direita, divisor entre linhas.

A sparkline são barras de 4dp de largura e 2dp de espaçamento, altura
proporcional ao valor, cantos superiores arredondados, em `Chalk` a 50% de
opacidade — exceto a última, que é `Accent` opaca. A linha inteira é
clicável e navega para o detalhe.

### 8.6 Progresso — detalhe

Barra superior com botão voltar e o nome do exercício. Herói: o PR em
`display44` na cor `Chalk`, com `recorde pessoal` ao lado, alinhado pela
base.

Gráfico de barras de 130dp de altura: uma coluna por valor, cantos
superiores arredondados, `Surface2` — exceto a última barra, em `Accent`.
Sob cada coluna, o índice 1..N em mono 10sp.

Dois `StatCard`: **Sessões** `18` e **Última carga**, esta derivada do
histórico (a carga da entrada mais recente — `80kg` de `80kg × 8`). Abaixo,
rótulo `Histórico` e as entradas em `HistoryRow`.

### 8.7 Novo treino

Barra superior com voltar e `Novo treino`. Campo de texto `Nome do treino`
com placeholder `Ex: Push A, Upper, Dia de perna` (44dp, `Surface`, borda
0.5dp, foco em `Accent`).

`Adicionar exercícios`: os 12 nomes do catálogo como chips. Chip selecionado
usa fundo `AccentDim`, borda `Accent`, texto `AccentText`.

`Selecionados`: cada escolha vira uma linha com botão `remover`. Lista vazia
mostra `Nenhum exercício selecionado ainda.` em `TextMuted`.

`Salvar treino` acrescenta o template ao repositório e navega para
`workouts`. Regras vindas do JS: nome vazio vira `Treino sem nome`; a tag
são os três primeiros exercícios unidos por ` · `, ou `Sem exercícios` se
nada foi escolhido. Abrir a tela sempre reinicia nome e seleção.

### 8.8 Perfil

Título `Perfil`. Cabeçalho com avatar circular de 56dp (`AccentDim` de
fundo, iniciais `LC` em `AccentText`), `Lucas Costa` e `Treinando há
8 meses`.

Quatro linhas de configuração, divisor 0.5dp entre elas:

1. **Unidade de peso** — seletor segmentado `kg` / `lb`, `kg` ativo
2. **Notificações de descanso** — subtítulo `Avisar quando o tempo de
   descanso acabar`, switch ligado
3. **Lembrete de treino** — subtítulo `Notificação nos dias programados`,
   switch desligado
4. **Backup automático** — switch ligado

Switch: 42×24dp, botão de 18dp que desliza (animado); ligado usa `AccentDim`
com borda e botão em `Accent`. Todos os controles alteram apenas o estado do
`ProfileViewModel` — nenhum efeito no resto do app.

Ao final, botão ghost `Sair da conta`, sem ação.

## 9. Timers

Ambos vivem no `ActiveWorkoutViewModel`, em corrotinas de `viewModelScope`
que emitem a cada segundo, e portanto sobrevivem à rotação da tela.

- **Cronômetro da sessão:** começa em 0 ao abrir a tela, formatado
  `HH:MM:SS`. (O protótipo exibia `00:04:12` fixo; aqui ele corre de verdade,
  porque um cronômetro parado numa tela de treino ativo é um bug visível.)
- **Descanso:** contagem regressiva de 60s, controlada pelos botões do
  overlay, cancelada ao pular ou ao sair da tela.

## 10. Entrega e verificação

Entregável: projeto Gradle completo, abrível no Android Studio, com previews
`@Preview` em cada tela.

**Limite conhecido e declarado:** esta máquina não tem Android SDK nem
Gradle, por decisão de escopo. Portanto o código é entregue **sem ter sido
compilado**. Nenhuma afirmação de "funcionando" será feita antes de o
primeiro build rodar no Android Studio; os erros que ele apontar serão
corrigidos em seguida.

## 11. Riscos

| Risco | Mitigação |
|---|---|
| Código não compila de primeira (sem build local) | Versões fixadas e conservadoras; correção na primeira rodada de feedback |
| TTFs do Google Fonts indisponíveis no download | Rede verificada, retorno 200/302; se falhar, fallback declarado para as fontes do sistema |
| Deriva visual entre CSS e Compose | Tokens traduzidos em tabela literal (seção 4), não por aproximação |
