/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appBlack.beans;

import appBlack.util.ServicesAppliancesBlack;
import dataservices.crud.dbServicesMethods;
import dataservices.map.*;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.jsoup.helper.StringUtil;
import org.primefaces.context.RequestContext;

/**
 *
 * @author luis
 */
@ManagedBean
@ViewScoped
public class OrdensBean {

    /**
     * Creates a new instance of OrdensBean
     */
    private Cliente cliente;
    private String parametroBusqCli;
    private String valorBusqCli;
    private Orden selectedOrdenCli;
    private Producto producto;
    private String parametroBusqPro;
    private String valorBusqPro;
    private Orden selectedOrdenPro;
    private String parametroBusqOrd;
    private String valorBusqOrd;
    private Orden selectedOrdenOrd;
    private ArrayList<Orden> ordensCli;
    private ArrayList<Orden> ordensPro;
    private ArrayList<Orden> ordens;

    private ArrayList<Estado> estados;

    public OrdensBean() {
        this.init();
    }

    private void init() {
        this.cliente = new Cliente();
        this.selectedOrdenCli = new Orden();
        this.ordensCli = new ArrayList<Orden>();
        this.producto = new Producto();
        this.selectedOrdenPro = new Orden();
        this.ordensPro = new ArrayList<Orden>();
        this.ordens = new ArrayList<Orden>();
        this.estados = new ArrayList<Estado>();
        this.parametroBusqCli = "";
        this.parametroBusqPro = "";
        this.parametroBusqPro = "";
        this.valorBusqCli = "";
        this.valorBusqPro = "";
        this.valorBusqOrd = "";
        this.LoadEstados();
    }

    public void FindClientes(ActionEvent event) {

        if (!this.parametroBusqCli.equals("") && !this.valorBusqCli.equals("")) {
            Cliente mCliente = dbServicesMethods.FindClientes(this.parametroBusqCli, this.valorBusqCli.trim());
            if (mCliente != null) {
                this.cliente = mCliente;
                ArrayList<Orden> listOrdCli = dbServicesMethods.FindOrdens("cliente.idCliente", this.cliente.getIdCliente(), "producto");
                if (listOrdCli != null) {
                    this.ordensCli = listOrdCli;
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El cliente");
            }
        }
    }

    public void FindProductos(ActionEvent event) {

        if (!this.parametroBusqPro.equals("") && !this.valorBusqPro.equals("")) {
            Producto mProducto = null;
            if (this.parametroBusqPro.equals("proNumSis")) {
                Boolean isnum = StringUtil.isNumeric(this.valorBusqPro.trim());
                if (isnum) {
                    mProducto = dbServicesMethods.FindProductos(this.parametroBusqPro, Integer.parseInt(this.valorBusqPro.trim()));
                } else {
                    generateMessage(FacesMessage.SEVERITY_ERROR, "Porfavor", "Ingrese un numero");
                }
            } else {
                mProducto = dbServicesMethods.FindProductos(this.parametroBusqPro, this.valorBusqPro.trim());
            }
            if (mProducto != null) {
                this.producto = mProducto;
                ArrayList<Orden> listPro = dbServicesMethods.FindOrdens("producto.idPro", this.producto.getIdPro(), "cliente");
                if (listPro != null) {
                    this.ordensPro = listPro;
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El producto");
            }
        }
    }

    public void FindOrdenes(ActionEvent event) {

        if (!this.parametroBusqOrd.equals("") && !this.valorBusqOrd.equals("")) {
            ArrayList<Orden> listOrd = null;
            if (this.parametroBusqOrd.equals("ordNumSis")) {
                listOrd = dbServicesMethods.FindOrdens(this.parametroBusqOrd, Integer.parseInt(this.valorBusqOrd), "cliente");
                if (listOrd != null) {
                    ArrayList<Orden> listOrdPro = dbServicesMethods.FindOrdens(this.parametroBusqOrd, Integer.parseInt(this.valorBusqOrd), "producto");
                    for (int i = 0; i < listOrdPro.size(); i++) {
                        listOrd.get(i).setProducto(listOrdPro.get(i).getProducto());
                    }
                }
            } else {
                listOrd = dbServicesMethods.FindOrdens(this.parametroBusqOrd, this.valorBusqOrd, "cliente");
                if (listOrd != null) {
                    ArrayList<Orden> listOrdPro = dbServicesMethods.FindOrdens(this.parametroBusqOrd, this.valorBusqOrd, "producto");
                    for (int i = 0; i < listOrdPro.size(); i++) {
                        listOrd.get(i).setProducto(listOrdPro.get(i).getProducto());
                    }
                }
            }
            if (listOrd != null && !listOrd.isEmpty()) {
                this.ordens = listOrd;
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El cliente");
            }
        }
    }

    public String getEstado(ActionEvent event, String Value) {
        String ValueOf = "";
        for (Estado es : this.estados) {
            if (String.valueOf(es.getIdEstado()).equals(Value)) {
                ValueOf = es.getEstNombre();
            }
        }
        return ValueOf;
    }

    private void LoadEstados() {
        ArrayList<Estado> items = dbServicesMethods.GetListEstados();
        if (items != null && !items.isEmpty()) {
            this.estados = new ArrayList<Estado>();
            for (Estado es : items) {
                this.estados.add(es);
            }
        }
    }

    public void openOrden(ActionEvent event, Orden ordCli) {
        RequestContext context = RequestContext.getCurrentInstance();
        String ruta = "";
        if (ordCli != null) {
            if (ordCli.getOrdFlag() == 1) {
                ruta = ServicesAppliancesBlack.getURL_Login() + "views/completar.xhtml";
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
                        put("idOrden", ordCli.getIdOrden());
            } else if (ordCli.getOrdFlag() == 0) {
                ruta = ServicesAppliancesBlack.getURL_Login() + "views/ver.xhtml";
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
                        put("idOrden", ordCli.getIdOrden());
            }
        }
        context.addCallbackParam("loggedIn", true);
        context.addCallbackParam("ruta", ruta);
    }

    public void generateMessage(FacesMessage.Severity Tipo, String Header, String Mensaje) {
        FacesMessage message = new FacesMessage(Tipo, Header, Mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getParametroBusqCli() {
        return parametroBusqCli;
    }

    public String getValorBusqCli() {
        return valorBusqCli;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getParametroBusqPro() {
        return parametroBusqPro;
    }

    public String getValorBusqPro() {
        return valorBusqPro;
    }

    public ArrayList<Orden> getOrdens() {
        return ordens;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setParametroBusqCli(String parametroBusqCli) {
        this.parametroBusqCli = parametroBusqCli;
    }

    public void setValorBusqCli(String valorBusqCli) {
        this.valorBusqCli = valorBusqCli;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setParametroBusqPro(String parametroBusqPro) {
        this.parametroBusqPro = parametroBusqPro;
    }

    public void setValorBusqPro(String valorBusqPro) {
        this.valorBusqPro = valorBusqPro;
    }

    public void setOrdens(ArrayList<Orden> ordens) {
        this.ordens = ordens;
    }

    public ArrayList<Orden> getOrdensCli() {
        return ordensCli;
    }

    public ArrayList<Orden> getOrdensPro() {
        return ordensPro;
    }

    public void setOrdensCli(ArrayList<Orden> ordensCli) {
        this.ordensCli = ordensCli;
    }

    public void setOrdensPro(ArrayList<Orden> ordensPro) {
        this.ordensPro = ordensPro;
    }

    public Orden getSelectedOrdenCli() {
        return selectedOrdenCli;
    }

    public void setSelectedOrdenCli(Orden selectedOrdenCli) {
        this.selectedOrdenCli = selectedOrdenCli;
    }

    public Orden getSelectedOrdenPro() {
        return selectedOrdenPro;
    }

    public void setSelectedOrdenPro(Orden selectedOrdenPro) {
        this.selectedOrdenPro = selectedOrdenPro;
    }

    public String getParametroBusqOrd() {
        return parametroBusqOrd;
    }

    public String getValorBusqOrd() {
        return valorBusqOrd;
    }

    public void setParametroBusqOrd(String parametroBusqOrd) {
        this.parametroBusqOrd = parametroBusqOrd;
    }

    public void setValorBusqOrd(String valorBusqOrd) {
        this.valorBusqOrd = valorBusqOrd;
    }

    public Orden getSelectedOrdenOrd() {
        return selectedOrdenOrd;
    }

    public void setSelectedOrdenOrd(Orden selectedOrdenOrd) {
        this.selectedOrdenOrd = selectedOrdenOrd;
    }

}
