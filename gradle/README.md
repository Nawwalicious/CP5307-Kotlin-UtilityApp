# UtilityApp — Metro-Style Android Utility App

A focused, "at-a-glance" utility app for **CP5307 Assignment 1**, styled
after the Windows Phone "Metro" design language.

## Overview

UtilityApp delivers quick, everyday information through a clean, tile-based
interface inspired by Windows 10 Mobile. The home screen shows a live clock and
date and acts as a launcher for individual utility tools. The first fully-built
tool is a live currency converter.

## Features

### Home screen
- Live, self-updating clock and date
- Metro-style tile grid that launches each utility

### Currency Converter
- Live exchange rates fetched from exchangerate-api.com via Retrofit
- Converts across six core currencies (USD, EUR, GBP, AUD, JPY, CAD), USD as base
- Add up to four extra currencies from a searchable picker, each removable
- Rates cached locally for one hour to reduce network calls
- API key entered once and stored locally on the device

### Design
- Windows Phone "Metro" aesthetic: flat tiles, sharp corners, dark palette
- Custom Segoe UI typography
- Restricted colour palette (#000000, #1F1F1F, #0078D4, #99FFFF)

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM (ViewModel + StateFlow), Repository pattern |
| Networking | Retrofit + Gson |
| Persistence / caching | Jetpack DataStore (Preferences) |
| Async | Kotlin Coroutines |

## Architecture / Implementation Notes

- **UI layer** — Composable screens (`UtilityScreen`, `CurrencyConverterScreen`)
  built from small, reusable composables (`MetroTile`, `CurrencyRow`). State is
  hoisted out of leaf composables and passed down via parameters and callbacks.
- **State management** — `CurrencyViewModel` exposes a single
  `StateFlow<CurrencyUiState>`; the UI collects it and recomposes on change.
- **Data layer** — `CurrencyRepository` handles Retrofit calls and DataStore
  caching, keeping networking logic out of the ViewModel.
- **Conversion logic** — all rates are stored relative to USD. Converting from
  any currency first normalises the amount back to USD, then out to each target,
  so cross-rates stay consistent.

## Getting Started

1. Clone the repo and open it in Android Studio.
2. Get a free API key from exchangerate-api.com.
3. Run the app, open the Currency Converter, and paste your key when prompted.

## Planned / In Progress

- Settings screen to control main-screen content
- Additional utilities (unit converter, weather, to-do, passwords)

## Acknowledgements

Developed with assistance from AI tools (see the Declaration of AI-Generated
Material submitted with this assignment).