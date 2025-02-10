package com.example.amai_b;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

public class activity_usuario extends AppCompatActivity {

    private static final int RC_REAUTH = 123;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private Button logoutButton;
    private Button deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario);

        // Initialize views
        userNameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        logoutButton = findViewById(R.id.btnLogout);
        deleteAccountButton = findViewById(R.id.btnEliminar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        loadUserData(); // Load user data

        logoutButton.setOnClickListener(v -> logout());
        deleteAccountButton.setOnClickListener(v -> deleteAccount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserAuthentication();
    }

    private void checkUserAuthentication() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);
        if (user == null && googleUser == null) {
            redirectToLogin();
        }
    }


    private void loadUserData() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);

        if (user != null) {
            userNameTextView.setText(user.getDisplayName());
            userEmailTextView.setText(user.getEmail());
        } else if (googleUser != null) {
            userNameTextView.setText(googleUser.getDisplayName());
            userEmailTextView.setText(googleUser.getEmail());
        }
    }

    private void logout() {
        firebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                redirectToLogin();
            } else {
                showError("Error al cerrar sesión");
            }
        });
    }


    private void deleteAccount() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);

        if (currentUser != null) {
            deleteFirebaseAccount(currentUser);
        } else if (googleUser != null) {
            deleteGoogleAccount(googleUser);
        } else {
            showError("No user is currently signed in.");
        }
    }

    private void deleteFirebaseAccount(FirebaseUser currentUser) {
        currentUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                handleGoogleRevoke(); // Revoke Google access if applicable
                redirectToLogin();
            } else if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                reauthenticateUser(currentUser);
            } else {
                showError("Error deleting Firebase account: " + task.getException().getMessage());
            }
        });
    }

    private void deleteGoogleAccount(GoogleSignInAccount googleUser) {
        googleSignInClient.revokeAccess().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                redirectToLogin();
            } else {
                showError("Error deleting Google account: " + task.getException().getMessage());
            }
        });
    }


    private void reauthenticateUser(FirebaseUser user) {
        for (UserInfo provider : user.getProviderData()) {
            if (provider.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) {
                reauthenticateGoogle();
                return;
            } else if (provider.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)) {
                showEmailReauthDialog(user);
                return;
            }
        }
    }

    private void reauthenticateGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_REAUTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_REAUTH) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showError("Error in Google re-authentication: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        deleteAccount(); // Retry delete after reauth
                    } else {
                        showError("Re-authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showEmailReauthDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");

        builder.setTitle("Re-authentication required")
                .setView(passwordInput)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String password = passwordInput.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                    user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                        if (reauthTask.isSuccessful()) {
                            deleteAccount(); // Retry delete after reauth
                        } else {
                            showError("Incorrect password");
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void handleGoogleRevoke() {
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            googleSignInClient.revokeAccess().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    showError("Error revoking Google access: " + task.getException().getMessage());
                }
            });
        }
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, loginActivity.class));
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}