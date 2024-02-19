package br.com.fiap.carstore.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "car")
data class Car(

    @PrimaryKey
    @ColumnInfo(name = "car_id")
    @SerializedName("id")
    val id : String,
    @ColumnInfo(name = "car_model")
    @SerializedName("model")
    val model: String,
    @ColumnInfo(name = "car_brand")
    @SerializedName("brand")
    val brand: String,
    @ColumnInfo(name = "car_color")
    @SerializedName("color")
    val color: String
) : Serializable {

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Car> =
            object : DiffUtil.ItemCallback<Car>() {
                override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
                    return oldItem == newItem
                }
            }
    }
}