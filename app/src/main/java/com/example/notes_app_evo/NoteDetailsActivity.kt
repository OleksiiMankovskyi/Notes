package com.example.notes_app_evo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_details.*
import java.lang.Exception
import java.util.ArrayList

class NoteDetailsActivity : AppCompatActivity() {

    lateinit var state: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        state = intent.getStringExtra("state")

        if(state == "AddOnly")
        {
            this.title = "Добавление новой заметки"
        }
        else
        {
            this.title = "Изменение Заметки"
            val db = DataBaseManager(this)

            try
            {
                val arr: ArrayList<String> = ArrayList(db.selectForUpdate(state.toInt()))
                editTextTitle.setText(arr[0])
                editTextDescription.setText(arr[1])
            }

            catch(e: Exception)
            {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_menu, menu)
        if (state == "AddOnly")
        {
            val item: MenuItem = menu!!.findItem(R.id.menuDelete)
            item.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        if (item != null)
        {
            if(item.itemId == android.R.id.home && state == "AddOnly")
            {
                if(editTextTitle.text.toString().isEmpty() || editTextDescription.text.toString().isEmpty())
                {
                    Toast.makeText(this, "Невозможно созать пустую заметку", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val db = DataBaseManager(this)
                    try
                    {
                        db.insert(editTextTitle.text.toString(), editTextDescription.text.toString())
                        Toast.makeText(this, "Новая заметка успешно добавлена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    catch(e: Exception)
                    {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            else if (item.itemId == android.R.id.home && state != "AddOnly")
            {
                if(editTextTitle.text.toString().isEmpty() || editTextDescription.text.toString().isEmpty())
                {

                    val db = DataBaseManager(this)
                    try{
                        db.delete(state.toInt())
                        Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    catch (e: Exception)
                    {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
                else
                {
                    val db = DataBaseManager(this)
                    try
                    {
                        db.update(state.toInt(), editTextTitle.text.toString(), editTextDescription.text.toString())
                        Toast.makeText(this, "Заметка успешно изменена", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    catch(e: Exception)
                    {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            else if (item.itemId == R.id.menuShare && state == "AddOnly" )
            {
                if(editTextTitle.text.toString().isEmpty() || editTextDescription.text.toString().isEmpty())
                {
                    Toast.makeText(this, "Невозможно отправить пустую заметку", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val share_intent = Intent(Intent.ACTION_SEND)
                    share_intent.type = "text/plain"
                    share_intent.putExtra(Intent.EXTRA_TEXT, " ")
                    startActivity(Intent.createChooser(share_intent, " "))
                }
            }

            else if (item.itemId == R.id.menuShare && state != "AddOnly" )
            {
                if(editTextTitle.text.toString().isEmpty() || editTextDescription.text.toString().isEmpty())
                {
                    Toast.makeText(this, "Невозможно отправить пустую заметку", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, " ")
                    intent.putExtra(Intent.EXTRA_TEXT, " ")
                    startActivity(Intent.createChooser(intent, " "))

                }
            }

            else if (item.itemId == R.id.menuDelete)
            {
                AlertDialog.Builder(this)
                    .setTitle("Удаление заметки")
                    .setMessage("Вы действительно хотите удалить эту заметку?")
                    .setIcon(R.drawable.ic_action_delete_black_24)
                    .setNegativeButton("Удалить") {dialog, which->
                        val db = DataBaseManager(this)
                        try
                        {
                            db.delete(state.toInt())
                            Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT).show()
                            finish()

                        }
                        catch(e: Exception)
                        {
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setPositiveButton("Отменить"){dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

