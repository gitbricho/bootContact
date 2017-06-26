package bootapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import bootapp.model.Kokyaku;
import bootapp.model.KokyakuList;
import bootapp.repo.KokyakuRepo;

/**
 * id | email から Kokyakuオブジェクトを取得するコンバーター.
 */
@Component
public class KokyakuListConverter implements Converter<String, List<Kokyaku>> {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private KokyakuRepo repo;
    
    @Autowired
    public KokyakuListConverter(KokyakuRepo repo) {
        this.repo = repo;
    }
    
    @Override
    public List<Kokyaku> convert(String searchStr) {
        log.info("searchStr="+searchStr);
        List<Kokyaku> list = null;
        if (searchStr.startsWith("nameLike:")) {
            String str = searchStr.substring("nameLike:".length());
            log.info("### str={}",str);
            list = repo.findByNameLikeOrderByNameDesc("%"+str+"%");
        } else if (searchStr.startsWith("name:")) {
            String str = searchStr.substring("name:".length());
            log.info("### str={}",str);
            list = repo.findByName(str);
        } else if (searchStr.startsWith("emailLike:")) {
            String str = searchStr.substring("emailLike:".length());
            log.info("### str={}",str);
            list = repo.findByEmailLike("%"+str+"%");
        } else if (searchStr.startsWith("email:")) {
            String str = searchStr.substring("email:".length());
            log.info("### str={}",str);
            list = repo.findByEmail(str);
        }
        return list;
    }

}
