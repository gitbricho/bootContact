package bootapp.model

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max

import bootapp.model.AbstractPersistentEntity

@Entity
public class Meisai extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;

    String syohin;
    int tanka;
    int suryo;
	
    @ManyToOne
    Jutyu jutyu;

    public Meisai() {
        this("", 0, 0, null);
    }

    public Meisai(String syohin, int tanka, int suryo, Jutyu jutyu) {
        this(null, syohin, tanka, suryo, jutyu);
    }

    public Meisai(Long id, String syohin, int tanka, int suryo, Jutyu jutyu) {
        this.id = id;
        this.syohin = syohin;
        this.tanka = tanka;
        this.suryo = suryo;
        this.jutyu = jutyu;
    }

    @Override
    public String toString() {
        return "Meisai(" + id + ")[" + syohin + ", 単価=" + tanka + 
            ", 数量=" + suryo + ", " + jutyu + "]";
    }
}
