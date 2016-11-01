/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appBlack.beans;

import dataservices.map.Orden;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author luis
 */
@ManagedBean
@ViewScoped
public class PrintBean {

    /**
     * Creates a new instance of PrintBean
     */
    private String CodeCrypo;
    private Orden printOrden;

    public PrintBean() {
        this.CodeCrypo = "";
        this.printOrden = new Orden();
    }

    public void findOrdenPrinter(ActionEvent event) {

    }

    public Orden getPrintOrden() {
        return printOrden;
    }

    public void setPrintOrden(Orden printOrden) {
        this.printOrden = printOrden;
    }

    public String getCodeCrypo() {
        return CodeCrypo;
    }

    public void setCodeCrypo(String CodeCrypo) {
        this.CodeCrypo = CodeCrypo;
    }

}
