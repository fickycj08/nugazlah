package org.d3ifcool.nugazyuk.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Token")
data class Token(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var token: String,
    var userId: String
)