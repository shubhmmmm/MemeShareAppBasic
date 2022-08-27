package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import com.bumptech.glide.request.RequestListener as RequestListener

class MainActivity : AppCompatActivity() {

    var currentImgUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shareButton.setOnClickListener { shareMeme() }
        nextButton.setOnClickListener { nextMeme() }

        loadMeme()
    }

    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        nextButton.isEnabled = false
        shareButton.isEnabled = false
//        val queue = Volley.newRequestQueue(this)

//        we don't need upper line now after getting a singleton class as volly should have a single
//        instance throughout the whole app thus using singleton class.
        val url = "https://meme-api.herokuapp.com/gimme#"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET,url, null,
            { response ->
               currentImgUrl = response.getString("url")
                Glide.with(this).load(currentImgUrl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        return false
                    }


                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }
                }).into(memeImageView)
            },
            { error_ ->
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            }
        )

         // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
     fun shareMeme() {
         val intent = Intent(Intent.ACTION_SEND)
         intent.type="text/plain"
         intent.putExtra(Intent.EXTRA_TEXT,"Le hans le thodda!! $currentImgUrl")
         val chooser = Intent.createChooser(intent,"hahaha")
         startActivity(chooser)
    }

     fun nextMeme() {
        loadMeme()
    }

}