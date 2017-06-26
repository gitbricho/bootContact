package bootcontact.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bootcontact.model.Contact;
import bootcontact.model.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	//$$$ ログインユーザー別、種別別の連絡先一覧 $$$$$
	//検索文字列あり（氏名、よみ に対するあいまい検索）concat(e.simei,e.yomi)
    // SQLite3 は concat が使えないのでとりあえず氏名だけで検索する
	@Query("select e from Contact e where e.user=:user and e.syubetu=:syubetu" + 
			" and e.simeiSei like :searchStr or e.simeiMei like :searchStr")
	public List<Contact> findAll(@Param("user") User user,
			@Param("syubetu") String syubetu,
			@Param("searchStr") String searchStr);

	//検索文字列なし
	@Query("select e from Contact e where e.user=:user and e.syubetu=:syubetu")
	public List<Contact> findAll(@Param("user") User user,
			@Param("syubetu") String syubetu);
	
	//$$$ ログインユーザー別、種別別の連絡先件数 $$$$$
	@Query("select count(e) from Contact e where e.user=:user and e.syubetu=:syubetu")
	public long countContact(@Param("user") User user,
			@Param("syubetu") String syubetu);
	
	public List<Contact> findBySimeiSeiAndSimeiMeiAndSyubetuAndUser(
	        String simeiSei, String simeiMei, String syubetu, User user);
	
}
