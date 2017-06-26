package bootapp.model

import javax.persistence.CascadeType;
import javax.persistence.Entity
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import bootapp.model.AbstractPersistentEntity

/**
 * 受注エンティティ (Order).
 * <p>
 * 受注：明細 = 1:N (双方向)
 * <p>
 * <ul>
 * <li>受注追加）明細０件でも可
 * <li>受注削除）明細も削除
 * <li>受注更新）受注項目の更新
 * <li>受注更新）明細追加、明細削除、明細項目の更新
 * </ul>
 */
@Entity
@NamedQuery(name = "Jutyu.findByNohinSaki",
    query = "select j from Jutyu j where j.nohinSaki = ?1")
public class Jutyu extends AbstractPersistentEntity {

    private static final long serialVersionUID = 1L;

    String jutyuBi;
    String nohinSaki;
    int jutyuGaku;

    // 受注：受注明細 = 1:N
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Meisai> meisaiList = new ArrayList<Meisai>();

    public Jutyu() {
        this("", "");
    }

    public Jutyu(String jutyuBi, String nohinSaki) {
        this(null, jutyuBi, nohinSaki);
    }
    
    public Jutyu(Long id, String jutyuBi, String nohinSaki) {
        this.id = id;
        this.jutyuBi = jutyuBi;
        this.nohinSaki = nohinSaki;
    }
    
    public int getJutyuGaku() {
        jutyuGaku = 0
        for(Meisai m: meisaiList) {
          jutyuGaku += m.getTanka() * m.getSuryo()   
        }
        return jutyuGaku
    }
    
    @Override
    public String toString() {
        //注意: + を後ろに置くこと
        return "Jutyu(" + id + ")[" + jutyuBi + ", " + nohinSaki  + 
                ", 受注額=" + getJutyuGaku() + "]";
    }
}
