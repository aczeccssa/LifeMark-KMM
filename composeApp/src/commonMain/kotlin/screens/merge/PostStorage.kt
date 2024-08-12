package screens.merge

import data.models.PhotoObject
import data.models.PostObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * PhotoStorage 接口定义了照片存储契约，用于管理照片对象的持久化和检索。
 */
interface PostStorage {
    /**
     * 异步保存新的照片对象列表到存储中。
     * @param newObject 要保存的照片对象列表。
     */
    suspend fun saveObjects(newObject: List<PhotoObject>)

    /**
     * 根据对象 ID 获取单个照片对象的 Flow。
     * @param objectId 要检索的照片对象的 ID。
     * @return 一个 Flow，发出与给定 ID 匹配的照片对象，如果没有找到则为 null。
     */
    fun getObjectByID(objectId: Int): Flow<PhotoObject?>

    fun getPostByID(postID: Int): Flow<PostObject?>

    /**
     * 获取所有存储的照片对象的 Flow。
     * @return 一个 Flow，发出当前存储的所有照片对象列表。
     */
    fun getObjects(): Flow<List<PhotoObject>>

    fun savePostObjects(newPostObject: List<PostObject>)
    fun getPostObjects(): Flow<List<PostObject>>
}

/**
 * InMemoryPhotoStorage 类实现了 [PostStorage] 接口，提供了一个基于内存的存储实现。
 * 它使用 [MutableStateFlow] 来存储和管理照片对象列表，这意味着数据不会持久化到磁盘，
 * 而是在应用运行时保持在内存中。
 */
class InMemoryPostStorage : PostStorage {
    /**
     * 使用 [MutableStateFlow] 来存储照片对象列表，允许观察者响应数据的变化。
     */
    private val storedObjects = MutableStateFlow(emptyList<PhotoObject>())
    private val storedPost = MutableStateFlow(emptyList<PostObject>())

    /**
     * 实现 [PostStorage.saveObjects] 方法，将新的照片对象列表保存到内存中。
     * 这将更新 [storedObjects] 的当前值。
     * @param newObject 要保存的新照片对象列表。
     */
    override suspend fun saveObjects(newObject: List<PhotoObject>) {
        print(newObject)
        storedObjects.value = newObject
    }

    /**
     * 实现 [PostStorage.getObjectByID] 方法，返回一个 Flow，它发出与给定 ID 匹配的单个照片对象。
     * 使用 [MutableStateFlow.map] 来转换和发出当前存储中找到的对象。
     * 如果没有找到匹配的对象，则发出 null。
     * @param objectId 要检索的照片对象的 ID。
     * @return 一个 Flow，发出与给定 ID 匹配的照片对象，或者如果没有找到则为 null。
     */
    override fun getObjectByID(objectId: Int): Flow<PhotoObject?> {
        return storedObjects.map { objects ->
            objects.find { it.objectID == objectId }
        }
    }

    override fun getPostByID(postID: Int): Flow<PostObject?> {
        return storedPost.map { posts ->
           posts.find { it.id == postID }
        }
    }

    /**
     * 实现 [PostStorage.getObjects] 方法，返回一个 Flow，它发出当前存储的所有照片对象列表。
     * 直接将 [storedObjects] 的当前值作为 Flow 发出。
     * @return 一个 Flow，发出当前存储的所有照片对象列表。
     */
    override fun getObjects(): Flow<List<PhotoObject>> = storedObjects

    override fun savePostObjects(newPostObject: List<PostObject>) {
        storedPost.value = newPostObject
    }
    override fun getPostObjects(): Flow<List<PostObject>> = storedPost
}