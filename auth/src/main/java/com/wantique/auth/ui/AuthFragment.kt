package com.wantique.auth.ui

import android.app.Activity.RESULT_OK
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
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

class AuthFragment : Fragment() {
    private val TAG = "AuthFragmentTag"
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInsets()
        setUpGoogleSignIn()
        setUpViewListener()
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
                Log.d(TAG, "uid: ${Firebase.auth.uid.toString()}")
            }
        }
    }

}