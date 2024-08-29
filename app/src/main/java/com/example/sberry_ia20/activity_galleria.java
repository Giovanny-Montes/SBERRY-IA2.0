package com.example.sberry_ia20;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class activity_galleria extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    public static Uri rutados;
    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleria);
        ImageButton btnOpenGallery = findViewById(R.id.btn_open_gallery);
        selectedImage = findViewById(R.id.selected_image);

        btnOpenGallery.setOnClickListener(v -> openGallery());
    }

    public void resultado(View view) {
        Intent resultado = new Intent(this, activity_resultado.class);
        //resultado.putExtra("rutados", rutados);
        startActivity(resultado);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImage.setImageURI(imageUri);
                try {
                    rutados = imageUri;
                    String nom_arch= rutados.toString();
                    Toast.makeText(this, "Se seleccionó la imagen: " + nom_arch.substring(nom_arch.lastIndexOf("/") + 1), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error en el nombre del archivo", LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se pudo seleccionar la imagen", LENGTH_SHORT).show();
            }
        }
    }
    public static Uri getUri() {
        return rutados;
    }
}
