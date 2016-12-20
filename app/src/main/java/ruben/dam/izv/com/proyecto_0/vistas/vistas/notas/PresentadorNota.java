package ruben.dam.izv.com.proyecto_0.vistas.vistas.notas;

import android.content.Context;
import android.widget.Toast;

import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoLista;
import ruben.dam.izv.com.proyecto_0.vistas.contrato.ContratoNota;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.ElementoLista;
import ruben.dam.izv.com.proyecto_0.vistas.pojo.Nota;
import ruben.dam.izv.com.proyecto_0.vistas.vistas.listas.ModeloListaNota;

public class PresentadorNota implements ContratoNota.InterfacePresentador {

    private ContratoNota.InterfaceModelo modelo;
    private ModeloListaNota modeloL;
    private ContratoNota.InterfaceVista vista;
    private ContratoLista.InterfazModelo.OnDataLoadListener oyente;

    public PresentadorNota(ContratoNota.InterfaceVista vista) {
        this.vista = vista;
        this.modelo = new ModeloNota((Context)vista);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public long onSaveNota(Nota n) {
        return this.modelo.saveNota(n);
    }

    @Override
    public void onAddAudio() {
        this.vista.mostrarAgregarAudio();
    }

    @Override
    public void onAddImg() {
        this.vista.mostrarImagen();
    }

    public long onSaveLista(ElementoLista l){
        return this.modeloL.saveLista(l);
    }

    @Override
    public void onShowBorrarItemLista(int position) {//muestra la confirmacion de borrarlo o no

        ElementoLista e = this.modeloL.getLista(position);
        this.vista.mostrarConfirmarBorrarItem(e);
    }

    @Override
    public void onDeleteItemLista(int position) {//borra la nota por posicion
        this.modeloL.deleteItem(position);
        this.modeloL.loadData(oyente);
    }


    @Override
    public void onDeleteItemLista(ElementoLista e) {//borra la nota por posicion
        this.modeloL.deleteItem(e);
        this.modeloL.loadData(oyente);
    }
}