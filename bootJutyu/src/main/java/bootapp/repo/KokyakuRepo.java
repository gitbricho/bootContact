package bootapp.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import bootapp.model.Kokyaku;

/**
 * 顧客リポジトリ.
 * <p>このリポジトリでは、単一テーブルに対する様々な CRUD 操作を試す.</p>
 * <p>また、RESTful Web サービスの機能を試すためにこのリポジトリを使用する.</p>
 */
@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface KokyakuRepo extends JpaRepository<Kokyaku, Long> {

    // データ取得
    /** 全件を取得 */
    public List<Kokyaku> findAll();

    /** 指定 ID の一件を取得 */
    public Kokyaku findOne(Long id);

    //#### メソッドの命名規則による検索 (主なもの抜粋)
    /**
     * 顧客名で検索 (ソート指定なし)
     * 
     * @param name
     *            顧客名
     * @return 検索結果
     */
    public List<Kokyaku> findByName(String name);
   
    /**
     * 顧客名で検索 (曖昧検索,逆順ソート)
     * 
     * @param name
     *            顧客名 (例："岡本%", "%川%" ..)
     * @return 検索結果
     */
    public List<Kokyaku> findByNameLikeOrderByNameDesc(String name);

    /**
     * 顧客名で検索 (曖昧検索,電子メールソート)
     * 
     * @param name
     *            氏名 (例："岡本%", "%川%" ..)
     * @return 検索結果
     */
    public List<Kokyaku> findByNameLikeOrderByEmailAsc(String name);

    /**
     * 受注回数で検索
     * 
     * @param jutyuKaisu
     *            受注回数
     * @return 検索結果
     */
    public List<Kokyaku> findByJutyuKaisuGreaterThanEqual(int jutyuKaisu);

    public List<Kokyaku> findByJutyuKaisuBetween(int min, int max);

    public List<Kokyaku> findByJutyuKaisuGreaterThanAndEmailLike(
    		int jutyuKaisu, String email);
    


    // データ操作：確認のため、あえて明示的に宣言
    /**
     * 顧客の作成｜更新
     * 
     * @param e
     *            保存する顧客インスタンス
     * @return 保存された顧客
     */
    public <T extends Kokyaku> T save(T e);

    /**
     * 顧客の削除
     * 
     * @param e
     *            削除する顧客
     */
    public void delete(Kokyaku e);

    /**
     * 顧客の削除
     * 
     * @param id
     *            削除する顧客のid
     */
    public void delete(Long id);
    
    /**
     * メールアドレスによる検索
     * @param email
     * @return
     */
    public List<Kokyaku> findByEmail(String email);
    public List<Kokyaku> findByEmailLike(String email);
    
    public List<Kokyaku> findByNameAndEmail(String name, String email);
    public List<Kokyaku> findByNameOrEmail(String name, String email);
    
    /**
     * 特殊パラメータ(Pageable, Sort) の使用と
     * 問い合わせ件数の制限
     * Pageable: クエリーメソッドでページング機能を使用.
     * Sort: クエリーメソッドでソート機能を使用.
     */
    public Page<Kokyaku> findAll(Pageable page);
    public Kokyaku findFirstByEmailLike(String email, Sort sort);
    public List<Kokyaku> findFirst5ByEmailLike(String email, Sort sort);
}