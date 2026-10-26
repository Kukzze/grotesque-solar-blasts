# Grotesque Solar Blasts

[![Plugin Hub](https://img.shields.io/badge/RuneLite-Plugin%20Hub-orange.svg)](https://runelite.net/plugin-hub)
[![License](https://img.shields.io/badge/License-BSD_2--Clause-blue.svg)](LICENSE)

A visual transmog plugin for Old School RuneScape that swaps Dusk's falling ceiling rocks at the Grotesque Guardians for Sol Heredit's sunfire light beams from the Fortis Colosseum.

---

## Why?

Dusk's rockfall phase has always looked dated and muddy. This plugin swaps out the falling debris projectiles and murky ground shadows for crisp, vertical solar beams, making the phase visually satisfying while keeping gameplay and boss mechanics untouched.

---

## Features

- **Projectile Transmog**: Despawns the default falling rock debris and replaces it with Sol Heredit's sunfire lightning or crystal beam animations.
- **Debris Shadow Cleanup**: Automatically removes the murky ceiling rock shadows to keep the arena visually clear.
- **Customizable Styles**: Choose between **Sunfire Lightning**, **Crystal Beam**, or **Solar Pillar** animations, with adjustable beam tints.
- **Discord Webhook Integration**: Optionally send Grotesque Guardians kill milestones and Personal Best times directly to your Discord server.
- **100% Client-Side**: Strictly visual transmog conforming to Jagex third-party client guidelines. Contains no mechanic timers or gameplay indicators.

---

## Configuration

| Setting | Type | Default | Description |
|---|---|---|---|
| **Hide Falling Rocks** | Checkbox | `true` | Suppresses Dusk's falling rock projectile and ceiling debris |
| **Beam Style** | Dropdown | `Sunfire Lightning` | Selects the animation (`Sunfire Lightning`, `Crystal Beam`, `Solar Pillar`) |
| **Beam Tint** | Color Picker | Gold (`#FFB923`) | Color tint applied to the descending light cylinder |
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
