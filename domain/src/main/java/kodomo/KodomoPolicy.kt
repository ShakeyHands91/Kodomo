package kodomo

import mihon.domain.extension.model.ContentWarning

/**
 * The parental policy of this build.
 *
 * Everything here is a compile-time constant on purpose. Upstream Mihon models the content rating
 * as a user preference that a device-credential prompt can unlock; this fork does not, because on a
 * child's device the device credential is the child's own PIN. Nothing at runtime — no preference,
 * no settings screen, no deep link, no restored backup — can widen what this object allows.
 *
 * The parent PIN gates *installing* extensions and managing extension stores. It does not gate this
 * object, and is not meant to: an 18+ extension is refused for everyone, parent included. Changing
 * that means changing this file and rebuilding.
 */
object KodomoPolicy {

    /**
     * Content ratings this build will load.
     *
     * [ContentWarning.MIXED] is deliberately excluded. A mixed extension points at a site carrying
     * both safe and adult titles: the extension loads, its sources register, and the adult titles
     * are reachable through ordinary in-app search. There is no way to admit the safe half alone.
     */
    val allowedContentWarnings: Set<ContentWarning> = setOf(ContentWarning.SAFE)

    /**
     * Index URLs of the extension stores this build will accept, matched exactly.
     *
     * This is the load-bearing half of the policy. An extension's rating is self-declared — it comes
     * from the APK's own metadata and from the store index's own JSON, both controlled by whoever
     * publishes the store. [allowedContentWarnings] is therefore only as trustworthy as the store it
     * is reading, and an arbitrary store can label an adult extension as safe.
     *
     * An empty set means no store can be added at all, which is the safe default: a build that has
     * not been told which stores to trust should trust none, rather than all. The app still works
     * with none — the built-in local source is registered independently of extensions, so files
     * side-loaded into the app's storage are readable either way.
     *
     * Match the URL exactly as it is typed into the app. A store's index may redirect to a second
     * URL, and that redirect target is checked against this set too; see ExtensionStoreService.
     */
    val allowedStoreIndexUrls: Set<String> = setOf(
        // Keiyoushi, the community extension store. Paste this exact URL into the app: it is the
        // protobuf index, which the app reads directly. The older /index.min.json entry point is
        // deliberately not listed — it redirects via an index_v2 field to a URL on another host,
        // and that redirect is checked against this set too (see ExtensionStoreService).
        "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.pb",
    )

    /**
     * Packages this build will install and load, out of everything the approved stores offer.
     *
     * The store allowlist decides who may supply extensions; this decides which ones. Keiyoushi
     * carries around 1,400 extensions and the overwhelming majority index unlicensed scan sites, so
     * approving the store alone would not be much of a control. Every entry here was picked
     * deliberately.
     *
     * An empty set allows nothing, matching [allowedStoreIndexUrls]: this build fails closed, so
     * emptying it by accident breaks loudly rather than silently admitting all 1,400.
     */
    val allowedExtensionPkgs: Set<String> = setOf(
        // Licensed publishers and official platforms
        "eu.kanade.tachiyomi.extension.all.mangaplus", // MANGA Plus by SHUEISHA
        "eu.kanade.tachiyomi.extension.en.vizshonenjump", // VIZ
        "eu.kanade.tachiyomi.extension.en.kmanga", // K Manga (Kodansha)
        "eu.kanade.tachiyomi.extension.all.mangaup", // Manga UP! (Square Enix)
        "eu.kanade.tachiyomi.extension.all.comikey", // Comikey
        "eu.kanade.tachiyomi.extension.en.mangamo", // Mangamo
        "eu.kanade.tachiyomi.extension.all.comicskingdom", // Comics Kingdom (King Features)

        // Official game and franchise comics
        "eu.kanade.tachiyomi.extension.all.leagueoflegends", // League of Legends
        "eu.kanade.tachiyomi.extension.all.holonometria", // HOLONOMETRIA
        "eu.kanade.tachiyomi.extension.en.mlbblore", // MLBB Lore
        "eu.kanade.tachiyomi.extension.en.honkaiimpact", // HonkaiImpact3

        // Free webcomics published by their own authors
        "eu.kanade.tachiyomi.extension.all.peppercarrot", // Pepper&Carrot
        "eu.kanade.tachiyomi.extension.all.xkcd", // xkcd
        "eu.kanade.tachiyomi.extension.all.commitstrip", // Commit Strip
        "eu.kanade.tachiyomi.extension.all.sandraandwoo", // Sandra and Woo
        "eu.kanade.tachiyomi.extension.all.dragonballmultiverse", // Dragon Ball Multiverse
        "eu.kanade.tachiyomi.extension.en.gunnerkriggcourt", // Gunnerkrigg Court
        "eu.kanade.tachiyomi.extension.en.darthsdroids", // Darths & Droids
        "eu.kanade.tachiyomi.extension.en.oots", // The Order of the Stick
        "eu.kanade.tachiyomi.extension.en.darklegacycomics", // Dark Legacy Comics
        "eu.kanade.tachiyomi.extension.en.schlockmercenary", // Schlock Mercenary
        "eu.kanade.tachiyomi.extension.en.swordscomic", // Swords Comic
        "eu.kanade.tachiyomi.extension.en.aurora", // aurora
        "eu.kanade.tachiyomi.extension.en.patchfriday", // Patch Friday

        // Unofficial scan site, added by parent. Re-check if its content rating
        // in the store index ever changes from SAFE.
        "eu.kanade.tachiyomi.extension.en.aquamanga", // Aqua Manga
    )

    fun isContentWarningAllowed(contentWarning: ContentWarning): Boolean {
        return contentWarning in allowedContentWarnings
    }

    fun isStoreAllowed(indexUrl: String): Boolean {
        return indexUrl.trim().trimEnd('/') in allowedStoreIndexUrls.map { it.trim().trimEnd('/') }
    }

    fun isExtensionAllowed(pkgName: String): Boolean {
        return pkgName in allowedExtensionPkgs
    }
}
