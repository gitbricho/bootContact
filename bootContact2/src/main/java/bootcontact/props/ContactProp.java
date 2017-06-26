package bootcontact.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "contact")
public class ContactProp {

    private int pageSize;
    private String defaultTheme;
    private String defaultSyubetu;
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public String getDefaultTheme() {
        return defaultTheme;
    }
    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
    public String getDefaultSyubetu() {
        return defaultSyubetu;
    }
    public void setDefaultSyubetu(String defaultSyubetu) {
        this.defaultSyubetu = defaultSyubetu;
    }
}
