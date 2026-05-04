package com.example.unnamedproject.features.launcher

import androidx.work.WorkManager
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LauncherViewModelTest {

    private val repository: GameRepository = mockk()
    private val workManager: WorkManager = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { workManager.enqueue(any<androidx.work.WorkRequest>()) } returns mockk()
        every { workManager.getWorkInfoByIdFlow(any()) } returns emptyFlow()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has first game selected`() = runTest {
        val games = listOf(mockk<Game>(), mockk<Game>())
        coEvery { repository.getInstalledGames() } returns games

        val viewModel = LauncherViewModel(repository, workManager)

        assertEquals(0, viewModel.uiState.value.selectedIndex)
        assertEquals(games, viewModel.uiState.value.games)
    }

    @Test
    fun `onGameSelected updates selectedIndex`() = runTest {
        coEvery { repository.getInstalledGames() } returns emptyList()
        val viewModel = LauncherViewModel(repository, workManager)

        viewModel.onGameSelected(2)

        assertEquals(2, viewModel.uiState.value.selectedIndex)
    }

    @Test
    fun `launchGame calls repository`() = runTest {
        coEvery { repository.getInstalledGames() } returns emptyList()
        val viewModel = LauncherViewModel(repository, workManager)
        val game = mockk<Game> {
            every { packageName } returns "com.example.game"
        }
        every { repository.launchGame(any()) } returns Unit

        viewModel.launchGame(game)

        io.mockk.verify { repository.launchGame("com.example.game") }
    }
}
