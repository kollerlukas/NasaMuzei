package us.koller.nasamuzei

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.TestDriver
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderClient
import io.reactivex.Single
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule



/**
 * simple test class for @see NasaArtWorker
 * */
@RunWith(MockitoJUnitRunner::class)
class NasaArtWorkerTest {

    companion object {
        /* mock input data */
        private val image = NasaService.ApodImage("2019-02-28",
                "On January 1, New Horizons [...]",
                "https://apod.nasa.gov/apod/image/1902/ultima-thule-1-ca06_022219.png",
                "image",
                "v1",
                "Sharpest Ultima Thule",
                "https://apod.nasa.gov/apod/image/1902/ultima-thule-1-ca06_022219_1024.jpg")
        /* mock output data */
        private val artwork = Artwork().apply {
            token = image.url
            title = image.title
            byline = "Public Domain"
            attribution = image.explanation
            persistentUri = Uri.parse(image.url)
            webUri = Uri.parse(NasaArtWorker.CURRENT_APOD_URL)
        }
    }

    /* mocks */
    @Mock
    private lateinit var nasaService: NasaService
    @Mock
    private lateinit var client: ProviderClient

    private lateinit var context: Context

    private lateinit var workerUtil: NasaArtWorker.Companion.Util
    private lateinit var workManager: WorkManager
    private lateinit var testDriver: TestDriver

    @Before
    fun setUp() {
        /* get instrumented context */
        context = ApplicationProvider.getApplicationContext<Context>()

        workerUtil = NasaArtWorker.Companion.Util(context, nasaService, client)

        /* init helper */
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        /* get WorkManager instance */
        workManager = WorkManager.getInstance()
        /* get testDriver */
        testDriver = WorkManagerTestInitHelper.getTestDriver()
    }

    /**
     * test for NasaArtWorker.doWork()
     * */
    @Test
    fun testDoWork1() {

        // -------------------------------------------------------- //
        // TODO: fix tests (issues with custom WorkerFactroy, etc.) //
        // -------------------------------------------------------- //

        /* init nasa service mock */
        Mockito.`when`(nasaService.apod(any(), any())).thenReturn(Single.just(image))

        /* enqueue work with mock input */
        //val request = workerUtil.enqueueLoad()
        /* trigger exec of work, regardless of constraints */
        //testDriver.setAllConstraintsMet(request.id)

        /* work done -> retrieve workInfo */
        //val workInfo = workManager.getWorkInfoById(request.id).get()
        /* check if work succeeded */
        //assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        /* verify artwork was set */
        //Mockito.verify(client).setArtwork(artwork)
    }
}