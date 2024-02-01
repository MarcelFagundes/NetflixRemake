import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.DownloadImageTask
import com.squareup.picasso.Downloader
import com.squareup.picasso.Picasso


//Aqui é a lista HORIZONTAL
class MovieAdapter(
    private val movies: List<Movie>,
    @LayoutRes private val layoutId: Int,
    private val onItemClickListener: ( (Int) -> Unit)? = null
    ) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // val view = layoutInflater.inflate(R.layout.movie_item, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return  movies.size
    }

        //Abordagem usando um o próprio Android
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val imageCover: ImageView = itemView.findViewById(R.id.img_cover)
            imageCover.setOnClickListener {
                onItemClickListener?.invoke(movie.id)
             }

            DownloadImageTask(object : DownloadImageTask.Callback {
                override fun OnResult(bitmap: Bitmap) {
                    imageCover.setImageBitmap(bitmap)
                }
            }).execute(movie.converUrl)

            //Abordagem usando uma dependencia pronta
//            Picasso.get()
//                .load(movie.converUrl)
//                .into(imageCover)
            //imageCover.setImageResource(movie.converUrl)
        }
    }
}
//    override fun onStart() {
//        super.onStart()
//        // Checar GPS, pedir permissoes
//        Log.i("Teste", "onStart")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // voltar de algo (refresh) de uma dela. Retorno de uma ligação
//        Log.i("Teste", "onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        //registrar algum evento. Trabalha em conjunto com o on resume
//        Log.i("Teste", "onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        // livrar recursos (camera, qrcode)
//        Log.i("Teste", "onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // analytics
//        Log.i("Teste", "onDestroy")
//    }