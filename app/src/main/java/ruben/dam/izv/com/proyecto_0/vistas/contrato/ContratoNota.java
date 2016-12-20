package ruben.dam.izv.com.proyecto_0.vistas.contrato;

import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;

public interface ContratoNota {

    interface InterfaceModelo {
        long deleteItem(int pos);//
        long deleteItem (ElementoLista e);//

        void close();

        Nota getNota(long id);

        long saveNota(Nota n);

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        void onShowBorrarItemLista(int position);//

        void onDeleteItemLista(int position);//
        void onDeleteItemLista(ElementoLista e);//




       long onSaveNota(Nota n);

      void onAddAudio();

      void onAddImg();


    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

        void mostrarAgregarAudio();

        void mostrarImagen();

        void mostrarConfirmarBorrarItem(ElementoLista e);//
    }

}