package us.koller.nasamuzei

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderClient
import com.google.android.apps.muzei.api.provider.ProviderContract
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * Factory to instantiate NasaArtWorker with params
 * */
class NasaWorkerFactory(
        private val nasaService: NasaService,
        private val client: ProviderClient) : WorkerFactory() {

    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        return NasaArtWorker(nasaService, client, appContext, workerParameters)
    }
}

/**
 *  worker to load images for @see NasaArtProvider
 * */
class NasaArtWorker(
        private val nasaService: NasaService,
        private val client: ProviderClient,
        context: Context,
        workerParams: WorkerParameters) : RxWorker(context, workerParams) {

    companion object {

        /* url to the current APOD */
        const val CURRENT_APOD_URL = "https://apod.nasa.gov/apod/astropix.html"
        /* Input Data Key */
        const val INPUT_DATA_DATE_KEY = "us.koller.nasamuzei.NasaArtWorker.INPUT_DATA_DATE_KEY"
        const val INPUT_DATA_INITAL_KEY = "us.koller.nasamuzei.NasaArtWorker.INPUT_DATA_INITAL_KEY"

        /**
         * helper class to easier use NasaArtWorker
         * */
        class Util(
                context: Context,
                nasaService: NasaService,
                client: ProviderClient) {

            init {
                /* create worker factory */
                val factory = NasaWorkerFactory(nasaService, client)
                val config = Configuration.Builder().setWorkerFactory(factory).build()
                /* init WorkManager */
                WorkManager.initialize(context, config)
            }

            constructor(context: Context) : this(context, NasaService.createService(),
                    ProviderContract.getProviderClient(context, NasaArtProvider.AUTHORITY))

            /**
             * util method to easier enqueue new work request
             * */
            fun enqueueLoad(date: String? = null, initial: Boolean = false): WorkRequest {
                /* create constraints */
                val constraints = Constraints.Builder()
                        /* only download images over the network */
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                /* create input data */
                val inputData = Data.Builder()
                        /* add date input data */
                        .putString(INPUT_DATA_DATE_KEY, date)
                        .putBoolean(INPUT_DATA_INITAL_KEY, initial)
                        .build()
                /* build worker */
                val request = OneTimeWorkRequestBuilder<NasaArtWorker>()
                        .setInputData(inputData)
                        .setConstraints(constraints)
                        .build()
                /* enqueue work */
                WorkManager.getInstance().enqueue(request)
                /* return request */
                return request
            }
        }
    }

    override fun createWork(): Single<Result> {
        /* get api key from resources */
        val apiKey = applicationContext.getString(R.string.nasa_api_key)
        /* get input data: date (date-format: YYYY-MM-DD) */
        val date = inputData.getString(INPUT_DATA_DATE_KEY)
        /* TODO: handle case inital=true and current APOD is a video */
        val initial = inputData.getBoolean(INPUT_DATA_INITAL_KEY, false)
        /* call api */
        return nasaService.apod(apiKey, date)
                /* set 5s timeout */
                .timeout(5, TimeUnit.SECONDS)
                .doOnSuccess {
                    if (it.media_type == "image") {
                        /* found image => set as artwork */
                        setApodImageAsArtwork(it)
                    }
                }
                /* only success when received an image */
                .map { if (it.media_type == "image") Result.success() else Result.failure() }
                /* something went wrong */
                .onErrorReturn { Result.failure() }
    }

    /**
     * helper method to set a Apod-Image received from api call as Muzei artwork
     * */
    private fun setApodImageAsArtwork(image: NasaService.ApodImage) {
        /* create new artwork instance */
        val artwork = Artwork().apply {
            token = image.url
            title = image.title
            byline = image.copyright ?: "Public Domain"
            attribution = image.explanation
            persistentUri = Uri.parse(image.url)
            webUri = Uri.parse(CURRENT_APOD_URL)
        }
        /* set new artwork */
        client.setArtwork(artwork)
    }
}