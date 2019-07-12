package com.r9software.wall.app.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.r9software.wall.app.R
import com.r9software.wall.app.network.NetworkService
import com.r9software.wall.app.util.afterTextChanged
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private val networkService = NetworkService.getService()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(networkService,compositeDisposable,this))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@RegisterActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@RegisterActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.token != "") {
                updateUiWithUser()
                setResult(Activity.RESULT_OK)
                //Complete and destroy login activity once successful
                finish()
            }
        })

        email.afterTextChanged {
            loginViewModel.registerDataChanged(
                email.text.toString(),
                password.text.toString(),
                password_confirmation.text.toString()
            )
        }
        password_confirmation.afterTextChanged {
            loginViewModel.registerDataChanged(
                email.text.toString(),
                password.text.toString(),
                password_confirmation.text.toString()
            )
        }
        password.apply {
            afterTextChanged {
                loginViewModel.registerDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    password_confirmation.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.register(
                            email.text.toString(),
                            password.text.toString(),
                            password_confirmation.text.toString(),
                            username.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.register(
                    email.text.toString(),
                    password.text.toString(),
                    password_confirmation.text.toString(),
                    username.text.toString()
                )
            }
        }
    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        Toast.makeText(
            applicationContext,
            welcome,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
