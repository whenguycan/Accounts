package moe.atalanta.accounts.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by wang on 2018/8/28.
 */
@Entity
public class Accounts implements Serializable {
    private static final long serialVersionUID = 5615762862726648146L;

    @Id
    private Long id;

    private String domain;

    private String label;

    private String username;

    private String password;

    private String remarks;

    private int using;

    @Generated(hash = 991955428)
    public Accounts(Long id, String domain, String label, String username,
            String password, String remarks, int using) {
        this.id = id;
        this.domain = domain;
        this.label = label;
        this.username = username;
        this.password = password;
        this.remarks = remarks;
        this.using = using;
    }

    @Generated(hash = 1756363702)
    public Accounts() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getUsing() {
        return this.using;
    }

    public void setUsing(int using) {
        this.using = using;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
