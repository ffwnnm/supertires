package kz.supershiny.core.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kz.supershiny.core.util.Crypter;

/**
 *
 * @author kilrwhle
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column(unique = true, nullable = false)
    private String phone;   //phone is a login
    @Column
    private String password;
    @Column
    private Integer loginHash;
    
    public User() {
    }

    public User(String username, String phone, String password) {
        this.username = username;
        this.phone = phone;
        this.password = Crypter.encryptString(password);
        this.loginHash = Integer.valueOf(hashLogin(phone));
    }
    
    public static int hashLogin(String value) {
        int hash = 3;
        hash = 677 * hash + (value != null ? value.hashCode() : 0);
        return hash;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 41 * hash + (this.phone != null ? this.phone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.phone == null) ? (other.phone != null) : !this.phone.equals(other.phone)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "User{" + "username=" + username + ", phone=" + phone + ", password=" + password + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLoginHash() {
        return loginHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.loginHash = Integer.valueOf(hashLogin(phone));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //turn plain password into hash
        this.password = Crypter.encryptString(password);
    }
}
