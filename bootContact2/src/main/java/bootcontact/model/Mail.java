package bootcontact.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
public class Mail extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;
    
    @Column(length = 40)
    @Email(message = "メールアドレスの形式が正しくありません")
    private String mail;
    @Column(length = 20)
    @NotNull
    @Size(min = 1, message = "メール種別の入力は必須です")
    private String mailType;

    @NotNull
    @ManyToOne
    private Contact contact;

    public Mail() {
        this(null, "", "");
    }

    public Mail(Contact contact, String mail, String mailType) {
        super();
        this.mail = mail;
        this.mailType = mailType;
        this.contact = contact;
    }

    public Mail(Mail mail) {
        this(mail.getContact(), mail.getMail(), mail.getMailType());
        setId(mail.getId());
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    @Override
    public String toString() {
        return "Mail (" + mailType + ":" + id + ")[mail=" + mail + "]";
    }
}
