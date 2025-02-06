package com.example.amai_b;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import com.example.amai_b.ml.Modelodos;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class activity_resultado extends AppCompatActivity {
    String desde_camara="";
    ImageView muestra;
    TextView texto_clase;
    Uri ruta_principal;
    InputStream inputStream = null;
    TextView texto_probabilidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        muestra = findViewById(R.id.selected_image);
        texto_clase =findViewById(R.id.texto_clase);
        texto_probabilidad=findViewById(R.id.texto_probabilidad);
        Intent intent = getIntent();

        // Desde Cámara(PATH)
        String ruta = intent.getStringExtra("ruta");
        Uri ruta2=activity_galleria.getUri();//recuperar Uri desde la galería

        if (ruta == null) {
            if (ruta2 == null) {
                Toast.makeText(this, "No se recibió ningún archivo.", Toast.LENGTH_LONG).show();
                                }
            else{

                String nom_arch=ruta2.toString();
                //Toast.makeText(this, "Se recibió el archivo desde galería: " + nom_arch.substring(nom_arch.lastIndexOf("/") + 1), Toast.LENGTH_LONG).show();
                muestra.setImageURI(ruta2);
                desde_camara="N";

                }
        }
        else{
                //Toast.makeText(this, "Se recibió el archivo desde cámara: " + ruta.substring(ruta.lastIndexOf("/") + 1), Toast.LENGTH_LONG).show();
                muestra.setImageURI(Uri.fromFile(new File(ruta)));
//                ruta_principal=Uri.fromFile(new File(ruta));
                desde_camara="Y";
             }

        Bitmap imagen_bit= ((BitmapDrawable)muestra.getDrawable()).getBitmap();
        int newWidth = 0;
        int newHeight = 0;
        int startX = 0;
        int startY = 0;

        try {
            if (imagen_bit != null && imagen_bit.getWidth() > 0 && imagen_bit.getHeight() > 0) {
                // Paso 1: Recortar la imagen a un cuadrado de 419x419 píxeles
                int width = imagen_bit.getWidth();
                int height = imagen_bit.getHeight();
                if (width > height) {
                    int m = width / 2;
                    startX = m - (height / 2);
                    newHeight = height;
                    newWidth = height;
                } else if (width < height) {
                    int n = height / 2;
                    startY = n - (width / 2);
                    newHeight = width;
                    newWidth = width;
                } else { // width == height
                    newWidth = width;
                    newHeight = height;
                }
            } else {
                Toast.makeText(this, "Error en la imagen, no se puede redimensionar", Toast.LENGTH_SHORT).show();
            };

                Bitmap croppedBitmap = Bitmap.createBitmap(imagen_bit, startX, startY, newWidth, newHeight);

                // Paso 2: Redimensionar la imagen recortada a 224x224 píxeles
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, 224, 224, true);
                //muestra.setImageBitmap(scaledBitmap);
//comienza la implementacion del modelo
                try {
                    Modelodos model = Modelodos.newInstance(getApplicationContext());

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*224*224*3);//TensorImage.fromBitmap(scaledBitmap).getBuffer()
                    byteBuffer.order(ByteOrder.nativeOrder());
                    //
                    int[] intValues = new int[scaledBitmap.getWidth() * scaledBitmap.getWidth()];
                    scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
                    int pixel = 0;
                    //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                    for(int i = 0; i < scaledBitmap.getWidth(); i ++){
                        for(int j = 0; j < scaledBitmap.getWidth(); j++){
                            int val = intValues[pixel++]; // RGB
                            byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                            byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                            byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                        }
                    }
                    //Creates inputs for reference.

                    //
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Modelodos.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    String cadena = getMax(outputFeature0.getFloatArray());
                    String clase_final = cadena.substring(0, cadena.lastIndexOf("/"));
                    String probabilidad_final = cadena.substring(cadena.lastIndexOf("/") + 1);

                    texto_clase.setText(clase_final);
                    texto_probabilidad.setText(probabilidad_final);
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                    Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Toast.makeText(this, "IOException2", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

//Terimna la implementacion del modelo
}




    public void regresar_menu(View view) {
        Intent regresarMenu = new Intent(this, activity_eleccion.class);
        startActivity(regresarMenu);
    }
    public String getMax(float[] array) {
        int index = 0;
        float max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index=i;
            }
        }

        String[] classNames  ={ "antracnosis","deficiencia de calcio","desconocido", "fruto saludable",  "hoja saludable", "mancha angular","moho gris del fruto", "oídio en fruto", "oídio en hoja","quemadura de la hoja", "viruela"};

        return classNames[index]+ "/" +max;
    }

}
