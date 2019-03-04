package us.koller.nasamuzei

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.apps.muzei.api.UserCommand
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider
import com.google.android.apps.muzei.api.provider.ProviderContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NasaArtProvider : MuzeiArtProvider() {

    companion object {
        private const val COMMAND_ID_VIEW_ARCHIVE = 1

        private const val CURRENT_APOD_URL = "https://apod.nasa.gov/apod/astropix.html"
        private const val APOD_ARCHIVE_URL = "https://apod.nasa.gov/apod/archivepix.html"
    }

    private var disposable: Disposable? = null

    override fun onLoadRequested(initial: Boolean) {
        val context = context ?: return

        val authority = context.getString(R.string.muzei_art_provider_authority)
        val apiKey = context.getString(R.string.nasa_api_key)

        disposable = NasaService.apod(apiKey, null /* Type: YYYY-MM-DD*/)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { res ->
                            val providerClient = ProviderContract.getProviderClient(context, authority)
                            providerClient.setArtwork(Artwork().apply {
                                token = res.url
                                title = res.title
                                byline = res.copyright ?: "Public Domain"
                                attribution = res.explanation
                                persistentUri = Uri.parse(res.url)
                                webUri = Uri.parse(CURRENT_APOD_URL)
                            })
                        }
                )
                /* error */
                { err -> Log.d("NasaArtProvider", "apod(), error: " + err.message) }
    }

    /* add user command to view the archive of all APOD pictures */
    override fun getCommands(artwork: Artwork): MutableList<UserCommand> {
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