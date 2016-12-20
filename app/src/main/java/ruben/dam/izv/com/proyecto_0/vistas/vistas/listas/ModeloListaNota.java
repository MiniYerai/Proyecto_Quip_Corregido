package ruben.dam.izv.com.proyecto_0.vistas.vistas.listas;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionUnion;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;

/**
 * Created by dam on 29/11/16.
 */

public class ModeloListaNota implements ContratoLista.InterfazModelo {
    private GestionUnion gu;
    private ContentResolver resolver;
    private Cursor cursor;
    private Uri uri = ContratoBaseDatos.CONTENT_URI_LITA;

    public ModeloListaNota(Context c) {
        gu = new GestionUnion(c);
        this.resolver = c.getContentResolver();
    }

    @Override
    public void close() {
        gu.close();
        cursor.close();
    }

    @Override
    public ElementoLista getLista(long id) {
        return gu.get(id);
    }

    @Override
    public long deleteItem(int pos) {
        String where = ContratoBaseDatos.TablaLista._ID + " = ? ";
        String[] argumentos = {pos + ""};
        return this.resolver.delete(uri, where, argumentos);
    }

    @Override
    public long deleteItem(ElementoLista e) {
        String where = ContratoBaseDatos.TablaLista._ID + " = ? ";
        String[] argumentos = {e.getId() + ""};
        return this.resolver.delete(uri, where, argumentos);
    }





    @Override
    public long saveLista(ElementoLista l) {
        long r;
        if (l.getId() == 0) {
            r = this.insertLista(l);//inserta la nota
        } else {
            r = this.updateLista(l);//modifica la nota
        }
        return r;
    }

    @Override
    public void loadData(OnDataLoadListener listener) {

    }

    private long deleteNota(ElementoLista n) {

        String where = ContratoBaseDatos.TablaLista._ID + " = ?";
        String[] arguments = {n.getId() + ""};
        return this.resolver.delete(uri, where, arguments);
        //return gn.delete(n);
    }

    private long insertLista(ElementoLista l) {
        if(l.getTexto().trim().compareTo("")==0 ) {
            return 0;
        }
        ContentValues valores=l.getCV();

        return Long.parseLong(this.resolver.insert(uri,l.getCV()).getLastPathSegment());

        //return gn.insert(n);
    }

    private long updateLista(ElementoLista l) {
        if(l.getTexto().trim().compareTo("")==0 ) {
            this.deleteNota(l);

            String where = ContratoBaseDatos.TablaNota._ID + " = ?";
            String [] arguments = {l.getId() + ""};
            resolver.delete(uri,where,arguments);
            //gn.delete(n);
            return 0;
        }
        String where = ContratoBaseDatos.TablaNota._ID + " = ?";
        String [] arguments = {l.getId() + ""};
        return resolver.update(uri,l.getCV(),where,arguments);
        //return gn.update(n);
    }


    public ArrayList<ElementoLista> arrayNota(long id){
        Cursor c =gu.getCursorId(id);
        ArrayList<ElementoLista> arr = new ArrayList<ElementoLista>();
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            ElementoLista e = new ElementoLista(c.getLong(0),c.getLong(1),c.getString(2));
            arr.add(e);
        }
        System.out.println(arr.toString());

        return arr;
    }
}