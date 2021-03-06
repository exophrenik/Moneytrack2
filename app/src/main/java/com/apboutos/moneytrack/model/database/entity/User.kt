package com.apboutos.moneytrack.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

/**
 * Represents a User. Users are stored in the local database to
 * enable offline login.
 */
@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = false)
    @Expose var username         : String,
    @Expose var password         : String,
    @Expose var email            : String,
    var registrationDate : String,  // format: yyyy-MM-dd
    var lastLogin        : String)  // format: yyyy-MM-dd HH-mm-ss
{

}