package ruben.dam.izv.com.proyecto_0.vistas.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

public class Nota implements Parcelable {

    private long id;
    private String titulo, nota,imagen,voz, color, fecha, latitud, longitud;
    private Integer lista;
    private ArrayList<ElementoLista> arrLista;

    public Nota() {
        this(0, null, null,null,null,null, null,null, null, null,null);
    }

    public Nota(long id, String titulo, String nota, String imagen, String voz, Integer lista, String color, String fecha, String latitud, String longitud, ArrayList<ElementoLista> array) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.imagen = imagen;
        this.voz = voz;
        this.lista = lista;
        this.color = color;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.arrLista=array;
    }

    protected Nota(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        nota = in.readString();
        imagen = in.readString();
        voz = in.readString();
        lista = in.readInt();
        color = in.readString();
        fecha = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        arrLista=in.readArrayList(null);
    }

    public static final Creator<Nota> CREATOR = new Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Long.parseLong(id);
        } catch(NumberFormatException e){
            this.id = 0;
        }
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLista() {
        return lista;
    }

    public void setLista(Integer lista) {
        this.lista = lista;
    }

    public String getTitulo() {
        return titulo;
    }//devuelve titulo

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }//devuelve nota

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }//devuelve la ruta de la imagen

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVoz() {
        return "voz";
    }//devuelve la ruta del audio

    public void setVoz(String voz) {
        this.voz = voz;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public ArrayList<ElementoLista> getArrayLista() {
        return arrLista;
    }

//estos metodos hacen que al añadir el array a la nota este tome automaticamente el valor de su ID

    public void setArrayLista(ArrayList<ElementoLista> arrayLista) {

        this.arrLista =arrayLista;
    }

    public static Creator<Nota> getCREATOR() {
        return CREATOR;
    }

    public ContentValues getContentValues(){
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(boolean withId){
        ContentValues valores = new ContentValues();
        if(withId){
            valores.put(ContratoBaseDatos.TablaNota._ID, this.getId());
        }
        valores.put(ContratoBaseDatos.TablaNota.TITULO, this.getTitulo());
        valores.put(ContratoBaseDatos.TablaNota.NOTA, this.getNota());
        valores.put(ContratoBaseDatos.TablaNota.IMAGEN, this.getImagen());
        valores.put(ContratoBaseDatos.TablaNota.VOZ, this.getVoz());
        valores.put(String.valueOf(ContratoBaseDatos.TablaNota.LISTA),this.getLista());
        valores.put(ContratoBaseDatos.TablaNota.COLOR, this.getColor());
        valores.put(ContratoBaseDatos.TablaNota.FECHA,this.getFecha());
       // valores.put(ContratoBaseDatos.TablaNota.LATITUD,this.getLatitud());
       // valores.put(ContratoBaseDatos.TablaNota.LONGITUD,this.getLongitud());

        return valores;
    }

    public static Nota getNota(Cursor c){
        Nota objeto = new Nota();

        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNota._ID)));
        objeto.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.TITULO)));
        objeto.setNota(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.NOTA)));
        objeto.setImagen(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.IMAGEN)));
        objeto.setVoz(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.VOZ)));
        objeto.setLista(c.getInt(c.getColumnIndex(String.valueOf(ContratoBaseDatos.TablaNota.LISTA))));
        objeto.setColor(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.COLOR)));
        objeto.setFecha(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.FECHA)));
        //objeto.setLatitud(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.LATITUD)));
       // objeto.setLongitud(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNota.LONGITUD)));

        return objeto;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nota='" + nota + '\'' +
                ", imagen='" + imagen + '\'' +
                ", voz='" + voz + '\''+
                ", lista=" + lista +
                ", color='" + color + '\'' +
                ", fecha='" + fecha + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                " ARRAY "+arrLista.toString()+
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(nota);
        dest.writeString(imagen);
        dest.writeString(voz);
        dest.writeInt(lista);
        dest.writeString(color);
        dest.writeString(fecha);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeList(arrLista);
    }


}