package br.com.fiap.carstore.presentation.carlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.carstore.R
import br.com.fiap.carstore.databinding.FragmentCarListBinding
import br.com.fiap.carstore.model.Car
import br.com.fiap.carstore.presentation.carlist.adapter.CarListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CarListFragment : Fragment() {

    private var _binding: FragmentCarListBinding? = null
    private val binding get() = _binding!!

    private val carViewModel: CarViewModel by viewModels()

    private val carListAdapter: CarListAdapter by lazy {
        CarListAdapter(
            onCarEdit = {car ->
                val bundle = Bundle()
                bundle.putSerializable(CAR, car)
                findNavController().navigate(
                    R.id.action_CarListFragment_to_CarRegisterFragment,
                    bundle
                )
            },
            onCarDelete = {
                dialogDelete(it).show()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCarListBinding.inflate(inflater, container, false)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            displayOptions
        }

        initViews()
        initObserver()

        return binding.root
    }

    private fun initViews() = with(binding) {
        setUpRecyclerView()

        btnAddCar.setOnClickListener {
            findNavController().navigate(R.id.action_CarListFragment_to_CarRegisterFragment)
        }

        btnLogout.setOnClickListener {
            dialogLogout().show()
        }
    }

    private fun setUpRecyclerView() = with(binding.rvCars) {
        adapter = carListAdapter
        layoutManager = LinearLayoutManager(context)
    }

    private fun initObserver() {
        carViewModel.cars.observe(viewLifecycleOwner) { cars ->
            cars?.let { carListAdapter.submitList(cars) }
        }
    }

    private fun dialogDelete(car: Car): AlertDialog {
        return AlertDialog.Builder(context)
            .setMessage(resources.getString(R.string.txt_delete_this_car))
            .setPositiveButton(resources.getString(R.string.txt_delete)) { dialog, _ ->
                carViewModel.delete(car)
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.txt_cancel)) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()
    }

    private fun dialogLogout(): AlertDialog {
        return AlertDialog.Builder(context)
            .setMessage(resources.getString(R.string.txt_want_to_log_out))
            .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, _ ->
                Firebase.auth.signOut()
                findNavController().navigate(R.id.action_CarListFragment_to_LoginFragment)
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.txt_cancel)) {
                    dialog, _ -> dialog.dismiss()
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAR = "car"
    }
}