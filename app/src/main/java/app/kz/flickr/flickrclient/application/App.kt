package app.kz.flickr.flickrclient.application

import android.app.Application
import android.provider.SearchRecentSuggestions
import app.kz.flickr.flickrclient.adapter.GridAdapter
import app.kz.flickr.flickrclient.logic.service.FlickrService
import app.kz.flickr.flickrclient.logic.helper.ServiceHelper
import app.kz.flickr.flickrclient.provider.SuggestionProvider
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.REST

class App : Application(){



    companion object {
        var app: App? = null
    }
    var kodein: Kodein? = null
    val apiKey = "d753282745a68a59c60a0d564bfab2dd"
    val sharedSecret = "9b1129000b302f4a"


    override fun onCreate() {
        super.onCreate()
        app = this;
        initKodein()
    }



    private fun initKodein(){
        kodein = Kodein {
            bind<Flickr>() with singleton { Flickr(apiKey, sharedSecret, REST()) }
            bind<FlickrService>() with singleton { FlickrService() }
            bind<ServiceHelper>() with singleton { ServiceHelper() }
            bind<GridAdapter>() with singleton { GridAdapter(applicationContext) }
            bind<SearchRecentSuggestions>() with singleton { SearchRecentSuggestions(applicationContext, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE) }
        }
    }

}