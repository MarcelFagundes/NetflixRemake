package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun OnPreExecute()
        fun OnResult(categories: List<Category>)
        fun onFailure(message: String)
    }

    fun execute(url: String) {
        callback.OnPreExecute()
        // nesse momento, estamos utilizando a UI-thread (1)


        executor.execute {
            var urlConnection: HttpsURLConnection? =null
            var buffer: BufferedInputStream? = null
            var stream: InputStream? = null
            try {
                // nesse momento, estamos utilizando a NOVA-thread [processo paralelo] (2)
                val requestURL = URL(url) //abrir uma URL
                urlConnection = requestURL.openConnection() as HttpsURLConnection // abri a conexão
                urlConnection.readTimeout = 2000 //tempo de leitura (2s)
                urlConnection.connectTimeout = 2000 //tempo de conexão (2s)

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }
//                //forma 1: simples e rápida para comunicar com uma requisição http
//                val stream = urlConnection.inputStream // sequencia bytes
//                val jsonAsString = stream.bufferedReader().use {it.readText()} //bytes -> String
//                Log.i("Test", jsonAsString)

                //forma 2: bytes -> string
                stream = urlConnection.inputStream // sequencia bytes
                buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)

                // o JSON está preparado para ser convertido em um DataClass
                val categories = toCategories(jsonAsString)
                //Log.i("Teste", categories.toString())

                handler.post {
                    // aqui roda dentro de UI-thread
                    callback.OnResult(categories)
                }


                    // Log.i("Test", jsonAsString)


            } catch (e: IOException) {
                val message = e.message ?: "erro desconhecido"
                Log.e("Teste",message, e)
                handler.post {
                    callback.onFailure(message)
                }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
                buffer?.close()
            }
        }
    }

    private fun toCategories(jsonAsString: String) : List<Category> {
        val categories = mutableListOf<Category>()

        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")
        for (i in 0 until jsonCategories.length()) {
            val jsonCategory =  jsonCategories.getJSONObject(i)

            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()
            for (j in 0  until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")

                movies.add(Movie(id, coverUrl))
            }

            categories.add(Category(title, movies))



        }


        return categories
    }

    private fun toString(stream: InputStream) : String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }
}