package com.alexnikola.supernotes.ui.notes_list.dummy

import java.util.*

object DummyContent {

    val NOTES:MutableList<Note> = ArrayList()

    private val COUNT = 100

    init {
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: Note) {
        NOTES.add(item)
    }

    private fun createDummyItem(position:Int): Note {
        val type = Random().nextInt(2)
        return when(type) {
            0 -> TextNote((position).toString(), Note.TEXT, "Item " + position)
            else -> ImageNote((position).toString(), Note.IMAGE, "https://stepupandlive.files.wordpress.com/2014/09/3d-animated-frog-image.jpg")
        }
    }




    abstract class Note(var id:String, var type: String) {
        companion object {
            val TEXT: String = "text"
            val IMAGE: String = "image"
        }
    }

    class TextNote(id: String, type: String, var text: String) : Note(id, type)

    class ImageNote(id: String, type: String, var url: String) : Note(id, type)
}
