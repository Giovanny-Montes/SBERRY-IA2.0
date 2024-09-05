package com.example.sberry_ia20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class activity_usuario extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView userNameTextView = findViewById(R.id.userName);
        TextView userEmailTextView = findViewById(R.id.userEmail);
        //ImageView userImageView = findViewById(R.id.userImagen);
        Button logoutButton = findViewById(R.id.btnLogout);
        Button deleteAccountButton = findViewById(R.id.btnEliminar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Cargar los datos de usuario si están disponibles

        FirebaseUser user = firebaseAuth.getCurrentUser();
        GoogleSignInAccount user2 = GoogleSignIn.getLastSignedInAccount(this);

        if (user != null || user2 != null) {
            if (user != null) {
                userNameTextView.setText(user.getDisplayName());
                userEmailTextView.setText(user.getEmail());
                // Falta cargar image de usuario pero es inproductivo de momento
            } else {
                userNameTextView.setText(user2.getDisplayName());
                userEmailTextView.setText(user2.getEmail());
            }
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)// Use string resource for client ID
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, loginActivity.class)); // Start LoginActivity
                    finish();
                } else {
                    Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        deleteAccountButton.setOnClickListener(v -> {
            if(user != null){
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(activity_usuario.this, "Firebase: Cuenta eliminada", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_usuario.this,loginActivity.class));
                    finish();
                } else {
                    Toast.makeText(activity_usuario.this, "Firebase: Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                }
            });}
            else{
                googleSignInClient.revokeAccess().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_usuario.this, "Google: Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity_usuario.this,loginActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(activity_usuario.this, "Google: Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();

                }
                });

                }

         });






}
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        GoogleSignInAccount user2 = GoogleSignIn.getLastSignedInAccount(this);
        if (user == null && user2 == null) {
            startActivity(new Intent(this, loginActivity.class)); // Start LoginActivity
            finish();
        }
    }

}