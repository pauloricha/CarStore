package br.com.fiap.carstore.presentation.carlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.carstore.databinding.CarItemBinding
import br.com.fiap.carstore.model.Car

class CarListAdapter(
    private val onCarEdit: (Car) -> Unit,
    private val onCarDelete: (Car) -> Unit
) : ListAdapter<Car, CarListViewHolder>(Car.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val binding =
            CarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CarListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bind(getItem(position), onCarEdit, onCarDelete)
    }
}

class CarListViewHolder(
    private val binding: CarItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        car: Car,
        onCarEdit: (Car) -> Unit,
        onCarDeleted: (Car) -> Unit
    ) {
        with(binding) {
            tvBrand.text = car.brand
            tvModel.text = car.model
            tvColor.text = car.color

            root.setOnClickListener {
                onCarEdit(car)
            }

            btnDelete.setOnClickListener {
                onCarDeleted(car)
            }
        }
    }
}