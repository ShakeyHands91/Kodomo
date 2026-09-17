package eu.kanade.tachiyomi.data.backup.restore.restorers

import dev.zacsweers.metro.Inject
import eu.kanade.tachiyomi.data.backup.models.BackupExtensionStore
import kodomo.KodomoPolicy
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import tachiyomi.data.Database

@Inject
class ExtensionStoreRestorer(
    private val database: Database,
) {

    suspend operator fun invoke(
        backupStore: BackupExtensionStore,
    ) {
        // Kodomo: restoring a backup is otherwise a silent way to add extension stores — no
        // prompt, no PIN, straight into the table. Anything outside the allowlist is dropped.
        if (!KodomoPolicy.isStoreAllowed(backupStore.indexUrl)) {
            logcat(LogPriority.WARN) { "Skipped restoring unapproved extension store ${backupStore.indexUrl}" }
            return
        }

        database.extension_storeQueries.upsert(
            indexUrl = backupStore.indexUrl,
            name = backupStore.name,
            badgeLabel = backupStore.badgeLabel ?: backupStore.name,
            signingKey = backupStore.signingKey,
            contactWebsite = backupStore.contactWebsite,
            contactDiscord = backupStore.contactDiscord,
            isLegacy = backupStore.isLegacy ?: true,
            extensionListUrl = backupStore.extensionListUrl,
        )
    }
}
