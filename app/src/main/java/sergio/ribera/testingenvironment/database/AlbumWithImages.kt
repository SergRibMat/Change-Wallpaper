package sergio.ribera.testingenvironment.database

class AlbumWithImages(
    val album: Album,
    var imageList: List<ImageUri> = listOf()
)