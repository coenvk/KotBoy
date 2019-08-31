# KotBoy

A Game Boy (Color) emulator written in Kotlin.

[![Build Status](https://travis-ci.org/campoe/KotBoy.svg?branch=master)](https://travis-ci.org/campoe/KotBoy)
[![GitHub](https://img.shields.io/github/license/campoe/KotBoy)](https://opensource.org/licenses/MIT)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/campoe/KotBoy/graphs/commit-activity)
[![GitHub last commit](https://img.shields.io/github/last-commit/campoe/KotBoy)](https://github.com/campoe/KotBoy/commits/master)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/campoe/KotBoy?include_prereleases)](https://github.com/campoe/KotBoy/releases)

## Installation

All currently available releases can be found [here](https://github.com/campoe/KotBoy/releases).

Or you can build the emulator yourself using Maven.

```bash
mvn clean package
```

The jar ends up in the `./target` directory.

## Usage

The jar file can be run with the following command.

```bash
java -jar kotboy-{verson}.jar
```

A GUI will pop-up which is quite self-explanatory. Open a ROM using the `Ctrl+O` shortcut.

By default the following key bindings are defined:
- Left - <kbd>&larr;</kbd>
- Right - <kbd>&rarr;</kbd>
- Up - <kbd>&uarr;</kbd>
- Down - <kbd>&darr;</kbd>
- B - <kbd>A</kbd>
- A - <kbd>S</kbd>
- Start - <kbd>Enter</kbd>
- Select - <kbd>Space</kbd>

## Features

- Game Boy CPU emulation
- Compatible with most Blargg's tests (sound not supported)
- Graphics using Swing frame
- Screen sizes: 1x1, 2x2, 3x3, 4x4, 5x5, 6x6 and full screen
- Joypad (from keyboard input)
- MBC1, MBC2, MBC3 and MBC5
- Battery saves
- Real time clock (for MBC3)
- Supports .zip, .gb, .gbc and .rom files
- Build with Travis CLI (including testing)
- Supports GBC and SGB
- Interrupt handling
- Bitmap border
- Support boot ROM
- Custom color palettes in DMG mode

## Running tests

The test ROMs from [Blargg](http://gbdev.gg8.se/wiki/articles/Test_ROMs) and [Mooneye](https://github.com/Gekkio/mooneye-gb) can be run inside the emulator.
They can be found in the `src/test/resources/roms` directory.

The tests can also be ran from Maven.

```bash
mvn clean test -Pblargg-test
mvn clean test -Pmooneye-test
```

## Contributing

Any contributions you make are greatly appreciated.
Contributions can be made using the following procedure.

- Fork the project
- Create a feature branch (git checkout -b feature/{feature-name})
- Commit your changes (git commit -m)
- Push commit to the branch (git push origin feature/{feature-name})
- Open a pull request

## License

Distributed under the MIT License. See [LICENSE](https://github.com/campoe/KotBoy/blob/master/LICENSE) for more information.

## Acknowledgements
- [Gameboy CPU (LR35902) instruction set](http://pastraiser.com/cpu/gameboy/gameboy_opcodes.html)
- [Game Boy CPU Manual](http://marc.rawer.de/Gameboy/Docs/GBCPUman.pdf)
- [Pandocs](http://bgb.bircd.org/pandocs.htm)
- [BGB emulator](http://bgb.bircd.org/) for testing and debugging
- [Coffee GB](https://github.com/trekawek/coffee-gb/) as inspiration

<!--
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/campoe/KotBoy)
![GitHub repo size](https://img.shields.io/github/repo-size/campoe/KotBoy)
-->

<!-- [![Website](https://img.shields.io/website/https/campoe.github.io?down_message=offline&up_message=online)](https://campoe.github.io) -->

<!--
![GitHub language count](https://img.shields.io/github/languages/count/campoe/KotBoy))
![GitHub top language](https://img.shields.io/github/languages/top/campoe/KotBoy)
![GitHub All Releases](https://img.shields.io/github/downloads/campoe/KotBoy/total)
![GitHub Releases](https://img.shields.io/github/downloads/campoe/KotBoy/latest/total)
![GitHub commit merge status](https://img.shields.io/github/commit-status/campoe/KotBoy/master/HEAD)
![GitHub issues](https://img.shields.io/github/issues/campoe/KotBoy)
![GitHub closed issues](https://img.shields.io/github/issues-closed/campoe/KotBoy)
![GitHub pull requests](https://img.shields.io/github/issues-pr/campoe/KotBoy)
![GitHub pull requests](https://img.shields.io/github/issues-pr-closed/campoe/KotBoy)
![GitHub contributors](https://img.shields.io/github/contributors/campoe/KotBoy)
![GitHub Release Date](https://img.shields.io/github/release-date/campoe/KotBoy)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/campoe/KotBoy)
-->
