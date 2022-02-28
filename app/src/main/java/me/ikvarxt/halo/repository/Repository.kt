package me.ikvarxt.halo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.ikvarxt.halo.AppExecutors
import me.ikvarxt.halo.entites.ListPostResponse
import me.ikvarxt.halo.entites.PostItem
import me.ikvarxt.halo.network.ContentApiService
import me.ikvarxt.halo.network.infra.ApiSuccessResponse
import me.ikvarxt.halo.network.infra.NetworkBoundResource
import me.ikvarxt.halo.network.infra.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val contentApiService: ContentApiService
) {

    fun getPostsList(): LiveData<Resource<List<PostItem>>> {
        return object : NetworkBoundResource<List<PostItem>, ListPostResponse>(appExecutors) {

            private var res : List<PostItem> = emptyList()

            override fun saveCallResult(item: ListPostResponse) {
                res = item.data.content
                Log.d("testtest", "saveCallResult: $res")
            }

            override fun shouldFetch(data: List<PostItem>?) = true

            override fun loadFromDb(): LiveData<List<PostItem>> {
                return MutableLiveData(res)
            }

            override fun createCall() = contentApiService.listPosts()
        }.asLiveData()
    }
}