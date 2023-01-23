package com.atilsamancioglu.artbookhilttesting.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.atilsamancioglu.artbookhilttesting.R
import com.atilsamancioglu.artbookhilttesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

// MediumTest: 외부와 통신 / 상호작용이 거의 없는 테스트
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // ArtToArtDetails로 이동하는 기능을 테스트
    @Test
    fun testNavigationFromArtToArtDetails() {
        // navController는 실제로 동작해야하기 때문에 Fake가 아닌 Mockito로 객체를 만들어준다
        val navController = Mockito.mock(NavController::class.java)

        // 구글에서 만들어준 launchFragmentInHiltContainer를 커스텀 해서 사용
        launchFragmentInHiltContainer<ArtFragment>(factory = fragmentFactory) {
            // ArtFragment에서 Navigation을 정의
            Navigation.setViewNavController(requireView(), navController)
        }

        // UI 컨트롤을 위해 Espresso를 사용한다
        // onView로 View를 지정
        // perform으로 동작(action) 지정
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())

        // 클릭으로 인해 화면이 바뀌었는지 확인한다
        Mockito.verify(navController).navigate(
            ArtFragmentDirections.actionArtFragmentToArtDetailsFragment()
        )
    }
}
