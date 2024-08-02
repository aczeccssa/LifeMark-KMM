package components.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import data.models.PostRepository
import data.models.PostObject
import kotlinx.coroutines.flow.Flow

class DetailScreenModel(private val mediaRepository: PostRepository): ScreenModel {
    fun getPost(postID: Int): Flow<PostObject?> =
        mediaRepository.getPostByID(postID)
}