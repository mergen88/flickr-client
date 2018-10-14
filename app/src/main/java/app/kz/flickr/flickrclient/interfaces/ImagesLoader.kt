package app.kz.flickr.flickrclient.interfaces

import com.googlecode.flickrjandroid.photos.Photo

interface ImagesLoader {

    fun onLoadComplete(photos: List<Photo>)
    fun onError(e: Throwable)
}