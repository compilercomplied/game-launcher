package com.example.unnamedproject.features.hiddengames

import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HiddenGamesViewModelTest {

    private val repository: GameRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads only hidden games`() = runTest {
        val game1 = mockk<Game> { every { isHidden } returns true }
        val game2 = mockk<Game> { every { isHidden } returns false }
        coEvery { repository.getInstalledGames() } returns listOf(game1, game2)

        val viewModel = HiddenGamesViewModel(repository)

        assertEquals(1, viewModel.uiState.value.hiddenGames.size)
        assertEquals(listOf(game1), viewModel.uiState.value.hiddenGames)
    }

    @Test
    fun `unhideGame updates repository and reloads list`() = runTest {
        val game = mockk<Game> {
            every { packageName } returns "com.example.game"
            every { isHidden } returns true
        }
        coEvery { repository.getInstalledGames() } returns listOf(game)
        coEvery { repository.setGameHiddenStatus("com.example.game", false) } returns Unit

        val viewModel = HiddenGamesViewModel(repository)
        
        // After unhiding, it should be empty
        coEvery { repository.getInstalledGames() } returns emptyList()

        viewModel.unhideGame(game)

        coVerify { repository.setGameHiddenStatus("com.example.game", false) }
        assertEquals(0, viewModel.uiState.value.hiddenGames.size)
    }
}
