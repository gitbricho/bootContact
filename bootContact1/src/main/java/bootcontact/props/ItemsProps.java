package bootcontact.props;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="items")
public class ItemsProps {
    private List<String> seibetu;
    private List<String> syumi;
    private List<String> contactSyubetu;
    private List<String> mailSyubetu;
    private List<String> phoneSyubetu;
    private List<String> jusyoSyubetu;
    private List<String> theme;
    public List<String> getSeibetu() {
        return seibetu;
    }
    public void setSeibetu(List<String> seibetu) {
        this.seibetu = seibetu;
    }
    public List<String> getSyumi() {
        return syumi;
    }
    public void setSyumi(List<String> syumi) {
        this.syumi = syumi;
    }
    public List<String> getContactSyubetu() {
        return contactSyubetu;
    }
    public void setContactSyubetu(List<String> contactSyubetu) {
        this.contactSyubetu = contactSyubetu;
    }
    public List<String> getMailSyubetu() {
        return mailSyubetu;
    }
    public void setMailSyubetu(List<String> mailSyubetu) {
        this.mailSyubetu = mailSyubetu;
    }
    public List<String> getPhoneSyubetu() {
        return phoneSyubetu;
    }
    public void setPhoneSyubetu(List<String> phoneSyubetu) {
        this.phoneSyubetu = phoneSyubetu;
    }
    public List<String> getJusyoSyubetu() {
        return jusyoSyubetu;
    }
    public void setJusyoSyubetu(List<String> jusyoSyubetu) {
        this.jusyoSyubetu = jusyoSyubetu;
    }
    public List<String> getTheme() {
        return theme;
    }
    public void setTheme(List<String> theme) {
        this.theme = theme;
    }
}
