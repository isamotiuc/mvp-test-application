package com.isamotiuc.ebstestapplication.products_list.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "products"
)
data class Product(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,

    @SerializedName("short_description")
    @ColumnInfo(name = "short_description")
    val shortDescription: String,

    @SerializedName("image")
    @ColumnInfo(name = "image")
    val image: String,

    @SerializedName("price")
    @ColumnInfo(name = "price")
    val price: Int,

    @SerializedName("sale_precent")
    @ColumnInfo(name = "sale_percent")
    val salePercent: Int,

    @SerializedName("details")
    @ColumnInfo(name = "details")
    val details: String,

    @ColumnInfo(name = "favorite")
    @Transient var favorite: Boolean
)