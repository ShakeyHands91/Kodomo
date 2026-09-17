package mihon.core.migration.migrations

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import eu.kanade.domain.source.service.SourcePreferences
import mihon.core.migration.Migration
import mihon.core.migration.MigrationContext

/**
 * Kodomo: upstream imports every URL from the legacy `extension_repos` preference straight into
 * the extension store table, with no check and no prompt. That is a way for an unapproved store to
 * appear in this build without anyone deciding to add it, so the import is dropped: the legacy
 * preference is simply discarded.
 *
 * Stores are added only through AddExtensionStore, which enforces kodomo.KodomoPolicy.
 */
@Inject
@ContributesIntoSet(AppScope::class)
class TrustExtensionRepositoryMigration(
    private val sourcePreferences: SourcePreferences,
) : Migration {
    override val version: Float = 7f

    override suspend fun invoke(migrationContext: MigrationContext): Boolean {
        sourcePreferences.extensionRepos.delete()
        return true
    }
}
