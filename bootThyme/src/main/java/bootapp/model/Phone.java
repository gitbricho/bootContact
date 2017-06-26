package bootapp.model;

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
    // メッセージを明示しない場合：JSR303のデフォルトメッセージが表示される.
    @Pattern(regexp = "^(\\d{2,4}\\d{2,4}\\d{4})?$", message = "{emsg.telno}")
    private String telno;
    // Size は EL式で messages.properties の cstm.size.msg を表示
    @Column(length = 20)
    @NotNull
    @Size(min = 1, message = "{emsg.phoneType.hissu}")
    private String phoneType;

    public Phone() {
        this(null, "", "");
    }

    public Phone(Long id, String telno, String phoneType) {
        super();
        super.setId(id);
        this.telno = telno;
        this.phoneType = phoneType;
    }

    public Phone(String telno, String phoneType) {
        this(null, telno, phoneType);
    }

    public Phone(Phone phone) {
        this(phone.getId(), phone.getTelno(), phone.getPhoneType());
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
        return "Phone(" + id + ":" + version + ")[telno=" + 
        		telno + ", phoneType=" + phoneType + "]";
    }
}
