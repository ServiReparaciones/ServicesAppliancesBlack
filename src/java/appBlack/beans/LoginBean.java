/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appBlack.beans;

import appBlack.util.ServicesAppliancesBlack;
import dataservices.crud.dbServicesMethods;
import dataservices.map.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author luis
 */
@ManagedBean
@SessionScoped
public class LoginBean {

    /**
     * Creates a new instance of LoginBean
     */
    private String Usuario;
    private String Password;

    HttpServletRequest origRequest
            = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private String urlRequest;

    public LoginBean() {
        this.urls_dbServices();
        this.init();
    }

    private void init() {
        this.setPassword("");
        this.setUsuario("");
    }

    private void urls_dbServices() {
        this.urlRequest = origRequest.getRequestURL().toString();
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/buscar.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/cliente.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/clientes.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/completar.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/menu.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/orden.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/producto.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/views/ver.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/resources/js/jquery-3.1.0min.js", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/index.xhtml", ""));
        this.setUrlRequest(this.urlRequest = this.urlRequest.replace("faces/loginPage", ""));
    }

    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean LoggedIn = false;
        String ruta = "";
        Usuario login = dbServicesMethods.FindUserLogin(Usuario.trim(), Password.trim(), 1);
        if (login != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
                    put("idFun", login.getFuncionarios().getIdFun());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
                    put("funNombres", login.getFuncionarios().getFunNombres());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
                    put("rolTipo", login.getFuncionarios().getRol().getRolTipo());

            Integer idRolTipo = login.getFuncionarios().getRol().getIdRol();

            if (idRolTipo == 1) {
                ruta = ServicesAppliancesBlack.getURL_Login()+"views/menu.xhtml";
            } else if (idRolTipo == 2) {
                ruta = ServicesAppliancesBlack.getURL_Login()+"";
            } else if (idRolTipo == 3) {
                ruta = ServicesAppliancesBlack.getURL_Login()+"";
            }

            LoggedIn = true;
        }
        context.addCallbackParam("loggedIn", LoggedIn);
        context.addCallbackParam("ruta", ruta);
    }
    
    public String getUserAttribute() {
        String UserAttribute = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session == null) {
        } else {
            Object idFun = session.getAttribute("funNombres");
            UserAttribute = idFun.toString();
        }
        return UserAttribute;
    }
    
    public Integer getUserAttributeId() {
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
    
    public void logout(java.awt.event.ActionEvent event) {
        String ruta = ServicesAppliancesBlack.getURL_Login() + "index.xhtml";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.removeAttribute(String.valueOf(this.getUserAttributeId()));
        session.invalidate();
        this.Usuario = "";
        this.Password = "";
        context.addCallbackParam("loggerOut", true);
        context.addCallbackParam("ruta", ruta);
    }
    
    public void generateMessage(FacesMessage.Severity Tipo, String Header, String Mensaje) {
        FacesMessage message = new FacesMessage(Tipo, Header, Mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getUrlRequest() {
        return urlRequest;
    }

    public void setUrlRequest(String urlRequest) {
        this.urlRequest = urlRequest;
    }
    
}
