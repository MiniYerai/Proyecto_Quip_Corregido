package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;

/**
 * Created by Ruben on 27/11/2016.
 */

public class CrearPDF extends AppCompatActivity {

    //PDF
    private final static String NOMBRE_DIRECTORIO = "PDF_Quip";
    private final static String NOMBRE_DOCUMENTO = "NotaPDF";
    private final static String EXTENSION_DOCUMENTO = ".pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    private static int c = 1;

    protected void crearPDF2(EditText editTextTitulo, String titulo, Bitmap imag, String nota) {

        if(editTextTitulo.getText().toString().length()>0) {

            // Creamos el documento.
            com.lowagie.text.Document documento = new com.lowagie.text.Document();
            try {

                // Creamos el fichero con el nombre que deseemos.
                String fichero = getNombreValido();
                File f = crearFichero(fichero);

                // Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
                FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

                // Asociamos el flujo que acabamos de crear al documento.
                com.lowagie.text.pdf.PdfWriter writer = com.lowagie.text.pdf.PdfWriter.getInstance(documento, ficheroPdf);

                // Incluimos el pie de pagina y una cabecera
                HeaderFooter pie = new HeaderFooter(new Phrase("Desarrollado por Rubén, Virginia y Andrés.                 " +
                        "         I.E.S Zaidin-Vergeles"), false);

                documento.setFooter(pie);

                // Abrimos el documento.
                documento.open();

                // Añadimos un titulo con una fuente personalizada.
                Font font = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.BLACK);
                documento.add(new com.lowagie.text.Paragraph(titulo, font));

                // Añadimos un espacio para que quede mejor xD
                String espacio = " ";
                Font fontE = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.BLACK);
                documento.add(new com.lowagie.text.Paragraph(espacio, fontE));

                // Añadimos la descripción de la nota.
                String descripcion = nota;
                Font fontD = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.NORMAL, Color.BLACK);
                documento.add(new com.lowagie.text.Paragraph(descripcion, fontD));

                //Añadimos la imagen con tamaño inferior
                if (imag != null) {
                    Bitmap bitmap = imag;
                    int origWidth = bitmap.getWidth();
                    int origHeight = bitmap.getHeight();
                    final int destWidth = 450;//or the width you need
                    if (origWidth > destWidth) {
                        int destHeight = origHeight / (origWidth / destWidth);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                        Bitmap auxB = Bitmap.createScaledBitmap(bitmap, 450, destHeight, false);
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        auxB.compress(Bitmap.CompressFormat.JPEG, 60, stream1);
                        Image imagen = Image.getInstance(stream1.toByteArray());
                        documento.add(imagen);
                    }
                }


                /*
                // Añadimos la imagen
                if(nota.getImagen()!=null) {
                    Image imagen = Image.getInstance(nota.getImagen());
                    documento.add(imagen);
                }
                */

            } catch (com.lowagie.text.DocumentException e) {

                Log.e(ETIQUETA_ERROR, e.getMessage());

            } catch (IOException e) {

                Log.e(ETIQUETA_ERROR, e.getMessage());

            } finally {

                // Cerramos el documento.
                documento.close();
            }
        }
    }

    // Crea un fichero con el nombre que se le pasa a la funcion y en la ruta especificada.

    public File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    // Hacemos una función para que no sobreescriba el documento
    public String getNombreValido() {
        File ruta = getRuta();
        File f = null;
        String nombre = NOMBRE_DOCUMENTO + EXTENSION_DOCUMENTO;
        f = new File(ruta, nombre);
        if (!f.exists()) {
            nombre = NOMBRE_DOCUMENTO + EXTENSION_DOCUMENTO;
        } else {
            c++;
            nombre = NOMBRE_DOCUMENTO + c + EXTENSION_DOCUMENTO;
        }
        return nombre;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     */
    public File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdir()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
}
