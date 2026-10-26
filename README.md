# Grotesque Solar Blasts

[![Plugin Hub](https://img.shields.io/badge/RuneLite-Plugin%20Hub-orange.svg)](https://runelite.net/plugin-hub)
[![License](https://img.shields.io/badge/License-BSD_2--Clause-blue.svg)](LICENSE)

A visual transmog plugin for Old School RuneScape that swaps Dusk's falling ceiling rocks at the Grotesque Guardians for Sol Heredit's sunfire light beams from the Fortis Colosseum.

---

## Why?

Dusk's rockfall phase has always looked dated and muddy. This plugin swaps out the falling debris projectiles and murky ground shadows for crisp, vertical solar beams and sunfire target reticles, making the phase visually satisfying while maintaining the exact same attack timings and floor locations.

---

## Features

- **Projectile Override**: Despawns the default falling rock debris and replaces it with Sol Heredit's sunfire lightning or crystal beam animations.
- **Debris Shadow Cleanup**: Automatically removes the default murky rock shadows to keep the arena clean.
- **Sunfire Ground Reticles**: Draws a clean targeting reticle on tiles where beams will strike.
- **Impact Countdown**: Optional tick counter displaying the time remaining until each beam lands.
- **Customizable Styles**: Choose between **Sunfire Lightning**, **Crystal Beam**, or **Solar Pillar** animations, with adjustable beam tints and reticle colors.
- **Discord Webhook Integration**: Optionally send Grotesque Guardians kill milestones and Personal Best times directly to your Discord server.
- **100% Client-Side**: Strictly visual transmog conforming to Jagex third-party client guidelines.

---

## Configuration

| Setting | Type | Default | Description |
|---|---|---|---|
| **Hide Falling Rocks** | Checkbox | `true` | Suppresses Dusk's falling rock projectile and ceiling debris |
| **Beam Style** | Dropdown | `Sunfire Lightning` | Selects the animation (`Sunfire Lightning`, `Crystal Beam`, `Solar Pillar`) |
| **Beam Tint** | Color Picker | Gold (`#FFB923`) | Color tint applied to the descending light cylinder |
| **Show Ground Reticle** | Checkbox | `true` | Draws a solar targeting circle on targeted ground tiles |
| **Show Impact Timer** | Checkbox | `true` | Displays remaining server ticks until the beam hits |
| **Reticle Color** | Color Picker | Orange (`#FF8C00`) | Border color for the ground target indicator |
| **Reticle Fill Color**| Color Picker | Amber (`#FFAA00`) | Fill opacity for the target indicator |
| **Discord Webhook URL** | Text Field | Empty | Webhook URL for posting kill logs to Discord |
| **Notify on Kill** | Checkbox | `false` | Sends an embed when defeating Grotesque Guardians |
| **Notify on PB** | Checkbox | `true` | Highlights new personal best times in Discord |

---

## Development & Testing

To run a development instance of RuneLite with this plugin pre-loaded:

```bash
# Clone the repository
git clone https://github.com/Kukzze/grotesque-solar-blasts.git
cd grotesque-solar-blasts

# Launch local RuneLite client
./gradlew run
```

To run unit tests:

```bash
./gradlew test
```

---

## License

This project is licensed under the [BSD 2-Clause License](LICENSE).
