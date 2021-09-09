package ru.maggy.markersmap.repository

import androidx.lifecycle.map
import ru.maggy.markersmap.dao.MarkerDao
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.entity.MarkerEntity
import ru.maggy.markersmap.entity.toDto

class MarkerRepositoryImpl(
    private val dao: MarkerDao
): MarkerRepository {

    override val data = dao.getAll().map(List<MarkerEntity>::toDto)

    //private var markers = MutableLiveData(data)

    override suspend fun save(marker: Marker) {
            try {
                dao.insert(MarkerEntity.fromDto(marker))
            } catch (e: Exception) {
                e.printStackTrace()
            }

    }

    /*override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "now",
                    likedByMe = false,
                    likes = 0,
                    reposts = 0
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }*/

    override suspend fun deleteById(id: Int) {
       try {
           dao.deleteById(id)
       } catch (e: Exception) {
           e.printStackTrace()
       }
    }
}