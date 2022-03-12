package com.example.myentertainmentlist.lists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.TVViewActivity
import com.example.myentertainmentlist.adapters.TVAdapter
import com.example.myentertainmentlist.databinding.ActivityTvListBinding
import com.example.myentertainmentlist.room.Database.EntertainmentDatabase
import com.example.myentertainmentlist.room.Entities.TV
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TVListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTvListBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: EntertainmentDatabase

    private lateinit var adapterTV: TVAdapter
    private lateinit var data: MutableList<TV>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTvListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //AuthService instance
        auth = Firebase.auth

        // Database service instance
        db = EntertainmentDatabase.getEntertainmentDatabase(this)!!

        //TODO recover putExtra information

        //Recover information from the DB
        data = db.getEntertainmentDao().getSeries()

        //Listeners Management

        //Single click
        val singleClickListenerManager: (TV) -> Unit = {

            val intent = Intent(this, TVViewActivity::class.java)
            intent.putExtra("_id", it.idTV)
            startActivity(intent)
        }

        //Long click
        val longClickListenerManager: (MenuItem, TV) -> Boolean = {

                item: MenuItem, serie: TV ->
            when (item.itemId) {

                R.id.editMenu -> {

                    db.getEntertainmentDao().insertSerie(serie)


                }

                R.id.deleteMenu -> {
                    
                    //Delete the series from the database
                    db.getEntertainmentDao().deleteSerie(serie)

                    //Search the index
                    val ind = data.indexOfFirst { it.equals(serie) }

                    //Removes the serie from the database
                    data.removeAt(ind)

                    adapterTV.notifyItemRemoved(ind)
                }
            }
            false
        }

        //Adapter creation
        adapterTV = TVAdapter(data).apply {
            singleClick = singleClickListenerManager
            longClick = longClickListenerManager
        }

        binding.TVrv.apply {
            adapter = adapterTV
            setHasFixedSize(true)
        }
    }
}