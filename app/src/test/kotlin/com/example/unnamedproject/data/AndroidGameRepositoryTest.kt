package com.example.unnamedproject.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.unnamedproject.data.local.GameMetadataDao
import com.example.unnamedproject.data.local.GameMetadataEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowPackageManager

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class AndroidGameRepositoryTest {

    private lateinit var context: Context
    private lateinit var packageManager: ShadowPackageManager
    private lateinit var repository: AndroidGameRepository
    private lateinit var fakeDao: GameMetadataDao

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        packageManager = Shadows.shadowOf(context.packageManager)
        fakeDao = object : GameMetadataDao {
            override suspend fun getAllMetadata(): List<GameMetadataEntity> = emptyList()
            override suspend fun getMetadataForPackage(packageName: String): GameMetadataEntity? = null
            override suspend fun insertMetadata(metadata: GameMetadataEntity) {}
            override suspend fun deleteMetadataForPackage(packageName: String) {}
        }
        repository = AndroidGameRepository(context, fakeDao)
    }

    @Test
    fun `getInstalledGames should return only apps categorized as games`() = runBlocking {
        // Arrange
        val modernGame = createResolveInfo("com.example.modern.game", "Modern Game").apply {
            activityInfo.applicationInfo.category = ApplicationInfo.CATEGORY_GAME
        }
        val legacyGame = createResolveInfo("com.example.legacy.game", "Legacy Game").apply {
            activityInfo.applicationInfo.flags = activityInfo.applicationInfo.flags or ApplicationInfo.FLAG_IS_GAME
        }
        val regularApp = createResolveInfo("com.example.regular.app", "Regular App")
        val selfApp = createResolveInfo(context.packageName, "Self App").apply {
            activityInfo.applicationInfo.category = ApplicationInfo.CATEGORY_GAME
        }

        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        packageManager.addResolveInfoForIntent(intent, modernGame)
        packageManager.addResolveInfoForIntent(intent, legacyGame)
        packageManager.addResolveInfoForIntent(intent, regularApp)
        packageManager.addResolveInfoForIntent(intent, selfApp)

        // Act
        val games = repository.getInstalledGames()

        // Assert
        assertEquals(2, games.size)
        assertTrue(games.any { it.packageName == "com.example.modern.game" })
        assertTrue(games.any { it.packageName == "com.example.legacy.game" })
    }

    @Test
    fun `launchGame should start activity with launch intent`() {
        // Arrange
        val packageName = "com.example.game"
        val label = "Game Label"
        val componentName = "com.example.game.MainActivity"
        
        val intentToResolve = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(packageName)
        }
        
        val resolveInfo = createResolveInfo(packageName, label).apply {
            activityInfo.name = componentName
        }
        
        packageManager.addResolveInfoForIntent(intentToResolve, resolveInfo)

        // Act
        repository.launchGame(packageName)

        // Assert
        val nextStartedActivity = Shadows.shadowOf(context as android.app.Application).nextStartedActivity
        assertEquals(packageName, nextStartedActivity.getPackage())
        assertEquals(Intent.ACTION_MAIN, nextStartedActivity.action)
        assertTrue((nextStartedActivity.flags and Intent.FLAG_ACTIVITY_NEW_TASK) != 0)
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
