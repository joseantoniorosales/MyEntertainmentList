package com.example.myentertainmentlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myentertainmentlist.adapters.GroupCreatorAdapter
import com.example.myentertainmentlist.databinding.ActivityGroupCreatorBinding
import com.example.myentertainmentlist.room.Entities.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*

class GroupCreatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupCreatorBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    //Recycler Variables
    private lateinit var userList: ArrayList<com.example.myentertainmentlist.room.Entities.User>
    private lateinit var groupCreatorAdapter: GroupCreatorAdapter
    private var groupCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityGroupCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var email = intent.extras?.get("email").toString()

        userList = arrayListOf()
        groupCreatorAdapter = GroupCreatorAdapter(userList, this, email)

        binding.recyclerUserGroup.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            adapter = groupCreatorAdapter
        }

        callDb()

        binding.butCreate.setOnClickListener {

            var groupName = binding.groupNameInput.text.toString().trim()
            var groupId = UUID.randomUUID().toString()
            userList.forEach {

                if (email == it.email) {
                    it.group = groupId
                    it.groupAdmin = true
                    it.onGroup = true
                }

                if (it.onGroup == true) {
                    it.group = groupId
                    groupCount++
                }
            }

            if (groupCount > 1) {

                if (groupName != "") {
                    db.collection("groups").document(groupId)
                        .set(hashMapOf("groupId" to groupId, "groupName" to groupName))
                        .addOnSuccessListener {
                            var batch = FirebaseFirestore.getInstance().batch()

                            userList.forEach {

                                if (it.onGroup == true) {
                                    var ref = db.collection("users").document(it.email)
                                    batch.set(ref, it)
                                }
                            }
                            batch.commit()
                            saveGroupId(groupId)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                } else {
                    Snackbar.make(binding.root, "The group must have a name", Snackbar.LENGTH_LONG)
                        .show()
                    groupCount = 0
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "The group must have at least 2 people",
                    Snackbar.LENGTH_LONG
                ).show()
                groupCount = 0
            }
        }

        binding.butCancel.setOnClickListener {
            finish()
        }
    }

    private fun callDb() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("onGroup", false)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) return

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            userList.add(dc.document.toObject(User::class.java))
                        }
                    }
                    groupCreatorAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onBackPressed() {}

    private fun saveGroupId(groupId: String) {
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

        prefs.putString("group", groupId)
        prefs.apply()
    }
}