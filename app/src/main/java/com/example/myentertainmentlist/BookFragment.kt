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
import com.example.myentertainmentlist.Entities.Book
import com.example.myentertainmentlist.Signing.LoginActivity
import com.example.myentertainmentlist.adapters.BookAdapter
import com.example.myentertainmentlist.databinding.ActivityBookFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import java.util.*

class BookFragment : Fragment() {

    //Firebase variables
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    //list of books
    private lateinit var bookList: ArrayList<Book>

    // Adapter
    private lateinit var bookShowAdapter: BookAdapter

    // Fragments variables
    private var bindingFrag: ActivityBookFragmentBinding? = null
    private val binding get() = bindingFrag!!

    // book info variables
    private lateinit var bookTitle: String
    private lateinit var bookAuthor: String
    private lateinit var bookRating: String
    private lateinit var bookGenre: String
    private var bookStatus: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFrag = ActivityBookFragmentBinding.inflate(inflater, container, false)
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

        bookList = arrayListOf()

        bookShowAdapter = BookAdapter(bookList, activity as Context)

        binding.bookRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = bookShowAdapter
        }


        binding.bookAddBut.setOnClickListener {

            db.collection("users").document(email as String).get().addOnSuccessListener {

                if (it.get("groupAdmin") == true) {

                    val bookDialog: View =
                        layoutInflater.inflate(R.layout.createbook_dialog, binding.root, false)

                    bookDialog.removeDialog()

                    MaterialAlertDialogBuilder(context as Context)
                        .setView(bookDialog)
                        .setTitle("Create book")
                        .setNegativeButton("Cancel") { dialog, which ->
                        }

                        .setPositiveButton("Create") { dialog, which ->
                            //bookTitle = bookDialog

                            createBook(groupId)
                        }
                        .show()
                } else {
                    Snackbar.make(
                        view,
                        "Only admins can create books",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        addDb()

        binding.bookMenuBut.setOnClickListener {

            ShowMenu(binding.bookMenuBut)
        }
    }

    private fun addDb() {

        db = FirebaseFirestore.getInstance()

        val prefs =
            activity?.getSharedPreferences(R.string.prefs_file.toString(), Context.MODE_PRIVATE)

        val groupID = prefs?.getString("group", "")

        db.collection("books").whereEqualTo("groupID", groupID)
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
                            var book = dc.document.toObject(Book::class.java)
                            book.idBook = dc.document.id

                            if (bookList.indexOf(book) == -1) {
                                bookList.add(book)
                                bookShowAdapter.notifyItemInserted(bookList.size)
                            }
                        }

                        //IF BOOK EDITED
                        if (dc.type == DocumentChange.Type.MODIFIED) {

                            var newBook = dc.document.toObject(Book::class.java)
                            newBook.idBook = dc.document.id

                            var position: Int = 0
                            for (book in bookList) {

                                if (book.idBook == dc.document.id) {
                                    position = bookList.indexOf(book)
                                    bookList[position] = newBook
                                    bookShowAdapter.notifyItemChanged(position)
                                    bookShowAdapter.notifyDataSetChanged()
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

    private fun createBook(groupId: String?) {

        var bookId = UUID.randomUUID().toString()

        if (bookTitle != "") {
            db.collection("books").document(bookId).set(
                hashMapOf(
                    "bookId" to bookId,
                    "title" to bookTitle,
                    "author" to bookAuthor,
                    "genre" to bookGenre,
                    "status" to bookStatus,
                    "rating" to bookRating,
                    "group" to groupId
                )
            ).addOnSuccessListener {
                Toast.makeText(
                    context,
                    "book Successfully created",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            Toast.makeText(context, "book failed to be created", Toast.LENGTH_LONG).show()

        }
    }
}
