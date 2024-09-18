package viewmodel

import androidx.lifecycle.ViewModel
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDateTime

class ProfileScreenViewModel(private val id: Uuid = uuid4()) : ViewModel() {
    companion object {
        private const val TAG = "ProfileScreenViewModel"
    }

    init {
        Napier.i("${LocalDateTime.now()} - Account screen view model online: $id", tag = TAG)
    }

    override fun onCleared() {
        Napier.i("${LocalDateTime.now()} - Home screen view model offline: $id", tag = TAG)
        super.onCleared()
    }
}