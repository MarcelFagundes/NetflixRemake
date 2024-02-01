package co.tiagoaguiar.netflixremake

import MovieAdapter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import co.tiagoaguiar.netflixremake.util.DownloadImageTask
import co.tiagoaguiar.netflixremake.util.MovieTask
import java.lang.IllegalStateException


class MovieActivity : AppCompatActivity(), MovieTask.Callback {

    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtCast: TextView
    private lateinit var progress: ProgressBar
    private lateinit var adapter: MovieAdapter

    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        txtTitle = findViewById(R.id.movie_txt_title)
        txtDesc = findViewById(R.id.movie_txt_desc)
        txtCast = findViewById(R.id.movie_txt_cast)
        progress = findViewById(R.id.movie_progress)

        val rv: RecyclerView = findViewById(R.id.movie_rv_similar)

        val id = intent?.getIntExtra("id", 0) ?: throw IllegalStateException("ID não foi encontrado!")

        val url = "https://api.tiagoaguiar.co/netflixapp/movie/$id?apiKey=4a46a658-cd8e-484c-962d-e06bcdee0eb8"

        MovieTask(this).execute(url)

//        txtTitle.text = "Batman Begins"
//        txtDesc.text = "Essa é a descrição do filme do Batman"
//        txtCast.text = getString(R.string.cast, "Ator A, Ator B")

       // val movies = mutableListOf<Movie>()
//        for (i in 0 until 15) {
//            val movie = Movie(R.drawable.movie)
//            movies.add(movie)
//        }

        adapter = MovieAdapter(movies, R.layout.movie_item_similar)
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = adapter


        val toolbar:Toolbar = findViewById(R.id.movie_toolbar)
        //Essa função é criada para informar a atividade que existe uma barra de ação
        setSupportActionBar(toolbar)


        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

//        // busquei o denhavel (layer-list)
//        val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
//
//        // busquei o filme que eu quero
//        val movieCover = ContextCompat.getDrawable(this,R.drawable.movie_4)
//
//        //atribui a esse layer-list o novo filme
//        layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)
//
//        // set no imageview
//        val coverImg: ImageView = findViewById(R.id.movie_img)
//        coverImg.setImageDrawable(layerDrawable)

    }

    override fun OnPreExecute() {
        progress.visibility = View.VISIBLE
    }

    override fun onFailure(message: String) {
        progress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun OnResult(movieDetail: MovieDetail) {
        progress.visibility = View.GONE
        //Log.i("teste", movieDetail.toString())

        txtTitle.text = movieDetail.movie.title
        txtDesc.text = movieDetail.movie.desc
        txtCast.text = getString(R.string.cast, movieDetail.movie.cast)

        movies.clear()
        movies.addAll(movieDetail.similars)
        adapter.notifyDataSetChanged()

        DownloadImageTask(object : DownloadImageTask.Callback {
            override fun OnResult(bitmap: Bitmap) {
                // busquei o denhavel (layer-list)
                val layerDrawable: LayerDrawable = ContextCompat.getDrawable(this@MovieActivity, R.drawable.shadows) as LayerDrawable

                // busquei o filme que eu quero
                //val movieCover = ContextCompat.getDrawable(this,R.drawable.movie_4)
                val movieCover = BitmapDrawable(resources, bitmap)

                //atribui a esse layer-list o novo filme
                layerDrawable.setDrawableByLayerId(R.id.cover_drawable, movieCover)

                // set no imageview
                val coverImg: ImageView = findViewById(R.id.movie_img)
                coverImg.setImageDrawable(layerDrawable)            }
        }).execute(movieDetail.movie.converUrl)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}