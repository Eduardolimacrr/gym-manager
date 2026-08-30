# Gym — app de treino

App Android nativo para acompanhar treinos de academia: montar fichas, executar
a sessão marcando séries com timer de descanso, e acompanhar a evolução de carga
por exercício.

**Estado atual: apenas front-end.** Todas as telas estão implementadas e
navegáveis, mas os dados vêm de um repositório fake em memória — não há API,
banco nem autenticação. Fechar o app descarta tudo.

As telas são a portabilidade de um protótipo clicável em HTML/CSS/JS
(`prototipo-app-treino.html`), com fidelidade visual alta: as cores, fontes e
medidas são traduções literais do CSS original, não aproximações do Material 3.

## Stack

| | |
|---|---|
| Linguagem | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material3 (Compose BOM 2024.10.01) |
| Navegação | Navigation Compose 2.8.4 |
| Estado | ViewModel por tela, `StateFlow<UiState>` |
| Build | AGP 8.7.2, Gradle 8.9 |
| SDK | `compileSdk` 35 · `targetSdk` 35 · `minSdk` 24 |
| Java | 17 |

Tema **dark-only**, sem `dynamicColor` — o protótipo não tem variante clara.

## Como rodar

Requer Android Studio (Ladybug ou mais recente) e JDK 17.

1. **Open** no Android Studio, apontando para a raiz do projeto (a pasta com
   `settings.gradle.kts`, não `app/`).
2. Deixe o Gradle Sync terminar. Na primeira vez ele baixa o Android SDK.
3. Crie um emulador em **Tools → Device Manager → + → Create Virtual Device**
   (Pixel 7, imagem de sistema API 35), ou conecte um aparelho com Depuração USB
   ativada.
4. Rode com **▶** (`Shift+F10`).

Pela linha de comando:

```bash
./gradlew installDebug   # compila e instala no dispositivo conectado
./gradlew assembleDebug  # só gera o APK
```

Cada tela tem um `@Preview`, então dá para inspecionar o visual no painel
**Split/Design** do editor sem emulador nenhum.

## Telas

| Tela | Arquivo | O que faz |
|---|---|---|
| Início | `feature/home/` | Saudação, sequência e volume da semana, treino do dia, últimos treinos |
| Meus treinos | `feature/workouts/` | Lista de fichas com atalhos de editar e iniciar |
| Treino ativo | `feature/active/` | Séries editáveis (kg/reps), marcar concluída, cronômetro da sessão |
| Descanso | `feature/active/RestOverlay.kt` | Sobreposição com anel de progresso, ±15s, pular |
| Progresso | `feature/progress/ProgressScreen.kt` | Exercícios acompanhados, com sparkline e recorde |
| Detalhe do exercício | `feature/progress/ProgressDetailScreen.kt` | PR, gráfico de barras, histórico de cargas |
| Novo treino | `feature/create/` | Nome, catálogo de exercícios em chips, selecionados |
| Perfil | `feature/profile/` | Unidade de peso, notificações, backup |

A barra inferior de 4 abas some em *Treino ativo* e *Novo treino*, que são
telas de tela cheia.

## Estrutura

```
app/src/main/
├── res/font/                 9 TTFs estáticos (Oswald, Inter, JetBrains Mono)
├── res/values/               strings.xml, themes.xml
└── java/com/example/gymmobile/
    ├── MainActivity.kt       hospeda GymTheme + GymApp
    ├── ui/theme/             Color · Type · Shape · Theme
    ├── ui/components/        componentes usados por 2+ telas
    ├── data/                 Models.kt, FakeRepository.kt
    ├── navigation/           Routes, GymNavHost, GymBottomBar
    └── feature/<tela>/       Screen.kt + ViewModel.kt por tela
```

Regra de organização: um componente só sobe para `ui/components/` quando é usado
por duas ou mais telas. Componente de uma tela só mora no pacote dela.

## Design tokens

O bloco `:root` do CSS do protótipo virou `ui/theme/Color.kt`, com os mesmos 15
valores. Os estilos de texto em `ui/theme/Type.kt` são nomeados pelo papel que
cumprem (`display22`, `sectionLabel`, `mono12`) em vez da escala genérica do
Material (`bodyLarge`), para que a correspondência com o CSS continue rastreável.

Convenções obrigatórias ao mexer na UI:

- Cores só via `GymColors` — nada de `Color(0xFF...)` fora de `Color.kt`
- Tipografia só via `GymType` — nada de `MaterialTheme.typography.*`
- Bordas de `0.5.dp` (o protótipo usa `0.5px`)
- Separador entre termos é `·` (U+00B7), nunca hífen

As fontes são TTFs estáticos empacotados no APK, um por peso. O repositório
`google/fonts` publica apenas fontes variáveis, que em `minSdk 24` não aplicam o
eixo de peso — Oswald 400 e 700 sairiam idênticas.

## Fora de escopo por enquanto

- Persistência (Room), API e autenticação
- Testes automatizados
- Tema claro
- Notificações reais — os interruptores do Perfil e o "Sair da conta" mudam
  apenas o estado da tela
- Exercícios por ficha: qualquer treino aberto mostra a mesma lista de três
  exercícios; o que muda é o nome no cabeçalho

## Documentação

- `docs/superpowers/specs/` — design e decisões, com o mapeamento CSS → Compose
- `docs/superpowers/plans/` — plano de implementação tarefa a tarefa
