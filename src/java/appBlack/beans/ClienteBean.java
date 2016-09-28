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

/**
 *
 * @author luis
 */
@ManagedBean
@ViewScoped
public class ClienteBean {

    /**
     * Creates a new instance of ClienteBean
     */
    private Cliente cliente;
    private String parametroBusqCli;
    private String valorBusqCli;
    private ArrayList<SelectItem> itemsProvincias;
    private ArrayList<SelectItem> itemsCantones;
    private ArrayList<SelectItem> itemsPrarroquias;

    public ClienteBean() {
        this.cliente = new Cliente();
        this.itemsProvincias = new ArrayList<SelectItem>();
        this.itemsCantones = new ArrayList<SelectItem>();
        this.itemsPrarroquias = new ArrayList<SelectItem>();
        this.loadItemsProvincias();
    }

    private void loadItemsProvincias() {
        ArrayList<Provincia> items = dbServicesMethods.GetListProvincias();
        if (items != null) {
            for (Provincia prov : items) {
                this.itemsProvincias.add(new SelectItem(prov.getProvNombre(), prov.getProvNombre()));
            }
        }
    }

    public void loadItemsCantones(ActionEvent event) {
        if (!this.cliente.getCliProvincia().equals("")) {
            ArrayList<Canton> itemsC = dbServicesMethods.GetListCantones(this.cliente.getCliProvincia());
            this.itemsCantones = new ArrayList<SelectItem>();
            if (itemsC != null) {
                for (Canton can : itemsC) {
                    this.itemsCantones.add(new SelectItem(can.getCanNombre(), can.getCanNombre()));
                }
            }
        } else {
            this.itemsCantones = new ArrayList<SelectItem>();
        }

    }

    public void loadItemsParroquias(ActionEvent event) {

        if (!this.cliente.getCliCanton().equals("")) {
            ArrayList<Parroquia> itemsP = dbServicesMethods.GetListParroquias(this.cliente.getCliCanton());
            this.itemsPrarroquias = new ArrayList<SelectItem>();
            for (Parroquia parr : itemsP) {
                this.itemsPrarroquias.add(new SelectItem(parr.getParrNombre(), parr.getParrNombre()));
            }
        } else {
            this.itemsPrarroquias = new ArrayList<SelectItem>();
        }

    }

    public void saveClientData(ActionEvent event) {
        if (this.cliente != null) {
            if (!this.cliente.getCliNombres().equals("")) {
                if (!this.cliente.getCliProvincia().equals("")) {
                    if (!this.cliente.getCliCanton().equals("")) {
                        if (!this.cliente.getCliDireccion().equals("")) {

                            Boolean exito = dbServicesMethods.InsertCliente(this.cliente);
                            if (exito) {
                                generateMessage(FacesMessage.SEVERITY_INFO, "Felicitaciones", "Se ha guardado");
                                this.cliente = new Cliente();
                            }

                        } else {
                            generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La Dirección");
                        }
                    } else {
                        generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El Cantón.");
                    }
                } else {
                    generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La Provincia.");
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "Los Nomombres y Apellidos.");
            }

        }
    }
    
    public void updateClientData(ActionEvent event) {
        if (this.cliente != null) {
            if (!this.cliente.getCliNombres().equals("")) {
                if (!this.cliente.getCliProvincia().equals("")) {
                    if (!this.cliente.getCliCanton().equals("")) {
                        if (!this.cliente.getCliDireccion().equals("")) {

                            Boolean exito = dbServicesMethods.UpdateCliente(this.cliente);
                            if (exito) {
                                generateMessage(FacesMessage.SEVERITY_INFO, "Felicitaciones", "Se ha actulizado");
                                this.cliente = new Cliente();
                            }

                        } else {
                            generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La Dirección");
                        }
                    } else {
                        generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El Cantón.");
                    }
                } else {
                    generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La Provincia.");
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "Los Nomombres y Apellidos.");
            }

        }
    }

    public void FindClientes(ActionEvent event) {

        if (!this.parametroBusqCli.equals("") && !this.valorBusqCli.equals("")) {
            Cliente mCliente = dbServicesMethods.FindClientes(this.getParametroBusqCli(), this.getValorBusqCli().trim());
            if (mCliente != null) {
                this.cliente = mCliente;
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El cliente");
            }
        }
    }

    public void generateMessage(FacesMessage.Severity Tipo, String Header, String Mensaje) {
        FacesMessage message = new FacesMessage(Tipo, Header, Mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<SelectItem> getItemsProvincias() {
        return itemsProvincias;
    }

    public ArrayList<SelectItem> getItemsCantones() {
        return itemsCantones;
    }

    public ArrayList<SelectItem> getItemsPrarroquias() {
        return itemsPrarroquias;
    }

    public void setItemsProvincias(ArrayList<SelectItem> itemsProvincias) {
        this.itemsProvincias = itemsProvincias;
    }

    public void setItemsCantones(ArrayList<SelectItem> itemsCantones) {
        this.itemsCantones = itemsCantones;
    }

    public void setItemsPrarroquias(ArrayList<SelectItem> itemsPrarroquias) {
        this.itemsPrarroquias = itemsPrarroquias;
    }

    public String getParametroBusqCli() {
        return parametroBusqCli;
    }

    public String getValorBusqCli() {
        return valorBusqCli;
    }

    public void setParametroBusqCli(String parametroBusqCli) {
        this.parametroBusqCli = parametroBusqCli;
    }

    public void setValorBusqCli(String valorBusqCli) {
        this.valorBusqCli = valorBusqCli;
    }

}
