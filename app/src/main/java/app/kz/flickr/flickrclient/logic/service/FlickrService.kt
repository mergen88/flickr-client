package app.kz.flickr.flickrclient.logic.service

import app.kz.flickr.flickrclient.application.App
import com.github.salomonbrys.kodein.instance
import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotoList
import com.googlecode.flickrjandroid.photos.SearchParameters
import io.reactivex.Observable

class FlickrService{

    val flickr = App.app!!.kodein!!.instance<Flickr>()
    fun getObservable(text: String?, page: Int): Observable<PhotoList> {
        return Observable.create<PhotoList> {
            subscriber ->
            try {
                subscriber.onNext(fl(text, page))
                subscriber.onComplete()
            } catch (e: Exception){
                subscriber.onError(e)
            }
        }
    }
    fun fl(text: String?, page: Int): PhotoList {
        val search = SearchParameters()
        search.text = text
        return flickr.photosInterface.search(search,40,page)
    }


}