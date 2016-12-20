package ruben.dam.izv.com.proyecto_0.vistas.contrato;

/*
 * Created by Ruben on 19/10/2016.
 */


import android.database.Cursor;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;

public interface ContratoLista {

    interface InterfazModelo {

        void close();

        ElementoLista getLista(long id);

        long deleteItem(int pos);
     long   deleteItem(ElementoLista e);

        long saveLista(ElementoLista l);

        void loadData(OnDataLoadListener listener);

        interface OnDataLoadListener {
            public void setCursor(Cursor c);
        }

    }

    interface InterfazPresentador {

        void onPause();

        void onResume();

        long onSaveLista(ElementoLista l);

    }

    interface InterfazVista {

        void mostrarLista(ElementoLista l);

    }
}
