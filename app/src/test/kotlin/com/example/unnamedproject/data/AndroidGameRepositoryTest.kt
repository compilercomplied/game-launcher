package com.example.unnamedproject.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
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

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        packageManager = Shadows.shadowOf(context.packageManager)
        repository = AndroidGameRepository(context)
    }

    @Test
    fun `getInstalledGames should return only apps categorized as games`() {
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
