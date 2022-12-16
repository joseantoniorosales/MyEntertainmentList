package com.example.myentertainmentlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myentertainmentlist.Entities.TV
import com.example.myentertainmentlist.Signing.LoginActivity
import com.example.myentertainmentlist.adapters.TVAdapter
import com.example.myentertainmentlist.databinding.ActivityTvfragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import java.util.*

class TVFragment : Fragment() {


    //Firebase variables
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    //list of Tv objects
    private lateinit var tvList: ArrayList<TV>

    // Adapter
    private lateinit var tvShowAdapter: TVAdapter

    // Fragments variables
    private var bindingFrag: ActivityTvfragmentBinding? = null
    private val binding get() = bindingFrag!!

    // tv info variables
    private lateinit var tvTitle: String
    private lateinit var tvPlatform: String
    private lateinit var tvRating: String
    private lateinit var tvGenre: String
    private var tvStatus: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFrag = ActivityTvfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        //Shared preferences to get the email
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val email = prefs?.getString("email", null)
        val groupId = prefs?.getString("group", "")

        auth = Firebase.auth

        tvList = arrayListOf()

        tvShowAdapter = TVAdapter(tvList, activity as Context)

        binding.tvRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = tvShowAdapter
        }


        binding.tvAddBut.setOnClickListener {

            db.collection("users").document(email as String).get().addOnSuccessListener {

                if (it.get("groupAdmin") == true) {

                    val tvDialog: View =
                        layoutInflater.inflate(R.layout.createtv_dialog, binding.root, false)

                    tvDialog.removeDialog()

                    MaterialAlertDialogBuilder(context as Context)
                        .setView(tvDialog)
                        .setTitle("Create TV object")
                        .setNegativeButton("Cancel") { dialog, which ->
                        }

                        .setPositiveButton("Create") { dialog, which ->
                            //tvTitle = tvDialog

                            CreateTV(groupId)
                        }
                        .show()
                } else {
                    Snackbar.make(
                        view,
                        "Only admins can create tvs",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        addDb()

        binding.tvMenuBut.setOnClickListener {

            ShowMenu(binding.tvMenuBut)
        }
    }

    private fun addDb() {

        db = FirebaseFirestore.getInstance()

        val prefs =
            activity?.getSharedPreferences(R.string.prefs_file.toString(), Context.MODE_PRIVATE)

        val groupID = prefs?.getString("group", "")

        db.collection("tvObjects").whereEqualTo("groupID", groupID)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {

                        Log.i("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            var tv = dc.document.toObject(TV::class.java)
                            tv.idTV = dc.document.id

                            if (tvList.indexOf(tv) == -1) {
                                tvList.add(tv)
                                tvShowAdapter.notifyItemInserted(tvList.size)
                            }
                        }

                        //IF tv EDITED
                        if (dc.type == DocumentChange.Type.MODIFIED) {

                            var newtv = dc.document.toObject(TV::class.java)
                            newtv.idTV = dc.document.id

                            var position: Int = 0
                            for (tv in tvList) {

                                if (tv.idTV == dc.document.id) {
                                    position = tvList.indexOf(tv)
                                    tvList[position] = newtv
                                    tvShowAdapter.notifyItemChanged(position)
                                    tvShowAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            })
    }

    private fun ShowMenu(view: View) {

        val menuShow = PopupMenu(activity as Context, view)
        val inflater: MenuInflater = menuShow.menuInflater
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
        inflater.inflate(R.menu.options_menu, menuShow.menu)
        menuShow.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    prefs?.clear()
                    prefs?.apply()
                    goLoginPage()
                    auth.signOut()
                    activity?.finish()
                }
            }

            true
        }

        menuShow.show()
    }

    fun goLoginPage() {

        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun View.removeDialog() {

        this ?: return
        val parentView = parent as? ViewGroup ?: return
        parentView.removeView(this)
    }

    private fun CreateTV(groupId: String?) {

        var tvId = UUID.randomUUID().toString()

        if (tvTitle != "") {
            db.collection("tvs").document(tvId).set(
                hashMapOf(
                    "tvId" to tvId,
                    "title" to tvTitle,
                    "platform" to tvPlatform,
                    "genre" to tvGenre,
                    "status" to tvStatus,
                    "rating" to tvRating,
                    "group" to groupId
                )
            ).addOnSuccessListener {
                Toast.makeText(
                    context,
                    "tv Successfully created",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            Toast.makeText(context, "tv failed to be created", Toast.LENGTH_LONG).show()

        }
    }
}