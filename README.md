<div align="center">

<img src="app pc/assets/logo_epms.png" height="90" alt="EPMS Logo" />

# EPMS Future Mirror

**"Experimenta o teu futuro em 30 segundos"**

A photo booth kiosk for school open days — visitors pick a professional course, strike a pose, and walk away with a branded composite photo.

[![Android](https://img.shields.io/badge/Android-Kotlin%20%2B%20Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](APP%20android/)
[![PC](https://img.shields.io/badge/PC-Python%20%2B%20CustomTkinter-3776AB?logo=python&logoColor=white)](app%20pc/)
[![School](https://img.shields.io/badge/Escola-Profissional%20Mariana%20Seixas-F47C20)](https://www.epms.pt)

</div>

---

## What is this?

EPMS Future Mirror is an experiential marketing kiosk built for **Escola Profissional Mariana Seixas** open days and career fairs.

A visitor walks up to the screen, picks one of the school's 13 professional courses, and the app takes their photo with a fully branded overlay — complete with the course name, school logo, a motivational tagline, and a QR code to the school's website. The result can be saved to the gallery or printed on the spot via a Canon SELPHY printer.

Built in two flavours — **Android** (tablet kiosk) and **Windows PC** (touchscreen).

---

## Features

| | |
|---|---|
| 🎭 **13 Course Profiles** | Each with a unique accent colour, emoji, headline and tagline |
| 📸 **3-Second Countdown** | Live camera preview with thematic overlay before capture |
| 🖼️ **Composite Export** | Landscape, Square and Story formats saved to gallery |
| 🖨️ **Direct Printing** | Canon SELPHY integration, always landscape |
| 🔁 **Kiosk Mode** | Fullscreen, screen-always-on, system bars hidden |
| 📱 **QR Code** | Auto-generated, links to epms.pt |
| ⚙️ **Fully Configurable** | PC app driven entirely by `config.json` — no recompile needed |

---

## Course Profiles

| Emoji | Course |
|:---:|---|
| 🧸 | Técnico/a de Ação Educativa |
| 🩺 | Técnico/a de Auxiliar de Saúde |
| ✂️ | Cabeleireiro/a |
| 👨‍🍳 | Técnico/a de Cozinha e Restauração |
| 📣 | Técnico/a de Comunicação — Marketing, Relações Públicas e Publicidade |
| ⚽ | Técnico/a de Desporto |
| 💻 | Técnico/a de Desenvolvimento de Software |
| 🤖 | Técnico/a de Eletrónica e Automação |
| 💅 | Esteticista |
| 📊 | Técnico/a de Informática de Gestão |
| 🎬 | Técnico/a de Multimédia |
| 🎮 | Técnico/a de Produção de Conteúdos Interativos |
| 🌐 | Técnico/a de Sistemas de Computação e Redes |

---

## Tech Stack

### Android App
```
Language     Kotlin
UI           Jetpack Compose
Camera       CameraX
Navigation   Navigation Compose
QR Code      ZXing
Print        Android PrintHelper
Min SDK      29 (Android 10)
```

### PC App
```
Language     Python
UI           CustomTkinter
Camera       OpenCV (cv2)
Packaging    PyInstaller → .exe
Config       config.json (no recompile needed)
Print        Canon SELPHY via win32print
```

---

## Project Structure

```
EPMS Future Mirror/
│
├── APP android/                   # Android source
│   └── app/src/main/
│       ├── java/.../
│       │   ├── ui/screens/        # HomeScreen, ProfileSelection, Camera, Result
│       │   ├── ui/components/     # ThematicOverlay
│       │   ├── ui/theme/          # Colours, gradients, typography
│       │   ├── data/              # Profile, AppData
│       │   └── utils/             # Photo, print, QR utilities
│       └── res/drawable/          # logo_epms.png, background.png
│
├── app pc/                        # Windows PC build
│   ├── config.json                # All settings — edit this to customise
│   ├── assets/                    # logo_epms.png, background.png
│   ├── data/leads_epms.csv        # Captured interaction data
│   └── EPMS Future Mirror.exe     # Ready-to-run executable
│
└── EPMS Future Mirror.canvas      # Obsidian architecture diagram
```

---

## Screen Flow

```
Home Screen
    │  tap anywhere
    ▼
Profile Selection  ──────────────────────────────────────────┐
    │  pick a course                                          │
    ▼                                                         │
Camera Screen                                                 │
    │  tap shutter → 3 s countdown → capture                 │
    ▼                                                         │
Result Screen                                                 │
    ├── Guardar   → saves Landscape + Square + Story          │
    ├── Imprimir  → prints landscape on SELPHY                │
    ├── Repetir   → back to camera                            │
    └── Nova Experiência ─────────────────────────────────────┘
```

---

## Configuration (PC App)

Everything lives in `app pc/config.json`. Key fields:

```jsonc
{
  "school_name": "Escola Profissional Mariana Seixas",
  "slogan": "Experimenta o teu futuro em 30 segundos",
  "camera_index": 0,           // which camera to use
  "countdown_seconds": 3,
  "fullscreen_on_launch": true,
  "allow_printing": true,
  "printing": {
    "preferred_printer_keyword": "SELPHY",
    "paper_width_mm": 148,
    "paper_height_mm": 100,
    "print_dpi": 300
  },
  "profiles": [ ... ]          // add/remove/edit courses here
}
```

---

## Running the Android App

1. Open the `APP android/` folder in **Android Studio**
2. Connect a tablet or start an emulator
3. Hit **Run** — that's it

> Minimum Android 10 (API 29). Camera permission is requested on first launch.

## Running the PC App

Just double-click **`app pc/EPMS Future Mirror.exe`** — no installation needed.

To change settings, edit `app pc/config.json` and relaunch.

---

## Brand Colours

| Name | Hex | Preview |
|---|---|---|
| Navy | `#26628F` | ![](https://placehold.co/16x16/26628F/26628F) |
| Deep Navy | `#0B1F3B` | ![](https://placehold.co/16x16/0B1F3B/0B1F3B) |
| Orange | `#F47C20` | ![](https://placehold.co/16x16/F47C20/F47C20) |
| Amber | `#FFC247` | ![](https://placehold.co/16x16/FFC247/FFC247) |

---

<div align="center">

Made with ❤️ for **Escola Profissional Mariana Seixas**

[www.epms.pt](https://www.epms.pt)

</div>
