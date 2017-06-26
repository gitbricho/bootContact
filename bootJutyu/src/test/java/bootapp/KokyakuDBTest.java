package bootapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import bootapp.model.Kokyaku;
import bootapp.repo.KokyakuRepo;

/**
 * 顧客エンティティ/リポジトリのメソッドをテストする.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KokyakuDBTest {

    private static final Logger log = LoggerFactory.getLogger(KokyakuDBTest.class);

    @Autowired
    private KokyakuRepo kRepo;
    
    @Rule
    public LogRule logRule = new LogRule();
    
    @Before
    public void setUp() {
    	createTestData();
    }
    private void createTestData() {
    	kRepo.deleteAll();
        assertThat(kRepo.findAll().size()).isEqualTo(0);
        // 顧客4件追加
        kRepo.save(new Kokyaku(1L,"倉岡宏", "h.kuraoka@xxx.com", 10));
        kRepo.save(new Kokyaku(2L,"田中優子", "y.tanaka@zzz.com", 20));
        kRepo.save(new Kokyaku(3L,"岡田英二", "e.okada@yyy.com", 5));
        kRepo.save(new Kokyaku(4L,"岡本恵子", "k.okamoto@yyy.com", 8));
        // さらに10件追加
        int kaisu=100;
        for (long id = 10L; id<20L; id++, kaisu+=100) {
        	kRepo.save(new Kokyaku(id, "氏名"+100*id, "a"+100*id+"@xxx.com", kaisu));
        }
        assertThat(kRepo.findAll().size()).isEqualTo(14);
        logAllKokyaku("セットアップ後");
	}
	private void logAllKokyaku(String setumei) {
        log.info("----- {}:顧客全件 --------------------", setumei);
        kRepo.findAll().forEach(k -> log.info(k.toString()));
    }
    
    //=====(TEST-METHOD)=====
    //## 顧客エンティティの操作
    @Test
    public void findKokyaku() {
        logRule.testStart("", "様々な顧客検索");
        List<Kokyaku> klist = null;
        log.info(">>> 名前(=岡本恵子) ==> 1");
        klist = kRepo.findByName("岡本恵子");
        assertThat(klist.size()).isEqualTo(1);
        assertThat(klist.get(0).getName()).isEqualTo("岡本恵子");
        assertThat(klist.get(0).getEmail()).isEqualTo("k.okamoto@yyy.com");
        assertThat(klist.get(0).getJutyuKaisu()).isEqualTo(8);
        klist.forEach((k) -> log.info(k.toString()));

        log.info(">>> 名前曖昧(=%岡%) NameDesc ==> 3");
        klist = kRepo.findByNameLikeOrderByNameDesc("%岡%");
        assertThat(klist.size()).isEqualTo(3);
        klist.forEach((k) -> log.info(k.toString()));

        log.info(">>> 名前曖昧(=%岡%) EmailAsc ==> 3");
        klist = kRepo.findByNameLikeOrderByEmailAsc("%岡%");
        assertThat(klist.size()).isEqualTo(3);
        assertThat(klist.get(0).getEmail()).isEqualTo("e.okada@yyy.com");
        klist.forEach((k) -> log.info(k.toString()));

        log.info(">>> email(=y.tanaka@zzz.com) ==> 1");
        klist = kRepo.findByEmail("y.tanaka@zzz.com");
        assertThat(klist.size()).isEqualTo(1);
        assertThat(klist.get(0).getEmail()).isEqualTo("y.tanaka@zzz.com");
        klist.forEach((k) -> log.info(k.toString()));

        log.info(">>> email(like %yyy.com%) ==> 2");
        klist = kRepo.findByEmailLike("%yyy.com%");
        assertThat(klist.size()).isEqualTo(2);
        assertThat(klist.get(0).getEmail().endsWith("yyy.com")).isEqualTo(true);
        klist.forEach((k) -> log.info(k.toString()));

        log.info(">>> 受注回数(>=10) ==> 12件");
        klist = kRepo.findByJutyuKaisuGreaterThanEqual(10);
        assertThat(klist.size()).isEqualTo(12);
        klist.forEach((k) -> log.info(k.toString()));
        
        log.info(">>> 受注回数(>300) email(like '%xxx.com') ==> ７件");
        klist = kRepo.findByJutyuKaisuGreaterThanAndEmailLike(300, "%xxx.com");
        assertThat(klist.size()).isEqualTo(7);
        klist.forEach((k) -> log.info(k.toString()));
        
        log.info(">>> 受注回数([100,300]) ==> 3件");
        klist = kRepo.findByJutyuKaisuBetween(100, 300);
        assertThat(klist.size()).isEqualTo(3);
        klist.forEach((k) -> log.info(k.toString()));
        
        log.info(">>> Sort(Sort.Direction.ASC, 'jutyuKaisu')");
        klist = kRepo.findAll(new Sort(Sort.Direction.ASC, "jutyuKaisu"));
        klist.forEach((k) -> log.info(k.toString()));
 
        log.info(">>> findAll(new PageRequest(2, 5)");
        Page<Kokyaku> kpage = kRepo.findAll(new PageRequest(2, 5));
        kpage.forEach(kp->{
        	log.info(kp.getName());
        });
        
        log.info(">>> findFirstByEmailLike(String email, Sort sort)");
        Kokyaku k1 = kRepo.findFirstByEmailLike("%xxx.com", new Sort("jutyuKaisu"));
        log.info(k1.toString());

        log.info(">>> Sort(Sort.Direction.ASC, 'jutyuKaisu')");
        klist = kRepo.findFirst5ByEmailLike("%xxx.com", new Sort(Sort.Direction.ASC, "jutyuKaisu"));
        klist.forEach((k) -> log.info(k.toString()));      
    }
    
    @Test
    public void updateKokyaku01() {
        logRule.testStart("", "顧客更新");
        log.info(">>> 倉岡宏の受注回数を更新 10 --> 15 #####");
        for (Kokyaku c : kRepo.findByName("倉岡宏")) {
            c.setJutyuKaisu(15);
            kRepo.save(c);
        }
        List<Kokyaku> klist = kRepo.findByName("倉岡宏");
        assertThat(klist.size()).isEqualTo(1);
        assertThat(klist.get(0).getJutyuKaisu()).isEqualTo(15);
        logAllKokyaku("更新後");
    }
    
    @Test
    public void deleteKokayku() {
    	logRule.testStart("", "顧客削除");
    	logAllKokyaku("削除前");
        log.info(">>> 田中優子を削除");
        for (Kokyaku c : kRepo.findByName("田中優子")) {
            kRepo.delete(c.getId());
        }
        assertThat(kRepo.findAll().size()).isEqualTo(13);
        List<Kokyaku> klist = kRepo.findByName("田中優子");
        assertThat(klist.size()).isEqualTo(0);
        logAllKokyaku("田中優子の削除後");

        log.info(">>> 岡田英二を削除");
        for (Kokyaku c : kRepo.findByName("岡田英二")) {
            kRepo.delete(c);
        }
        assertThat(kRepo.findAll().size()).isEqualTo(12);
        klist = kRepo.findByName("岡田英二");
        assertThat(klist.size()).isEqualTo(0);
        logAllKokyaku("岡田の削除後");
    }
}
