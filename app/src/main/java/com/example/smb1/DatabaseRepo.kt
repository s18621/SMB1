package com.example.smb1

import android.util.Log
import com.example.smb1.Models.dbModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class databaseRepo(private val fbdb: FirebaseDatabase) {

    var allGroceries: MutableList<dbModel> = mutableListOf()
    private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw Exception()

    init {
        fbdb.getReference("users/${user.uid}/Grocery").addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val dbModel = dbModel(
                        id = snapshot.ref.key as String,
                        price = snapshot.child("price").value as Double,
                        itemName = snapshot.child("itemName").value as String,
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                    )
                    allGroceries.add(dbModel)
                }


                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val dbModel = dbModel(
                        id = snapshot.ref.key as String,
                        price = snapshot.child("price").value as Double,
                        itemName = snapshot.child("itemName").value as String,
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                    )
                    allGroceries.remove(dbModel)
                    allGroceries.add(dbModel)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val dbModel = dbModel(
                        id = snapshot.ref.key as String,
                        price = snapshot.child("price").value as Double,
                        itemName = snapshot.child("itemName").value as String,
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                    )
                    allGroceries.remove(dbModel)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    fun insert(item: dbModel) {
        fbdb.getReference("users/${user.uid}/Grocery").push().also {
            item.id = it.ref.key.toString()
            it.setValue(item)
        }
    }

    //suspend fun update(student: Student) = studentDao.update(student)
    fun update(item: dbModel) {
        var itemref = fbdb.getReference("users/${user.uid}/Grocery/${item.id}")
        itemref.child("price").setValue(item.price)
        itemref.child("itemName").setValue(item.itemName)
        itemref.child("quantity").setValue(item.quantity)
        itemref.child("bought").setValue(item.bought)
    }

    //suspend fun delete(student: Student) = studentDao.delete(student)
    fun delete(item: dbModel) =
        fbdb.getReference("users/${user.uid}/Grocery/${item.id}").removeValue()

}