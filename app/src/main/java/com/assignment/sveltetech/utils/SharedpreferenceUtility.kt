package com.assignment.sveltetech.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedpreferenceUtility {
    private var mEditor: SharedPreferences.Editor? = null
    private val CHECK_LOGIN = "check_login"
    var login: String?
        get() {
            val check_login: String?
            check_login = mPref!!.getString(CHECK_LOGIN, "")
            return check_login
        }
        set(check_login) {
            mEditor = mPref!!.edit()
            mEditor?.putString(CHECK_LOGIN, check_login)
            mEditor?.commit()
        }

    //Put long value into sharedpreference
    fun putLong(key: String?, value: Long) {
        try {
            mEditor = mPref!!.edit()
            mEditor?.putLong(key, value)
            mEditor?.commit()
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
    }

    //Get long value from sharedpreference
    fun getLong(key: String?): Long {
        return try {
            val lvalue: Long
            lvalue = mPref!!.getLong(key, 0)
            lvalue
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
            0
        }
    }

    // Put int value into sharedpreference
    fun putInt(key: String?, value: Int) {
        try {
            mEditor = mPref!!.edit()
            mEditor?.putInt(key, value)
            mEditor?.commit()
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
    }

    // Get int value from sharedpreference
    fun getInt(key: String?): Int {
        return try {
            val lvalue: Int
            lvalue = mPref!!.getInt(key, 0)
            lvalue
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
            0
        }
    }

    // * Put String value into sharedpreference
    fun putString(key: String?, value: String?) {
        try {
            mEditor = mPref!!.edit()
            mEditor?.putString(key, value)
            mEditor?.commit()
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
    }

    fun getString(key: String?): String {
        return try {
            val lvalue: String
            lvalue = mPref!!.getString(key, "")?:""
            lvalue
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
            ""
        }
    }

    /**
     * Put String value into sharedpreference
     */
    fun putBoolean(key: String?, value: Boolean) {
        try {
            mEditor = mPref!!.edit()
            mEditor?.putBoolean(key, value)
            mEditor?.commit()
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
    }

    /**
     * Get String value from sharedpreference
     */
    fun getBoolean(key: String?): Boolean {
        return try {
            val lvalue: Boolean
            lvalue = mPref!!.getBoolean(key, false)
            lvalue
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
            false
        }
    }

    companion object {
        var mPref: SharedPreferences? = null
        private var mRef: SharedpreferenceUtility?=null

        /**
         * Singleton method return the instance
         */
        fun getInstance(context: Context): SharedpreferenceUtility? {
            if (mRef == null) {
                mRef = SharedpreferenceUtility()
                mPref = context.applicationContext.getSharedPreferences(
                    "MyPref", 0
                )
                return mRef
            }
            return mRef
        }
    }
}