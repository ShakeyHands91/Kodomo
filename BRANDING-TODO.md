# Branding

The rebrand is complete — no Mihon artwork, naming or GitHub configuration remains in this repo.

## What the icons are now

Built from the Kodomo logo: a calico cat in round glasses reading a picture book.

| Resource | What it is |
|---|---|
| `res/drawable-*dpi/ic_launcher_foreground.png` | The cat, cut out of its background, at five densities. Sits inside the 72dp adaptive-icon safe zone, so nothing is clipped by round, squircle or square masks |
| `res/drawable/ic_launcher_background.xml` | Flat `#BFE3D8` mint, picked to contrast the cream cat and echo the book |
| `res/drawable/ic_launcher_monochrome.xml` | Themed-icon layer (Android 13+): the flat mark below, scaled into the safe zone |
| `res/drawable/ic_app.xml` | 24dp flat mark — cat head, two round lenses knocked out, open book. Used for the settings header (tinted by `LogoHeader`) and as the small icon on every notification |
| `res/drawable-*dpi/ic_app_logo.png` | Full-colour cat for the splash screen, via `ic_app_splash.xml` |
| `fastlane/.../images/icon.png`, `featureGraphic.png` | 512×512 icon and 1024×500 feature graphic for a store listing |
| `.github/assets/logo.png`, `logo-1024.png` | The circular badge — cat on mint, transparent outside the circle. Used in the README header; this is the file to hand anyone who asks for "the logo" |

`ic_mihon.xml` and `ic_mihon_splash.xml` are gone, along with upstream's launcher vectors and the debug-variant overrides (debug builds now use the main icons).

## About the circle

The circular badge in `.github/assets/` is a fixed image and always looks the same.

The **launcher icon is different**: Android adaptive icons ship as separate foreground and background layers, and the *launcher* decides the outline — Pixel crops to a circle, Samsung One UI to a squircle, others to a rounded square. An app cannot force one. That is why the foreground sits inside the 72dp safe zone: whichever shape a launcher picks, it clips only mint.

So on most phones the launcher icon already looks like the badge. On the ones that don't, the shape is the launcher's choice, not a setting anyone can change.

## Why the notification icon is a flat mark rather than the illustration

Android renders a notification small icon from its **alpha channel only**, painted in a single colour. A detailed illustration becomes a white blob, and a plain silhouette of this cat is an unreadable lump — the glasses and book vanish. So `ic_app.xml` is a purpose-drawn 24dp mark in the logo's spirit: round head, ears, two lens holes, open book. It stays legible down to 24px, which is where it is actually used.

The same mark serves as the Android 13+ themed-icon (monochrome) layer, for the same reason.

## Still worth doing

- **The source logo appears to be AI-generated.** In several jurisdictions, including the US, purely AI-generated images may not attract copyright, so you may have no rights to enforce if someone reuses it. Fine for a personal app; worth knowing if this repo stays public.
- Screenshots for the store listing (`fastlane/.../images/phoneScreenshots/`) — upstream's were deleted rather than reused, and none have replaced them.
- The feature graphic is a centre crop of the original wide illustration. It works, but a purpose-made 1024×500 with the app name would be better if you ever publish a listing.
