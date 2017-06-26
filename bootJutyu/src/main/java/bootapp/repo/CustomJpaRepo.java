package bootapp.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * リポジトリの定義で、直接 JpaRepository を拡張しないで、
 * このカスタム版の JpaRepository を拡張する.
 * これにより、SpringData は実行時に JpaRepository の実装クラスを作成するのではなく、
 * この CustomJpaRepo インターフェースを実装した CustomJpaRepoImpl クラスを生成する.
 * @param <T> エンティティ
 * @param <ID> エンティティのID
 */
@NoRepositoryBean
public interface CustomJpaRepo<T, ID extends Serializable> 
	extends JpaRepository<T, ID> {

	/**
	 * 本来は padZero などの名前にすべきだが、このインターフェースを
	 * 継承するインターフェースに対して共通の処理を提供する例として kyotuSyori
	 * という名前にしている.
	 * @param id
	 * @return
	 */
	public String kyotuSyori(ID id);
}