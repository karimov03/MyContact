package com.karimov03.mycontact

import com.karimov03.contact.Adapter
import com.karimov03.contact.MyContact


import Helper.MyButton
import Helper.MySwipeHelper
import Listnenr.MyButtonClickListener
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.karimov03.mycontact.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var list = ArrayList<MyContact>()
    private lateinit var adapter: Adapter
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myMethod()

        //add swipe
        val swipe = object : MySwipeHelper(this, binding.rv, 120){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //add button
                buffer.add(
                    MyButton(this@MainActivity,
                        "Sms",
                        30,
                        R.drawable.ic_sms,
                        Color.parseColor("#FFDD2371"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {

                            }
                        })
                )
                buffer.add(
                    MyButton(this@MainActivity,
                        "Call",
                        30,
                        R.drawable.ic_call_2,
                        Color.parseColor("#FFF8CA2A"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                Toast.makeText(this@MainActivity, "Call", Toast.LENGTH_SHORT).show()
                                val phoneNumber = "tel:" + list[position].number

                                val callIntent = Intent(Intent.ACTION_CALL)
                                callIntent.data = Uri.parse(phoneNumber)

                                if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // Permission is not granted, request the permission
                                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                                    return
                                }

// Start the phone call
                                startActivity(callIntent)


                            }
                        })
                )
            }

        }
    }

    @SuppressLint("Range")
    fun readContact(): ArrayList<MyContact> {
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()) {
            val contact = MyContact(
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            )
            list.add(contact)
            list.sortBy { it.name }

        }
        return list
    }

    fun myMethod() {
        askPermission(android.Manifest.permission.READ_CONTACTS) {
            list = readContact()
            adapter = Adapter(list)
            binding.rv.adapter = adapter
            adapter.notifyDataSetChanged()


        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }



}