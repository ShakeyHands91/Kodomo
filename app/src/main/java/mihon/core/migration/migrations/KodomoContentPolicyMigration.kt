package mihon.core.migration.migrations

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import mihon.core.migration.Migration
import mihon.core.migration.MigrationContext
import tachiyomi.core.common.preference.PreferenceStore

/**
 * Kodomo: clears every stored value that upstream used to decide which content ratings load.
 *
 * Replaces upstream's ContentWarningMigration, which migrated the legacy `show_nsfw_source` boolean
 * into `enabled_content_warnings`. This build reads none of these keys — kodomo.KodomoPolicy
 * decides, at compile time — so the migration's job is the opposite one: leave nothing behind that a
 * future change could accidentally start honouring again.
 *
 * Also covers an install that used to be upstream Mihon, or an earlier Kodomo build, and still
 * carries a permissive value in its preferences.
 */
@Inject
@ContributesIntoSet(AppScope::class)
class KodomoContentPolicyMigration(
    private val preferenceStore: PreferenceStore,
) : Migration {

    override val version: Float = 31f

    override suspend fun invoke(migrationContext: MigrationContext): Boolean {
        preferenceStore.getBoolean("show_nsfw_source", true).delete()
        preferenceStore.getStringSet("enabled_content_warnings", emptySet()).delete()
        preferenceStore.getBoolean("apply_content_warnings_to_installed", true).delete()
        return true
    }
}
