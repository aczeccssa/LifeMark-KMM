package components.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import data.models.PhotoRepository
import data.models.PostObject
import kotlinx.coroutines.flow.Flow

class DetailScreenModel(private val mediaRepository: PhotoRepository): ScreenModel {
    fun getPost(postID: Int): Flow<PostObject?> =
        mediaRepository.getPostByID(postID)
}