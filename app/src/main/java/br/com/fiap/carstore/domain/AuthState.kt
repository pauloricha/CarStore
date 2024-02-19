package br.com.fiap.carstore.domain

import br.com.fiap.carstore.Util.CodeErrorEnum

sealed class AuthState {
    object Idle : AuthState()
    object Success : AuthState()
    class Error(val codeError: CodeErrorEnum ?= null, val message: String ?= null) : AuthState()
}