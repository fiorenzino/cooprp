package org.giavacms.commons.model.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class Utente implements Serializable {
    private static final long serialVersionUID = -2548444688311424091L;

    private String username;
    private String nome;
    private String dn;
    private List<String> ruoli;

    public List<String> getRuoli() {
        if (ruoli == null) {
            ruoli = new ArrayList<String>();
        }
        return ruoli;
    }

    public void setRuoli(List<String> ruoli) {
        this.ruoli = ruoli;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDn()
    {
        return dn;
    }

    public void setDn(String dn)
    {
        this.dn = dn;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", username);
        claims.put("name", nome);
        claims.put("roles", ruoli.toArray());
        claims.put("dn", dn);
        return claims;

    }

}
