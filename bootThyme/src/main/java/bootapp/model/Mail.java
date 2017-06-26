package bootapp.model;

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
    @Email(message = "{emsg.mail}")
    private String mail;
    @Column(length = 20)
    @NotNull
    @Size(min = 1, message = "{emsg.mailType.hissu}")
    private String mailType;

    public Mail() {
        this(null, "", "");
    }

    public Mail(Long id, String mail, String mailType) {
        super();
        super.setId(id);
        this.mail = mail;
        this.mailType = mailType;
    }

    public Mail(String mail, String mailType) {
        this(null, mail, mailType);
    }

    public Mail(Mail mail) {
        this(mail.getId(), mail.getMail(), mail.getMailType());
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
        return "Mail(" + id + ":" + version + ")[mail=" + 
        		mail + ", mailType=" + mailType + "]";
    }
}
