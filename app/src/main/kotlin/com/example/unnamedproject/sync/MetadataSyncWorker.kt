package com.example.unnamedproject.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.unnamedproject.contracts.host.GameRepository
import com.example.unnamedproject.data.local.GameMetadataDao
import com.example.unnamedproject.data.local.GameMetadataEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@HiltWorker
class MetadataSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: GameRepository,
    private val metadataDao: GameMetadataDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val games = repository.getInstalledGames()
            val existingMetadata = metadataDao.getAllMetadata().associateBy { it.packageName }

            val metadataDir = File(context.filesDir, "metadata")
            if (!metadataDir.exists()) {
                metadataDir.mkdirs()
            }

            for (game in games) {
                if (!existingMetadata.containsKey(game.packageName)) {
                    fetchAndStoreMetadata(game.packageName, metadataDir)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("MetadataSyncWorker", "Error syncing metadata", e)
            Result.retry()
        }
    }

    private suspend fun fetchAndStoreMetadata(packageName: String, metadataDir: File) {
        try {
            val url = "https://play.google.com/store/apps/details?id=$packageName"
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            // Try to find banner (Feature Graphic)
            val bannerUrl = doc.select("meta[property=og:image]").attr("content")
            Log.d("MetadataSyncWorker", "Banner URL for $packageName: $bannerUrl")
            
            // Try to find cover (usually the first large image or app icon if vertical not found)
            // Google Play often has a specific image for the icon/cover
            var coverUrl = doc.select("img[alt=Icon]").attr("abs:src")
            if (coverUrl.isEmpty()) {
                coverUrl = doc.select("img[src*='googleusercontent.com/']").firstOrNull()?.attr("abs:src") ?: ""
            }
            Log.d("MetadataSyncWorker", "Cover URL for $packageName: $coverUrl")

            var localBannerPath: String? = null
            var localCoverPath: String? = null

            if (bannerUrl.isNotEmpty()) {
                val bannerFile = File(metadataDir, "${packageName}_banner.jpg")
                if (downloadFile(bannerUrl, bannerFile)) {
                    localBannerPath = bannerFile.absolutePath
                    Log.d("MetadataSyncWorker", "Downloaded banner to $localBannerPath")
                }
            }

            if (coverUrl.isNotEmpty()) {
                val coverFile = File(metadataDir, "${packageName}_cover.jpg")
                if (downloadFile(coverUrl, coverFile)) {
                    localCoverPath = coverFile.absolutePath
                    Log.d("MetadataSyncWorker", "Downloaded cover to $localCoverPath")
                }
            }

            if (localBannerPath != null || localCoverPath != null) {
                metadataDao.insertMetadata(
                    GameMetadataEntity(
                        packageName = packageName,
                        bannerPath = localBannerPath,
                        coverPath = localCoverPath,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("MetadataSyncWorker", "Failed to fetch metadata for $packageName", e)
        }
    }

    private fun downloadFile(url: String, destination: File): Boolean {
        return try {
            URL(url).openStream().use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("MetadataSyncWorker", "Failed to download $url", e)
            false
        }
    }
}
