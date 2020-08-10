package com.deepak.locus.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.deepak.locus.BuildConfig
import com.deepak.locus.R
import com.deepak.locus.adapters.MainAdapter
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.data.model.DataRepostiory
import com.deepak.locus.data.model.local.AssetData
import com.deepak.locus.utils.getOutputMediaFile
import com.deepak.locus.utils.saveBitMap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_image.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var dataRepostiory: DataRepostiory

    private lateinit var mainAdapter: MainAdapter

    private lateinit var listItems: List<DataItem>

    private lateinit var dataItem: DataItem

    private  var itemPosition: Int = -1

    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataRepostiory = DataRepostiory(AssetData(this))
        mainViewModelFactory = MainViewModelFactory(dataRepostiory)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        progress_bar.visibility = View.GONE
        mainViewModel.data.observe(this, Observer {
            it?.let { this.listItems = it }

            mainAdapter = MainAdapter(listItems, this)


            with (rv_items) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter =  mainAdapter
            }
        })

    }

    override fun onItemClick(position: Int, item: DataItem) {
        dataItem = item
        itemPosition = position
        checkPermission()

    }

    override fun onCrossClick(position: Int, item: DataItem) {
        item.dataMap?.addProperty("imagePath", "null")
        //dataItem.currentValue = null
        mainAdapter.notifyItemChanged(position)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takePictureIntent()
        }
    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            resolveActivity(packageManager)?.let {
                startActivityForResult(this@apply, REQUEST_IMAGE_CAPTURE)
            }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            dataItem.dataMap?.addProperty("imagePath", getOutputMediaFile(this).toString())
            if (data?.extras != null && data.extras!!.get("data") != null) {
                dataItem.currentValue = saveBitMap(data.extras?.get("data") as Bitmap, this)
            }
            dataItem.dataMap?.get("imagePath")?.asString?.let { loadImage(image_view, it) }
            mainAdapter.notifyItemChanged(listItems.indexOf(dataItem))
        } else {
            Toast.makeText(this, "Could not take pitcure from camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImage(view: ImageView, imagePath: String){

        getOutputMediaFile(view.context).let{
            Glide.with(view.context).asBitmap()
                .load(File(dataItem.currentValue))
                .apply(RequestOptions()
                    .optionalFitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(view)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean  {
         menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
                     R.id.submit -> { printItems()
                         true }
                   else -> { super.onOptionsItemSelected(item)
                   false}
        }
    }

    private fun printItems(){
        for(item in listItems)
        Log.i("TAG","$item")
    }
}