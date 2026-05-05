package com.example.unnamedproject.features.settings

import com.example.unnamedproject.contracts.host.ThemeSettings
import com.example.unnamedproject.contracts.host.ThemeSettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository: ThemeSettingsRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val settingsFlow = MutableStateFlow(ThemeSettings())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { repository.settings } returns settingsFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `settings flow is exposed correctly`() = runTest {
        val expectedSettings = ThemeSettings(cornerRadiusDp = 12f)
        
        val viewModel = SettingsViewModel(repository)
        
        // Collect the flow to trigger stateIn
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { 
            viewModel.settings.collect {} 
        }
        
        settingsFlow.value = expectedSettings
        
        assertEquals(expectedSettings, viewModel.settings.value)
        job.cancel()
    }

    @Test
    fun `updateCornerRadius calls repository`() = runTest {
        coEvery { repository.updateCornerRadius(any()) } returns Unit
        val viewModel = SettingsViewModel(repository)
        
        viewModel.updateCornerRadius(15f)
        
        coVerify { repository.updateCornerRadius(15f) }
    }

    @Test
    fun `updateCoverWidth calls repository`() = runTest {
        coEvery { repository.updateCoverWidth(any()) } returns Unit
        val viewModel = SettingsViewModel(repository)
        
        viewModel.updateCoverWidth(120f)
        
        coVerify { repository.updateCoverWidth(120f) }
    }

    @Test
    fun `updateSelectedScale calls repository`() = runTest {
        coEvery { repository.updateSelectedScale(any()) } returns Unit
        val viewModel = SettingsViewModel(repository)
        
        viewModel.updateSelectedScale(1.25f)
        
        coVerify { repository.updateSelectedScale(1.25f) }
    }
}
