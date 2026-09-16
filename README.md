<div align="center">
<img src="./.github/assets/logo.png" alt="Mihon Kids logo" width="120"/>

# Mihon Kids
</div>

A parental-control fork of [Mihon](https://github.com/mihonapp/mihon), an Android reader for manga, webtoons and comics.

Two things differ from upstream:

- **Extensions rated 18+ or mixed cannot be installed, loaded or listed.** This is a compile-time constant, not a setting. There is no toggle, no prompt and no preference that widens it.
- **Extension stores must be on a build-time allowlist**, and adding one is gated behind a parent PIN that is separate from the device's own screen lock.

## Why a fork rather than a setting

Upstream Mihon already models content ratings properly and can filter on them. What it does is put that filter behind the device credential — so on a child's own tablet, the child's own PIN unlocks it. Upstream's `authenticate()` helper also returns success outright when no screen lock is configured at all.

Mihon Kids removes the choice instead of guarding it. The rating check runs first in the extension loader, ahead of signature trust, so a disallowed extension cannot be talked into loading by trusting it, and an APK sideloaded with `adb` is inert on disk rather than merely hidden from the UI.

## What this does not do

Be clear-eyed about the limits:

- **Ratings are self-declared.** An extension's rating comes from its own package metadata and its store's own index — both controlled by whoever published them. The store allowlist, not the rating, is what the ban ultimately rests on.
- **A "safe" source can still carry mature titles.** This filters extensions, not content. Many general sites carry mature material under a safe-rated extension.
- **Nothing stops a second reader being installed.** Android's own parental controls are the outer perimeter; this is the inner one. Use both.
- **A child with USB debugging can clear app data**, which resets the PIN.

This is a speed bump sized for a young child, not a security boundary against a determined teenager.

## Building

Requires JDK 17 and the Android SDK (compileSdk 37.1). Create `keystore.properties` at the repo root with `storeFile`, `storePassword`, `keyAlias` and `keyPassword`, then:

```
./gradlew assembleRelease
```

**The store allowlist ships empty.** `domain/src/main/java/mihon/kids/KidsPolicy.kt` starts with `allowedStoreIndexUrls = emptySet()`, so no extension store can be added — a build that has not been told which stores to trust trusts none.

That is not the same as the app being useless. Mihon's built-in **local source** is registered independently of extensions, so comics and books copied into the app's storage are readable with no store at all. For a child's device that may be the whole answer: your own files, nothing fetched from anywhere.

## Changes from upstream

This is a modified version of Mihon. Modifications are made in accordance with section 4(b) of the Apache License 2.0. Files changed relative to upstream:

- `domain/src/main/java/mihon/kids/KidsPolicy.kt` *(new)* — the content and store policy
- `app/.../extension/util/ExtensionLoader.kt` — rating checked first and unconditionally
- `app/.../domain/extension/interactor/GetExtensionsByType.kt` — disallowed extensions not listed
- `app/.../domain/source/service/SourcePreferences.kt` — content-warning preferences removed
- `app/.../presentation/more/settings/screen/SettingsBrowseScreen.kt` — their settings UI removed
- `app/.../extension/ExtensionManager.kt` — no longer reloads on preference change
- `domain/.../extension/interactor/AddExtensionStore.kt` — store allowlist enforced
- `app/.../backup/restore/restorers/ExtensionStoreRestorer.kt` — unapproved stores skipped on restore
- `app/.../migrations/TrustExtensionRepositoryMigration.kt` — legacy store import dropped
- `app/.../migrations/KidsContentPolicyMigration.kt` *(new, replaces `ContentWarningMigration`)*
- `app/build.gradle.kts` — application ID, version series
- Branding, README, issue templates and release workflows replaced or removed

Forked from `mihonapp/mihon` at commit `a7179805` (v0.20.4).

## Disclaimer

The developer of this application has no affiliation with any content provider, and this application hosts zero content. It is not affiliated with or endorsed by the Mihon project.

## License

```
Copyright © 2015 Javier Tomás
Copyright © 2024 Mihon Open Source Project
Copyright © 2026 Mihon Kids contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

"Mihon" and the Mihon logo are the property of the Mihon Open Source Project and are not licensed under Apache-2.0. They are referenced here only to identify the upstream project this is derived from.
