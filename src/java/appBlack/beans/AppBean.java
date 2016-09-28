/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appBlack.beans;

import appBlack.util.ServicesAppliancesBlack;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author luis
 */
@ManagedBean
@ApplicationScoped
public class AppBean {

    /**
     * Creates a new instance of AppBean
     */
    public AppBean() {
    }
    
    public String getURL_Menu(){
        return ServicesAppliancesBlack.getURL_Menu();
    }
    
}
