package com.example.testingenvironment.imagealbum


import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.AlbumWithImages
import com.example.testingenvironment.database.ImageUri

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AlbumWithImages>?) {
    val adapter = recyclerView.adapter as ImageAlbumRecyclerViewAdapter
    if (data != null) {
        adapter.submitList(data)
    }
}

@BindingAdapter("albumName")
fun declareAlbumName(textView: TextView, album: Album){
    textView.text = album.name
}

@BindingAdapter("listImageUriData")
fun bindImageListRecyclerView(recyclerView: RecyclerView, data: List<ImageUri>?) {
    //si haces debug aqui, data siempre va a ser null, aunque  funcione perfectamente
    //es como si este metodo cargase todas las listas con las imagenes del ultimo album
    //
    //cada vez que este metodo es llamado, pinta una lista en todas las vistas, una y otra vez
    //hasta que llega a la ultima lista, la pinta y para porque no hay mas albumes
        try{
            val adapter = recyclerView.adapter as ImageAlbumItemRecyclerViewAdapter

            adapter.submitList(data)
            //hay algunos fallos a la hora de abrir el nuevo intent que seguramente se arreglen
            //ejecutando ese codigo en otro hilo de ejecucion

            //cuando agregas una imagen nueva desde la pantalla de los albums, no se actualiza la lista de albums
            //pero cuando entras en un album y vuelves atras, si que lo hace.

        }catch (e: Exception){

        }

}