package co.tiagoaguiar.netflixremake.model

// import androidx.annotation.DrawableRes

// data class Movie(@DrawableRes val converUrl: Int)
data class Movie(
    val id:Int,
    val converUrl: String,
    val title: String = "",
    val desc: String = "",
    val cast: String = ""
)
