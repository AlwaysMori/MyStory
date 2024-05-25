package com.nanda.mystory.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import com.nanda.mystory.data.api.remote.retrofit.ApiService
import com.nanda.mystory.data.entity.RemoteKeys
import com.nanda.mystory.data.room.StoryDatabase

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getClosestRemoteKey(state)
                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_INDEX_PAGE
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getFirstItemRemoteKeys(state)
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getLastItemRemoteKeys(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val response = apiService.getStories(page, state.config.pageSize)
            val endOfPage = response.listStory.isNullOrEmpty()

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.remoteKeysDao().deleteRemoteKeys()
                    storyDatabase.storyDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPage) null else page + 1

                val keys = response.listStory?.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { storyDatabase.remoteKeysDao().insertAll(it) }
                response.listStory?.let { storyDatabase.storyDao().insertStory(it) }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPage)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    private suspend fun getFirstItemRemoteKeys(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { firstItem ->
            firstItem.id.let { itemId ->
                storyDatabase.remoteKeysDao().getRemoteKeys(itemId)
            }
        }
    }


    private suspend fun getLastItemRemoteKeys(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { lastItem ->
            lastItem.id.let { itemId ->
                storyDatabase.remoteKeysDao().getRemoteKeys(itemId)
            }
        }
    }


    private suspend fun getClosestRemoteKey(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id?.let { itemId ->
                storyDatabase.remoteKeysDao().getRemoteKeys(itemId)
            }
        }
    }


    companion object {
        private const val INITIAL_INDEX_PAGE = 1
    }


}