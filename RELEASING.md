# Publishing a release

Pushing a `v*` tag builds signed APKs and publishes them as a GitHub release. Because `release.yml` builds with `-Penable-updater`, release builds also check this repository for newer releases and offer them in-app — so once the first release is out, later ones reach devices without a cable.

## Every release

1. Bump **both** numbers in `app/build.gradle.kts`:

   ```kotlin
   versionCode = 32      // must increase, or Android refuses the update
   versionName = "0.2.0" // must match the tag, minus the "v"
   ```

   Editable in GitHub's web editor if you have no local checkout.

2. Tag and push:

   ```powershell
   git pull
   git tag v0.2.0
   git push origin v0.2.0
   ```

3. Watch the Actions tab. About ten minutes later the release appears under **Releases** with five APKs attached.

## Why the two numbers both matter

They do different jobs, and getting either wrong fails in a different way.

**`versionCode`** is what Android compares when installing over an existing app. If it hasn't increased, the install is rejected — the update simply won't apply.

**`versionName`** is what the in-app updater compares against the release tag. `GetApplicationRelease` strips everything but digits and dots from both, then compares them component by component. So:

- The tag must have the same number of parts as `versionName`. Tag `v0.2` against `versionName = "0.1.0"` throws an index error inside the comparison.
- Use three parts consistently: `0.1.0`, `0.2.0`, `1.0.0`.

The workflow refuses to build if the tag and `versionName` disagree, which catches the common mistake before anything is published.

## Why the asset names are what they are

`ReleaseServiceImpl.getDownloadLink` picks which APK to offer by looking for an ABI substring in the asset's **filename** — `-arm64-v8a`, `-armeabi-v7a`, `-x86_64`, `-x86` — and falls back to the asset containing none of them, which is the universal build.

So `mihon-kids-v0.2.0-arm64-v8a.apk` works and `mihon-kids-v0.2.0-arm64.apk` does not: the app would find no matching asset and silently report no update available. If you ever rename the assets, keep those substrings.

## Signing

The release workflow fails if `KEYSTORE_BASE64` is unset, rather than quietly publishing a build signed with a throwaway debug key. A release like that could never be installed as an update over anything, including itself, and people would only find out when the next one wouldn't install.

All releases must be signed with the same key. See `SIGNING.md`.

## Drafts and pre-releases

The updater calls `/releases/latest`, which GitHub defines as the most recent **published, non-prerelease, non-draft** release. Marking a release as a pre-release therefore hides it from the updater — useful for testing a build on one device before it reaches the others.

To do that, add `--prerelease` to the `gh release create` line in the workflow, or flip the checkbox on the release page afterwards.

## The first release

`versionName` is already `0.1.0` and `versionCode` is `31`, so the first release needs no bump:

```powershell
git tag v0.1.0
git push origin v0.1.0
```

Nothing installed yet will see it as an update, since nothing has been installed from a release — that's expected. It's the baseline everything later updates from.
