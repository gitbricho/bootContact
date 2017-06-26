package bootapp.repo;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * CustomJpaRepo インターフェースの実装クラス.
 * CustomJpaRepo を拡張したリポジトリを定義すると、Spring Data が自動で
 * この実装クラスのインスタンスを生成する.
 * Spring Data にこのクラスが CustomJpaRepo の実装クラスであることを伝えるには
 * Java構成クラスで、以下の注釈を使用する.
 * @EnableJpaRepositories(repositoryBaseClass=CustomJpaRepoImpl.class)
 */
public class CustomJpaRepoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements CustomJpaRepo<T, ID> {

	private final EntityManager entityManager;

	public CustomJpaRepoImpl(JpaEntityInformation entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);

		// Keep the EntityManager around to used from the newly introduced
		// methods.
		this.entityManager = entityManager;
	}

	public String kyotuSyori(ID id) {
		return String.format("%08d", id);
	}
}
