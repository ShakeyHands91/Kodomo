# Branding still to replace

The text-level rebrand is done: app name, README, NOTICE, store metadata, and the removal of upstream's logo, funding config, issue templates and release workflows.

**What remains is artwork, and it still contains Mihon's.** Apache-2.0 §6 grants no trademark rights, so these must be replaced before any public release or distribution. Each is a vector drawable whose path data draws the Mihon logo — renaming the file changes nothing.

| File | What it is |
|---|---|
| `app/src/main/res/drawable/ic_mihon.xml` | App logo. Used in the settings header (`LogoHeader.kt`) and as the small icon on every notification (`LibraryUpdateNotifier`, `BackupNotifier`, `ExtensionInstallService`) |
| `app/src/main/res/drawable/ic_mihon_splash.xml` | Splash screen icon; wraps `ic_mihon`. Referenced from `res/values/themes.xml` |
| `app/src/main/res/drawable/ic_launcher_foreground.xml` | Launcher icon foreground |
| `app/src/main/res/drawable/ic_launcher_background.xml` | Launcher icon background |
| `app/src/main/res/drawable/ic_launcher_monochrome.xml` | Themed-icon monochrome layer (Android 13+) |
| `app/src/debug/res/drawable/ic_launcher_foreground.xml` | Debug variant |
| `app/src/debug/res/drawable/ic_launcher_background.xml` | Debug variant |

`res/mipmap/ic_launcher.xml` is just the adaptive-icon wrapper pointing at the three launcher drawables — it needs no change once they are replaced.

Also missing, and needed if this ever goes to a store listing: `fastlane/metadata/android/en-US/images/` (icon, feature graphic, phone screenshots). Upstream's were deleted rather than reused.

## Replacing them

Android Studio's Image Asset tool (right-click `res` → New → Image Asset) generates the launcher set from a single source image and overwrites the foreground/background/monochrome drawables directly. For `ic_mihon`, any 24dp vector works — notification small icons must be a solid white silhouette on transparent, or Android renders them as a grey blob.

If you want to keep the file names stable, replace the path data inside `ic_mihon.xml` and leave the eight code references alone. If you'd rather rename to `ic_app`, the references are in the five files listed in the table plus `themes.xml`.
