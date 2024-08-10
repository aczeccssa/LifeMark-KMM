package data.models

import androidx.paging.PagingSource
import androidx.paging.PagingState
import data.units.minusDays
import data.units.now
import kotlinx.datetime.LocalDateTime
import kotlin.math.max

/** The first item key(here is index 0). */
private const val STARTING_KEY = 0
/** The number per page. */
const val ITEMS_PER_PAGE = 50
/** The first article created time. */
private val firstArticleCreatedTime = LocalDateTime.now()

/** Article structure. */
data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val created: LocalDateTime,
) {
    // Create article by identifier using default stencil.
    constructor(id: Int) : this(
        id = id,
        title = "Article $id",
        description = "This describes article $id",
        created = firstArticleCreatedTime.minusDays(id.toLong())
    )
}

/** Paging source for article. */
class ArticlePagingSource : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Start paging with the STARTING_KEY if this is the first load
        val start = params.key ?: STARTING_KEY
        // Load as many items as hinted by params.loadSize
        val range = start.until(start + params.loadSize)

        return LoadResult.Page(
            data = range.map { number ->
                // Generate consecutive increasing numbers as the article id
                Article(number)
            },

            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = article.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}