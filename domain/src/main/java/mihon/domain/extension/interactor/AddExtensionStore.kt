package mihon.domain.extension.interactor

import dev.zacsweers.metro.Inject
import kodomo.KodomoPolicy
import mihon.domain.extension.repository.ExtensionStoreRepository

@Inject
class AddExtensionStore(
    private val repository: ExtensionStoreRepository,
) {
    suspend operator fun invoke(indexUrl: String): Result<Unit> {
        // Kodomo: an extension store controls both its index and the ratings inside it, so an
        // arbitrary store can hand this build an 18+ extension labelled safe. Refused before the
        // parent PIN is even consulted — this is not something a parent can wave through.
        if (!KodomoPolicy.isStoreAllowed(indexUrl)) {
            return Result.failure(NotAnApprovedStoreException(indexUrl))
        }
        return repository.insert(indexUrl)
    }
}

class NotAnApprovedStoreException(indexUrl: String) : Exception(
    "$indexUrl is not an approved extension store for this build.",
)
