package com.example.myentertainmentlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myentertainmentlist.R.*
import com.example.myentertainmentlist.Signing.LoginActivity
import com.example.myentertainmentlist.adapters.UserAdapter
import com.example.myentertainmentlist.databinding.ActivityUserFragmentBinding
import com.example.myentertainmentlist.room.Entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userList: ArrayList<User>
    private lateinit var userShowAdapter: UserAdapter
    private lateinit var auth: FirebaseAuth

    private var bindingUserFragment: ActivityUserFragmentBinding? = null
    private val binding get() = bindingUserFragment!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingUserFragment = ActivityUserFragmentBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        userList = arrayListOf()

        userShowAdapter = UserAdapter(userList, activity as Context)
        binding.recyclerUsers.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = userShowAdapter
        }

        callDb()
    }


    private fun callDb() {

        val prefs =
            activity?.getSharedPreferences(getString(string.prefs_file), Context.MODE_PRIVATE)
        val groupId = prefs?.getString("group", "")

        db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("group", groupId)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    /*if(error != null) {

                    } */

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) userList.add(
                            dc.document.toObject(
                                User::class.java
                            )
                        )
                    }
                    userShowAdapter.notifyDataSetChanged()
                }
            })
    }
}
