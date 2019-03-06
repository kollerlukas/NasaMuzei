package us.koller.nasamuzei

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import io.reactivex.Single

class NasaArtWorker(context: Context, workerParams: WorkerParameters) : RxWorker(context, workerParams) {

    companion object {

        class Util {
            internal fun enqueueLoad() {
                val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                val workManager = WorkManager.getInstance()
                workManager.enqueue(OneTimeWorkRequestBuilder<NasaArtWorker>()
                        .setConstraints(constraints)
                        .build())
            }
        }

        const val CURRENT_APOD_URL = "https://apod.nasa.gov/apod/astropix.html"
    }

    override fun createWork(): Single<Result> {
        val apiKey = applicationContext.getString(R.string.nasa_api_key)

        return NasaService.createService().apod(apiKey, null /* Type: YYYY-MM-DD*/)
                .doOnSuccess { res ->
                    val client = ProviderContract.getProviderClient(applicationContext, NasaArtProvider.AUTHORITY)
                    val artwork = Artwork().apply {
                        token = res.url
                        title = res.title
                        byline = res.copyright ?: "Public Domain"
                        attribution = res.explanation
                        persistentUri = Uri.parse(res.url)
                        webUri = Uri.parse(CURRENT_APOD_URL)
                    }
                    client.setArtwork(artwork)
                }.map { a -> Result.success(Data.Builder().putString("url", a.url).build()) }
                .onErrorReturn { Result.failure() }
    }
}