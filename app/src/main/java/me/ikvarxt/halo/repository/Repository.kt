package me.ikvarxt.halo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.ikvarxt.halo.AppExecutors
import me.ikvarxt.halo.dao.PostDetailsDao
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
    private val postItemDao: PostItemDao,
    private val postDetailDao: PostDetailsDao,
) {

    fun getPostsList(): LiveData<Resource<List<PostItem>>> {
        return object :
            NetworkBoundResource<List<PostItem>, HaloResponse<ListPostResponse>>(appExecutors) {

            override fun saveCallResult(item: HaloResponse<ListPostResponse>) {
                val content = item.data.content
                postItemDao.insertPostItem(*content.toTypedArray())
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

            override fun saveCallResult(item: HaloResponse<PostDetails>) {
                postDetailDao.insertPostDetails(item.data)
            }

            override fun shouldFetch(data: PostDetails?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<PostDetails> {
                return postDetailDao.loadPostDetailWithId(postId)
            }

            override fun createCall(): LiveData<ApiResponse<HaloResponse<PostDetails>>> {
                return contentApiService.getPostDetailsWithId(postId)
            }
        }.asLiveData()
    }
}