package br.com.fiap.carstore.model.enum

import br.com.fiap.carstore.R

enum class CarBrandsEnum {
    Fiat,
    Honda,
    Hyundai,
    Volkswagen;

    companion object {
        fun getCarModelsByKey(key: String?) = when (key) {
            Honda.name -> R.array.honda_models_array
            Hyundai.name -> R.array.hyundai_models_array
            Volkswagen.name -> R.array.volkswagen_models_array
            else -> {
                R.array.fiat_models_array
            }
        }
    }
}
