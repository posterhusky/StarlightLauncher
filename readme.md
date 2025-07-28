# StarlightLauncher

**StarlightLauncher** is a launcher application designed to simplify the management of multiple Fortnite accounts. You can link all of your accounts once and start Fortnite from any of them without needing to re-login.

---

## 🌟 Features

* **One-click launch** of Fortnite from any linked account at any time
* **Epic Account Linking** flow (device-code OAuth + auto-refresh)
* **Save/Load** account credentials to skip repeated sign-in
* **Auto-update** via GitHub Releases
* **Multi-language support** (English, Français, Русский, Deutsch, Español)

---

## ⚙️ Config
For now there is no way to change the settings in the launcher itself, all files are stored in the `.../AppData/Roaming/StarlightLauncher/` folder. You can edit the `config.json` file to change settings and upload a `background.png` to set a custom background.

---

## 🚀 Installation

* **Setup Wizard** Dowload the `StarlightLauncher-1.2-rc1.exe` setup wizard and launch it, it will guide you through the rest of the installations.
* **Manual Installation** Download the `StarlightLauncher-1.2-rc1.zip` archive, unzip it anywhere you want and run the `StarlightLauncher.exe` file.
* **Jar Installation** Download the `StarlightLauncher-1.2-rc1.jar` file and run it using `java -jar StarlightLauncher-1.2-rc1.jar`. Warning: this method is unoptimal as compatibility with all java version is not guaranteed and as of v1.2-rc1 auto updates will not work.

---

## 🛠️ Building from Source

```bash
git clone https://github.com/posterhusky/StarlightLauncher.git
cd StarlightLauncher

# Generate Version.kt, compile and bundle into a fat JAR
./gradlew shadowJar

# Run the JAR
java -jar build/libs/StarlightLauncher-1.2-rc1.jar
```

* **Kotlin** 95 % of codebase
* Uses **Ktor** (HTTP client), **kotlinx-serialization**, **Gson**, **Coroutines**, **ShadowJar**

---

## 📄 License

* **CC BY-SA 4.0** Attribution-ShareAlike 4.0 International 