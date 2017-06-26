package bootapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.test.context.junit4.SpringRunner;

import bootapp.model.Jutyu;
import bootapp.model.Meisai;
import bootapp.repo.JutyuRepo;

/**
 * 受注エンティティ/リポジトリ、明細エンティティ/リポジトリの
 * メソッドをテストする.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JutyuDBTest {

    private static final Logger log = LoggerFactory.getLogger(JutyuDBTest.class);

    @Autowired
    private JutyuRepo jRepo;
    
    @Rule
    public LogRule logRule = new LogRule();

    @Before
    public void setUp() {
    	createTestData();
    }
    private void createTestData() {
    	jRepo.deleteAll();
    	assertThat(jRepo.findAll().size()).isEqualTo(0);
        Jutyu jutyu = new Jutyu(1L, "2015/09/01", "納品先１");
        jutyu.getMeisaiList().add(new Meisai(101L, "商品１", 100, 2, jutyu));
        jutyu.getMeisaiList().add(new Meisai(102L, "商品２", 200, 2, jutyu));
        jutyu = jRepo.save(jutyu);
        assertThat(jutyu.getId()).isEqualTo(1L);
        jutyu = new Jutyu(2L, "2015/09/01", "納品先２");
        jutyu.getMeisaiList().add(new Meisai(201L, "商品１", 100, 5, jutyu));
        jutyu.getMeisaiList().add(new Meisai(202L, "商品２", 200, 3, jutyu));
        jutyu = jRepo.save(jutyu);
        assertThat(jutyu.getId()).isNotEqualTo(null);
        jutyu = new Jutyu(3L, "2015/09/02", "納品先３");
        jutyu.getMeisaiList().add(new Meisai(301L, "商品１", 100, 5, jutyu));
        jutyu.getMeisaiList().add(new Meisai(302L, "商品２", 200, 3, jutyu));
        jutyu.getMeisaiList().add(new Meisai(303L, "商品３", 300, 8, jutyu));
        jutyu = jRepo.save(jutyu);
        assertThat(jutyu.getId()).isEqualTo(3L);
        jutyu = new Jutyu(4L, "2015/09/03", "納品先２");
        jutyu.getMeisaiList().add(new Meisai(401L, "商品４", 400, 2, jutyu));
        jutyu.getMeisaiList().add(new Meisai(402L, "商品５", 500, 3, jutyu));
        jutyu = jRepo.save(jutyu);
        assertThat(jutyu.getId()).isNotEqualTo(null);
        log.info(jRepo.kyotuSyori(jutyu.getId()));
        assertThat(jRepo.findAll().size()).isEqualTo(4);
    }
    
    private void logAllMeisai(String setumei, List<Jutyu> jlist) {
        log.info("----- {}:明細一覧 --------------------", setumei);
        jlist.forEach(j -> {
            j.getMeisaiList().forEach(m -> log.info(m.toString()));
            log.info("--------------------");
        });
    }
    
    //=====(TEST-METHOD)=====
    @Test
    public void addJutyu() {
    	logRule.testStart("", "受注追加");
        log.info(">>> setUp で受注4件を追加.");
        logAllMeisai("追加後", jRepo.findAll());
    }

    @Test
    public void findJutyu() {
    	    logRule.testStart("", "受注検索");
        log.info(">>> 商品２で受注を検索: findBySyohin(\"商品２\")");
        List<Jutyu> jlist = jRepo.findBySyohin("商品２");
        assertThat(jlist.size()).isEqualTo(3);
        logAllMeisai("検索結果", jlist);

        log.info(">>> 受注額で受注を検索: findJutyugakuGreaterThan(1300)");
        jlist = jRepo.findJutyugakuGreaterThan(1300);
        //assertThat(jlist.size()).isEqualTo(3);
        logAllMeisai("検索結果", jlist);

    	    log.info(">>> 受注別、受注額一覧: jutyuBetuJutyugaku()");
        jRepo.jutyuBetuJutyugaku()
        	.stream().map(Arrays::toString).forEach(log::info);

        log.info(">>> 商品別、受注額一覧: syohinBetuJutyugaku()");
        jRepo.syohinBetuJutyugaku()
        	.stream().map(Arrays::toString).forEach(log::info);
        
        assertThat(jRepo.kyotuSyori(2L)).isEqualTo("00000002");
    }

    @Test
    public void updateJutyu01() {
    	logRule.testStart("", "受注更新01");
        log.info(">> 受注日 9/2 のデータ(jutyu.id=3)の 受注日を 9/12 に変更");
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/02")) {
        	assertThat(j.getJutyuBi()).isEqualTo("2015/09/02");
        	log.info(j.toString());
            j.setJutyuBi("2015/09/12");
            jRepo.save(j);
        }
        log.info("更新後 ----------------------");
        Jutyu j1 = jRepo.findOne(3L);
        log.info(j1.toString());
        assertThat(j1.getJutyuBi()).isEqualTo("2015/09/12");
    }

    @Test
    public void updateJutyu02() {
    	logRule.testStart("", "受注更新02");
        log.info(">> 受注日 9/3 のデータ(jutyu.id=4)の 商品４(meisai.id=401L)の数量を 2->3 に変更");
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/03")) {
            for (Meisai m : j.getMeisaiList()) {
            	log.info(m.toString());
                if (m.getSyohin().equals("商品４")) {
                    m.setSuryo(3);
                }
            }
            jRepo.save(j);
        }
        logAllMeisai("更新後", jRepo.findByJutyuBi("2015/09/03"));
    }

    @Test
    public void updateJutyu03() {
    	logRule.testStart("", "受注更新03");
        log.info(">> 受注日 9/1 のデータ(jutyu.id=1,2)に明細２件を追加");
        long meisaiId;
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/01")) {
        	j.getMeisaiList().forEach(m->{
        		log.info(m.toString());
        	});
        	meisaiId = j.getId() * 100;
            j.getMeisaiList().add(new Meisai(meisaiId + 11, "商品６", 600, 10, j));
            j.getMeisaiList().add(new Meisai(meisaiId + 12, "商品７", 700, 11, j));
            jRepo.save(j);
        }
        log.info("追加後 ----------------------");
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/01")) {
        	assertThat(j.getMeisaiList().size()).isEqualTo(4);
        	j.getMeisaiList().forEach(m->{
        		log.info(m.toString());
        	});
        }
    }

    @Test
    public void updateJutyu04() {
    	logRule.testStart("", "受注更新04");
        log.info(">> 受注日 9/1 のデータ(jutyu.id=1,2)から商品２の明細を削除");
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/01")) {
        	j.getMeisaiList().forEach(m->{
        		log.info(m.toString());
        	});
            List<Meisai> dels = new ArrayList<>();
            for (Meisai m : j.getMeisaiList()) {
                if (m.getSyohin().equals("商品２")) {
                    dels.add(m);
                }
            }
            j.getMeisaiList().removeAll(dels);
            jRepo.save(j);
        }
        logAllMeisai("削除後の9/1のデータ", jRepo.findByJutyuBi("2015/09/01"));
    }

    @Test
    public void deleteJutyu() {
    	logRule.testStart("", "受注削除");
        log.info("受注削除：受注日 9/3 のデータを削除、カスケード削除により、 明細も削除される");
        logAllMeisai("削除前の 9/3のデータ", jRepo.findByJutyuBi("2015/09/03"));
        for (Jutyu j : jRepo.findByJutyuBi("2015/09/03")) {
            jRepo.delete(j);
        }
        logAllMeisai("削除後の 9/3のデータ", jRepo.findByJutyuBi("2015/09/03"));
    }

    @Test
    public void updateSyohinTanka() {
    	logRule.testStart("", "modify クエリー");
        log.info("modify クエリーで商品単価を20%引きにする.");
        logAllMeisai("割引前", jRepo.findAll());
        jRepo.waribiki(20);
        logAllMeisai("割引後", jRepo.findAll());
    }
        

}
