package com.example.testingenvironment.imagealbum


import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.MainActivity
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.AlbumWithImages
import com.example.testingenvironment.database.ImageUri

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AlbumWithImages>?) {

    val adapter = recyclerView.adapter as ImageAlbumRecyclerViewAdapter
    if (data != null) {
        adapter.submitList(data)
    }

    //puedo hacer esto aqui?
    //bindImageListRecyclerView()
}

@BindingAdapter("albumName")
fun declareAlbumName(textView: TextView, album: Album){
    textView.text = album.name
}

@BindingAdapter("listImageUriData")
fun bindImageListRecyclerView(recyclerView: RecyclerView, data: List<ImageUri>?) {
    //Toast.makeText(MainActivity.applicationContext(), "text", Toast.LENGTH_SHORT).show()
    //si haces debug aqui, data siempre va a ser null, aunque  funcione perfectamente
    //es como si este metodo cargase todas las listas con las imagenes del ultimo album
    //
    //cada vez que este metodo es llamado, pinta una lista en todas las vistas, una y otra vez
    //hasta que llega a la ultima lista, la pinta y para porque no hay mas albumes
        try{
            val adapter = recyclerView.adapter as ImageAlbumItemRecyclerViewAdapter

            adapter.submitList(data)

            //cuando agregas una imagen nueva desde la pantalla de los albums, no se actualiza la lista de albums
            //pero cuando entras en un album y vuelves atras, si que lo hace. Es porque el bindingAdapter
            //de la childlist no se ejecuta

            //lo que pasaba es que hay que crear un adapter para la sublista anonimo para cada lista, es decir
            //binding.adapter = MySublistAdapter()

            //nada de lo probado ha funcionado para poder ver las imagenes nada mas las agregas, asi que he
            //tenido que optar por recargar la lista entera. Como la funcionalidad de reload en youtube
            //al hacer un swipe hacia abajo. Basta con volver a reejecutar el valor asignado al adapter principal.
            //cosa mala es que necesito entonces una animacion de carga mientras el proceso esta en ello

        }catch (e: Exception){

        }

}