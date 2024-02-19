package br.com.fiap.carstore.presentation.carlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.fiap.carstore.R
import br.com.fiap.carstore.databinding.FragmentCarRegisterBinding
import br.com.fiap.carstore.model.Car
import br.com.fiap.carstore.model.enum.CarBrandsEnum
import java.util.UUID

class CarRegisterFragment : Fragment() {

    private var _binding: FragmentCarRegisterBinding? = null
    private val binding get() = _binding!!

    private val carViewModel: CarViewModel by viewModels()

    private var shouldEditCar: Boolean? = false
    private var car: Car? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarRegisterBinding.inflate(inflater, container, false)

        if (arguments?.containsKey(CAR) == true) {
            shouldEditCar = true
            car = arguments?.getSerializable(CAR) as? Car
        }

        initViews()
        setupSpinners()

        return binding.root
    }

    private fun initViews() = with(binding) {
        if (shouldEditCar == true) {
            (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.car_edit_fragment_label)
            btnRegister.text = resources.getString(R.string.txt_save)
            btnRegister.setOnClickListener {
                carViewModel.update(getCar())
                findNavController().navigate(R.id.action_CarRegisterFragment_to_CarListFragment)
            }
        } else {
            btnRegister.setOnClickListener {
                carViewModel.insert(getCar())
                findNavController().navigate(R.id.action_CarRegisterFragment_to_CarListFragment)
            }
        }
    }

    private fun setupSpinners() = with(binding) {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.brands_car_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBrands.adapter = adapter
                spinnerBrands.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        spinnerModels.isEnabled = true
                        spinnerColor.isEnabled = false
                        when (parent.getItemAtPosition(position).toString()) {
                            CarBrandsEnum.Fiat.name ->
                                setupSpinnerModelCars(
                                    CarBrandsEnum.getCarModelsByKey(CarBrandsEnum.Fiat.name))
                            CarBrandsEnum.Honda.name ->
                                setupSpinnerModelCars(
                                    CarBrandsEnum.getCarModelsByKey(CarBrandsEnum.Honda.name))
                            CarBrandsEnum.Hyundai.name ->
                                setupSpinnerModelCars(
                                    CarBrandsEnum.getCarModelsByKey(CarBrandsEnum.Hyundai.name))
                            CarBrandsEnum.Volkswagen.name ->
                                setupSpinnerModelCars(
                                    CarBrandsEnum.getCarModelsByKey(CarBrandsEnum.Volkswagen.name))
                            else -> {
                                spinnerModels.adapter = null
                                spinnerModels.isEnabled = false
                                btnRegister.isEnabled = false
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        spinnerModels.adapter = null
                        spinnerModels.isEnabled = false
                    }
                }

                if (shouldEditCar == true) {
                    spinnerBrands.setSelection(
                        resources.getStringArray(R.array.brands_car_array).indexOf(car?.brand))
                }
            }
        }
    }

    private fun setupSpinnerModelCars(modelsArray: Int) = with(binding) {
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                modelsArray,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerModels.adapter = adapter
                spinnerModels.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        spinnerColor.isEnabled = true
                        btnRegister.isEnabled = true
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        spinnerColor.isEnabled = false
                        btnRegister.isEnabled = false
                    }
                }

                if (shouldEditCar == true) {
                    spinnerModels.setSelection(
                        resources.getStringArray(
                            CarBrandsEnum.getCarModelsByKey(car?.brand)).indexOf(car?.model))
                    spinnerColor.setSelection(
                        resources.getStringArray(R.array.colors_array).indexOf(car?.color))
                }
            }
        }
    }

    private fun getCar(): Car = with(binding) {
        val id = car?.id.takeIf { (shouldEditCar == true) } ?: UUID.randomUUID().toString()
        val brand = spinnerBrands.selectedItem.toString()
        val model = spinnerModels.selectedItem.toString()
        val color = spinnerColor.selectedItem.toString()
        return Car(id = id, brand = brand, model = model, color = color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAR = "car"
    }
}