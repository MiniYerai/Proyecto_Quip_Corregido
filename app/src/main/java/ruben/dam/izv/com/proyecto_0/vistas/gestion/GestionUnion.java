package ruben.dam.izv.com.proyecto_0.vistas.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoBaseDatos;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;

/**
 * Created by dam on 4/11/16.
 */

public class GestionUnion extends Gestion<ElementoLista> {

    public GestionUnion(Context c) {
        super(c);
    }

    public GestionUnion(Context c, boolean write) {
        super(c, write);
    }


    public long insert(ElementoLista objeto) {
        return this.insert(ContratoBaseDatos.TablaLista.TABLA,objeto.getCV());
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaLista.TABLA, objeto);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaLista.TABLA);
    }

    @Override
    public int delete(ElementoLista objeto) {
        String condicion = ContratoBaseDatos.TablaLista._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        return this.delete(ContratoBaseDatos.TablaLista.TABLA, condicion, argumentos);

    }

    @Override
    public int update(ElementoLista objeto) {
        ContentValues valores = objeto.getCV();
        String condicion = ContratoBaseDatos.TablaLista._ID + " = ?";
        String[] argumentos = { objeto.getId() + "" };
        return this.update(ContratoBaseDatos.TablaLista.TABLA, valores, condicion, argumentos);

    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return this.update(ContratoBaseDatos.TablaLista.TABLA, valores, condicion, argumentos);
    }

    @Override
    public Cursor getCursor() {
        return null;
    }


    public Cursor getCursorId(long idNota) {
        SQLiteDatabase bd= this.getBasedatos();
        String sql ="SELECT Lista._id,Lista.id_nota,Lista.texto from Lista  inner join nota on Lista.id_nota=nota._id where nota._id="+idNota;
        return bd.rawQuery(sql,null);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return null;
    }
    public ElementoLista get(long id){
        String where = ContratoBaseDatos.TablaLista._ID + " = ? ";
        String[] parametros = {id + ""};
        Cursor c = this.getCursor(ContratoBaseDatos.TablaLista.PROJECTION_lISTA, where, parametros, null, null, ContratoBaseDatos.TablaLista.SORT_ORDER_DEFAULT_LISTA);
        if(c.getCount() > 0) {
            c.moveToFirst();
            ElementoLista l = ElementoLista.getLista(c);
            return l;
        }
        return null;
    }
}
