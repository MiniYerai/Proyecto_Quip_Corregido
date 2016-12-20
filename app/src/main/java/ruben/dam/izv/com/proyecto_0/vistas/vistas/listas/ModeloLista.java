package ruben.dam.izv.com.proyecto_0.vistas.vistas.listas;

import android.content.Context;
import android.database.Cursor;


import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;

import ruben.dam.izv.com.proyecto_0.vistas.gestion.GestionLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;


/**
 * Created by Vir on 03/11/2016.
 */

public class ModeloLista implements ContratoLista.InterfazModelo {

    private GestionLista gl;
    Cursor cursor;

    public ModeloLista(Context c){ gl = new GestionLista(c); }

    @Override
    public void close() {

    }

    @Override
    public ElementoLista getLista(long id) {
        return null;
    }



    @Override
    public long deleteItem(int position) {
        cursor.moveToPosition(position);
       ElementoLista e = ElementoLista.getLista(cursor);
        return this.deleteItem(e);
    }

    @Override
    public long deleteItem(ElementoLista e) {//borra la nota
        return gl.delete(e);

    }

    public long saveLista(ElementoLista l) {
        return gl.insert(l);
    }



    @Override
    public void loadData(OnDataLoadListener listener) {//Muestra las notas en la lista
      cursor = gl.getCursor();
        listener.setCursor(cursor);
    }
}



