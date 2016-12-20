package ruben.dam.izv.com.proyecto_0.vistas.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;

/*
 * Created by Ruben on 19/10/2016.
 */

public class ElementoLista implements Parcelable {

    private long id, id_nota;
    private String texto;

    //CREADOR POR DEFECTO
    public ElementoLista(){ this(0,0,null);}

    public ElementoLista(long id, long id_nota, String texto){
        this.id=id;
        this.id_nota=id_nota;
        this.texto=texto;
    }
    //PARCELABLE
    protected ElementoLista(Parcel in) {
        id = in.readLong();
        id_nota = in.readLong();
        texto = in.readString();
    }


    public static final Creator<ElementoLista> CREATOR = new Creator<ElementoLista>() {
        @Override
        public ElementoLista createFromParcel(Parcel in) {
            return new ElementoLista(in);
        }

        @Override
        public ElementoLista[] newArray(int size) {
            return new ElementoLista[size];
        }
    };

    //GETS Y SETS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_nota() {
        return id_nota;
    }

    public void setId_nota(long id_nota) {
        this.id_nota = id_nota;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public static Creator<ElementoLista> getCREATOR() {
        return CREATOR;
    }

    public ContentValues getCV(){ return this.getCV(false);}

    public ContentValues getCV(boolean withId){
        ContentValues valores = new ContentValues();

        if(withId){
            valores.put(ContratoBaseDatos.TablaLista._ID, this.getId());
        }

        valores.put(ContratoBaseDatos.TablaLista.ID_NOTA, this.getId_nota());
        valores.put(ContratoBaseDatos.TablaLista.TEXTO, this.getTexto());

        return valores;
    }

    public static ElementoLista getLista(Cursor c){
        ElementoLista objeto = new ElementoLista();

        objeto.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista._ID)));
        objeto.setId_nota(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaLista.ID_NOTA)));
        objeto.setTexto(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaLista.TEXTO)));

        return objeto;
    }

    public String toString(){

        return "ElementoLista{" + "id = " + id + ", id_nota = " + id_nota + ", texto = '" + texto + '\'' + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeLong(id_nota);
        dest.writeString(texto);
    }
}
