package bootapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "user")
public class User extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;

    private static final BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();

    // ログイン情報
    @Column(unique = true, length = 12)
    @Length(min = 4, max = 12, message = "ユーザー名は{min}から{max}文字の範囲で入力してください。")
    private String name;

    /** 暗号化されたパスワード */
    @Column(length = 128)
    private String encodePass;
    /** ロール一覧 */
    @Transient
    private List<String> roleList;
    /** ロールカンマ区切り文字列*/
    @Column(length = 128)
    private String roles;
    /** ユーザーのデフォルト種別 */
    @Column(length = 20)
    private String syubetu;
    /** ユーザーのデフォルトテーマ */
    @Column(length = 20)
    private String theme;
    
    // 個人情報
    @Column(length = 30)
    private String simei;
    @Column(length = 30)
    private String yomi;
    @Email
    private String email;

    public User() {
        super();
    }

    public User(String name) {
        this(name, name, "User", "家族知人", "", "", "", "");
    }

    public User(String name, String rawPass, String roles) {
        this(name, rawPass, roles, "家族知人", "", "", "", "");
    }

    public User(String name, String rawPass, String roles, String syubetu,
            String theme, String simei, String yomi, String email) {
        super();
        this.name = name;
        this.encodePass = bpe.encode(rawPass);
        setRoles(roles);
        this.syubetu = syubetu;
        this.theme = theme;
        this.simei = simei;
        this.yomi = yomi;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // %%%%% パスワード %%%%%
    public String getPass() {
        return encodePass;
    }

    public void setPass(String rawPass) {
        this.encodePass = bpe.encode(rawPass);
    }

    // %%%%% Role %%%%%
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
        if (roles != null && roles.length() > 0) {
            this.roleList = Arrays.asList(roles.split(",", 0));
        }
    }

    public List<String> getRoleList() {
        if (this.roleList == null) {
            this.roleList = new ArrayList<String>();
        }
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
        if (roleList != null) {
            this.roles = String.join(",", roleList);
        }
    }

    public String getSyubetu() {
        return syubetu;
    }

    public void setSyubetu(String syubetu) {
        this.syubetu = syubetu;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getSimei() {
        return simei;
    }

    public void setSimei(String simei) {
        this.simei = simei;
    }

    public String getYomi() {
        return yomi;
    }

    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", encodePass=" + encodePass + ", roleList="
                + roleList + ", roles=" + roles + ", syubetu=" + syubetu + ", theme="
                + theme + ", simei=" + simei + ", yomi=" + yomi + ", email=" + email
                + ", id=" + id + ", version=" + version + "]";
    }

}
