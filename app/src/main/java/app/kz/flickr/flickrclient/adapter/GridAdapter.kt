package app.kz.flickr.flickrclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import com.googlecode.flickrjandroid.photos.Photo
import com.squareup.picasso.Picasso
import app.kz.flickr.flickrclient.R
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Callback
import java.lang.Exception

class GridAdapter(context: Context) : ArrayAdapter<Photo>(context, R.layout.image_item) {

    var images: ArrayList<Photo>? = null

    init {
        images = ArrayList()
    }

    fun addAll(items: List<Photo>) {
       images!!.addAll(items)
    }

    fun clearImages(){
        images!!.clear()
    }
    override fun getItem(position: Int): Photo {
        return images!!.get(position)
    }

    override fun getCount(): Int {
        return images!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val photo = getItem(position)
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView!!.tag as ViewHolder
        }
        holder.progressBar.visibility = View.VISIBLE
        Picasso.get().load(photo.largeSquareUrl).placeholder(R.drawable.image_placeholder).fit().into(holder.image, object: Callback{
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                e!!.printStackTrace()
            }

        })
        return convertView
    }
    internal class ViewHolder(view: View) {
        @BindView(R.id.imageItem)
        lateinit var image: ImageView
        @BindView(R.id.progressBar)
        lateinit var progressBar: ProgressBar
        init {
            ButterKnife.bind(this, view)
        }
    }
}
