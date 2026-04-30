package com.example.unnamedproject.data

import com.example.unnamedproject.data.local.GameMetadataDao
import com.example.unnamedproject.data.local.GameMetadataEntity
import com.example.unnamedproject.models.Game
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import org.junit.Before
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith
import androidx.test.core.app.ApplicationProvider
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowPackageManager

@RunWith(RobolectricTestRunner::class)
class AndroidGameRepositoryMetadataTest {

    private lateinit var context: Context
    private lateinit var packageManager: ShadowPackageManager
    private lateinit var metadataDao: GameMetadataDao
    private lateinit var repository: AndroidGameRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        packageManager = Shadows.shadowOf(context.packageManager)
        metadataDao = mockk()
        repository = AndroidGameRepository(context, metadataDao)
    }

    @Test
    fun `getInstalledGames should merge metadata from DAO`() = runBlocking {
        // Arrange
        val packageName = "com.example.game"
        val gameName = "Test Game"
        val gameInfo = createResolveInfo(packageName, gameName).apply {
            activityInfo.applicationInfo.category = ApplicationInfo.CATEGORY_GAME
        }
        
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        packageManager.addResolveInfoForIntent(intent, gameInfo)

        val metadata = GameMetadataEntity(
            packageName = packageName,
            coverPath = "/path/to/cover.jpg",
            bannerPath = "/path/to/banner.jpg",
            lastUpdated = 12345L
        )
        coEvery { metadataDao.getAllMetadata() } returns listOf(metadata)

        // Act
        val games = repository.getInstalledGames()

        // Assert
        assertEquals(1, games.size)
        val game = games[0]
        assertEquals(packageName, game.packageName)
        assertEquals("/path/to/cover.jpg", game.coverPath)
        assertEquals("/path/to/banner.jpg", game.bannerPath)
    }

    @Test
    fun `getInstalledGames should handle missing metadata`() = runBlocking {
        // Arrange
        val packageName = "com.example.game"
        val gameInfo = createResolveInfo(packageName, "Test Game").apply {
            activityInfo.applicationInfo.category = ApplicationInfo.CATEGORY_GAME
        }
        
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        packageManager.addResolveInfoForIntent(intent, gameInfo)

        coEvery { metadataDao.getAllMetadata() } returns emptyList()

        // Act
        val games = repository.getInstalledGames()

        // Assert
        assertEquals(1, games.size)
        assertNull(games[0].coverPath)
        assertNull(games[0].bannerPath)
    }

    private fun createResolveInfo(packageName: String, label: String): ResolveInfo {
        val applicationInfo = ApplicationInfo().apply {
            this.packageName = packageName
            this.flags = ApplicationInfo.FLAG_INSTALLED
            this.name = label
        }
        val activityInfo = ActivityInfo().apply {
            this.packageName = packageName
            this.name = "MainActivity"
            this.applicationInfo = applicationInfo
        }
        return ResolveInfo().apply {
            this.activityInfo = activityInfo
            this.nonLocalizedLabel = label
        }
    }
}
