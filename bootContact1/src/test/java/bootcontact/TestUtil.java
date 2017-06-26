package bootcontact;

import static bootcontact.AppConst.C_UPD_SEINEN_GAPPI;
import static bootcontact.AppConst.C_UPD_SIMEI_SEI;
import static bootcontact.AppConst.C_UPD_YOMI_SEI;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.NamedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import bootcontact.model.Contact;
import bootcontact.repo.ContactRepository;

class TestUtil {
    private static final Logger log = LoggerFactory.getLogger(TestUtil.class);
    private AppData data;
    private ContactRepository contactRepo;
    
    public TestUtil(AppData data, ContactRepository repo) {
    	this.data = data;
    	this.contactRepo = repo;
    }

    Contact getContactLikeSimei(String srchStr) {
        List<Contact> contacts = contactRepo.findAll(
        		data.getUser("tosi"), "家族親戚", srchStr);
        assertThat(contacts.size()).isEqualTo(1);
        return contacts.get(0);
    }
    
    //修正された連絡先の取得
    Contact getEditedContact(Long id) {
        Contact c = contactRepo.findOne(id);
        c.setSimeiSei(C_UPD_SIMEI_SEI);
        c.setYomiSei(C_UPD_YOMI_SEI);
        c.setSeinenGappi(C_UPD_SEINEN_GAPPI);    
        return c;
    }
    
    void logSetup() {
        log.debug("$$$$$$ テストの初期処理： ユーザー({}件), 連絡先({}件)を作成"
        		, data.getUserKensu()
        		, data.getTosiKazokuSinsekiKensu() + data.getTosiYujinTijinKensu());
    }
    
    /**
     * 連絡先一覧をログ出力する.
     * @param contacts 連絡先リスと
     * @param rem 説明文
     */
    void logAllContact(List<Contact> contacts, String rem) {
        log.info(">> {}:連絡先一覧 --------------", rem);
        contacts.forEach(c -> {
        	log.info(c.toString());
            c.getAddresses().forEach(e->{log.info(">>{}", e.toString()); });
            c.getMails().forEach(e->{ log.info(">>{}", e.toString()); });
            c.getPhones().forEach(e->{ log.info(">>{}", e.toString()); });
            log.info("---------------------------");
        });
    }
    
    
    MockHttpServletRequestBuilder buildForm(Map<String, String> params,
    		MockHttpServletRequestBuilder form) {
    	for (Entry<String, String> param: params.entrySet()) {
    		form = form.param(param.getKey(), param.getValue());
    	}
    	return form;
    }
}
