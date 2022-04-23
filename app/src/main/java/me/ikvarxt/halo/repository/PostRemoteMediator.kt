package me.ikvarxt.halo.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.entites.PostStatus
import me.ikvarxt.halo.network.PostApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val categoryId: Int? = null,
    private val keyword: String? = null,
    private val page: Int? = null,
    private val size: Int? = null,
    private val sorts: Array<String>? = null,
    private val status: PostStatus? = null,
    private val statuses: Array<String>? = null,
    private val more: Boolean? = null,
    private val apiService: PostApiService,
    private val database: HaloDatabase,
    private val dao: PostItemDao
) : RemoteMediator<Int, PostItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

//            val response =
//                apiService.listPosts(categoryId, keyword, page, size, sorts, status, statuses, more)
//
//            database.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    dao.clearAll()
//                }
//
//                response.content?.let {
//                    dao.insertPostItem(it)
//                }
//            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}