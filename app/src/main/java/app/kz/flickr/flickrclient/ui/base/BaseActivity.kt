package app.kz.flickr.flickrclient.ui.base

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import app.kz.flickr.flickrclient.application.App
import app.kz.flickr.flickrclient.logic.helper.ServiceHelper
import butterknife.ButterKnife
import com.github.salomonbrys.kodein.instance

open abstract class BaseActivity : AppCompatActivity(){

    val kodein = App.app!!.kodein!!
    val suggestion = kodein.instance<SearchRecentSuggestions>()
    val presenter = kodein.instance<ServiceHelper>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        setContentView(getContentView())
        ButterKnife.bind(this)

    }

    protected fun showToastMessage(text: String){
        Toast.makeText(applicationContext,text, Toast.LENGTH_LONG).show()
    }
    protected abstract fun getContentView() : Int
}