package us.koller.nasamuzei

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.*
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class NasaArtWorkerTest {

    @Test
    @Throws(Exception::class)
    fun testDoWork() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        /* create request */
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val request = OneTimeWorkRequestBuilder<NasaArtWorker>()
                .setConstraints(constraints)
                .build()

        /* init helper */
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        /* get WorkManager instance */
        val workManager = WorkManager.getInstance()
        /* get testDriver */
        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        /* enqueue request */
        workManager.enqueue(request).result.get()
        /* trigger exec of work, regardless of constraints */
        testDriver.setAllConstraintsMet(request.id)
        /* work done -> retrieve workInfo */
        val workInfo = workManager.getWorkInfoById(request.id).get()
        /* check if work succeeded */
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
    }
}