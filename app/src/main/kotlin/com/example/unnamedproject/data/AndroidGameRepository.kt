package com.example.unnamedproject.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.data.local.GameMetadataDao
import com.example.unnamedproject.models.Game

class AndroidGameRepository(
    private val context: Context,
    private val metadataDao: GameMetadataDao
) : GameRepository {
    override suspend fun getInstalledGames(): List<Game> {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val metadataMap = metadataDao.getAllMetadata().associateBy { it.packageName }
        
        return pm.queryIntentActivities(mainIntent, 0)
            .filter(::shouldIncludeApp)
            .map { resolveInfo ->
                val packageName = resolveInfo.activityInfo.packageName
                val metadata = metadataMap[packageName]
                Game(
                    name = resolveInfo.loadLabel(pm).toString(),
                    packageName = packageName,
                    icon = Icons.Default.PlayArrow,
                    coverPath = metadata?.coverPath,
                    bannerPath = metadata?.bannerPath
                )
            }
            .sortedBy { it.name }
    }

    private fun shouldIncludeApp(resolveInfo: ResolveInfo): Boolean {
        val appInfo = resolveInfo.activityInfo.applicationInfo
        val packageName = appInfo.packageName
        
        // Exclude ourselves
        if (packageName == context.packageName) return false
        
        // Check if the app is categorized as a game
        val isLegacyGame = (appInfo.flags and ApplicationInfo.FLAG_IS_GAME) != 0
        val isModernGame = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && 
                          appInfo.category == ApplicationInfo.CATEGORY_GAME

        return isLegacyGame || isModernGame
    }
}
