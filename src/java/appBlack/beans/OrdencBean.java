/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appBlack.beans;

import dataservices.crud.dbServicesMethods;
import dataservices.map.*;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luis
 */
@ManagedBean
@ViewScoped
public class OrdencBean {

    /**
     * Creates a new instance of OrdencBean
     */
    private Cliente cliente;
    private Producto producto;
    private Garantia garantia;
    private Orden orden;
    private ArrayList<DetallleR> detallers;
    private Boolean HabilitarGarantia;

    private ArrayList<SelectItem> itemsMarcas;
    private ArrayList<SelectItem> itemsProNombres;
    private ArrayList<SelectItem> itemsAlmacen;

    public OrdencBean() {
        this.loadItemsAlmacenes();
        this.loadItemsMarcas();
        this.loadItemsProNombres();
        this.find();
    }

    private void init() {
        this.orden = new Orden();
        this.cliente = new Cliente();
        this.producto = new Producto();
        this.garantia = new Garantia();
        this.detallers = new ArrayList<DetallleR>();
        this.HabilitarGarantia = true;
    }

    private void find() {
        if (this.getIdOrden() != 0) {
            ArrayList<Orden> listCli = dbServicesMethods.FindOrdens("idOrden", this.getIdOrden(), "cliente");
            if (listCli != null && !listCli.isEmpty()) {
                ArrayList<Orden> listPro = dbServicesMethods.FindOrdens("idOrden", this.getIdOrden(), "producto");
                for (int i = 0; i < listPro.size(); i++) {
                    listCli.get(i).setProducto(listPro.get(i).getProducto());
                }
                this.orden = listCli.get(0);
                this.cliente = this.orden.getCliente();
                this.producto = this.orden.getProducto();
                Garantia garPro = dbServicesMethods.FindGarantias("producto.idPro", this.producto.getIdPro(),"garFlag",this.orden.getOrdNumSis());
                if (garPro != null) {
                    this.garantia = garPro;
                    this.HabilitarGarantia = false;
                } else {
                    this.garantia = new Garantia();
                    this.HabilitarGarantia = true;
                }
                this.detallers = new ArrayList<DetallleR>();
            }
        } else {
            this.init();
        }

    }

    private Integer getIdOrden() {
        Integer UserAttribute = 0;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null) {
        } else {
            Object idFun = session.getAttribute("idOrden");
            UserAttribute = Integer.parseInt(idFun.toString());
        }
        return UserAttribute;
    }

    public void updateProductos(ActionEvent event) {
        
        if (!this.producto.getProNombre().equals("")) {
            if (!this.producto.getProMarca().equals("")) {
                Boolean exito = dbServicesMethods.UpdateProducto(this.producto);
                if (exito) {
                    this.orden.getProducto().setIdPro(this.producto.getIdPro());
                    generateMessage(FacesMessage.SEVERITY_INFO, "Se actualizo correctamente", "El producto");
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La marca del producto");
            }
        } else {
            generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El nombre del producto");
        }
    }

    public void generateMessage(FacesMessage.Severity Tipo, String Header, String Mensaje) {
        FacesMessage message = new FacesMessage(Tipo, Header, Mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void loadItemsMarcas() {
        ArrayList<Marca> items = dbServicesMethods.GetListMarcas();
        if (items != null) {
            this.itemsMarcas = new ArrayList<SelectItem>();
            for (Marca m : items) {
                this.itemsMarcas.add(new SelectItem(m.getMarNombre(), m.getMarNombre()));
            }
        }
    }

    private void loadItemsProNombres() {
        ArrayList<Pro> items = dbServicesMethods.GetListProNombres();
        if (items != null) {
            this.itemsProNombres = new ArrayList<SelectItem>();
            for (Pro prn : items) {
                this.itemsProNombres.add(new SelectItem(prn.getNproNombre(), prn.getNproNombre()));
            }
        }
    }

    private void loadItemsAlmacenes() {
        ArrayList<Almacen> items = dbServicesMethods.GetListAlmacenes();
        if (items != null) {
            this.itemsAlmacen = new ArrayList<SelectItem>();
            for (Almacen al : items) {
                String value = al.getAlmNombre() + " (" + al.getAlmCiudad() + ")";
                this.itemsAlmacen.add(new SelectItem(value, value));
            }
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public ArrayList<DetallleR> getDetallers() {
        return detallers;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setDetallers(ArrayList<DetallleR> detallers) {
        this.detallers = detallers;
    }

    public ArrayList<SelectItem> getItemsMarcas() {
        return itemsMarcas;
    }

    public ArrayList<SelectItem> getItemsProNombres() {
        return itemsProNombres;
    }

    public ArrayList<SelectItem> getItemsAlmacen() {
        return itemsAlmacen;
    }

    public void setItemsMarcas(ArrayList<SelectItem> itemsMarcas) {
        this.itemsMarcas = itemsMarcas;
    }

    public void setItemsProNombres(ArrayList<SelectItem> itemsProNombres) {
        this.itemsProNombres = itemsProNombres;
    }

    public void setItemsAlmacen(ArrayList<SelectItem> itemsAlmacen) {
        this.itemsAlmacen = itemsAlmacen;
    }

    public Boolean getHabilitarGarantia() {
        return HabilitarGarantia;
    }

    public void setHabilitarGarantia(Boolean HabilitarGarantia) {
        this.HabilitarGarantia = HabilitarGarantia;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

}
