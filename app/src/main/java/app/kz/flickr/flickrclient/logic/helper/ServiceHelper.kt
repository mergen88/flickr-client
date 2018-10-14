package app.kz.flickr.flickrclient.logic.helper

import app.kz.flickr.flickrclient.application.App
import app.kz.flickr.flickrclient.interfaces.ImagesLoader
import app.kz.flickr.flickrclient.logic.service.FlickrService
import com.github.salomonbrys.kodein.instance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ServiceHelper {


    private var imagesLoader: ImagesLoader? = null

    fun getPhotos(text: String?, page: Int) {
        var service = App.app!!.kodein!!.instance<FlickrService>()
        service.getObservable(text, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    photoList ->
                    if(photoList!=null && imagesLoader!=null){
                        imagesLoader!!.onLoadComplete(photoList)
                    }
                },{
                    error ->
                    imagesLoader!!.onError(error)
                })

    }


    fun subscribeLoader(imagesLoader: ImagesLoader) {
        this.imagesLoader = imagesLoader
    }

    fun unscribeLoader(){
        this.imagesLoader = null
    }

}