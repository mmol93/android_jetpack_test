package com.atilsamancioglu.artbookhilttesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

/**
 * Room은 Context를 사용해야한다 = instrumented test = 실제 애뮬레이터 기기를 사용한 테스트가 필요하다
 * 그렇기 때문에 (test) 패키지가 아닌 (androidTest) 패키지에서 테스트를 실시한다
 * 테스트를 실행해보면 에뮬레이터가 실행되서 테스트를 하는 것을 알 수 있다
 *
 * */
// @smallTest: 200ms 미만의 짧은 테스트를 의미한다 = unit 테스트와 비슷함
@SmallTest
@ExperimentalCoroutinesApi
// Test에서 Hilt를 사용할거라는 의미
@HiltAndroidTest
class ArtDaoTest {

    // @get:Rule - 테스트 룰을 지정한다
    // InstantTaskExecutorRule:
    // 백그라운드 작업과 연관된 모든 아키텍처 컴포넌트들을 같은(한개의) 스레드에서 실행되게 해서 테스트 결과들이 동기적으로 실행되게 해준다
    // 즉, 모든 작업들을 동기적(synchronous) 하게 해준다
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // HiltAndroidRule: 테스트에서 Hilt를 사용할 수 있게 해준다
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    private lateinit var database: ArtDatabase

    private lateinit var dao: ArtDao

    // Test가 시작되기 전에 실행됨
    @Before
    fun setup() {
        // inMemoryDatabaseBuilder: Room DB를 Memory에서 구축한다 = 테스트를 위한 임시 DB를 생성한다
        // allowMainThreadQueries: mainThread에서 실시할 수 있게 해준다
        // 하지만 이러한 방법을 사용하면 @Test를 할 때마다 database 객체를 만들게 된다
        /*
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),ArtDatabase::class.java)
            .allowMainThreadQueries() //this is a test case, we don't want other thread pools
            .build()
         */

        hiltRule.inject()
        dao = database.artDao()
    }

    // Test가 끝난 후 실행됨
    @After
    fun teardown() {
        database.close()
    }


    // 실제 테스트 부분
    @Test
    // insert는 suspend 함수이기 때문에 runTest 안에서 실시한다
    fun insertArtTesting() = runTest {
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1700, "test.com", 1)
        dao.insertArt(exampleArt)

        // ArtsData를 DB에서 가져온다
        val artDataList = dao.getArtsData().getOrAwaitValue()

        // 가져온 데이터에 exampleArt이 있는지 확인한다
        assertThat(artDataList).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runTest {
        val exampleArt = Art("Mona Lisa", "Da Vinci", 1700, "test.com", 1)
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)

        val list = dao.getArtsData().getOrAwaitValue()

        // 삭제한 데이터가 exampleArt에 없는지 확인한다
        assertThat(list).doesNotContain(exampleArt)
    }
}
