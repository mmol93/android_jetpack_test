package com.atilsamancioglu.artbookhilttesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.atilsamancioglu.artbookhilttesting.MainCoroutineRule
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.atilsamancioglu.artbookhilttesting.repo.FakeArtRepository
import com.atilsamancioglu.artbookhilttesting.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * viewModel에 대한 테스트
 * */
@ExperimentalCoroutinesApi
class ArtViewModelTest {

    // 이 안에 있는 모든 테스트를 하나의 스레드에서 실시
    // viewModel 테스트라면 반드시 설정해야함
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    /*
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

     */

    // 똑같이 사용중인 viewModel에 대한 변수를 선언한다
    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup() {
        // 실제 Repository처럼 Room을 사용하지 않고 Test 하기 때문에
        // 의존성을 부여할 필요가 없는 ViewModel을 만들 수 있다
        // 간단하게 viewModel에 있는 기능을 테스트 하기 위함이다
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error`() {
        viewModel.makeArt("Mona Lisa", "Da Vinci", "")

        val value = viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert art without name returns error`() {
        viewModel.makeArt("", "Da Vinci", "1500")

        val value = viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error`() {
        viewModel.makeArt("Mona Lisa", "", "1500")

        val value = viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}
