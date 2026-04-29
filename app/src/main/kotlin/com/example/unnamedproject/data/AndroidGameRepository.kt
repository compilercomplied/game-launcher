package com.example.unnamedproject.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.models.Game

class AndroidGameRepository(private val context: Context) : GameRepository {
    override fun getInstalledGames(): List<Game> {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        return pm.queryIntentActivities(mainIntent, 0)
            .filter(::shouldIncludeApp)
            .map { resolveInfo ->
                Game(
                    name = resolveInfo.loadLabel(pm).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = Icons.Default.PlayArrow
                )
            }
            .sortedBy { it.name }
    }

    private fun shouldIncludeApp(resolveInfo: ResolveInfo): Boolean {
        val appInfo = resolveInfo.activityInfo.applicationInfo
        val packageName = appInfo.packageName
        
        // Exclude ourselves
        val isNotSelf = packageName != context.packageName
        
        // On many Android versions, side-loaded games aren't automatically 
        // categorized as ApplicationInfo.CATEGORY_GAME. 
        // For a launcher, we generally want to show all launchable apps 
        // that aren't system/pre-installed apps (unless we want to be a full launcher).
        
        val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val isUpdatedSystemApp = (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0

        return isNotSelf && (!isSystemApp || isUpdatedSystemApp)
    }
}
