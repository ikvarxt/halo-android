package me.ikvarxt.halo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.ikvarxt.halo.AppExecutors
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.database.HaloDatabase
import me.ikvarxt.halo.entites.HaloResponse
import me.ikvarxt.halo.entites.ListPostResponse
import me.ikvarxt.halo.entites.PostDetails
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.ContentApiService
import me.ikvarxt.halo.network.infra.ApiResponse
import me.ikvarxt.halo.network.infra.ApiSuccessResponse
import me.ikvarxt.halo.network.infra.NetworkBoundResource
import me.ikvarxt.halo.network.infra.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val contentApiService: ContentApiService,
    private val database: HaloDatabase,
    private val postItemDao: PostItemDao,
) {

    fun getPostsList(): LiveData<Resource<List<PostItem>>> {
        return object :
            NetworkBoundResource<List<PostItem>, HaloResponse<ListPostResponse>>(appExecutors) {

            // currently workaround of no database implementation
            private var res: List<PostItem> = emptyList()

            override fun saveCallResult(item: HaloResponse<ListPostResponse>) {
                res = item.data.content
                val content = item.data.content
                content.forEach { postItemDao.insertPostItem(it) }
            }

            override fun shouldFetch(data: List<PostItem>?) = true

            override fun loadFromDb(): LiveData<List<PostItem>> {
                return postItemDao.loadAllPosts()
            }

            override fun createCall() = contentApiService.listPosts()
        }.asLiveData()
    }

    fun getPostDetails(postId: Long): LiveData<Resource<PostDetails>> {
        return object : NetworkBoundResource<PostDetails, HaloResponse<PostDetails>>(appExecutors) {
            private var res = PostDetails(0, "", "", "", 0)
            override fun saveCallResult(item: HaloResponse<PostDetails>) {
                res = item.data
            }

            override fun shouldFetch(data: PostDetails?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<PostDetails> {
                return MutableLiveData(res)
            }

            override fun createCall(): LiveData<ApiResponse<HaloResponse<PostDetails>>> {
                return contentApiService.getPostDetailsWithId(postId)
            }
        }.asLiveData()
    }
}