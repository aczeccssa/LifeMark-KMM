package data.models

import com.usecase.picture_selector.Media
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * PhotoRepository 类负责管理和获取照片数据。
 *
 * @param photoApi PhotoApi 实例，用于从远程 API 获取照片数据。
 * @param photoStorage PhotoStorage 实例，用于在本地存储中保存和检索照片数据。
 */
class PhotoRepository(
    private val photoApi: PhotoApi,
    private val photoStorage: PhotoStorage
) {
    private val scpoe = CoroutineScope(SupervisorJob())

    /**
     * 初始化数据的方法，启动协程以刷新和加载照片列表。
     */
    fun initalize() {
        scpoe.launch {
            // 初始化数据
            refresh()
        }
    }

    /**
     * 刷新数据的挂起函数，从 [photoApi] 获取最新数据并调用 [photoStorage] 保存。
     */
    suspend fun refresh() {
        photoStorage.saveObjects(photoApi.getData())
        //photoStorage.savePostObjects(photoApi.getData())
    }

    /**
     * 获取所有照片的流。
     * @return 一个 [Flow]，发出存储中的所有照片对象列表。
     */
    fun getObjects(): Flow<List<PhotoObject>> = photoStorage.getObjects()

    /**
     * 根据 ID 获取单个照片对象的流。
     * @param objectId 要检索的照片对象的 ID。
     * @return 一个 [Flow]，发出与给定 ID 匹配的单个照片对象。
     */
    fun getObjectByID(objectId: Int): Flow<PhotoObject?> = photoStorage.getObjectByID(objectId)


    suspend fun uploadPicture(post: Post): List<PostObject> {
        return photoApi.uploadPicture(post)
    }

}