package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import harmony.java.awt.Color;
import ruben.dam.izv.com.proyecto_0.R;
import ruben.dam.izv.com.proyecto_0.databinding.ActivityNotaBinding;
import ruben.dam.izv.com.proyecto_0.vistas.adaptadores.AdaptadorRvLista;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.dialogo.DialogoBorrarItemLista;
import ruben.dam.izv.com.proyecto_0.vistas.dialogo.DialogoColor;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;
import ruben.dam.izv.com.proyecto_0.vistas.util.UtilFecha;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.audios.VistaAudio;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.listas.ModeloListaNota;



public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista, TextToSpeech.OnInitListener/*, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener */{
    //listas
    private int tieneLista;
    private LinearLayout ly;
    private ArrayList<EditText> listaET;
    private ArrayList<ElementoLista> arrayLi;
    private RecyclerView rv;
    private ImageButton bguardar;
    private Button anadirItem;
    private ModeloListaNota mln;
    private LinearLayout lyLi;
    AdaptadorRvLista adaptador;


    //Hablar
    private TextToSpeech textToSpeech;

    //Crear PDF
    private ImageButton btnpdf;
    //private  CrearPDF pdf;

    //PDF
    private final static String NOMBRE_DIRECTORIO = "PDF_Quip";
    private final static String NOMBRE_DOCUMENTO = "NotaPDF";
    private final static String EXTENSION_DOCUMENTO = ".pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    private static int c = 1;

    //llamo a la clase MagicalCamera
    private MagicalCamera magicalCamera;

    //ACTIVITY RESOULT CODE (PARA SABER LA RESPUESTA QUE LE PIDO)
    private int RESIZE_PHOTO_PIXELS_PRECENTAGE = 1000;

    //EDITOR DE TEXTO
    //private EditText editTextTitulo, editTextNota;

    //TEXT VIEW
    //private TextView tvFecha, tvColor;

    //IMAGEN VIEW
    private ImageView img;

    //CLASES POJO
    private Nota nota = new Nota();
    private ElementoLista lista = new ElementoLista();

    //PRESENTADOR NOTA
    private PresentadorNota presentador;

    //BOTTOM SHEET
    private BottomSheetBehavior mBottomSheetBehavior;

    //guardara la ruta para la Base de Datos
    private String ruta="X";

    //PERMISOS
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2 ;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3 ;
    private static final int MY_PERMISSIONS_WRITE_SYNC_SETTINGS = 4;
    private static final int MY_PERMISSIONS_READ_SYNC_SETTINGS = 5;
    private static final int MY_PERMISSIONS_INTERNET = 6;

    //DEVUELVE FECHAS
    private UtilFecha fecha;

    private Boolean borrado = false;

    //LOCALIZACIÓN GOOGLE
  //  private GoogleMap mMap;
  //  private GoogleApiClient googleApiClient;

    //DATABINDING
    private ActivityNotaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nota);


        Bundle ex = getIntent().getExtras();
        if (ex != null) {
            nota = (Nota) ex.get("nota");
            Log.d("id", String.valueOf( nota.getId()));
        }

        binding.setNota(nota);

        mln = new ModeloListaNota(this);
        tieneLista = 0;
        arrayLi = new ArrayList<>();
        arrayLi = mln.arrayNota(nota.getId());
        if (arrayLi != null) {
            presentador = new PresentadorNota(this);


            rv = (RecyclerView) findViewById(R.id.rvLista);
            rv.setLayoutManager(new LinearLayoutManager(this));
            adaptador = new AdaptadorRvLista(arrayLi);


            adaptador.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(VistaNota.this, "Editar", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            adaptador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(VistaNota.this, "Yo podria borrar al item " + position + " con la id : " + id, Toast.LENGTH_SHORT).show();
                    System.out.println("Yo podria borrar al item " + position + " con la id : " + id);

                    System.out.println("soy  la id : " + arrayLi.get(position).getId() + " de la nota  :" + arrayLi.get(position).getId_nota() + "y mi texto dice..... " + arrayLi.get(position).getTexto());
                    mln.deleteItem((int) arrayLi.get(position).getId());
                    arrayLi.remove(position);


                }
            });


            rv.setAdapter(adaptador);
            rv.setVisibility(View.VISIBLE);


            textToSpeech = new TextToSpeech(VistaNota.this, this);

            //editTextTitulo = (EditText) findViewById(R.id.etTitulo);
            //editTextNota = (EditText) findViewById(R.id.etNota);

            //tvColor = (TextView) findViewById(R.id.color);
            //tvFecha = (TextView) findViewById(R.id.tvFecha);

            //btnpdf = (ImageButton) findViewById(R.id.btnPDF);
            //anadirItem = (Button) findViewById(R.id.btAnadirItem);
            //bguardar = (ImageButton) findViewById(R.id.bGuardar);


            //muestra el texto traducido


            //recoge los permisos
            //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //comprueba de que ha solicitado los permisos
            HiloPermisos HP = new HiloPermisos();
            HP.execute();

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.get("voz") != null) {
                    if (savedInstanceState != null) {
                        nota = savedInstanceState.getParcelable("nota");
                    } else {
                        Bundle b = getIntent().getExtras();
                        if (b != null) {
                            nota = b.getParcelable("nota");
                        }
                    }
                }
            }
            colores();
        /*if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment, "MainFragment")
                    .commit();
        }*/
            mostrarNota(nota);

            //BOTTOM BAR
            //bottomSheet();

            //Añadir Imagen
            presentador.onAddImg();

            //Añadir Audio
            addAudio();

            //Mostrar imagen
            //mostrarIMG();
            //lista
            addLista();

            HiloTexto ht = new HiloTexto();
            ht.execute();

            //Crear PDF
            crearPDF2();
        /*btnpdf.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  pdf = new CrearPDF();
                  Bitmap bit=MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(nota.getImagen()));
                  pdf.crearPDF2(editTextTitulo, nota.getTitulo(), bit, nota.getNota());

              }
        });*/

            //Hablar
            HiloHablar hh = new HiloHablar();
            hh.execute();

            //Borrar imagen
            borrarImagen();

            //Cambia de color
            color();

            //Localización
            //  googleGPS();
        }
    }

   /* private void googleGPS(){
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }*/

    private void crearPDF2() {
        if (binding.etTitulo.getText().toString().length() > 0) {
            //ImageButton btnpdf = (ImageButton) findViewById(R.id.btnPDF);
            binding.btnPDF.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Creamos el documento.
                    com.lowagie.text.Document documento = new com.lowagie.text.Document();
                    try {

                        // Creamos el fichero con el nombre que deseemos.
                        String fichero = getNombreValido();
                        File f = crearFichero(fichero);

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(), "PDF creado", Toast.LENGTH_SHORT);

                        toast1.show();

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
                        documento.add(new com.lowagie.text.Paragraph(nota.getTitulo(), font));

                        // Añadimos un espacio para que quede mejor xD
                        String espacio = " ";
                        Font fontE = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.BLACK);
                        documento.add(new com.lowagie.text.Paragraph(espacio, fontE));

                        // Añadimos la descripción de la nota.
                        String descripcion = nota.getNota();
                        Font fontD = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.NORMAL, Color.BLACK);
                        documento.add(new com.lowagie.text.Paragraph(descripcion, fontD));

                        //Añadimos la imagen con tamaño inferior
                        if (nota.getImagen() != null) {
                            Bitmap bitmap = MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(nota.getImagen()));
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            Image imagen = Image.getInstance(stream.toByteArray());
                            documento.add(imagen);
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
            });
        }
    }

    private void color() {
        //ImageButton color = (ImageButton) findViewById(R.id.ibColor);
        binding.ibColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(VistaNota.this, "Entra", Toast.LENGTH_SHORT).show();
                FragmentManager ft = getSupportFragmentManager();
                FragmentManager fragmentManager = getSupportFragmentManager();
                new DialogoColor(binding.etTitulo, binding.etNota, binding.color).show(ft, "DialogoColor");

            }
        });
    }
    private void colores(){
        String co=nota.getColor();
        if(co!=null) {
            switch (co) {
                case "rojo":
                    binding.etTitulo.setBackgroundResource(R.color.rojo);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul":
                    binding.etTitulo.setBackgroundResource(R.color.azul);
                    binding.etTitulo.setTextColor(Color.BLACK.getRGB());
                    break;
                case "verde":
                    binding.etTitulo.setBackgroundResource(R.color.verde);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "morado":
                    binding.etTitulo.setBackgroundResource(R.color.morado);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "magenta":
                    binding.etTitulo.setBackgroundResource(R.color.magenta);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "gris":
                    binding.etTitulo.setBackgroundResource(R.color.gris);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "azul_oscuro":
                    binding.etTitulo.setBackgroundResource(R.color.azul_oscuro);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
                case "naranja":
                    binding.etTitulo.setBackgroundResource(R.color.naranja);
                    binding.etTitulo.setTextColor(Color.WHITE.getRGB());
                    break;
            }
        }
    }

    /*private void bottomSheet() {
        //final View bottomSheet = findViewById( R.id.activity_dialogo_fragment );

        ImageButton bMas=(ImageButton) findViewById(R.id.IBMas2);
        ImageButton bSou=(ImageButton) findViewById(R.id.IBSouce);

//--------------------------------------------------------------------------------------------------

        bMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bsdFragment =
                        DialogoFragment.newInstance();

                bsdFragment.show(VistaNota.this.getSupportFragmentManager(), "BSDialog");
            }
        });
//--------------------------------------------------------------------------------------------------
    }*/

    protected void tipo(String t){
        if(t.equals("img")) {
            mostrarImagen();
        }
    }
    private void borrarImagen(){
        if(img!=null) {
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Representa a un conjunto de caracteres
                    final CharSequence[] opciones = {"Modificar Imagen", "Borrar Imagen", "Cancelar"};
                    //Parametro de dialogo
                    final AlertDialog.Builder builder = new AlertDialog.Builder(VistaNota.this);
                    //Titulo del Dialogo
                    builder.setTitle("Opciones de Imagen");
                    //Muestra las opciones
                    builder.setItems(opciones, new DialogInterface.OnClickListener() {
                        @Override//Detecta la opción que hayamos elegido
                        public void onClick(DialogInterface dialog, int opcion) {
                            if (opciones[opcion] == "Modificar Imagen") {
                                addImagen();
                                borrado=false;
                            } else if (opciones[opcion] == "Borrar Imagen") {
                               img.setVisibility(View.GONE);
                                borrado=true;
                            } else if (opciones[opcion] == "Cancelar") {
                                //desaparece el dialogo
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show(); //muestra el dialogo
                    return false;
                }
            });
        }
    }

    private void permisos(){
        //PERMISO DE ESCRITURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
        //PERMISO DE LECTURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }
        //PERMISO DE CAMARA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }

        //PERMISO DE SINCRONIZACION DE ESCRITURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_SYNC_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_SYNC_SETTINGS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_SYNC_SETTINGS},
                        MY_PERMISSIONS_WRITE_SYNC_SETTINGS);

            }
        }
        //PERMISO DE SINCRONIZACION DE LECTURA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SYNC_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SYNC_SETTINGS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SYNC_SETTINGS},
                        MY_PERMISSIONS_READ_SYNC_SETTINGS);

            }
        }
        //PERMISO DE INTERNET
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_INTERNET);

            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_WRITE_SYNC_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_READ_SYNC_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    private void addAudio(){
        ImageButton btAudi=(ImageButton) findViewById(R.id.btAddAudio);

        btAudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.onAddAudio();
            }
        });

    }

    private void addLista() {
lyLi= (LinearLayout)findViewById(R.id.lyFragment);
        lyLi.setVisibility(View.VISIBLE);

        rv.setVisibility(View.VISIBLE);

        /*guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listaET.size(); i++) {
                    //if (!listaET.get(i).getText().equals("") && listaET.get(i).getText() != null) {
                        ElementoLista e = new ElementoLista();
                        e.setTexto(listaET.get(i).getText().toString());
                        e.setId_nota(nota.getId());
                        Toast.makeText(VistaNota.this, nota.getId() + "", Toast.LENGTH_SHORT).show();
                        long in = presentador.onSaveLista(e);
                        Toast.makeText(VistaNota.this, in + "", Toast.LENGTH_SHORT).show();
                    //}
                }

            }
        });
        guardar.setVisibility(View.INVISIBLE);
        */
        ImageButton btLi = (ImageButton) findViewById(R.id.btAddLista);

        btLi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usoLista();
            }
        });

    }
    //-----------dialogo borrar lista
    /*@Override
    public void mostrarConfirmarBorrarNota(ElementoLista e ) {//Llama a DialogoBorrar
        DialogoBorrarItemLista fragmentBorrar = DialogoBorrarItemLista.newInstance(e);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");



    @Override
    public void onBorrarPossitiveButtonClick(ElementoLista e ) {
        presentador.onDeleteItemLista(e);
    }//lo borra si le da a ACEPTAR

    @Override
    public void onBorrarNegativeButtonClick() {//no hace nada si le da CANCELAR

    }
*/
    public void TextAudio(){


        /*if(!equals(audio.notaA)) {
            String voz2=audio.notaA;
            Toast.makeText(VistaNota.this, voz2, Toast.LENGTH_LONG).show();
        }*/

        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            if(extras.get("voz")!=null) {
                String voz = (String) extras.get("voz");//Obtengo la voz
                binding.etNota.setText(voz);

                //audi=voz;
                //Toast.makeText(VistaNota.this, voz, Toast.LENGTH_LONG).show();
            }
        }/*else {
            Toast.makeText(VistaNota.this,"Error",Toast.LENGTH_LONG).show();
        }*/

    }
    private void usoLista() {

        ly = (LinearLayout) findViewById(R.id.lyListaET);
        listaET = new ArrayList<>();
        Button ba = (Button) findViewById(R.id.btAnadirItem);
        ba.setVisibility(View.VISIBLE);
        rv.setVisibility(View.VISIBLE);

        ba.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                EditText et = new EditText(v.getContext());

                et.setLayoutParams(lp);
                et.setHint("escriba aqui");
                ly.addView(et);
                listaET.add(et);
               /* if(listaET.size()>1){
                    listaET.get(listaET.size()-1).setText( listaET.get(listaET.size()-1).getText());
                }
                Toast.makeText(VistaNota.this, listaET.get(listaET.size()-1).getText()+"", Toast.LENGTH_SHORT).show();*/



            }
        });


    }
    private void hablar() {

        ImageButton btnHablar = (ImageButton) findViewById(R.id.btnHablar);
        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etNota.getText().toString().length()>0) {
                    textToSpeech.setLanguage(new Locale("spa", "ESP"));
                    speak(binding.etNota.getText().toString());
                }
            }

            private void speak(String str){

                textToSpeech.speak( str, TextToSpeech.QUEUE_FLUSH, null );
                textToSpeech.setSpeechRate( 0.0f );
                textToSpeech.setPitch( 0.0f );
            }
        });
    }

    @Override
    protected void onDestroy(){

        if ( textToSpeech != null )
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        VistaNota.super.onDestroy();
    }


    @Override
    public void onInit(int status) {
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText( VistaNota.this, "ERROR", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override //Para saber de donde viene la peticion.
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==-1) {
            borrado=false;
            img.setVisibility(View.VISIBLE);
            magicalCamera.resultPhoto(requestCode, resultCode, data);

            img.setImageBitmap(scaleDownBitmap(magicalCamera.getMyPhoto(),100,getApplicationContext()));
            ruta = MagicalCamera.bytesToStringBase64(MagicalCamera.bitmapToBytes(scaleDownBitmap(magicalCamera.getMyPhoto(),100,getApplicationContext()), MagicalCamera.PNG));
            magicalCamera.savePhotoInMemoryDevice(magicalCamera.getMyPhoto(), "nota", "Fotos de Quip", MagicalCamera.PNG, true);
        }else{
            Toast.makeText(this, "Error de imagen "+resultCode, Toast.LENGTH_SHORT).show();
        }
    }
    //redimensiona la imagen para mostrar cualquier resolucion !!!IMPORTANTE PARA EL BITMAP¡¡¡
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
    //CONVERSOR DE BITMAP A URI
    private Uri BitmapUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //convertidor de BitMap a String
    private String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        /*ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,60, baos);*/

        //byte [] b=baos.toByteArray();
        //Bitmap bb=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        StringBuilder base64 = new StringBuilder(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //String temp= Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("LOOK", base64.toString());
        //ruta=temp;
        return base64.toString();
    }

    //Convertidor de String a Bitmap
    public Bitmap StringToBitMap(String encodedString){

        byte[] byteArray = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        /*try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }*/
    }


    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);

        String rut= ruta;
        outState.putString("rut",rut);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String rut= savedInstanceState.getString("rut");
        if (rut!="X"){
            img.setImageBitmap(MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(rut)));
            ruta=rut;
        }
    }

    @Override
    public void mostrarNota(Nota n) {// muestra la nota entera para modificarla


        // $$$$$$$$$
        //muestra la imagen
        if(nota.getImagen()!=null) {
            if(!nota.getImagen().equals("X")) {
                img = (ImageView) findViewById(R.id.iv);

                //Bitmap aux = StringToBitMap(nota.getImagen());
                //Uri aux = StringToUri(nota.getImagen());
                //img.setImageBitmap(Uri.parse(nota.getImagen()));
                img.setImageBitmap(MagicalCamera.bytesToBitmap(MagicalCamera.stringBase64ToBytes(nota.getImagen())));
            }
        }
        mostrarLista();

        binding.etTitulo.setText(nota.getTitulo());
        binding.etNota.setText(nota.getNota());
        if (nota.getFecha() != null) {
            binding.tvFecha.setText(nota.getFecha());
        } else {
            binding.tvFecha.setText("");
        }
        //Toast.makeText(VistaNota.this, nota.getFecha(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarAgregarAudio() {
        Intent audio = new Intent(this, VistaAudio.class);
        startActivity(audio);
    }

    public void mostrarLista() {
        if (tieneLista == 1) {
            rv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void mostrarImagen() {
        //ImageButton btImg = (ImageButton) findViewById(R.id.btAddImg);

        binding.btAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addImagen();
                //Toast.makeText(getApplicationContext(), "Añadir Imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void mostrarConfirmarBorrarItem(ElementoLista e) {

   //Llama a DialogoBorrar
        DialogoBorrarItemLista fragmentBorrar = DialogoBorrarItemLista.newInstance(e);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");

    }

    private void addImagen(){
        //Representa a un conjunto de caracteres
        final CharSequence[] opciones = {"Camara", "Galería", "Cancelar"};
        //Parametro de dialogo
        final AlertDialog.Builder builder = new AlertDialog.Builder(VistaNota.this);
        //Titulo del Dialogo
        builder.setTitle("Opciones de Imagen");
        //Muestra las opciones
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override//Detecta la opción que hayamos elegido
            public void onClick(DialogInterface dialog, int opcion) {
                if (opciones[opcion] == "Camara") {
                    HiloCamara hta = new HiloCamara();
                    hta.execute();
                    //Camara();//Nos abre la camara del movil
                } else if (opciones[opcion] == "Galería") {
                    HiloGaleria hta = new HiloGaleria();
                    hta.execute();
                    //galeria();
                } else if (opciones[opcion] == "Cancelar") {
                    //desaparece el dialogo
                    dialog.dismiss();
                }
            }
        });
        builder.show(); //muestra el dialogo
    }

    private void saveNota() {//guarda la nota

        Date d = new Date();
        if (nota.getTitulo() != null) {
            if (!nota.getTitulo().equals(binding.etTitulo.getText().toString())) {
                nota.setFecha(fecha.formatDate(d));
            } else {
                nota.setFecha(nota.getFecha());
            }
        }else{
            nota.setFecha(fecha.formatDate(d));
        }
        if (binding.etTitulo.getText().toString().length() > 0) {
            nota.setTitulo(binding.etTitulo.getText().toString());
        } else if (binding.etNota.getText().toString().length() > 0 || ruta != null) {
            //Toast.makeText(this, ruta, Toast.LENGTH_SHORT).show();
            nota.setTitulo("Nota " + fecha.formatDate2(d));
        } else {
            nota.setTitulo(binding.etTitulo.getText().toString());
        }

        nota.setNota(binding.etNota.getText().toString());

        if (listaET != null && listaET.size() > 0) {
                                                                            System.out.println("DEEEENTROOOOOOOOOO");
            tieneLista = 1;
            añadirArrayNota();
            nota.setArrayLista(arrayLi);
        }


                                                                            System.out.println("fueraaaaaaaa-------------------------------------");

        if(borrado){
            nota.setImagen("X");
        }else {
            if (nota.getImagen() != null) {
                if (!ruta.equals("X")) {
                    nota.setImagen(ruta);
                } else if (!nota.getImagen().equals("X")) {
                    nota.setImagen(nota.getImagen());
                }
            } else {
                nota.setImagen(ruta);
            }
        }
        //nota.setImagen("img");

        //Toast.makeText(getApplicationContext(),ruta,Toast.LENGTH_SHORT).show();
        nota.setVoz("voz");
        nota.setLista(tieneLista);
        if (binding.color.getText().toString() != "") {
            if (binding.color.getText().toString().equals("exit")) {
                nota.setColor("");
            } else {
                nota.setColor(binding.color.getText().toString());
            }
        }else if(nota.getColor()==null){
            nota.setColor("");
        }


        long r = presentador.onSaveNota(nota);
        if(r > 0 & nota.getId() == 0){
            nota.setId(r);
        }
        if (tieneLista == 1) {//guarda la lista
            for (int i = 0; i < listaET.size(); i++) {
                if (!listaET.get(i).getText().equals("") && listaET.get(i).getText() != null) {
                    ElementoLista e = new ElementoLista();
                    e.setTexto(listaET.get(i).getText().toString());
                    e.setId_nota(r);
                    long s =mln.saveLista(e);
                    Toast.makeText(this, s+"<-", Toast.LENGTH_SHORT).show();
                    if(s>0 & e.getId()==0){
                        e.setId(r);
                    }
    }

            }
        }
    }



    private void añadirArrayNota() {

        if (listaET != null) {
            for (int i = 0; i < listaET.size(); i++) {
                if (listaET.get(i) != null && listaET.get(i).getText().toString() != "") {
                    ElementoLista e = new ElementoLista();
                    e.setId_nota(nota.getId());
                    e.setTexto(listaET.get(i).getText().toString());
                    arrayLi.add(e);


                }
            }
        }
    }

    /*@Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitud= String.valueOf(location.getLatitude());
        longitud = String.valueOf(location.getLongitude());
        Toast.makeText(this, "lATITUD: " + location.getLatitude() + "LONGITUD: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        //LatLng otro = new LatLng(location.getLatitude(), -location.getLongitude());
    }*/

    class HiloCamara extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            magicalCamera = new MagicalCamera(VistaNota.this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
            img=(ImageView) findViewById(R.id.iv);

            magicalCamera.takePhoto();
            return null;
        }
    }
    class HiloGaleria extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            magicalCamera = new MagicalCamera(VistaNota.this,RESIZE_PHOTO_PIXELS_PRECENTAGE);
            img=(ImageView) findViewById(R.id.iv);
            permisos();

            magicalCamera.selectedPicture("Selecciona la imagen");
            return null;
        }
    }
    class HiloPermisos extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            permisos();
            return null;
        }
    }
    class HiloTexto extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            TextAudio();
            return null;
        }
    }
    class HiloHablar extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            hablar();
            return null;
        }
    }
}