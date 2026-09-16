package mihon.kids

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
object KidsPolicy {

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
     * not been told which stores to trust should trust none, rather than all.
     */
    val allowedStoreIndexUrls: Set<String> = emptySet()

    fun isContentWarningAllowed(contentWarning: ContentWarning): Boolean {
        return contentWarning in allowedContentWarnings
    }

    fun isStoreAllowed(indexUrl: String): Boolean {
        return indexUrl.trim().trimEnd('/') in allowedStoreIndexUrls.map { it.trim().trimEnd('/') }
    }
}
