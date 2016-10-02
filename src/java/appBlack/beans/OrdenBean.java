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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.jsoup.helper.StringUtil;

/**
 *
 * @author luis
 */
@ManagedBean
@ViewScoped
public class OrdenBean {

    /**
     * Creates a new instance of OrdenBean
     */
    private String ParametroBusqCli;
    private String ValorBusqCli;
    private Orden orden;
    private Cliente cliente;
    private Boolean HabilitarCliente;
    private Boolean HablilitaProducto;
    private Boolean HablilitaGuardarProducto;
    private Producto producto;
    private Garantia garantia;
    private String ParametroBusqPro;
    private String ValorBusqPro;
    private Boolean HablilitaGarantia;
    private Boolean HablilitaOrden;

    private ArrayList<SelectItem> itemsMarcas;
    private ArrayList<SelectItem> itemsProNombres;
    private ArrayList<SelectItem> itemsAlmacen;

    public OrdenBean() {
        this.init();
    }

    private void init() {
        this.orden = new Orden();
        this.cliente = new Cliente();
        this.HabilitarCliente = true;
        this.HablilitaProducto = true;
        this.HablilitaGuardarProducto = true;
        this.producto = new Producto();
        this.garantia = new Garantia();
        this.HablilitaGarantia = true;
        this.HablilitaOrden = true;
        this.itemsMarcas = new ArrayList<SelectItem>();
        this.itemsProNombres = new ArrayList<SelectItem>();
        this.itemsAlmacen = new ArrayList<SelectItem>();
        this.loadItemsMarcas();
        this.loadItemsProNombres();
        this.loadItemsAlmacenes();
    }

    public void FindClientes(ActionEvent event) {

        if (!this.ParametroBusqCli.equals("") && !this.ValorBusqCli.equals("")) {
            Cliente mCliente = dbServicesMethods.FindClientes(this.ParametroBusqCli, this.ValorBusqCli.trim());
            if (mCliente != null) {
                this.cliente = mCliente;
                this.HabilitarCliente = false;
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El cliente");
            }
        }
    }

    public void FindProductos(ActionEvent event) {

        if (!this.ParametroBusqPro.equals("") && !this.ValorBusqPro.equals("")) {
            Producto mProducto = null;
            if (this.ParametroBusqPro.equals("proNumSis")) {
                Boolean isnum = StringUtil.isNumeric(this.ValorBusqPro.trim());
                if (isnum) {
                    mProducto = dbServicesMethods.FindProductos(this.ParametroBusqPro, Integer.parseInt(this.ValorBusqPro.trim()));
                } else {
                    generateMessage(FacesMessage.SEVERITY_ERROR, "Porfavor", "Ingrese un numero");
                }
            } else {
                mProducto = dbServicesMethods.FindProductos(this.ParametroBusqPro, this.ValorBusqPro.trim());
            }
            if (mProducto != null) {
                this.producto = mProducto;
                this.HablilitaProducto = false;
                this.HablilitaGuardarProducto = true;
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "No se encontro", "El producto");
                this.HablilitaGuardarProducto = false;
                this.HablilitaProducto = true;
            }
        }
    }

    public void EstablecerCliente(ActionEvent event) {
        if (this.cliente != null && this.cliente.getIdCliente() != 0) {
            this.orden.getCliente().setIdCliente(this.cliente.getIdCliente());
            this.HablilitaProducto = false;
            generateMessage(FacesMessage.SEVERITY_INFO, "Se establecio correctamente", "El cliente");
            this.HabilitarCliente = true;
        }
    }

    public void EstablecerProducto(ActionEvent event) {
        if (this.producto != null && this.producto.getIdPro() != 0) {
            this.orden.getProducto().setIdPro(this.producto.getIdPro());
            this.HablilitaProducto = true;
            this.HablilitaGarantia = false;
            this.HablilitaOrden = false;
            generateMessage(FacesMessage.SEVERITY_INFO, "Se establecio correctamente", "El producto");
        }
    }

    public void saveProductos(ActionEvent event) {
        Integer nextNumPro = dbServicesMethods.NextNumProductos();
        this.producto.setProNumSis(nextNumPro);
        if (!this.producto.getProNombre().equals("")) {
            if (!this.producto.getProMarca().equals("")) {
                this.producto.setIdPro(null);
                Boolean exito = dbServicesMethods.InsertProducto(this.producto);
                if (exito) {
                    this.producto = dbServicesMethods.FindProductos("proNumSis", nextNumPro);
                    this.orden.getProducto().setIdPro(this.producto.getIdPro());
                    this.HablilitaGuardarProducto = true;
                    this.HablilitaGarantia = false;
                    this.HablilitaOrden = false;
                    generateMessage(FacesMessage.SEVERITY_INFO, "Se establecio correctamente", "El producto");
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "La marca del producto");
            }
        } else {
            generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El nombre del producto");
        }
    }

    private void loadItemsMarcas() {
        ArrayList<Marca> items = dbServicesMethods.GetListMarcas();
        if (items != null) {
            for (Marca m : items) {
                this.itemsMarcas.add(new SelectItem(m.getMarNombre(), m.getMarNombre()));
            }
        }
    }

    private void loadItemsProNombres() {
        ArrayList<Pro> items = dbServicesMethods.GetListProNombres();
        if (items != null) {
            for (Pro prn : items) {
                this.itemsProNombres.add(new SelectItem(prn.getNproNombre(), prn.getNproNombre()));
            }
        }
    }

    private void loadItemsAlmacenes() {
        ArrayList<Almacen> items = dbServicesMethods.GetListAlmacenes();
        if (items != null) {
            for (Almacen al : items) {
                String value = al.getAlmNombre() + " (" + al.getAlmCiudad() + ")";
                this.itemsAlmacen.add(new SelectItem(value, value));
            }
        }
    }

    public void saveGarantias(ActionEvent event) {
        if (this.producto.getIdPro() != null) {
            if (!this.garantia.getGarAlmacen().equals("")) {
                if (!this.garantia.getGarNumFactura().equals("")) {
                    if (StringUtil.isNumeric(this.garantia.getGarNumFactura().trim())) {
                        if (this.garantia.getGarFechaFactura() != null) {
                            if (!this.garantia.getGarTelefonoAlmacen().equals("")) {
                                if (StringUtil.isNumeric(this.garantia.getGarTelefonoAlmacen().trim())) {
                                    this.garantia.getProducto().setIdPro(this.producto.getIdPro());
                                    Boolean exito = dbServicesMethods.InsertGarantia(this.garantia);
                                    if (exito) {
                                        this.HablilitaGarantia = true;
                                        generateMessage(FacesMessage.SEVERITY_INFO, "Se establecio correctamente", "La garantia del producto");
                                    }
                                } else {
                                    generateMessage(FacesMessage.SEVERITY_INFO, "Profavor", "Ingrese un numero en el teléfono del almacen");
                                }
                            } else {
                                generateMessage(FacesMessage.SEVERITY_INFO, "Falta", "El numero de teléfono del almacen");
                            }
                        } else {
                            generateMessage(FacesMessage.SEVERITY_ERROR, "Porfavor", "Seleccione la fecha de factura del producto");
                        }
                    } else {
                        generateMessage(FacesMessage.SEVERITY_ERROR, "Porfavor", "Ingrese el numero de factura del producto");
                    }
                } else {
                    generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El numero de factura del producto");
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_ERROR, "Falta", "El almacén del producto");
            }
        }
    }

    public void ActiveGarantias(ActionEvent event) {
        if (this.producto.getIdPro() != null) {
            this.garantia.getProducto().setIdPro(this.producto.getIdPro());
            Integer ordNumSis = dbServicesMethods.NextNumOrdenes();
            this.garantia.setGarFlag(ordNumSis);
            Boolean exito = dbServicesMethods.InsertGarantia(this.garantia);
            if (exito) {
                this.HablilitaGarantia = true;
                generateMessage(FacesMessage.SEVERITY_INFO, "Se establecio correctamente", "La garantia del producto");
            }

        }
    }

    public void generatedOrden(ActionEvent event) {
        if (this.orden.getCliente().getIdCliente() != null
                && this.orden.getProducto().getIdPro() != null) {
            if (this.orden.getOrdFechaVisita() != null) {
                Integer ordNumSis = dbServicesMethods.NextNumOrdenes();
                this.orden.setOrdNumSis(ordNumSis);
                this.orden.setOrdFlag(1);
                this.orden.setOrdEstado("1");
                Boolean exito = dbServicesMethods.InsertOrden(this.orden);
                if (exito) {
                    this.orden = dbServicesMethods.FindOrden("ordNumSis", ordNumSis);
                    Registro registro = new Registro();
                    Estado estado = new Estado();
                    estado.setIdEstado(1);
                    registro.setEstado(estado);
                    Orden regOrden = new Orden();
                    regOrden.setIdOrden(this.orden.getIdOrden());
                    registro.setOrden(regOrden);
                    Funcionarios usuario = new Funcionarios();
                    usuario.setIdFun(this.getUserAttribute());
                    registro.setFuncionarios(usuario);
                    Boolean regexito = dbServicesMethods.InsertRegistro(registro);
                    if (regexito) {
                        this.init();
                        generateMessage(FacesMessage.SEVERITY_INFO, "Se genero correctamente", "La orden");
                    }
                }
            } else {
                generateMessage(FacesMessage.SEVERITY_INFO, "Porfavor seleccione", "La fecha de visita de la orden");
            }

        }
    }

    public void GeneratePDF() {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        String serverPath = ctx.getRealPath("/");
        String filepath = "WEB-INF/docs/";
        // Boolean exito = DocumentsPdf.CreateFilePDF(this.SelectedDocument, serverPath + filepath);

    }

//    public void downloadDocsPDF(String fileName) {
//        if (this.getSelectedDocument() != null) {
//            InputStream CmpPDF = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("WEB-INF/docs/" + fileName);
//            this.setPdfFile(new DefaultStreamedContent(CmpPDF, "application/pdf", fileName));
//
//        }
//    }
    private Integer getUserAttribute() {
        Integer UserAttribute = 0;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null) {
        } else {
            Object idFun = session.getAttribute("idFun");
            UserAttribute = Integer.parseInt(idFun.toString());
        }
        return UserAttribute;
    }

    public void generateMessage(FacesMessage.Severity Tipo, String Header, String Mensaje) {
        FacesMessage message = new FacesMessage(Tipo, Header, Mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getParametroBusqCli() {
        return ParametroBusqCli;
    }

    public String getValorBusqCli() {
        return ValorBusqCli;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setParametroBusqCli(String ParametroBusqCli) {
        this.ParametroBusqCli = ParametroBusqCli;
    }

    public void setValorBusqCli(String ValorBusqCli) {
        this.ValorBusqCli = ValorBusqCli;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ArrayList<SelectItem> getItemsMarcas() {
        return itemsMarcas;
    }

    public ArrayList<SelectItem> getItemsProNombres() {
        return itemsProNombres;
    }

    public void setItemsMarcas(ArrayList<SelectItem> itemsMarcas) {
        this.itemsMarcas = itemsMarcas;
    }

    public void setItemsProNombres(ArrayList<SelectItem> itemsProNombres) {
        this.itemsProNombres = itemsProNombres;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

    public ArrayList<SelectItem> getItemsAlmacen() {
        return itemsAlmacen;
    }

    public void setItemsAlmacen(ArrayList<SelectItem> itemsAlmacen) {
        this.itemsAlmacen = itemsAlmacen;
    }

    public String getParametroBusqPro() {
        return ParametroBusqPro;
    }

    public String getValorBusqPro() {
        return ValorBusqPro;
    }

    public void setParametroBusqPro(String ParametroBusqPro) {
        this.ParametroBusqPro = ParametroBusqPro;
    }

    public void setValorBusqPro(String ValorBusqPro) {
        this.ValorBusqPro = ValorBusqPro;
    }

    public Boolean getHablilitaGarantia() {
        return HablilitaGarantia;
    }

    public void setHablilitaGarantia(Boolean HablilitaGarantia) {
        this.HablilitaGarantia = HablilitaGarantia;
    }

    public Boolean getHablilitaProducto() {
        return HablilitaProducto;
    }

    public void setHablilitaProducto(Boolean HablilitaProducto) {
        this.HablilitaProducto = HablilitaProducto;
    }

    public Boolean getHablilitaOrden() {
        return HablilitaOrden;
    }

    public void setHablilitaOrden(Boolean HablilitaOrden) {
        this.HablilitaOrden = HablilitaOrden;
    }

    public Boolean getHabilitarCliente() {
        return HabilitarCliente;
    }

    public void setHabilitarCliente(Boolean HabilitarCliente) {
        this.HabilitarCliente = HabilitarCliente;
    }

    public Boolean getHablilitaGuardarProducto() {
        return HablilitaGuardarProducto;
    }

    public void setHablilitaGuardarProducto(Boolean HablilitaGuardarProducto) {
        this.HablilitaGuardarProducto = HablilitaGuardarProducto;
    }

}
