package app.kz.flickr.flickrclient.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import app.kz.flickr.flickrclient.interfaces.ImagesLoader
import app.kz.flickr.flickrclient.R
import app.kz.flickr.flickrclient.adapter.GridAdapter
import app.kz.flickr.flickrclient.ui.base.BaseActivity
import butterknife.BindView
import com.github.salomonbrys.kodein.instance
import com.googlecode.flickrjandroid.photos.Photo
import android.app.SearchManager
import android.database.CursorWrapper
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.widget.*
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class MainActivity : BaseActivity(), ImagesLoader {



    var adapter = kodein.instance<GridAdapter>()
    var page: Int = 0
    var textSearch = "car"
    var loadAvailable = true
    @BindView(R.id.imageListView)
    lateinit var gridView: GridView
    @BindView(R.id.notFound)
    lateinit var notFound: TextView

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridView.adapter = adapter
        gridView.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if(firstVisibleItem+visibleItemCount >= totalItemCount){
                    if(loadAvailable) {
                        loadImages(true)
                    }
                }
            }
        })
        gridView.setOnItemClickListener { _, _, position, _ ->
            val mView = layoutInflater.inflate(R.layout.show_image, null)
            val alertDialog = AlertDialog.Builder(this).setView(mView).create()
            Picasso.get().load(adapter!!.getItem(position).largeUrl).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(mView.findViewById<PhotoView>(R.id.photo_view),object : Callback{
                override fun onSuccess() {
                    mView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                }
                override fun onError(e: Exception?) {
                   e!!.printStackTrace()
                }
            })
            mView.findViewById<ImageButton>(R.id.closeImage).setOnClickListener {
                _ ->
                alertDialog.dismiss()
            }
            alertDialog.show()
        }


    }

    override fun onResume() {
        super.onResume()
        presenter.subscribeLoader(this)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState!=null && savedInstanceState.containsKey("page")){
            this.page = savedInstanceState.getInt("page")
            this.textSearch = savedInstanceState.getString("textSearch")
            this.loadAvailable = savedInstanceState.getBoolean("loadAvailable")
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt("page",page)
        outState!!.putString("textSearch",textSearch)
        outState!!.putBoolean("loadAvailable",loadAvailable)
    }

    private fun loadImages(isNext: Boolean){
        loadAvailable = true
        if(!isNext){
            page = 0
            adapter!!.clearImages()
        } else {
            presenter.getPhotos(textSearch, page)
            page++
        }
    }


    override fun onStop() {
        super.onStop()
        presenter.unscribeLoader()
    }


    override fun onError(e: Throwable) {
        showToastMessage(getString(R.string.error_connection))
    }

    override fun onLoadComplete(photos: List<Photo>) {
        if(photos.size>0) {
            notFound.visibility = View.GONE
            adapter!!.addAll(photos)
            adapter!!.notifyDataSetChanged()
        } else if(adapter.count == 0){
            notFound.visibility = View.VISIBLE
            loadAvailable = false
        } else {
            loadAvailable = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchManager = getSystemService(android.content.Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                textSearch = query!!
                loadImages(false)
                suggestion.saveRecentQuery(query, null)
                searchItem.collapseActionView()
                return true
            }
            override fun onQueryTextChange(query: String?): Boolean { return false }
        })
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(p0: Int): Boolean { return false }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as CursorWrapper
                searchView.setQuery(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)), true)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu);
    }
}



