package com.deepak.locus.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deepak.locus.R
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.data.model.DataRepostiory
import com.deepak.locus.data.model.local.AssetData
import kotlinx.android.synthetic.main.activity_main.*
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
            it?.let {
                //listItems = it
                mainAdapter = MainAdapter(it, this)
                listItems = it

                with (rv_items) {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter =  mainAdapter
                }
                dataView.text = listItems.toString()
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

            createImageFile()?.let{ imageFile ->
                FileProvider.getUriForFile(this@MainActivity, "", imageFile).run {
                    putExtra(MediaStore.EXTRA_OUTPUT, this)
                }
            }
            resolveActivity(packageManager)?.let {
                startActivityForResult(this@apply, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            dataItem.dataMap?.addProperty("imagePath", createImageFile().toString())
            mainAdapter.notifyItemChanged(itemPosition)
        } else {
            Toast.makeText(this, "Could not take pitcure from camera", Toast.LENGTH_SHORT).show()
        }
    }

    fun createImageFile(): File? {
        return  File.createTempFile(
            ("IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}"),
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))

    }
}