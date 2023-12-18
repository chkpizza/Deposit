package com.wantique.auth.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wantique.auth.BuildConfig
import com.wantique.auth.R
import com.wantique.auth.databinding.FragmentAuthBinding
import com.wantique.auth.di.AuthComponentProvider
import com.wantique.base.ui.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthFragment : BaseFragment<FragmentAuthBinding>(R.layout.fragment_auth) {
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<AuthViewModel>(R.id.auth_nav_graph) { factory }

    private val TAG = "AuthFragmentTag"

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val oneTapClientResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                credential.googleIdToken?.let {
                    signInWithCredential(it)
                } ?: run {
                    Log.d(TAG, "credential is null")
                }
            } catch (e: ApiException) {
                Log.d(TAG, e.localizedMessage.toString())
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as AuthComponentProvider).getAuthComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInsets()
        setUpGoogleSignIn()
        setUpViewListener()
        setUpNavigateObserver()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            view.updatePadding(
                top = insets.systemWindowInsets.top,
                bottom = insets.systemWindowInsets.bottom
            )
            insets
        }
    }

    private fun setUpGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build(),
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun setUpViewListener() {
        binding.authBtnGoogleSignIn.setOnClickListener {
            oneTapClient
                .beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        oneTapClientResult.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                    } catch (e: IntentSender.SendIntentException) {
                        Log.d(TAG,"Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    Log.d(TAG, e.localizedMessage.toString())
                }
        }
    }

    private fun signInWithCredential(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                viewModel.isValidUser()
            }
        }
    }

    private fun setUpPreferences() {
        lifecycleScope.launch {
            requireActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("SIGN_IN", true)
                .apply()

            navigator.navigateToContent()
        }
    }

    private fun setUpNavigateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToHome.collect {
                    setUpPreferences()
                }
            }
        }
    }
}