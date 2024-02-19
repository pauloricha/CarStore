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
import br.com.fiap.carstore.databinding.FragmentLoginBinding
import br.com.fiap.carstore.domain.AuthState
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        checkIsLoggedIn()
        initViews()
        initObserver()

        return binding.root
    }

    private fun checkIsLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(R.id.action_LoginFragment_to_CarListFragment)
        }
    }

    private fun initViews() = with(binding) {
        btnSignin.setOnClickListener {
            loginViewModel.login(
                email = edtEmailAddress.text.toString(),
                password = edtPassword.text.toString()
            )
        }

        tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }

    private fun initObserver() {
        loginViewModel.authState.observe(viewLifecycleOwner) { authState ->
            when(authState) {
                is AuthState.Success -> {
                    findNavController().navigate(R.id.action_LoginFragment_to_CarListFragment)
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
                resources.getString(R.string.txt_invalid_email)
            CodeErrorEnum.MUST_ENTER_PASSWORD ->
                resources.getString(R.string.txt_must_enter_password)
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