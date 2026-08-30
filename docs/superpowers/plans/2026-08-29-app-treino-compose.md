# App de Treino (Compose) — Plano de Implementação

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Construir o front-end Android das 7 telas do protótipo HTML em Kotlin + Jetpack Compose, com dados mock em memória.

**Architecture:** Módulo único `app`. Cada tela é um pacote em `feature/` com um `@Composable` sem lógica e um `ViewModel` expondo `StateFlow<UiState>`. Um `FakeRepository` singleton guarda todos os dados em memória. Navegação por Navigation Compose, com a barra inferior visível apenas nas 4 rotas-aba.

**Tech Stack:** Kotlin 2.0.21, AGP 8.7.2, Jetpack Compose (BOM 2024.10.01), Material3, Navigation Compose 2.8.4, lifecycle-viewmodel-compose 2.8.7.

**Spec:** `docs/superpowers/specs/2026-08-29-app-treino-compose-design.md`

## Global Constraints

- **Pacote:** `com.example.gymmobile`. **Namespace/applicationId:** idem.
- **SDK:** `compileSdk = 35`, `targetSdk = 35`, `minSdk = 24`.
- **Java/Kotlin target:** 17.
- **Tema:** dark-only. Proibido `dynamicColor`, `isSystemInDarkTheme()` ou qualquer variante clara.
- **Cores:** exclusivamente via `GymColors`. Nenhum literal `Color(0x...)` fora de `ui/theme/Color.kt`.
- **Tipografia:** exclusivamente via `GymType`. Proibido `MaterialTheme.typography.*` nas telas.
- **Bordas:** `0.5.dp` (o protótipo usa `0.5px`). **Raios:** conforme cada tela.
- **Textos em pt-BR**, copiados literalmente do protótipo, com acentuação correta (`exercícios`, `Sequência`, `Histórico`, `sáb`, `há 8 meses`). O separador entre termos é `·` (U+00B7), nunca `-`.
- **Sem verificação local:** esta máquina não tem Android SDK nem Gradle. Nenhuma tarefa roda build aqui. Cada tarefa termina com uma verificação a ser feita no Android Studio, e nenhuma alegação de "funciona" é feita antes disso.
- **Sem testes automatizados** (fora de escopo pela spec, seção 2). A verificação é build + inspeção de `@Preview`.

## Estrutura de arquivos

| Arquivo | Responsabilidade | Tarefa |
|---|---|---|
| `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties` | Build e catálogo de versões | 1 |
| `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml` | Config do módulo, activity | 1 |
| `app/src/main/java/.../MainActivity.kt` | Entrada; hospeda `GymTheme` + `GymApp` | 1, 5 |
| `app/src/main/res/font/*.ttf` | 9 TTFs estáticos | 2 |
| `ui/theme/Color.kt` `Type.kt` `Shape.kt` `Theme.kt` | Tokens do CSS | 2 |
| `data/Models.kt` `data/FakeRepository.kt` | Modelos e dados do protótipo | 3 |
| `ui/components/*.kt` | 9 componentes reutilizados | 4 |
| `navigation/Routes.kt` `GymBottomBar.kt` `GymNavHost.kt` | Rotas e chrome | 5 |
| `feature/home/*` | Tela Início | 6 |
| `feature/workouts/*` | Tela Meus treinos | 7 |
| `feature/create/*` | Tela Novo treino | 8 |
| `feature/progress/ProgressScreen.kt` `Sparkline.kt` `ProgressViewModel.kt` | Progresso — lista | 9 |
| `feature/progress/ProgressDetailScreen.kt` `BarChart.kt` `ProgressDetailViewModel.kt` | Progresso — detalhe | 10 |
| `feature/active/ActiveWorkoutScreen.kt` `ActiveWorkoutViewModel.kt` | Treino ativo | 11 |
| `feature/active/RestOverlay.kt` | Overlay de descanso | 12 |
| `feature/profile/*` | Tela Perfil | 13 |

Raiz dos fontes: `app/src/main/java/com/example/gymmobile/`. Nas tarefas abaixo, `.../` abrevia esse caminho.

---

### Task 1: Esqueleto Gradle que compila e abre

**Files:**
- Create: `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`, `gradle/libs.versions.toml`
- Create: `gradle/wrapper/gradle-wrapper.properties`, `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`
- Create: `app/build.gradle.kts`, `app/proguard-rules.pro`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/res/values/strings.xml`, `app/src/main/res/values/themes.xml`
- Create: `.gitignore`
- Create: `app/src/main/java/com/example/gymmobile/MainActivity.kt`

**Interfaces:**
- Consumes: nada.
- Produces: projeto abrível no Android Studio; `class MainActivity : ComponentActivity`.

- [ ] **Step 1: Criar o `.gitignore`**

```gitignore
*.iml
.gradle/
/local.properties
/.idea/
.DS_Store
/build
/app/build
/captures
.externalNativeBuild
.cxx
local.properties
```

- [ ] **Step 2: Criar `gradle/libs.versions.toml`**

Todas as versões do projeto vivem só aqui.

```toml
[versions]
agp = "8.7.2"
kotlin = "2.0.21"
coreKtx = "1.13.1"
lifecycle = "2.8.7"
activityCompose = "1.9.3"
composeBom = "2024.10.01"
navigation = "2.8.4"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

- [ ] **Step 3: Criar `settings.gradle.kts`**

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "gym-mobile"
include(":app")
```

- [ ] **Step 4: Criar `build.gradle.kts` (raiz) e `gradle.properties`**

`build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

`gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
android.useAndroidX=true
android.nonTransitiveRClass=true
kotlin.code.style=official
```

- [ ] **Step 5: Criar `app/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.gymmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gymmobile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
}
```

Criar também `app/proguard-rules.pro` vazio (arquivo em branco; o `proguardFiles` acima o referencia).

- [ ] **Step 6: Criar o Gradle wrapper**

O wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`) é binário e não pode ser escrito à mão. Criar apenas o `.properties`:

`gradle/wrapper/gradle-wrapper.properties`:

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

Baixar os três arquivos restantes do wrapper oficial da versão 8.9:

```bash
mkdir -p gradle/wrapper
BASE="https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradle/wrapper"
curl -fL -o gradle/wrapper/gradle-wrapper.jar "$BASE/gradle-wrapper.jar"
curl -fL -o gradlew "https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradlew"
curl -fL -o gradlew.bat "https://raw.githubusercontent.com/gradle/gradle/v8.9.0/gradlew.bat"
chmod +x gradlew
```

Verificar que o jar é mesmo um jar (não uma página de erro HTML):

```bash
file gradle/wrapper/gradle-wrapper.jar   # esperado: Java archive data (JAR)
```

Se o download falhar, o Android Studio regenera o wrapper sozinho ao abrir o projeto — anotar isso e seguir.

- [ ] **Step 7: Criar `AndroidManifest.xml` e recursos base**

`app/src/main/AndroidManifest.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/sym_def_app_icon"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.GymMobile">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.GymMobile">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

`app/src/main/res/values/strings.xml`:

```xml
<resources>
    <string name="app_name">Gym</string>
</resources>
```

`app/src/main/res/values/themes.xml` — tema de plataforma mínimo. `windowBackground` usa o `--bg` do CSS para não haver flash branco antes do Compose desenhar:

```xml
<resources>
    <style name="Theme.GymMobile" parent="android:Theme.Material.NoActionBar">
        <item name="android:windowBackground">#0E0F13</item>
        <item name="android:statusBarColor">#0E0F13</item>
        <item name="android:navigationBarColor">#0E0F13</item>
    </style>
</resources>
```

- [ ] **Step 8: Criar `MainActivity.kt` provisória**

Prova que o build fecha antes de existir qualquer tela. É substituída na Task 5.

```kotlin
package com.example.gymmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF0E0F13)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Gym", color = Color(0xFFF2F1ED))
            }
        }
    }
}
```

Este é o **único** lugar do projeto onde um literal `Color(0x...)` é permitido, e some na Task 5.

- [ ] **Step 9: Verificação (Android Studio)**

Abrir a pasta `gym-mobile` no Android Studio, aguardar o Gradle Sync e rodar:

```
./gradlew :app:assembleDebug
```

Esperado: `BUILD SUCCESSFUL`. Rodando no emulador, uma tela preta com "Gym" centralizado.

Falhas prováveis e o que fazer: se o Sync reclamar de SDK ausente, aceitar o download que o Studio oferece; se reclamar de JDK, apontar Gradle JDK 17 em *Settings → Build Tools → Gradle*.

- [ ] **Step 10: Commit**

```bash
git add -A
git commit -m "chore: esqueleto do projeto Android Compose"
```

---

### Task 2: Fontes e design tokens

**Files:**
- Create: `app/src/main/res/font/` — 9 arquivos `.ttf`
- Create: `.../ui/theme/Color.kt`, `.../ui/theme/Type.kt`, `.../ui/theme/Shape.kt`, `.../ui/theme/Theme.kt`

**Interfaces:**
- Consumes: projeto da Task 1.
- Produces:
  - `object GymColors` com `Bg, Surface, Surface2, Raised, BorderColor, TextPrimary, TextSecondary, TextMuted, Accent, AccentDim, AccentText, Success, SuccessDim, Chalk, OnAccent, Scrim` (todos `androidx.compose.ui.graphics.Color`).
  - `object GymType` com `display44, display40, display24, display22, display20, display19, display18, display16, sectionLabel, mono13, mono12, mono11, mono10, body15, body14, body13, body12, body11` (todos `TextStyle`).
  - `object GymShape` com `card = RoundedCornerShape(14.dp)`, `cardLarge = RoundedCornerShape(16.dp)`, `button = RoundedCornerShape(11.dp)`, `input = RoundedCornerShape(8.dp)`, `iconButton = RoundedCornerShape(10.dp)`, `miniButton = RoundedCornerShape(9.dp)`, `pill = RoundedCornerShape(20.dp)`.
  - `@Composable fun GymTheme(content: @Composable () -> Unit)`.

- [ ] **Step 1: Baixar os 9 TTFs estáticos**

O repositório `google/fonts` só publica fontes **variáveis**, que em `minSdk 24` não aplicam eixo de peso. A API CSS do Google Fonts, consultada com um user-agent legado, devolve TTF estático por peso. Este script foi verificado e produz 9 arquivos distintos:

```bash
mkdir -p app/src/main/res/font && cd app/src/main/res/font
UA="Mozilla/5.0 (Windows NT 6.1; WOW64)"
fetch() { # $1=família na URL  $2=peso  $3=arquivo de saída
  url=$(curl -s --max-time 25 -A "$UA" "https://fonts.googleapis.com/css?family=$1:$2" \
        | grep -o 'https://[^)]*\.ttf' | head -1)
  [ -z "$url" ] && { echo "FALHOU $3"; return 1; }
  curl -sfL --max-time 25 -o "$3" "$url" && printf "ok %s (%s bytes)\n" "$3" "$(stat -c%s "$3")"
}
fetch Oswald 400 oswald_regular.ttf
fetch Oswald 500 oswald_medium.ttf
fetch Oswald 600 oswald_semibold.ttf
fetch Oswald 700 oswald_bold.ttf
fetch Inter 400 inter_regular.ttf
fetch Inter 500 inter_medium.ttf
fetch Inter 600 inter_semibold.ttf
fetch JetBrains+Mono 400 jetbrains_mono_regular.ttf
fetch JetBrains+Mono 500 jetbrains_mono_medium.ttf
cd -
```

Nomes de arquivo em `res/font/` **precisam** ser minúsculos com `_` — qualquer outra grafia quebra a compilação de recursos.

- [ ] **Step 2: Verificar os downloads**

```bash
ls app/src/main/res/font/ | wc -l                        # esperado: 9
file app/src/main/res/font/*.ttf | grep -c TrueType      # esperado: 9
md5sum app/src/main/res/font/*.ttf | awk '{print $1}' | sort -u | wc -l   # esperado: 9
```

Os três números precisam bater. Nove hashes distintos provam que cada peso veio diferente — se algum vier repetido, a API devolveu o mesmo arquivo e a hierarquia tipográfica ficaria invisível.

Se o download falhar (rede bloqueada), o fallback declarado na spec é `FontFamily.SansSerif` / `FontFamily.Monospace` em `GymFonts` — e isso deve ser **reportado**, não escondido.

- [ ] **Step 3: Criar `Color.kt`**

```kotlin
package com.example.gymmobile.ui.theme

import androidx.compose.ui.graphics.Color

/** Tradução literal do bloco `:root` do protótipo HTML. */
object GymColors {
    val Bg = Color(0xFF0E0F13)
    val Surface = Color(0xFF1C1F26)
    val Surface2 = Color(0xFF242832)
    val Raised = Color(0xFF2A2E38)
    val BorderColor = Color(0xFF31353F)

    val TextPrimary = Color(0xFFF2F1ED)
    val TextSecondary = Color(0xFF9A9CA8)
    val TextMuted = Color(0xFF5F626D)

    val Accent = Color(0xFFFF6A39)
    val AccentDim = Accent.copy(alpha = 0.14f)
    val AccentText = Color(0xFFFF8B5F)

    val Success = Color(0xFF7FB069)
    val SuccessDim = Success.copy(alpha = 0.16f)

    val Chalk = Color(0xFFE9DCC0)

    /** Texto sobre `Accent` (`.btn-primary` do CSS). */
    val OnAccent = Color(0xFF1A0A04)

    /** Fundo do overlay de descanso (`rgba(10,11,14,0.92)`). */
    val Scrim = Color(0xFF0A0B0E).copy(alpha = 0.92f)
}
```

Nomes ficam dentro de `object GymColors` de propósito: uma `val Surface` de topo colidiria com o composable `androidx.compose.material3.Surface` em qualquer arquivo que importasse os dois.

- [ ] **Step 4: Criar `Type.kt`**

```kotlin
package com.example.gymmobile.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.gymmobile.R

object GymFonts {
    /** `--font-display`: Oswald */
    val Display = FontFamily(
        Font(R.font.oswald_regular, FontWeight.Normal),
        Font(R.font.oswald_medium, FontWeight.Medium),
        Font(R.font.oswald_semibold, FontWeight.SemiBold),
        Font(R.font.oswald_bold, FontWeight.Bold),
    )

    /** `--font-body`: Inter */
    val Body = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
    )

    /** `--font-mono`: JetBrains Mono */
    val Mono = FontFamily(
        Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
    )
}

/** Estilos nomeados pelo papel que cumprem no protótipo, não pela escala do Material. */
object GymType {
    private val display = TextStyle(fontFamily = GymFonts.Display, color = GymColors.TextPrimary)
    private val body = TextStyle(fontFamily = GymFonts.Body, color = GymColors.TextPrimary)
    private val mono = TextStyle(fontFamily = GymFonts.Mono, color = GymColors.TextPrimary)

    // Display (Oswald)
    val display44 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 44.sp, lineHeight = 44.sp)
    val display40 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 40.sp)
    val display24 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
    val display22 = display.copy(fontWeight = FontWeight.Medium, fontSize = 22.sp)
    val display20 = display.copy(fontWeight = FontWeight.SemiBold, fontSize = 20.sp)  // .avatar
    val display19 = display.copy(fontWeight = FontWeight.Medium, fontSize = 19.sp)
    val display18 = display.copy(fontWeight = FontWeight.Medium, fontSize = 18.sp)    // .profile-header h3
    val display16 = display.copy(fontWeight = FontWeight.Medium, fontSize = 16.sp)

    // Mono (JetBrains Mono)
    val sectionLabel = mono.copy(
        fontSize = 12.sp,
        letterSpacing = 0.04.em,
        color = GymColors.TextSecondary,
    )
    val mono13 = mono.copy(fontSize = 13.sp)
    val mono12 = mono.copy(fontSize = 12.sp)
    val mono11 = mono.copy(fontSize = 11.sp)
    val mono10 = mono.copy(fontSize = 10.sp, letterSpacing = 0.03.em)

    // Body (Inter)
    val body15 = body.copy(fontSize = 15.sp)
    val body14 = body.copy(fontSize = 14.sp)
    val body13 = body.copy(fontSize = 13.sp)
    val body12 = body.copy(fontSize = 12.sp)
    val body11 = body.copy(fontSize = 11.sp)
}
```

`sectionLabel` não carrega `uppercase` — em Compose isso é conteúdo, não estilo. Quem usa passa o texto já em maiúsculas (a Task 4 resolve isso dentro do componente `SectionLabel`).

- [ ] **Step 5: Criar `Shape.kt`**

```kotlin
package com.example.gymmobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** Raios do protótipo, por papel. */
object GymShape {
    val cardLarge = RoundedCornerShape(16.dp)   // .today-card
    val card = RoundedCornerShape(14.dp)        // .stat-card, .template-card, .exercise-card
    val button = RoundedCornerShape(11.dp)      // .btn, .text-input
    val iconButton = RoundedCornerShape(10.dp)  // .icon-btn
    val miniButton = RoundedCornerShape(9.dp)   // .mini-btn
    val input = RoundedCornerShape(8.dp)        // .set-row input, .check-toggle
    val pill = RoundedCornerShape(20.dp)        // .chip
}
```

- [ ] **Step 6: Criar `Theme.kt`**

```kotlin
package com.example.gymmobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GymColorScheme = darkColorScheme(
    primary = GymColors.Accent,
    onPrimary = GymColors.OnAccent,
    background = GymColors.Bg,
    onBackground = GymColors.TextPrimary,
    surface = GymColors.Surface,
    onSurface = GymColors.TextPrimary,
    outline = GymColors.BorderColor,
)

/**
 * Tema do app. Dark-only por decisão de spec: o protótipo não tem variante
 * clara, e `dynamicColor` destruiria a paleta laranja/giz.
 */
@Composable
fun GymTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GymColorScheme, content = content)
}
```

- [ ] **Step 7: Verificação (Android Studio)**

Adicionar temporariamente ao fim de `Theme.kt` um preview que prova que as três famílias carregaram:

```kotlin
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF0E0F13)
@Composable
private fun TokensPreview() {
    GymTheme {
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.ui.Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp),
        ) {
            androidx.compose.material3.Text("Oswald 600", style = GymType.display24)
            androidx.compose.material3.Text("Inter 400", style = GymType.body14)
            androidx.compose.material3.Text("JETBRAINS 12", style = GymType.sectionLabel)
            androidx.compose.material3.Text("Accent", style = GymType.body14.copy(color = GymColors.Accent))
        }
    }
}
```

(requer `import androidx.compose.ui.unit.dp` no arquivo)

Abrir o painel *Split/Design*. Esperado: as três linhas em tipografias **visivelmente diferentes** — Oswald condensada, Inter larga, JetBrains monoespaçada. Se as três parecerem iguais, os TTFs não entraram: voltar ao Step 2.

- [ ] **Step 8: Commit**

```bash
git add -A
git commit -m "feat: fontes e design tokens do protótipo"
```

---

### Task 3: Modelos e FakeRepository

**Files:**
- Create: `.../data/Models.kt`, `.../data/FakeRepository.kt`

**Interfaces:**
- Consumes: nada (Kotlin puro).
- Produces:
  - `data class WorkoutTemplate(id: String, name: String, tag: String, exerciseCount: Int)`
  - `data class ExerciseSet(weight: Double, reps: Int, done: Boolean = false)`
  - `data class ActiveExercise(name: String, sets: List<ExerciseSet>)`
  - `data class HistoryEntry(date: String, value: String)`
  - `data class ProgressExercise(id: String, name: String, pr: String, bars: List<Int>, history: List<HistoryEntry>)`
  - `data class WorkoutSummary(name: String, day: String, duration: String)`
  - `object FakeRepository` com `templates: StateFlow<List<WorkoutTemplate>>`, `recentWorkouts`, `activeExercises`, `progressExercises`, `exerciseCatalog`, `userName`, `userFullName`, `userInitials`, `userSubtitle`, `todayTag`, `todayTitle`, `todaySubtitle`, `todayTemplateId`, `streakDays`, `weekVolume`, `sessionsCount`, e as funções `templateById(String): WorkoutTemplate?`, `progressById(String): ProgressExercise?`, `addTemplate(name: String, picked: List<String>)`.

- [ ] **Step 1: Criar `Models.kt`**

```kotlin
package com.example.gymmobile.data

/** Um treino salvo, exibido em Meus treinos. */
data class WorkoutTemplate(
    val id: String,
    val name: String,
    val tag: String,
    val exerciseCount: Int,
)

/** Uma série: carga, repetições e se já foi concluída. */
data class ExerciseSet(
    val weight: Double,
    val reps: Int,
    val done: Boolean = false,
)

/** Um exercício dentro do treino em execução. */
data class ActiveExercise(
    val name: String,
    val sets: List<ExerciseSet>,
)

/** Uma linha do histórico: `18 ago` / `80kg × 8`. */
data class HistoryEntry(
    val date: String,
    val value: String,
)

/** Um exercício acompanhado na tela de Progresso. */
data class ProgressExercise(
    val id: String,
    val name: String,
    val pr: String,
    val bars: List<Int>,
    val history: List<HistoryEntry>,
)

/** Um treino concluído, listado em "Últimos treinos". */
data class WorkoutSummary(
    val name: String,
    val day: String,
    val duration: String,
)
```

- [ ] **Step 2: Criar `FakeRepository.kt`**

Todos os valores vêm literalmente do `<script>` do protótipo.

```kotlin
package com.example.gymmobile.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Fonte de dados única do app, em memória.
 *
 * É um `object` para que o estado sobreviva à troca de telas: um treino
 * salvo em Novo treino precisa aparecer imediatamente em Meus treinos,
 * como acontecia no protótipo com o array global `templates`.
 */
object FakeRepository {

    // ----- perfil -----
    const val userName = "Lucas"
    const val userFullName = "Lucas Costa"
    const val userInitials = "LC"
    const val userSubtitle = "Treinando há 8 meses"

    // ----- home -----
    const val streakDays = 4
    const val weekVolume = "12,4"
    const val todayTag = "Push · dia 3"
    const val todayTitle = "Peito, ombro e tríceps"
    const val todaySubtitle = "5 exercícios · ~50 min"

    /** Template aberto pelo botão "Iniciar treino" da Home. */
    const val todayTemplateId = "push-a"

    val recentWorkouts = listOf(
        WorkoutSummary("Pull B", "seg", "48min"),
        WorkoutSummary("Legs", "sáb", "55min"),
        WorkoutSummary("Push A", "qui", "51min"),
    )

    // ----- treinos -----
    private val _templates = MutableStateFlow(
        listOf(
            WorkoutTemplate("push-a", "Push A", "Peito · Ombro · Tríceps", 5),
            WorkoutTemplate("pull-b", "Pull B", "Costas · Bíceps", 6),
            WorkoutTemplate("legs", "Legs", "Pernas · Glúteos", 6),
            WorkoutTemplate("full-body", "Full body", "Corpo inteiro", 8),
        )
    )
    val templates: StateFlow<List<WorkoutTemplate>> = _templates.asStateFlow()

    fun templateById(id: String): WorkoutTemplate? =
        _templates.value.firstOrNull { it.id == id }

    /**
     * Regras copiadas de `saveWorkout()` do protótipo: nome em branco vira
     * "Treino sem nome"; a tag são os três primeiros exercícios unidos por
     * " · ", ou "Sem exercícios" quando nada foi escolhido.
     */
    fun addTemplate(name: String, picked: List<String>) {
        val finalName = name.trim().ifBlank { "Treino sem nome" }
        val tag = if (picked.isEmpty()) "Sem exercícios" else picked.take(3).joinToString(" · ")
        val id = "custom-${System.currentTimeMillis()}"
        _templates.value = _templates.value + WorkoutTemplate(id, finalName, tag, picked.size)
    }

    // ----- treino ativo -----
    /**
     * Lista única, devolvida para qualquer template: o protótipo também não
     * tinha exercícios por treino. O que muda por template é só o nome
     * exibido no rótulo da tela.
     */
    val activeExercises = listOf(
        ActiveExercise(
            "Supino reto",
            listOf(ExerciseSet(60.0, 10, done = true), ExerciseSet(60.0, 8)),
        ),
        ActiveExercise(
            "Desenvolvimento halteres",
            listOf(ExerciseSet(18.0, 12), ExerciseSet(18.0, 10)),
        ),
        ActiveExercise(
            "Elevação lateral",
            listOf(ExerciseSet(8.0, 15)),
        ),
    )

    // ----- progresso -----
    const val sessionsCount = 18

    val progressExercises = listOf(
        ProgressExercise(
            id = "supino-reto",
            name = "Supino reto",
            pr = "80kg",
            bars = listOf(40, 55, 60, 80, 95, 100),
            history = listOf(
                HistoryEntry("18 ago", "80kg × 8"),
                HistoryEntry("14 ago", "75kg × 10"),
                HistoryEntry("10 ago", "75kg × 8"),
            ),
        ),
        ProgressExercise(
            id = "agachamento-livre",
            name = "Agachamento livre",
            pr = "110kg",
            bars = listOf(50, 60, 70, 85, 90, 100),
            history = listOf(
                HistoryEntry("17 ago", "110kg × 6"),
                HistoryEntry("12 ago", "100kg × 8"),
            ),
        ),
        ProgressExercise(
            id = "levantamento-terra",
            name = "Levantamento terra",
            pr = "140kg",
            bars = listOf(60, 65, 75, 80, 92, 100),
            history = listOf(
                HistoryEntry("15 ago", "140kg × 4"),
                HistoryEntry("8 ago", "130kg × 5"),
            ),
        ),
    )

    fun progressById(id: String): ProgressExercise? =
        progressExercises.firstOrNull { it.id == id }

    // ----- catálogo do criador de treinos -----
    val exerciseCatalog = listOf(
        "Supino reto", "Supino inclinado", "Desenvolvimento halteres", "Elevação lateral",
        "Tríceps corda", "Puxada frente", "Remada curvada", "Rosca direta",
        "Agachamento livre", "Leg press", "Cadeira extensora", "Levantamento terra",
    )
}
```

- [ ] **Step 3: Verificação (Android Studio)**

`./gradlew :app:compileDebugKotlin` → `BUILD SUCCESSFUL`.

Conferir à vista: 4 templates, 3 exercícios ativos, 3 exercícios de progresso, 12 nomes no catálogo, 3 treinos recentes. Os acentos precisam estar corretos (`Tríceps`, `Glúteos`, `Elevação`, `sáb`, `há 8 meses`) — arquivo salvo em UTF-8.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: modelos e repositório fake com os dados do protótipo"
```

---

### Task 4: Componentes compartilhados

**Files:**
- Create: `.../ui/components/StatCard.kt`, `SectionLabel.kt`, `Buttons.kt`, `SquareIconButton.kt`, `HistoryRow.kt`, `SettingsRow.kt`, `ToggleSwitch.kt`, `SelectableChip.kt`

**Interfaces:**
- Consumes: `GymColors`, `GymType`, `GymShape` (Task 2).
- Produces:
  - `@Composable fun StatCard(label: String, value: String, unit: String? = null, modifier: Modifier = Modifier)`
  - `@Composable fun SectionLabel(text: String, modifier: Modifier = Modifier)`
  - `@Composable fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null)`
  - `@Composable fun GhostButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null)`
  - `@Composable fun SquareIconButton(icon: ImageVector, contentDescription: String?, onClick: () -> Unit, modifier: Modifier = Modifier, accent: Boolean = false, size: Dp = 34.dp, shape: Shape = GymShape.iconButton, container: Color = GymColors.Surface)`
  - `@Composable fun HistoryRow(left: String, right: String, showDivider: Boolean = true)`
  - `@Composable fun SettingsRow(title: String, subtitle: String? = null, showDivider: Boolean = true, trailing: @Composable () -> Unit)`
  - `@Composable fun ToggleSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit)`
  - `@Composable fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit)`

Todos no pacote `com.example.gymmobile.ui.components`.

- [ ] **Step 1: `StatCard.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.stat-card` — rótulo pequeno acima, número grande abaixo com unidade opcional. */
@Composable
fun StatCard(
    label: String,
    value: String,
    unit: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .padding(14.dp)
    ) {
        Text(text = label, style = GymType.body11.copy(color = GymColors.TextMuted))
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = GymType.display24)
            if (unit != null) {
                Spacer(Modifier.width(5.dp))
                Text(
                    text = unit,
                    style = GymType.body13.copy(color = GymColors.TextSecondary),
                    modifier = Modifier.padding(bottom = 3.dp),
                )
            }
        }
    }
}
```

- [ ] **Step 2: `SectionLabel.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymType

/**
 * `.section-label`. O `text-transform:uppercase` do CSS é aplicado aqui, no
 * conteúdo — Compose não tem equivalente de estilo.
 */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = GymType.sectionLabel,
        modifier = modifier.padding(top = 4.dp, bottom = 10.dp),
    )
}
```

- [ ] **Step 3: `Buttons.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.btn.btn-primary` — laranja, largura total. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    ButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        background = GymColors.Accent,
        contentColor = GymColors.OnAccent,
        borderColor = null,
    )
}

/** `.btn.btn-ghost` — superfície escura com borda fina. */
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    ButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        background = GymColors.Surface2,
        contentColor = GymColors.TextPrimary,
        borderColor = GymColors.BorderColor,
    )
}

@Composable
private fun ButtonBase(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    icon: ImageVector?,
    background: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    borderColor: androidx.compose.ui.graphics.Color?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(GymShape.button)
            .background(background)
            .then(
                if (borderColor != null) Modifier.border(0.5.dp, borderColor, GymShape.button)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = GymType.body14.copy(color = contentColor, fontWeight = FontWeight.Medium),
        )
    }
}
```

- [ ] **Step 4: `SquareIconButton.kt`**

Cobre `.icon-btn` (34dp, raio 10) e `.mini-btn` (32dp, raio 9) — mesma anatomia, medidas diferentes.

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape

@Composable
fun SquareIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
    size: Dp = 34.dp,
    shape: Shape = GymShape.iconButton,
    /** `.icon-btn` usa `--surface`; `.mini-btn` passa `--surface-2` aqui. */
    container: Color = GymColors.Surface,
) {
    val background = if (accent) GymColors.Accent else container
    val border = if (accent) GymColors.Accent else GymColors.BorderColor
    val tint = if (accent) GymColors.OnAccent else GymColors.TextPrimary

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background)
            .border(0.5.dp, border, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(if (size <= 32.dp) 14.dp else 17.dp),
        )
    }
}
```

- [ ] **Step 5: `HistoryRow.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/** `.history-row` — texto à esquerda, valor mono à direita, divisor abaixo. */
@Composable
fun HistoryRow(left: String, right: String, showDivider: Boolean = true) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = left, style = GymType.body13)
            Text(text = right, style = GymType.mono12.copy(color = GymColors.TextMuted))
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}
```

- [ ] **Step 6: `SettingsRow.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/** `.settings-row` — título (+ subtítulo opcional) à esquerda, controle à direita. */
@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    showDivider: Boolean = true,
    trailing: @Composable () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(text = title, style = GymType.body14)
                if (subtitle != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(text = subtitle, style = GymType.body12.copy(color = GymColors.TextMuted))
                }
            }
            trailing()
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}
```

- [ ] **Step 7: `ToggleSwitch.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors

/** `.toggle` — 42×24 com botão de 18 que desliza de 2dp a 20dp. */
@Composable
fun ToggleSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    val knobX by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        label = "knobX",
    )

    Box(
        modifier = Modifier
            .size(width = 42.dp, height = 24.dp)
            .clip(shape)
            .background(if (checked) GymColors.AccentDim else GymColors.Surface2)
            .border(0.5.dp, if (checked) GymColors.Accent else GymColors.BorderColor, shape)
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobX, y = 2.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) GymColors.Accent else GymColors.TextMuted),
        )
    }
}
```

- [ ] **Step 8: `SelectableChip.kt`**

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/** `.chip` / `.chip.selected`. */
@Composable
fun SelectableChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        style = GymType.body13.copy(
            color = if (selected) GymColors.AccentText else GymColors.TextSecondary,
        ),
        modifier = Modifier
            .clip(GymShape.pill)
            .background(if (selected) GymColors.AccentDim else GymColors.Surface)
            .border(
                width = 0.5.dp,
                color = if (selected) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.pill,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 8.dp),
    )
}
```

- [ ] **Step 9: Verificação (Android Studio)**

Criar `.../ui/components/ComponentsPreview.kt` com um preview de galeria:

```kotlin
package com.example.gymmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymTheme

@Preview(widthDp = 390, heightDp = 640)
@Composable
private fun ComponentsGalleryPreview() {
    GymTheme {
        var on by remember { mutableStateOf(true) }
        var picked by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxSize().background(GymColors.Bg).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SectionLabel("Componentes")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard("Sequência", "4", "dias", Modifier.weight(1f))
                StatCard("Volume da semana", "12,4", "t", Modifier.weight(1f))
            }
            PrimaryButton("Iniciar treino", onClick = {}, icon = Icons.Filled.PlayArrow)
            GhostButton("Novo treino", onClick = {})
            SquareIconButton(Icons.Filled.PlayArrow, null, onClick = {}, accent = true)
            HistoryRow("Pull B", "seg · 48min")
            SettingsRow("Backup automático") { ToggleSwitch(on) { on = it } }
            SelectableChip("Supino reto", picked) { picked = !picked }
        }
    }
}
```

Conferir no painel *Design*: cartões cinza com número grande, botão laranja com play, o toggle deslizando ao clicar (usar *Interactive Preview*), e o chip virando laranja quando selecionado.

- [ ] **Step 10: Commit**

```bash
git add -A
git commit -m "feat: componentes compartilhados de UI"
```

---

### Task 5: Navegação e chrome do app

**Files:**
- Create: `.../navigation/Routes.kt`, `.../navigation/GymBottomBar.kt`, `.../navigation/GymNavHost.kt`
- Create (stubs com assinatura final): `.../feature/home/HomeScreen.kt`, `.../feature/workouts/WorkoutsScreen.kt`, `.../feature/progress/ProgressScreen.kt`, `.../feature/progress/ProgressDetailScreen.kt`, `.../feature/active/ActiveWorkoutScreen.kt`, `.../feature/create/CreateWorkoutScreen.kt`, `.../feature/profile/ProfileScreen.kt`
- Modify: `.../MainActivity.kt` (substitui a versão provisória da Task 1)

**Interfaces:**
- Consumes: `GymTheme`, `GymColors`, `GymType` (Task 2).
- Produces:
  - `object Routes` com `HOME, WORKOUTS, PROGRESS, PROFILE, CREATE`, os padrões `ACTIVE = "active/{templateId}"` e `PROGRESS_DETAIL = "progress/{exerciseId}"`, os construtores `active(templateId: String): String` e `progressDetail(exerciseId: String): String`, e `TAB_ROUTES: List<String>`.
  - `@Composable fun GymApp()`
  - **Assinaturas finais das telas** — as Tasks 6–13 preenchem o corpo sem alterar a assinatura:
    - `HomeScreen(onStartWorkout: (String) -> Unit)`
    - `WorkoutsScreen(onOpenTemplate: (String) -> Unit, onEditTemplate: () -> Unit, onCreate: () -> Unit)`
    - `ProgressScreen(onOpenExercise: (String) -> Unit)`
    - `ProgressDetailScreen(exerciseId: String, onBack: () -> Unit)`
    - `ActiveWorkoutScreen(templateId: String, onBack: () -> Unit, onFinish: () -> Unit)`
    - `CreateWorkoutScreen(onBack: () -> Unit, onSaved: () -> Unit)`
    - `ProfileScreen()`

- [ ] **Step 1: `Routes.kt`**

```kotlin
package com.example.gymmobile.navigation

object Routes {
    const val HOME = "home"
    const val WORKOUTS = "workouts"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val CREATE = "create"

    const val ARG_TEMPLATE_ID = "templateId"
    const val ARG_EXERCISE_ID = "exerciseId"

    const val ACTIVE = "active/{$ARG_TEMPLATE_ID}"
    const val PROGRESS_DETAIL = "progress/{$ARG_EXERCISE_ID}"

    fun active(templateId: String) = "active/$templateId"
    fun progressDetail(exerciseId: String) = "progress/$exerciseId"

    /** Rotas que mostram a barra inferior. */
    val TAB_ROUTES = listOf(HOME, WORKOUTS, PROGRESS, PROFILE)
}
```

- [ ] **Step 2: Criar os 7 stubs de tela**

Cada arquivo com a assinatura final e um corpo mínimo. Padrão (repetir para os sete, trocando pacote, nome e parâmetros conforme a lista em **Produces**):

```kotlin
package com.example.gymmobile.feature.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onStartWorkout: (String) -> Unit) {
    Text("Início")
}
```

Os stubs existem só para o NavHost compilar nesta tarefa; cada um é substituído por inteiro pela sua tarefa de tela.

- [ ] **Step 3: `GymBottomBar.kt`**

```kotlin
package com.example.gymmobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/** `.bottom-nav`. Renderizada apenas nas rotas-aba — ver `GymApp`. */
@Composable
fun GymBottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.background(GymColors.Bg)) {
        HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 22.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            NavItem(Icons.Outlined.Home, "Início", Routes.HOME, currentRoute, onNavigate)
            NavItem(Icons.Outlined.FitnessCenter, "Treinos", Routes.WORKOUTS, currentRoute, onNavigate)
            NavItem(Icons.Outlined.ShowChart, "Progresso", Routes.PROGRESS, currentRoute, onNavigate)
            NavItem(Icons.Outlined.Person, "Perfil", Routes.PROFILE, currentRoute, onNavigate)
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    route: String,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    val selected = currentRoute == route
    val color = if (selected) GymColors.AccentText else GymColors.TextMuted
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.clickable { onNavigate(route) }.padding(horizontal = 8.dp),
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(21.dp))
        Text(text = label, style = GymType.body11.copy(color = color))
    }
}
```

- [ ] **Step 4: `GymNavHost.kt`**

```kotlin
package com.example.gymmobile.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymmobile.feature.active.ActiveWorkoutScreen
import com.example.gymmobile.feature.create.CreateWorkoutScreen
import com.example.gymmobile.feature.home.HomeScreen
import com.example.gymmobile.feature.profile.ProfileScreen
import com.example.gymmobile.feature.progress.ProgressDetailScreen
import com.example.gymmobile.feature.progress.ProgressScreen
import com.example.gymmobile.feature.workouts.WorkoutsScreen
import com.example.gymmobile.ui.theme.GymColors

@Composable
fun GymApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        containerColor = GymColors.Bg,
        bottomBar = {
            // `.bottom-nav.hidden`: some fora das 4 abas.
            if (currentRoute in Routes.TAB_ROUTES) {
                GymBottomBar(currentRoute) { navController.navigateToTab(it) }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onStartWorkout = { id -> navController.navigate(Routes.active(id)) },
                )
            }
            composable(Routes.WORKOUTS) {
                WorkoutsScreen(
                    onOpenTemplate = { id -> navController.navigate(Routes.active(id)) },
                    onEditTemplate = { navController.navigate(Routes.CREATE) },
                    onCreate = { navController.navigate(Routes.CREATE) },
                )
            }
            composable(Routes.PROGRESS) {
                ProgressScreen(
                    onOpenExercise = { id -> navController.navigate(Routes.progressDetail(id)) },
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen()
            }
            composable(Routes.CREATE) {
                CreateWorkoutScreen(
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Routes.WORKOUTS) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(
                route = Routes.ACTIVE,
                arguments = listOf(navArgument(Routes.ARG_TEMPLATE_ID) { type = NavType.StringType }),
            ) { entry ->
                ActiveWorkoutScreen(
                    templateId = entry.arguments?.getString(Routes.ARG_TEMPLATE_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                    onFinish = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = Routes.PROGRESS_DETAIL,
                arguments = listOf(navArgument(Routes.ARG_EXERCISE_ID) { type = NavType.StringType }),
            ) { entry ->
                ProgressDetailScreen(
                    exerciseId = entry.arguments?.getString(Routes.ARG_EXERCISE_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

/** Troca de aba sem empilhar destinos, preservando o estado de cada aba. */
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
```

- [ ] **Step 5: Substituir `MainActivity.kt`**

```kotlin
package com.example.gymmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gymmobile.navigation.GymApp
import com.example.gymmobile.ui.theme.GymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GymTheme {
                GymApp()
            }
        }
    }
}
```

Não deve restar nenhum literal `Color(0x...)` neste arquivo.

- [ ] **Step 6: Verificação (Android Studio)**

`./gradlew :app:assembleDebug` e rodar no emulador. Conferir:

1. Abre em Início, com a barra inferior mostrando 4 abas.
2. Tocar cada aba troca o texto do stub e **move o destaque laranja**.
3. Voltar para uma aba já visitada não empilha telas — o botão voltar do sistema sai do app a partir de Início.
4. A barra inferior tem o divisor de 0,5dp no topo.

- [ ] **Step 7: Commit**

```bash
git add -A
git commit -m "feat: navegação, barra inferior e stubs de tela"
```

---

### Task 6: Tela Início

**Files:**
- Create: `.../feature/home/HomeViewModel.kt`
- Modify: `.../feature/home/HomeScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository` (Task 3); `StatCard`, `SectionLabel`, `PrimaryButton`, `HistoryRow` (Task 4); assinatura `HomeScreen(onStartWorkout: (String) -> Unit)` (Task 5).
- Produces: `class HomeViewModel : ViewModel()` com `uiState: StateFlow<HomeViewModel.UiState>`.

- [ ] **Step 1: `HomeViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.home

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.WorkoutSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    data class UiState(
        val greetingName: String,
        val streakValue: String,
        val weekVolume: String,
        val todayTag: String,
        val todayTitle: String,
        val todaySubtitle: String,
        val todayTemplateId: String,
        val recent: List<WorkoutSummary>,
    )

    private val _uiState = MutableStateFlow(
        UiState(
            greetingName = FakeRepository.userName,
            streakValue = FakeRepository.streakDays.toString(),
            weekVolume = FakeRepository.weekVolume,
            todayTag = FakeRepository.todayTag,
            todayTitle = FakeRepository.todayTitle,
            todaySubtitle = FakeRepository.todaySubtitle,
            todayTemplateId = FakeRepository.todayTemplateId,
            recent = FakeRepository.recentWorkouts,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
```

- [ ] **Step 2: `HomeScreen.kt`**

```kotlin
package com.example.gymmobile.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.HistoryRow
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.StatCard
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun HomeScreen(
    onStartWorkout: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Bom treino,",
            style = GymType.body12.copy(color = GymColors.TextMuted),
        )
        Text(
            text = state.greetingName,
            style = GymType.display22,
            modifier = Modifier.padding(top = 2.dp, bottom = 18.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                label = "Sequência",
                value = state.streakValue,
                unit = "dias",
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = "Volume da semana",
                value = state.weekVolume,
                unit = "t",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(20.dp))
        SectionLabel("Treino de hoje")
        TodayCard(
            tag = state.todayTag,
            title = state.todayTitle,
            subtitle = state.todaySubtitle,
            onStart = { onStartWorkout(state.todayTemplateId) },
        )

        Spacer(Modifier.height(22.dp))
        SectionLabel("Últimos treinos")
        state.recent.forEachIndexed { index, workout ->
            HistoryRow(
                left = workout.name,
                right = "${workout.day} · ${workout.duration}",
                showDivider = index != state.recent.lastIndex,
            )
        }
    }
}

/**
 * `.today-card`. O CSS empilha `linear-gradient(155deg, accent-dim, transparent 60%)`
 * sobre `--surface`. 155° em coordenadas de tela (y para baixo) é a direção
 * (sen 155°, cos 155° invertido) ≈ (0.423, 0.906) — daí o `end.x` proporcional
 * à altura abaixo.
 */
@Composable
private fun TodayCard(
    tag: String,
    title: String,
    subtitle: String,
    onStart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.cardLarge)
            .drawBehind {
                drawRect(GymColors.Surface)
                drawRect(
                    Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to GymColors.AccentDim,
                            0.6f to Color.Transparent,
                        ),
                        start = Offset.Zero,
                        end = Offset(size.height * 0.466f, size.height),
                    )
                )
            }
            .border(0.5.dp, GymColors.BorderColor, GymShape.cardLarge)
            .padding(16.dp)
    ) {
        Text(text = tag, style = GymType.mono12.copy(color = GymColors.AccentText))
        Text(text = title, style = GymType.display19, modifier = Modifier.padding(top = 4.dp))
        Text(
            text = subtitle,
            style = GymType.body13.copy(color = GymColors.TextSecondary),
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp),
        )
        PrimaryButton(text = "Iniciar treino", onClick = onStart, icon = Icons.Filled.PlayArrow)
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun HomeScreenPreview() {
    GymTheme { HomeScreen(onStartWorkout = {}) }
}
```

- [ ] **Step 3: Verificação (Android Studio)**

Preview e emulador. Conferir contra o protótipo:

1. `Bom treino,` cinza acima de `Lucas` grande em Oswald.
2. Dois cartões lado a lado: `4 dias` e `12,4 t` — a unidade menor e alinhada pela base do número.
3. Cartão de hoje com **brilho laranja no canto superior esquerdo** desvanecendo na diagonal. Se o cartão estiver cinza chapado, o gradiente não desenhou.
4. `Iniciar treino` navega para a tela de treino ativo.
5. Três linhas de histórico, sem divisor após a última.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: tela Início"
```

---

### Task 7: Tela Meus treinos

**Files:**
- Create: `.../feature/workouts/WorkoutsViewModel.kt`
- Modify: `.../feature/workouts/WorkoutsScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.templates` (Task 3); `GhostButton`, `SquareIconButton` (Task 4); assinatura `WorkoutsScreen(onOpenTemplate, onEditTemplate, onCreate)` (Task 5).
- Produces: `class WorkoutsViewModel : ViewModel()` com `templates: StateFlow<List<WorkoutTemplate>>`.

- [ ] **Step 1: `WorkoutsViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.workouts

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.WorkoutTemplate
import kotlinx.coroutines.flow.StateFlow

class WorkoutsViewModel : ViewModel() {
    /** Vem direto do repositório: um treino salvo aparece aqui sem recarregar a tela. */
    val templates: StateFlow<List<WorkoutTemplate>> = FakeRepository.templates
}
```

- [ ] **Step 2: `WorkoutsScreen.kt`**

```kotlin
package com.example.gymmobile.feature.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.WorkoutTemplate
import com.example.gymmobile.ui.components.GhostButton
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun WorkoutsScreen(
    onOpenTemplate: (String) -> Unit,
    onEditTemplate: () -> Unit,
    onCreate: () -> Unit,
    viewModel: WorkoutsViewModel = viewModel(),
) {
    val templates by viewModel.templates.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 18.dp),
    ) {
        item {
            Text(
                text = "Meus treinos",
                style = GymType.display19,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            )
        }
        items(items = templates, key = { it.id }) { template ->
            TemplateCard(
                template = template,
                onEdit = onEditTemplate,
                onPlay = { onOpenTemplate(template.id) },
            )
            Spacer(Modifier.height(10.dp))
        }
        item {
            Spacer(Modifier.height(6.dp))
            GhostButton(text = "Novo treino", onClick = onCreate, icon = Icons.Filled.Add)
        }
    }
}

/** `.template-card`. */
@Composable
private fun TemplateCard(
    template: WorkoutTemplate,
    onEdit: () -> Unit,
    onPlay: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.card)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
            Text(text = template.name, style = GymType.display16)
            Text(
                text = template.tag,
                style = GymType.body12.copy(color = GymColors.TextSecondary),
                modifier = Modifier.padding(top = 2.dp),
            )
            Text(
                text = "${template.exerciseCount} exercícios",
                style = GymType.mono11.copy(color = GymColors.TextMuted),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SquareIconButton(
                icon = Icons.Outlined.Edit,
                contentDescription = "Editar ${template.name}",
                onClick = onEdit,
                size = 32.dp,
                shape = GymShape.miniButton,
                container = GymColors.Surface2,
            )
            SquareIconButton(
                icon = Icons.Filled.PlayArrow,
                contentDescription = "Iniciar ${template.name}",
                onClick = onPlay,
                accent = true,
                size = 32.dp,
                shape = GymShape.miniButton,
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun WorkoutsScreenPreview() {
    GymTheme {
        WorkoutsScreen(onOpenTemplate = {}, onEditTemplate = {}, onCreate = {})
    }
}
```

- [ ] **Step 3: Verificação (Android Studio)**

1. Quatro cartões: Push A, Pull B, Legs, Full body — com tag e contagem.
2. Cada cartão tem lápis cinza e play **laranja**, ambos 32dp.
3. Play abre o treino ativo; lápis abre Novo treino.
4. `Novo treino` no fim da lista, com borda fina e ícone `+`.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: tela Meus treinos"
```

---

### Task 8: Tela Novo treino

**Files:**
- Create: `.../feature/create/CreateWorkoutViewModel.kt`
- Modify: `.../feature/create/CreateWorkoutScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.exerciseCatalog`, `FakeRepository.addTemplate` (Task 3); `SelectableChip`, `PrimaryButton`, `SquareIconButton` (Task 4); assinatura `CreateWorkoutScreen(onBack, onSaved)` (Task 5).
- Produces: `class CreateWorkoutViewModel : ViewModel()` com `uiState: StateFlow<UiState>` e `onNameChange(String)`, `toggleExercise(String)`, `save()`.

- [ ] **Step 1: `CreateWorkoutViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.create

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateWorkoutViewModel : ViewModel() {

    data class UiState(
        val name: String = "",
        val catalog: List<String> = FakeRepository.exerciseCatalog,
        val picked: List<String> = emptyList(),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    /** `toggleChip` do protótipo: alterna, preservando a ordem de escolha. */
    fun toggleExercise(name: String) {
        _uiState.update { state ->
            val picked =
                if (name in state.picked) state.picked - name
                else state.picked + name
            state.copy(picked = picked)
        }
    }

    fun save() {
        val state = _uiState.value
        FakeRepository.addTemplate(name = state.name, picked = state.picked)
    }
}
```

O ViewModel nasce zerado a cada entrada na rota, o que reproduz o `openCreate()` do protótipo, que limpava nome e seleção.

- [ ] **Step 2: `CreateWorkoutScreen.kt`**

```kotlin
package com.example.gymmobile.feature.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SelectableChip
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun CreateWorkoutScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CreateWorkoutViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SquareIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                onClick = onBack,
            )
            Text(text = "Novo treino", style = GymType.display19)
        }

        FieldLabel("Nome do treino")
        NameField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
        )

        Spacer(Modifier.height(18.dp))
        FieldLabel("Adicionar exercícios")
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.catalog.forEach { name ->
                SelectableChip(
                    text = name,
                    selected = name in state.picked,
                    onClick = { viewModel.toggleExercise(name) },
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        FieldLabel("Selecionados")
        if (state.picked.isEmpty()) {
            Text(
                text = "Nenhum exercício selecionado ainda.",
                style = GymType.body13.copy(color = GymColors.TextMuted),
            )
        } else {
            state.picked.forEach { name ->
                PickedRow(name = name, onRemove = { viewModel.toggleExercise(name) })
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            text = "Salvar treino",
            onClick = {
                viewModel.save()
                onSaved()
            },
        )
    }
}

/** `.field-label`. */
@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = GymType.sectionLabel,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}

/** `.text-input` — borda vira laranja com o foco. */
@Composable
private fun NameField(value: String, onValueChange: (String) -> Unit) {
    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = GymType.body15,
        cursorBrush = SolidColor(GymColors.Accent),
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(GymShape.button)
            .background(GymColors.Surface)
            .border(
                width = 0.5.dp,
                color = if (focused) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.button,
            )
            .onFocusChanged { focused = it.isFocused }
            .padding(horizontal = 14.dp),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = "Ex: Push A, Upper, Dia de perna",
                        style = GymType.body15.copy(color = GymColors.TextMuted),
                    )
                }
                innerTextField()
            }
        },
    )
}

/** `.picked-row`. */
@Composable
private fun PickedRow(name: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.iconButton)
            .background(GymColors.Surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = name, style = GymType.body13)
        Text(
            text = "remover",
            style = GymType.body13.copy(color = GymColors.TextMuted),
            modifier = Modifier.clickable(onClick = onRemove),
        )
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun CreateWorkoutScreenPreview() {
    GymTheme { CreateWorkoutScreen(onBack = {}, onSaved = {}) }
}
```

- [ ] **Step 3: Verificação (Android Studio)**

No emulador, o fluxo inteiro:

1. Treinos → `Novo treino`. Campo vazio mostra o placeholder; ao focar, a borda fica **laranja**.
2. Os 12 chips quebram em várias linhas. Tocar um o pinta de laranja e o adiciona em "Selecionados".
3. `remover` tira o item e apaga o destaque do chip correspondente.
4. Digitar "Upper", escolher 4 exercícios, `Salvar treino` → volta para Meus treinos com um **5º cartão** `Upper`, tag com os 3 primeiros exercícios separados por `·`, e `4 exercícios`.
5. Salvar sem nome e sem seleção cria `Treino sem nome` / `Sem exercícios` / `0 exercícios`.
6. Reabrir Novo treino mostra tudo limpo.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: tela Novo treino"
```

---

### Task 9: Progresso — lista

**Files:**
- Create: `.../feature/progress/ProgressViewModel.kt`, `.../feature/progress/Sparkline.kt`
- Modify: `.../feature/progress/ProgressScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.progressExercises` (Task 3); assinatura `ProgressScreen(onOpenExercise: (String) -> Unit)` (Task 5).
- Produces: `class ProgressViewModel : ViewModel()` com `exercises: StateFlow<List<ProgressExercise>>`; `@Composable fun Sparkline(bars: List<Int>, modifier: Modifier = Modifier)`.

- [ ] **Step 1: `ProgressViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.lifecycle.ViewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.ProgressExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel : ViewModel() {
    private val _exercises = MutableStateFlow(FakeRepository.progressExercises)
    val exercises: StateFlow<List<ProgressExercise>> = _exercises.asStateFlow()
}
```

- [ ] **Step 2: `Sparkline.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors

/**
 * `.spark` — barras de 4dp, a última em destaque.
 * O CSS usa `height:${v/4}px` num contêiner de 28px; a divisão por 4 é mantida.
 */
@Composable
fun Sparkline(bars: List<Int>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(28.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        bars.forEachIndexed { index, value ->
            val isLast = index == bars.lastIndex
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height((value / 4f).dp)
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    .background(
                        if (isLast) GymColors.Accent
                        else GymColors.Chalk.copy(alpha = 0.5f)
                    )
            )
        }
    }
}
```

- [ ] **Step 3: `ProgressScreen.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.ProgressExercise
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProgressScreen(
    onOpenExercise: (String) -> Unit,
    viewModel: ProgressViewModel = viewModel(),
) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Progresso",
            style = GymType.display19,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
        )
        exercises.forEachIndexed { index, exercise ->
            ExerciseListRow(
                exercise = exercise,
                showDivider = index != exercises.lastIndex,
                onClick = { onOpenExercise(exercise.id) },
            )
        }
    }
}

/** `.exercise-list-row`. */
@Composable
private fun ExerciseListRow(
    exercise: ProgressExercise,
    showDivider: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Sparkline(bars = exercise.bars)
            Text(
                text = exercise.name,
                style = GymType.body14.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = exercise.pr,
                style = GymType.mono12.copy(color = GymColors.TextSecondary),
            )
        }
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = GymColors.BorderColor)
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProgressScreenPreview() {
    GymTheme { ProgressScreen(onOpenExercise = {}) }
}
```

- [ ] **Step 4: Verificação (Android Studio)**

1. Três linhas: Supino reto `80kg`, Agachamento livre `110kg`, Levantamento terra `140kg`.
2. Cada sparkline **cresce da esquerda para a direita**, com a última barra laranja e as demais em bege esmaecido.
3. Tocar qualquer linha abre o detalhe.

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: Progresso - lista de exercícios"
```

---

### Task 10: Progresso — detalhe

**Files:**
- Create: `.../feature/progress/ProgressDetailViewModel.kt`, `.../feature/progress/BarChart.kt`
- Modify: `.../feature/progress/ProgressDetailScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.progressById`, `FakeRepository.sessionsCount` (Task 3); `StatCard`, `SectionLabel`, `HistoryRow`, `SquareIconButton` (Task 4); assinatura `ProgressDetailScreen(exerciseId: String, onBack: () -> Unit)` (Task 5).
- Produces: `class ProgressDetailViewModel(exerciseId: String) : ViewModel()` com `uiState: StateFlow<UiState>` e `companion object { fun factory(exerciseId: String): ViewModelProvider.Factory }`; `@Composable fun BarChart(bars: List<Int>, modifier: Modifier = Modifier)`.

- [ ] **Step 1: `ProgressDetailViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.data.HistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressDetailViewModel(exerciseId: String) : ViewModel() {

    data class UiState(
        val name: String,
        val pr: String,
        val bars: List<Int>,
        val sessions: String,
        val lastLoad: String,
        val history: List<HistoryEntry>,
    )

    private val _uiState: MutableStateFlow<UiState>
    val uiState: StateFlow<UiState>

    init {
        val exercise = FakeRepository.progressById(exerciseId)
            ?: FakeRepository.progressExercises.first()

        _uiState = MutableStateFlow(
            UiState(
                name = exercise.name,
                pr = exercise.pr,
                bars = exercise.bars,
                sessions = FakeRepository.sessionsCount.toString(),
                // `.val.split(' ')[0]` do protótipo: "80kg × 8" -> "80kg".
                lastLoad = exercise.history.firstOrNull()
                    ?.value?.substringBefore(' ')
                    .orEmpty(),
                history = exercise.history,
            )
        )
        uiState = _uiState.asStateFlow()
    }

    companion object {
        fun factory(exerciseId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer { ProgressDetailViewModel(exerciseId) }
        }
    }
}
```

- [ ] **Step 2: `BarChart.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymType

/**
 * `.chart-bars` — contêiner de 130dp, alturas em dp iguais aos px do CSS
 * (máximo 100), última coluna em `Accent` (`.bar.peak`).
 */
@Composable
fun BarChart(bars: List<Int>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        bars.forEachIndexed { index, value ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(value.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                            if (index == bars.lastIndex) GymColors.Accent
                            else GymColors.Surface2
                        )
                )
                Text(
                    text = "${index + 1}",
                    style = GymType.mono10.copy(color = GymColors.TextMuted),
                )
            }
        }
    }
}
```

- [ ] **Step 3: `ProgressDetailScreen.kt`**

```kotlin
package com.example.gymmobile.feature.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.ui.components.HistoryRow
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.components.StatCard
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProgressDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ProgressDetailViewModel = viewModel(
        factory = ProgressDetailViewModel.factory(exerciseId)
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SquareIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                onClick = onBack,
            )
            Text(text = state.name, style = GymType.display19)
        }

        // `.detail-hero`
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = state.pr, style = GymType.display44.copy(color = GymColors.Chalk))
            Spacer(Modifier.width(10.dp))
            Text(
                text = "recorde pessoal",
                style = GymType.body13.copy(color = GymColors.TextSecondary),
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        Spacer(Modifier.height(20.dp))
        BarChart(bars = state.bars)
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(label = "Sessões", value = state.sessions, modifier = Modifier.weight(1f))
            StatCard(label = "Última carga", value = state.lastLoad, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        SectionLabel("Histórico")
        state.history.forEachIndexed { index, entry ->
            HistoryRow(
                left = entry.date,
                right = entry.value,
                showDivider = index != state.history.lastIndex,
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProgressDetailScreenPreview() {
    GymTheme { ProgressDetailScreen(exerciseId = "supino-reto", onBack = {}) }
}
```

- [ ] **Step 4: Verificação (Android Studio)**

Abrir Supino reto pela lista. Conferir:

1. `80kg` enorme em **bege** (`Chalk`), com `recorde pessoal` alinhado pela base.
2. Seis barras crescentes, a última **laranja**, numeradas 1..6 abaixo.
3. `Sessões 18` e `Última carga 80kg` — não `80kg × 8`.
4. Três linhas de histórico; sem divisor na última.
5. A seta volta para a lista. **O botão voltar do sistema também** — é o ganho da rota separada.
6. Abrir Levantamento terra mostra `140kg` e duas linhas de histórico.

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: Progresso - detalhe do exercício"
```

---

### Task 11: Treino ativo

**Files:**
- Create: `.../feature/active/ActiveWorkoutViewModel.kt`
- Modify: `.../feature/active/ActiveWorkoutScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.templateById`, `FakeRepository.activeExercises`, `ActiveExercise`, `ExerciseSet` (Task 3); `SectionLabel`, `PrimaryButton`, `SquareIconButton` (Task 4); assinatura `ActiveWorkoutScreen(templateId, onBack, onFinish)` (Task 5).
- Produces: `class ActiveWorkoutViewModel(templateId: String) : ViewModel()` com:
  - `data class RestState(val remaining: Int, val total: Int)` e `val progress: Float`
  - `data class UiState(contextLabel: String, elapsedLabel: String, exercises: List<ActiveExercise>, rest: RestState?)`
  - `uiState: StateFlow<UiState>`
  - `onWeightChange(exerciseIndex: Int, setIndex: Int, raw: String)`, `onRepsChange(exerciseIndex: Int, setIndex: Int, raw: String)`, `toggleSetDone(exerciseIndex: Int, setIndex: Int)`, `addSet(exerciseIndex: Int)`, `adjustRest(deltaSeconds: Int)`, `skipRest()`
  - `companion object { fun factory(templateId: String): ViewModelProvider.Factory; fun formatWeight(value: Double): String }`

- [ ] **Step 1: `ActiveWorkoutViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymmobile.data.ActiveExercise
import com.example.gymmobile.data.ExerciseSet
import com.example.gymmobile.data.FakeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ActiveWorkoutViewModel(templateId: String) : ViewModel() {

    /** Estado do descanso. `null` em `UiState.rest` significa overlay escondido. */
    data class RestState(val remaining: Int, val total: Int) {
        val progress: Float get() = if (total <= 0) 0f else remaining.toFloat() / total
    }

    data class UiState(
        val contextLabel: String,
        val elapsedLabel: String,
        val exercises: List<ActiveExercise>,
        val rest: RestState? = null,
    )

    private val templateName = FakeRepository.templateById(templateId)?.name ?: "Treino"

    private val _exercises = MutableStateFlow(FakeRepository.activeExercises)
    private val _elapsedSeconds = MutableStateFlow(0)
    private val _rest = MutableStateFlow<RestState?>(null)

    val uiState: StateFlow<UiState> =
        combine(_exercises, _elapsedSeconds, _rest) { exercises, elapsed, rest ->
            UiState(
                contextLabel = buildContextLabel(templateName, exercises),
                elapsedLabel = formatElapsed(elapsed),
                exercises = exercises,
                rest = rest,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(
                contextLabel = buildContextLabel(templateName, _exercises.value),
                elapsedLabel = formatElapsed(0),
                exercises = _exercises.value,
            ),
        )

    init {
        // Cronômetro da sessão. No protótipo era o texto fixo "00:04:12".
        viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                _elapsedSeconds.update { it + 1 }
            }
        }
    }

    // ----- séries -----

    fun onWeightChange(exerciseIndex: Int, setIndex: Int, raw: String) {
        val weight = raw.replace(',', '.').toDoubleOrNull() ?: 0.0
        updateSet(exerciseIndex, setIndex) { it.copy(weight = weight) }
    }

    fun onRepsChange(exerciseIndex: Int, setIndex: Int, raw: String) {
        val reps = raw.toIntOrNull() ?: 0
        updateSet(exerciseIndex, setIndex) { it.copy(reps = reps) }
    }

    /** `toggleSet`: marcar dispara o descanso; desmarcar apenas limpa. */
    fun toggleSetDone(exerciseIndex: Int, setIndex: Int) {
        val wasDone = _exercises.value[exerciseIndex].sets[setIndex].done
        updateSet(exerciseIndex, setIndex) { it.copy(done = !wasDone) }
        if (!wasDone) startRest(REST_SECONDS)
    }

    /** `addSet`: copia peso e reps da última série, sempre não concluída. */
    fun addSet(exerciseIndex: Int) {
        _exercises.update { exercises ->
            exercises.mapIndexed { index, exercise ->
                if (index != exerciseIndex) {
                    exercise
                } else {
                    val last = exercise.sets.lastOrNull() ?: ExerciseSet(0.0, 0)
                    exercise.copy(sets = exercise.sets + last.copy(done = false))
                }
            }
        }
    }

    private fun updateSet(
        exerciseIndex: Int,
        setIndex: Int,
        transform: (ExerciseSet) -> ExerciseSet,
    ) {
        _exercises.update { exercises ->
            exercises.mapIndexed { ei, exercise ->
                if (ei != exerciseIndex) {
                    exercise
                } else {
                    exercise.copy(
                        sets = exercise.sets.mapIndexed { si, set ->
                            if (si == setIndex) transform(set) else set
                        }
                    )
                }
            }
        }
    }

    // ----- descanso -----

    private var restJob: Job? = null

    private fun startRest(seconds: Int) {
        restJob?.cancel()
        _rest.value = RestState(remaining = seconds, total = seconds)
        restJob = viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                val current = _rest.value ?: break
                if (current.remaining <= 1) {
                    _rest.value = null
                    break
                }
                _rest.value = current.copy(remaining = current.remaining - 1)
            }
        }
    }

    /**
     * `adjustRest`: piso de 5s. O total acompanha para cima, para que o anel
     * nunca precise desenhar mais de uma volta.
     */
    fun adjustRest(deltaSeconds: Int) {
        val current = _rest.value ?: return
        val remaining = (current.remaining + deltaSeconds).coerceAtLeast(5)
        _rest.value = current.copy(
            remaining = remaining,
            total = maxOf(current.total, remaining),
        )
    }

    fun skipRest() {
        restJob?.cancel()
        _rest.value = null
    }

    companion object {
        const val REST_SECONDS = 60

        fun factory(templateId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer { ActiveWorkoutViewModel(templateId) }
        }

        /** 60.0 -> "60"; 62.5 -> "62,5" (vírgula, como se escreve em pt-BR). */
        fun formatWeight(value: Double): String =
            if (value % 1.0 == 0.0) value.toInt().toString()
            else value.toString().replace('.', ',')

        internal fun formatElapsed(totalSeconds: Int): String {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return "%02d:%02d:%02d".format(hours, minutes, seconds)
        }

        /**
         * `Push A · exercício 2 de 5` do protótipo, agora derivado: o índice é o
         * do primeiro exercício com série pendente, e o total é o tamanho real
         * da lista.
         */
        internal fun buildContextLabel(
            templateName: String,
            exercises: List<ActiveExercise>,
        ): String {
            val total = exercises.size
            if (total == 0) return templateName
            val pendingIndex = exercises.indexOfFirst { exercise ->
                exercise.sets.any { !it.done }
            }
            val current = if (pendingIndex == -1) total else (pendingIndex + 1).coerceAtMost(total)
            return "$templateName · exercício $current de $total"
        }
    }
}
```

- [ ] **Step 2: `ActiveWorkoutScreen.kt`**

O overlay de descanso é acrescentado na Task 12. Ao fim desta tarefa a tela já é completa e usável — séries editáveis, cronômetro correndo — apenas sem a contagem de descanso aparecendo.

```kotlin
package com.example.gymmobile.feature.active

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.ActiveExercise
import com.example.gymmobile.ui.components.PrimaryButton
import com.example.gymmobile.ui.components.SectionLabel
import com.example.gymmobile.ui.components.SquareIconButton
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ActiveWorkoutScreen(
    templateId: String,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    viewModel: ActiveWorkoutViewModel = viewModel(
        factory = ActiveWorkoutViewModel.factory(templateId)
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(GymColors.Bg)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
        ) {
            // `.workout-topbar`
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SquareIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    onClick = onBack,
                )
                Text(
                    text = state.elapsedLabel,
                    style = GymType.mono13.copy(color = GymColors.AccentText),
                )
                SquareIconButton(
                    icon = Icons.Filled.Close,
                    contentDescription = "Encerrar treino",
                    onClick = onFinish,
                )
            }

            SectionLabel(state.contextLabel)

            state.exercises.forEachIndexed { exerciseIndex, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onWeightChange = { setIndex, raw ->
                        viewModel.onWeightChange(exerciseIndex, setIndex, raw)
                    },
                    onRepsChange = { setIndex, raw ->
                        viewModel.onRepsChange(exerciseIndex, setIndex, raw)
                    },
                    onToggleDone = { setIndex -> viewModel.toggleSetDone(exerciseIndex, setIndex) },
                    onAddSet = { viewModel.addSet(exerciseIndex) },
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))
            PrimaryButton(text = "Finalizar treino", onClick = onFinish)
        }

        // O overlay de descanso é acrescentado aqui na Task 12.
    }
}

/** `.exercise-card`. */
@Composable
private fun ExerciseCard(
    exercise: ActiveExercise,
    onWeightChange: (Int, String) -> Unit,
    onRepsChange: (Int, String) -> Unit,
    onToggleDone: (Int) -> Unit,
    onAddSet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(GymShape.card)
            .background(GymColors.Surface)
            .border(0.5.dp, GymColors.BorderColor, GymShape.card)
            .padding(14.dp)
    ) {
        Text(
            text = exercise.name,
            style = GymType.display16,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        // `.set-header` — colunas 26 | 1fr | 1fr | 32
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeaderCell("Série", Modifier.width(26.dp))
            HeaderCell("Kg", Modifier.weight(1f))
            HeaderCell("Reps", Modifier.weight(1f))
            Spacer(Modifier.width(32.dp))
        }

        exercise.sets.forEachIndexed { setIndex, set ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${setIndex + 1}",
                    style = GymType.mono12.copy(color = GymColors.TextSecondary),
                    modifier = Modifier.width(26.dp),
                )
                SetNumberField(
                    fieldKey = "$setIndex-kg",
                    initial = ActiveWorkoutViewModel.formatWeight(set.weight),
                    onValueChange = { raw -> onWeightChange(setIndex, raw) },
                    modifier = Modifier.weight(1f),
                )
                SetNumberField(
                    fieldKey = "$setIndex-reps",
                    initial = set.reps.toString(),
                    onValueChange = { raw -> onRepsChange(setIndex, raw) },
                    modifier = Modifier.weight(1f),
                )
                CheckToggle(done = set.done, onClick = { onToggleDone(setIndex) })
            }
        }

        Text(
            text = "+ Adicionar série",
            style = GymType.body12.copy(color = GymColors.TextSecondary),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAddSet)
                .padding(vertical = 6.dp),
        )
    }
}

@Composable
private fun HeaderCell(text: String, modifier: Modifier) {
    Text(
        text = text.uppercase(),
        style = GymType.mono10.copy(color = GymColors.TextMuted),
        modifier = modifier,
    )
}

/**
 * `.set-row input`. O texto digitado é local para que o campo possa ficar
 * vazio enquanto o usuário apaga; o ViewModel recebe o valor já parseado.
 */
@Composable
private fun SetNumberField(
    fieldKey: String,
    initial: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember(fieldKey) { mutableStateOf(initial) }
    var focused by remember(fieldKey) { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = { raw ->
            text = raw
            onValueChange(raw)
        },
        singleLine = true,
        textStyle = GymType.mono13.copy(textAlign = TextAlign.Center),
        cursorBrush = SolidColor(GymColors.Accent),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .height(32.dp)
            .clip(GymShape.input)
            .background(GymColors.Surface2)
            .border(
                width = 0.5.dp,
                color = if (focused) GymColors.Accent else GymColors.BorderColor,
                shape = GymShape.input,
            )
            .onFocusChanged { focused = it.isFocused },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { innerTextField() }
        },
    )
}

/** `.check-toggle` / `.check-toggle.done`. */
@Composable
private fun CheckToggle(done: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(GymShape.input)
            .background(if (done) GymColors.SuccessDim else GymColors.Surface2)
            .border(
                width = 0.5.dp,
                color = if (done) GymColors.Success else GymColors.BorderColor,
                shape = GymShape.input,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = if (done) "Desmarcar série" else "Concluir série",
            tint = if (done) GymColors.Success else GymColors.TextMuted,
            modifier = Modifier.size(15.dp),
        )
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ActiveWorkoutScreenPreview() {
    GymTheme { ActiveWorkoutScreen(templateId = "push-a", onBack = {}, onFinish = {}) }
}
```

- [ ] **Step 3: Verificação (Android Studio)**

1. O rótulo mostra `PUSH A · EXERCÍCIO 1 DE 3` — nome do template que você abriu, não fixo.
2. O cronômetro **corre**: `00:00:01`, `00:00:02`…
3. Abrir por Legs mostra `LEGS · EXERCÍCIO 1 DE 3`.
4. Supino reto já vem com a 1ª série marcada em verde.
5. Marcar a 2ª série do Supino faz o rótulo virar `EXERCÍCIO 2 DE 3`.
6. Editar Kg/Reps funciona, inclusive apagando tudo (campo aceita ficar vazio).
7. `+ Adicionar série` cria uma linha copiando peso/reps da anterior, desmarcada.
8. Girar o aparelho **não zera** o cronômetro.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: tela de treino ativo com séries editáveis"
```

---

### Task 12: Overlay de descanso

**Files:**
- Create: `.../feature/active/RestOverlay.kt`
- Modify: `.../feature/active/ActiveWorkoutScreen.kt` (substituir o comentário do overlay)

**Interfaces:**
- Consumes: `ActiveWorkoutViewModel.RestState`, `adjustRest`, `skipRest` (Task 11).
- Produces: `@Composable fun RestOverlay(remaining: Int, progress: Float, onAdjust: (Int) -> Unit, onSkip: () -> Unit)`.

- [ ] **Step 1: `RestOverlay.kt`**

```kotlin
package com.example.gymmobile.feature.active

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymType

/**
 * `.rest-overlay`. O anel substitui o `stroke-dasharray/dashoffset` do SVG por
 * um `drawArc` cujo `sweepAngle` acompanha o tempo restante.
 */
@Composable
fun RestOverlay(
    remaining: Int,
    progress: Float,
    onAdjust: (Int) -> Unit,
    onSkip: () -> Unit,
) {
    val sweep by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f) * 360f,
        animationSpec = tween(durationMillis = 300),
        label = "restSweep",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Scrim)
            // Bloqueia toques na tela de baixo enquanto o descanso está ativo.
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp),
        ) {
            Text(
                text = "DESCANSO",
                style = GymType.sectionLabel,
                modifier = Modifier.padding(bottom = 20.dp),
            )

            Box(
                modifier = Modifier.size(180.dp).padding(bottom = 26.dp),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 10.dp.toPx()
                    val inset = strokeWidth / 2f
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                    drawArc(
                        color = GymColors.BorderColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = Stroke(width = strokeWidth),
                    )
                    drawArc(
                        color = GymColors.Accent,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
                Text(text = remaining.toString(), style = GymType.display40)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                RestControl(text = "-15s", onClick = { onAdjust(-15) })
                RestControl(text = "+15s", onClick = { onAdjust(15) })
            }

            Text(
                text = "Pular descanso",
                style = GymType.body13.copy(
                    color = GymColors.TextMuted,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable(onClick = onSkip),
            )
        }
    }
}

/** `.rest-controls button`. */
@Composable
private fun RestControl(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = GymType.mono13,
        modifier = Modifier
            .clip(GymShape.iconButton)
            .background(GymColors.Surface2)
            .border(0.5.dp, GymColors.BorderColor, GymShape.iconButton)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp),
    )
}
```

- [ ] **Step 2: Ligar o overlay em `ActiveWorkoutScreen.kt`**

Substituir a linha `// O overlay de descanso é acrescentado aqui na Task 12.` por:

```kotlin
        state.rest?.let { rest ->
            RestOverlay(
                remaining = rest.remaining,
                progress = rest.progress,
                onAdjust = viewModel::adjustRest,
                onSkip = viewModel::skipRest,
            )
        }
```

O `Box` externo da tela já existe desde a Task 11, então o overlay se sobrepõe naturalmente.

- [ ] **Step 3: Verificação (Android Studio)**

1. Marcar uma série abre o overlay com `60` e o anel **cheio** em laranja.
2. O número decresce a cada segundo e o anel **encolhe** junto.
3. `-15s` e `+15s` mudam o número na hora; apertar `-15s` várias vezes **para em 5**, não vai a negativo.
4. Depois de `+15s`, o anel continua dentro de uma volta — nunca ultrapassa o círculo.
5. Tocar atrás do overlay não interage com a lista de exercícios.
6. `Pular descanso` fecha na hora.
7. Deixar chegar a zero fecha sozinho.
8. **Desmarcar** uma série concluída **não** abre o overlay.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "feat: overlay de descanso com anel de progresso"
```

---

### Task 13: Tela Perfil

**Files:**
- Create: `.../feature/profile/ProfileViewModel.kt`
- Modify: `.../feature/profile/ProfileScreen.kt` (substitui o stub da Task 5)

**Interfaces:**
- Consumes: `FakeRepository.userFullName`, `userInitials`, `userSubtitle` (Task 3); `SettingsRow`, `ToggleSwitch`, `GhostButton` (Task 4); assinatura `ProfileScreen()` (Task 5).
- Produces: `class ProfileViewModel : ViewModel()` com `uiState: StateFlow<UiState>` e `setUnit(String)`, `setRestNotifications(Boolean)`, `setWorkoutReminder(Boolean)`, `setAutoBackup(Boolean)`.

- [ ] **Step 1: `ProfileViewModel.kt`**

```kotlin
package com.example.gymmobile.feature.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    data class UiState(
        val unit: String = "kg",
        val restNotifications: Boolean = true,
        val workoutReminder: Boolean = false,
        val autoBackup: Boolean = true,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setUnit(value: String) = _uiState.update { it.copy(unit = value) }
    fun setRestNotifications(value: Boolean) = _uiState.update { it.copy(restNotifications = value) }
    fun setWorkoutReminder(value: Boolean) = _uiState.update { it.copy(workoutReminder = value) }
    fun setAutoBackup(value: Boolean) = _uiState.update { it.copy(autoBackup = value) }
}
```

Os valores iniciais são os do protótipo: `kg` ativo, descanso ligado, lembrete desligado, backup ligado.

- [ ] **Step 2: `ProfileScreen.kt`**

```kotlin
package com.example.gymmobile.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmobile.data.FakeRepository
import com.example.gymmobile.ui.components.GhostButton
import com.example.gymmobile.ui.components.SettingsRow
import com.example.gymmobile.ui.components.ToggleSwitch
import com.example.gymmobile.ui.theme.GymColors
import com.example.gymmobile.ui.theme.GymShape
import com.example.gymmobile.ui.theme.GymTheme
import com.example.gymmobile.ui.theme.GymType

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymColors.Bg)
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 18.dp)
    ) {
        Text(
            text = "Perfil",
            style = GymType.display19,
            modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
        )

        // `.profile-header`
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(GymColors.AccentDim),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = FakeRepository.userInitials,
                    style = GymType.display20.copy(color = GymColors.AccentText),
                )
            }
            Column {
                Text(text = FakeRepository.userFullName, style = GymType.display18)
                Text(
                    text = FakeRepository.userSubtitle,
                    style = GymType.body12.copy(color = GymColors.TextSecondary),
                )
            }
        }

        SettingsRow(title = "Unidade de peso") {
            UnitToggle(selected = state.unit, onSelect = viewModel::setUnit)
        }
        SettingsRow(
            title = "Notificações de descanso",
            subtitle = "Avisar quando o tempo de descanso acabar",
        ) {
            ToggleSwitch(
                checked = state.restNotifications,
                onCheckedChange = viewModel::setRestNotifications,
            )
        }
        SettingsRow(
            title = "Lembrete de treino",
            subtitle = "Notificação nos dias programados",
        ) {
            ToggleSwitch(
                checked = state.workoutReminder,
                onCheckedChange = viewModel::setWorkoutReminder,
            )
        }
        SettingsRow(title = "Backup automático") {
            ToggleSwitch(
                checked = state.autoBackup,
                onCheckedChange = viewModel::setAutoBackup,
            )
        }

        Spacer(Modifier.height(22.dp))
        GhostButton(text = "Sair da conta", onClick = { /* sem ação: fora de escopo */ })
    }
}

/**
 * `.unit-toggle`. O CSS pinta o item ativo com `var(--surface-1,#31353f)` —
 * `--surface-1` nunca é definido, então vale o fallback, que é exatamente
 * `--border`. Daí `GymColors.BorderColor` aqui.
 */
@Composable
private fun UnitToggle(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clip(GymShape.iconButton)
            .background(GymColors.Surface2)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        listOf("kg", "lb").forEach { unit ->
            val active = unit == selected
            Text(
                text = unit,
                style = GymType.mono12.copy(
                    color = if (active) GymColors.TextPrimary else GymColors.TextMuted,
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (active) GymColors.BorderColor else Color.Transparent)
                    .clickable { onSelect(unit) }
                    .padding(horizontal = 12.dp, vertical = 5.dp),
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 760)
@Composable
private fun ProfileScreenPreview() {
    GymTheme { ProfileScreen() }
}
```

- [ ] **Step 3: Verificação (Android Studio)**

1. Avatar redondo laranja-esmaecido com `LC`; ao lado, `Lucas Costa` e `Treinando há 8 meses`.
2. Estado inicial: `kg` destacado, descanso **ligado**, lembrete **desligado**, backup **ligado**.
3. Tocar `lb` move o destaque; tocar um switch **anima** o botão deslizando.
4. Divisores de 0,5dp entre as quatro linhas.
5. `Sair da conta` é um botão ghost sem ação — comportamento correto, não um bug.

- [ ] **Step 4: Verificação final do app inteiro**

Percorrer o app inteiro no emulador uma vez:

- As 4 abas trocam e destacam corretamente.
- Início → Iniciar treino → marcar série → descanso → pular → Finalizar → volta a Início.
- Treinos → play → treino ativo com o nome certo → voltar.
- Treinos → Novo treino → salvar → o novo cartão aparece.
- Progresso → detalhe → voltar (seta e botão do sistema).
- Perfil → mexer nos controles.
- Barra inferior **invisível** em treino ativo e em Novo treino; **visível** nas 4 abas.

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: tela Perfil"
```
