package me.ikvarxt.halo.repository

import androidx.lifecycle.LiveData
import me.ikvarxt.halo.dao.PostDetailsDao
import me.ikvarxt.halo.dao.PostItemDao
import me.ikvarxt.halo.entites.*
import me.ikvarxt.halo.network.PostApiService
import me.ikvarxt.halo.network.infra.NetworkBoundResource
import me.ikvarxt.halo.network.infra.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiService: PostApiService,
    private val postItemDao: PostItemDao,
    private val postDetailDao: PostDetailsDao,
) {

    // TODO: need add some control to shouldFetch
//    fun getPostsList(): LiveData<Resource<List<PostItem>>> = object :
//        NetworkBoundResource<List<PostItem>, PagesResponse<PostItem>>() {
//
//        override suspend fun saveCallResult(item: PagesResponse<PostItem>) {
//            val content = item.content
//            if (item.hasContent && content != null) {
//                postItemDao.insertPostItem(content)
//            }
//        }
//
//        override fun shouldFetch(data: List<PostItem>?) = true
//
//        override fun loadFromDb() = postItemDao.loadAllPosts()
//
//        override fun createCall() = apiService.listPosts()
//    }.asLiveData()

    fun getPostDetails(postId: Int): LiveData<Resource<PostDetails>> = object :
        NetworkBoundResource<PostDetails, PostDetails>() {

        override suspend fun saveCallResult(item: PostDetails) {
            postDetailDao.insertPostDetails(item)
        }

        override fun shouldFetch(data: PostDetails?) = true

        override fun loadFromDb() = postDetailDao.loadPostDetailWithId(postId)

        override fun createCall() = apiService.getPostDetailsWithId(postId)
    }.asLiveData()
}