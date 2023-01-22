package com.atilsamancioglu.artbookhilttesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atilsamancioglu.artbookhilttesting.model.ImageResponse
import com.atilsamancioglu.artbookhilttesting.roomdb.Art
import com.atilsamancioglu.artbookhilttesting.util.Resource

/**
 * 해당 패키지(test)에서 실시하는 테스트는 Local Unit 테스트이며 에뮬레이터가 필요하지 않다
 * viewModel 테스트에 사용하기 위한 Fake Repository 클래스
 * 이 Repository의 목적은 viewModel의 기능을 테스트하기 위함이기 때문에
 * 똑같이 ArtRepositoryInterface을 상속받으며
 * 실제 Room을 사용하지 않지만 비슷한 동작을 구현했다
 * 실제 Room을 사용하지 않기 때문에 실제 Repository 처럼 dependency를 구현할 필요가 없다
 * */
class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)

    // Room을 테스트 하는 부분은 직접 데이터를 SQL에 넣을 수 없으니 list에 넣고 빼는 형식으로 테스트 했다
    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshLiveData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshLiveData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    // 이미지를 찾는 테스트는 의미 없는 ImageResponse를 반환하도록 했다
    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(), 0, 0))
    }

    private fun refreshLiveData() {
        artsLiveData.postValue(arts)
    }
}
