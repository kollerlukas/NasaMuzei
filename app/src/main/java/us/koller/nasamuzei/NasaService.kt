package us.koller.nasamuzei

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    companion object {
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

    /* api call to get the current APOD */
    @GET("apod")
    fun apod(@Query("api_key") api_key: String, @Query("date") date: String?): Single<ApodImage>

    /*
    * ApodImage sample data:
    * {
        "date": "2019-02-28",
        "explanation": "On January 1, New Horizons swooped to within 3,500 kilometers of the Kuiper Belt world known as Ultima Thule. That's about 3 times closer than its July 2015 closest approach to Pluto. The spacecraft's unprecedented feat of navigational precision, supported by data from ground and space-based observing campaigns, was accomplished 6.6 billion kilometers (over 6 light-hours) from planet Earth. Six and a half minutes before closest approach to Ultima Thule it captured the nine frames used in this composite image. The most detailed picture possible of the farthest object ever explored, the image has a resolution of about 33 meters per pixel, revealing intriguing bright surface features and dark shadows near the terminator. A primitive Solar System object, Ultima Thule's two lobes combine to span just 30 kilometers. The larger lobe, referred to as Ultima, is recently understood to be flattened like a fluffy pancake, while the smaller, Thule, has a shape that resembles a dented walnut.",
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

