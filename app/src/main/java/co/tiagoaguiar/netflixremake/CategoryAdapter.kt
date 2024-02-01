import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie

//Aqui é a lista VERTICAL
class CategoryAdapter(
    private val categories: List<Category>,
    private val onIntemClickListener: (Int) -> Unit
    ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // val view = layoutInflater.inflate(R.layout.movie_item, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return  categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
            val rvCategory: RecyclerView = itemView.findViewById(R.id.rv_category)
            txtTitle.text = category.name

            rvCategory.layoutManager = LinearLayoutManager(itemView.context,RecyclerView.HORIZONTAL,false)
            rvCategory.adapter = MovieAdapter(category.movies, R.layout.movie_item, onIntemClickListener)

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