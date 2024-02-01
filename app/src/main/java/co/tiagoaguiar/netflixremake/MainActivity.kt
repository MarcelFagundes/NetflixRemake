package co.tiagoaguiar.netflixremake

import CategoryAdapter
import MovieAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {

    private lateinit var progress: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private  val categories = mutableListOf<Category>()

    // M-V-C (model - [view - controller] activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_main)
        //Log.i("Teste", "onCreate")

        adapter = CategoryAdapter(categories) { id ->
            val intent = Intent(this@MainActivity, MovieActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

//// List vertical
//        // cat 1
//            // listas horizontal f1 - f2 - f3 ...
//        // cat 2
//            // listas horizontal f1 - f2 - f3 ...
//
        //val categories = mutableListOf<Category>()
//        for (j in 0 until 10) {
//            val movies = mutableListOf<Movie>()
//            for (i in 0 until 15) {
//                val movie = Movie(R.drawable.movie)
//                movies.add(movie)
//            }
//            val category = Category("cat $j", movies)
//            categories.add(category)
//        }

        //teste com dados de urls ficticias para testar o adapter

 //       val movies = mutableListOf<Movie>()
//        for(i in 0 until 60) {
//            val movie = Movie(R.drawable.movie)
//            movies.add(movie)
//        }

        //na vertical a lista (CategoryAdapter) de categorias
        // e dentro de cada item [TextView+recyclerView horizontal]
        //(cada categoria) teremos
        //uma lista (MovieAdapter) de filmes (ImageView)

        val rv: RecyclerView = findViewById(R.id.rv_main)
//        rv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = LinearLayoutManager(this) // por padrão é vertical
        rv.adapter = adapter

        CategoryTask(this).execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=4a46a658-cd8e-484c-962d-e06bcdee0eb8")
    }

    override fun OnPreExecute() {
        progress.visibility = View.VISIBLE
    }

    override fun OnResult(categories: List<Category>) {
        //aqui será quando o CategoryTask chamará de volta (callback) - listener
        //Log.i("Teste MainActivity", categories.toString())
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged() //força o adapter a chamar de novo o onBindViewHolder,  etc

        progress.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
    }


}