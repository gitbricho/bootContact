package bootapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import bootapp.model.Jutyu;


/*
 * interface は実装クラスが必要だが、Spring Data の Repository を拡張した
 * interface では、Spring Data が、実行時に実装クラスを作成する.
 * 開発者はこのクラスの機能を使用するか、自前で実装するかを選択できる. 
 */
@RepositoryRestResource(collectionResourceRel = "order", path = "order")
public interface JutyuRepo extends CustomJpaRepo<Jutyu, Long>{
    public List<Jutyu> findByJutyuBi(String jutyuBi);
    
    // @Queryによる検索の例：
    /**
     * 明細の商品で受注を検索.
     * @param syohin 商品名
     * @return
     */
    @Query("select j from Jutyu j join j.meisaiList m where m.syohin=:syohin")
    public List<Jutyu> findBySyohin(@Param("syohin") String syohin);

    /**
     * sum(明細.単価 * 明細.数量)で受注額を求めて、指定受注額以上の受注を検索.
     * @param kingaku 検索する受注金額
     * @return
     */
    @Query("select j from Jutyu j join j.meisaiList m group by j having sum(m.tanka * m.suryo) > :kingaku")
    public List<Jutyu> findJutyugakuGreaterThan(@Param("kingaku") int kingaku);

    @Query("select j, sum(m.tanka*m.suryo) from Jutyu j join j.meisaiList m group by j order by j.jutyuBi")
    public List<Object[]> jutyuBetuJutyugaku();
    
    @Query("select m.syohin, sum(m.tanka*m.suryo) from Meisai m group by m.syohin order by m.syohin")
    public List<Object[]> syohinBetuJutyugaku();
    
    public List<Jutyu> findByNohinSaki();
        
    // modify クエリーによる更新の例：
    /**
     * 全明細の単価を指定した%分、割引する
     */
    @Modifying
    @Transactional
    @Query("update Meisai m set m.tanka = m.tanka * (100 - :waribikiRitu) / 100")
    public void waribiki(@Param("waribikiRitu") int waribikiRitu);

}
