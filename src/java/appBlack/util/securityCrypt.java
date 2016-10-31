/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appBlack.util;

/**
 *
 * @author luis
 */
public class securityCrypt {

    public static String Security(String input_encrypt) {
        String code = "";
        Aes128 aes128 = new Aes128();
        if (!input_encrypt.equals("") && input_encrypt != null) {
            try {
                code = new String(aes128.decrypt(input_encrypt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return code;
    }

}
