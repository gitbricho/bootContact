package bootcontact.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Phone extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;
    
    @Column(length = 13)
    @Size(max = 13)
    // Pattern のエラーメッセージは明示しないので、JSR303のデフォルトメッセージを表示
    @Pattern(regexp = "\\d{2,4}\\d{2,4}\\d{4}")
    private String telno;
    // NotNull はエラーメッセージを明示しないので、JSR303デフォルトメッセージを表示
    // Size は EL式で messages.properties の cstm.size.msg を表示
    @Column(length = 20)
    @NotNull
    @Size(min = 1, message = "{cstm.size.msg}")
    private String phoneType;

    @NotNull
    @ManyToOne
    private Contact contact;

    public Phone() {
        this(null, "", "");
    }

    public Phone(Contact contact, String telno, String phoneType) {
        super();
        this.telno = telno;
        this.phoneType = phoneType;
        this.contact = contact;
    }

    public Phone(Phone phone) {
        this(phone.getContact(), phone.getTelno(), phone.getPhoneType());
        setId(phone.getId());
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public String toString() {
        return "Phone (" + phoneType + ":" + id + ")[telno=" + telno + "]";
    }
}
