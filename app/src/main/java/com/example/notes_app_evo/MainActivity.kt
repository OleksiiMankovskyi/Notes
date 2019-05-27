package com.example.notes_app_evo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.all_existing_notes, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        if (item != null)
        {
            if (item.itemId == R.id.add_new_note)
            {
                val intent = Intent(this, NoteDetailsActivity::class.java)
                intent.putExtra("state", "AddOnly")
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun loadData()
    {
        val db = DataBaseManager(this)
        val adapter = listAdapter(db.select())
        listNotes.adapter = adapter
        listNotes.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, NoteDetailsActivity::class.java)
            intent.putExtra("state", listNotes.getItemAtPosition(position).toString())
            startActivity(intent)
        }
    }

    override fun onResume()
    {
        super.onResume()
        loadData()
    }

    inner class listAdapter : BaseAdapter
    {
        var listItem = ArrayList<listItem>()
        constructor(listItem: ArrayList<listItem>)
        {
            this.listItem = listItem
        }

        override fun getCount(): Int
        {
            return listItem.size
        }

        override fun getItemId(position: Int): Long
        {
            return position.toLong()
        }

        override fun getItem(position: Int): Any
        {
            return listItem[position].id
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
        {
            val view: View = layoutInflater.inflate(R.layout.notes_list, null)

            val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
            textViewTitle.text = listItem[position].title

            val textViewDescription: TextView = view.findViewById(R.id.textViewDescription)
            textViewDescription.text = listItem[position].details

            return view
        }
    }
}
