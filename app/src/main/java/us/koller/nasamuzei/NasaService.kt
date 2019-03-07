package us.koller.nasamuzei

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for NASA APOD API: https://api.nasa.gov/api.html#apod
 * */
interface NasaService {

    companion object {

        /**
         * static factory method to construct NasaService instance
         * */
        fun createService(): NasaService {
            /* instantiate service */
            return Retrofit.Builder()
                    /* add RxJava */
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    /* add Gson */
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.nasa.gov/planetary/")
                    .build()
                    .create(NasaService::class.java)
        }
    }

    /**
     *  api call to get the current APOD image
     *  */
    @GET("apod")
    fun apod(@Query("api_key") api_key: String, @Query("date") date: String?): Single<ApodImage>

    /**
     * simple data-class to store the image retrieved by the APOD-Api
     *
     * ApodImage sample data:
     * {
        "date": "2019-02-28",
        "explanation": "On January 1, New Horizons [...]",
        "hdurl": "https://apod.nasa.gov/apod/image/1902/ultima-thule-1-ca06_022219.png",
        "media_type": "image",
        "service_version": "v1",
        "title": "Sharpest Ultima Thule",
        "url": "https://apod.nasa.gov/apod/image/1902/ultima-thule-1-ca06_022219_1024.jpg"
        }
     * */
    data class ApodImage(
            val date: String,
            val explanation: String,
            val hdurl: String,
            val media_type: String,
            val service_version: String,
            val title: String,
            val url: String,
            val copyright: String?)
}

