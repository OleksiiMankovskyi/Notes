package com.example.notes_app_evo

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseManager (context: Context): SQLiteOpenHelper(context, "MyNotes", null, 1) {

    val database = this.writableDatabase
    var context = context

    override fun onCreate(db: SQLiteDatabase?)
    {
        db!!.execSQL("CREATE TABLE MyNotes(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DETAILS TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS MyNotes")
        onCreate(db)
    }

    fun insert(TITLE: String, DETAILS: String)
    {
        database.execSQL("INSERT INTO MyNotes(TITLE, DETAILS) VALUES('$TITLE', '$DETAILS')")
    }

    fun update(ID: Int, TITLE: String, DETAILS: String)
    {
        database.execSQL("UPDATE MyNotes SET TITLE = '$TITLE', DETAILS = '$DETAILS' WHERE ID = $ID")
    }

    fun delete(ID: Int)
    {
        database.execSQL("DELETE FROM MyNotes WHERE ID = $ID")
    }

    fun select(): ArrayList<listItem>
    {
        var array_list_item = ArrayList<listItem>()
        var cursor: Cursor = database.rawQuery("SELECT * FROM MyNotes", null)

        cursor.moveToFirst()

        while(!cursor.isAfterLast)
        {
            val ID: String = cursor.getString(0)
            val TITLE: String = cursor.getString(1)
            val DETAILS: String = cursor.getString(2)
            array_list_item.add(listItem(ID, TITLE, DETAILS))

            cursor.moveToNext()
        }
        return array_list_item
    }

    fun selectForUpdate(ID: Int): ArrayList<String>{
        var array_of_notes = ArrayList<String>()
        var cursor: Cursor = database.rawQuery("SELECT TITLE, DETAILS FROM MyNotes WHERE ID = $ID", null)

        cursor.moveToFirst()

        while(!cursor.isAfterLast)
        {
            val TITLE: String = cursor.getString(0)
            val DETAILS: String = cursor.getString(1)
            array_of_notes.add(0, TITLE.toString())
            array_of_notes.add(1, DETAILS.toString())
            cursor.moveToNext()
        }
        return  array_of_notes
    }
}