package bootapp.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bootapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u where u.name=:userName")
    public List<User> findByName(@Param("userName") String userName);
    
    @Query(value = "select u from User u order by u.name")
    public List<User> findAll();

    // $$$ ページ取得 $$$$$
    // 検索文字列あり
    @Query("select u from User u where "
            + "concat(u.name, u.simei, u.yomi) like %:searchStr%")
    public List<User> findPage(@Param("searchStr") String searchStr,
            Pageable request);

    // 検索文字列なし
    @Query("select u from User u")
    public List<User> findPage(Pageable request);
}
