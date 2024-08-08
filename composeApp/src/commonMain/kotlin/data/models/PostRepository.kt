package data.models

import coil3.network.HttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okio.IOException
import screens.merge.LoginModel
import screens.merge.Resource

/**
 * PhotoRepository 类负责管理和获取照片数据。
 *
 * @param postApi PhotoApi 实例，用于从远程 API 获取照片数据。
 * @param postStorage PhotoStorage 实例，用于在本地存储中保存和检索照片数据。
 */
class PostRepository(
    private val postApi: PostApi,
    private val postStorage: PostStorage
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
     * 刷新数据的挂起函数，从 [postApi] 获取最新数据并调用 [postStorage] 保存。
     */
    suspend fun refresh() {
        postStorage.saveObjects(postApi.getData())
        //photoStorage.savePostObjects(photoApi.getData())
    }

    /**
     * 获取所有照片的流。
     * @return 一个 [Flow]，发出存储中的所有照片对象列表。
     */
    fun getObjects(): Flow<List<PhotoObject>> = postStorage.getObjects()

    /**
     * 根据 ID 获取单个照片对象的流。
     * @param objectId 要检索的照片对象的 ID。
     * @return 一个 [Flow]，发出与给定 ID 匹配的单个照片对象。
     */
    fun getObjectByID(objectId: Int): Flow<PhotoObject?> = postStorage.getObjectByID(objectId)

    fun getPostByID(postID: Int): Flow<PostObject?> = postStorage.getPostByID(postID)

    suspend fun uploadPicture(post: Post): List<PostObject> {
        return postApi.uploadPicture(post)
    }


    fun getUserLogin(email:String, password:String):Flow<Resource<LoginModel>> = flow {

        try {
            println("PostRepository -> Loading")
            emit(Resource.Loading())

            val process = postApi.userLogin(email, password)
            println("PostRepository -> $process")
            coroutineScope {
                println("PostRepository -> Success")
                emit(Resource.Success(process))
            }

        } catch (e: HttpException){
            println("PostRepository -> e: HttpException")
            emit(Resource.Error("e: HttpException"))
        } catch (e: IOException) {
            println("PostRepository -> e: IOException")
            // TODO网络链接测试
            emit(Resource.Internet("e: IOException"))
        }
    }

}