package br.com.fiap.carstore.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.fiap.carstore.R
import br.com.fiap.carstore.util.CodeErrorEnum
import br.com.fiap.carstore.databinding.FragmentRegisterBinding
import br.com.fiap.carstore.domain.AuthState

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        initViews()
        initObserver()

        return binding.root
    }

    private fun initViews() = with(binding) {
        btnSignup.setOnClickListener {
            loginViewModel.register(
                email = edtEmailAddress.text.toString(),
                password = edtPassword.text.toString()
            )
        }

        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
        }
    }

    private fun initObserver() {
        loginViewModel.authState.observe(viewLifecycleOwner) { authState ->
            when(authState) {
                is AuthState.Success -> {
                    Toast.makeText(context, resources.getString(R.string.txt_user_registered), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                }
                is AuthState.Error -> {
                    Toast.makeText(context, getMessageError(authState), Toast.LENGTH_SHORT).show()
                }
                is AuthState.Idle -> Unit
            }
        }
    }

    private fun getMessageError(authState: AuthState.Error): String? {
        return when (authState.codeError) {
            CodeErrorEnum.INVALID_EMAIL ->
                context?.getString(R.string.txt_invalid_email)
            CodeErrorEnum.MUST_ENTER_PASSWORD ->
                context?.getString(R.string.txt_must_enter_password)
            else -> {
                authState.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}