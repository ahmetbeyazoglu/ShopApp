package com.herpestes.myshoppal.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.herpestes.myshoppal.activities.LoginActivity
import com.herpestes.myshoppal.activities.RegisterActivity
import com.herpestes.myshoppal.models.User
import com.herpestes.myshoppal.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, user: User) {
        mFireStore.collection(SyncStateContract.Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }

    }
    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {

                    val sharedPref = activity.getSharedPreferences(
                        Constants.MY_SHOP_PAL_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                    )
                    editor.apply()

                    when (activity) {
                        is LoginActivity -> activity.userLoggedInSuccess(user)
                        is RegisterActivity -> activity.userLoggedInSuccess(user)
                        is SettingsActivity -> activity.userDetailSuccess(user)
                    }
                }

            }.addOnFailureListener { exception ->
                when (activity) {
                    is LoginActivity -> activity.hideProgressDialog()
                    is RegisterActivity -> activity.hideProgressDialog()
                    is Settings     Activity -> activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }
    }


}