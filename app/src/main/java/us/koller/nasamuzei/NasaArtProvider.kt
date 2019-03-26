package us.koller.nasamuzei

import android.content.Intent
import android.net.Uri
import com.google.android.apps.muzei.api.UserCommand
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

/**
 * provide loaded images via the Muzei Api: https://github.com/romannurik/muzei/wiki/API
 * */
class NasaArtProvider : MuzeiArtProvider() {

    companion object {
        const val AUTHORITY = "us.koller.nasamuzei"

        private const val COMMAND_ID_VIEW_ARCHIVE = 1
        private const val APOD_ARCHIVE_URL = "https://apod.nasa.gov/apod/archivepix.html"
    }

    private val workerUtil: NasaArtWorker.Companion.Util by lazy {
        NasaArtWorker.Companion.Util(context)
    }

    public override fun onLoadRequested(initial: Boolean) {
        /* enqueue new request to the worker via the util function */
        workerUtil.enqueueLoad()
    }

    override fun getCommands(artwork: Artwork): MutableList<UserCommand> {
        /* add user command to view the archive of all APOD pictures */
        return mutableListOf(UserCommand(COMMAND_ID_VIEW_ARCHIVE, context.getString(R.string.view_archive)))
    }

    override fun onCommand(artwork: Artwork, id: Int) {
        val context = context ?: return
        when (id) {
            COMMAND_ID_VIEW_ARCHIVE -> {
                /* open archive website */
                context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(APOD_ARCHIVE_URL))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}