package bootapp.model

import javax.persistence.Entity;

import bootapp.model.AbstractPersistentEntity

/**
 * 顧客エンティティ (Customer).
 */
@Entity
public class Kokyaku extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;

    /** 顧客名 */
    String name = null;
    /** 電子メール */
    String email = null;
    /** 受注回数 */
    Integer jutyuKaisu = 0;

    public Kokyaku() {
        this("", "", 0)
    }

    public Kokyaku(String name, String email, Integer jutyuKaisu) {
        this(null, name, email, jutyuKaisu);
    }
    
    public Kokyaku(Long id, String name, String email, Integer jutyuKaisu) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.jutyuKaisu = jutyuKaisu;
    }

     public Kokyaku(Kokyaku kokyaku) {
        this(kokyaku.name, kokyaku.email, kokyaku.jutyuKaisu);
        this.id = kokyaku.id;
    }

    @Override
    public String toString() {
        return "Kokyaku(" + id + ")[name=" + name + ", email=" + email + 
            ", jutyuKaisu=" + jutyuKaisu + "]";
    }
}
