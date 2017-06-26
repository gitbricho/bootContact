package bootapp.controller;

import static bootapp.AppConst.ATTR_KEKKA_LIST;
import static bootapp.AppConst.VIEW_KEKKA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bootapp.model.Jutyu;
import bootapp.model.Kokyaku;
import bootapp.model.KokyakuList;
import bootapp.repo.JutyuRepo;
import bootapp.repo.KokyakuRepo;

@Controller
public class JutyuController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JutyuRepo jutyuRepo;
    
    @Autowired
    private KokyakuRepo kokyakuRepo;
    
    @ModelAttribute("syohinMeiValues")
    public List<String> loadSyohinValues() {
        return Arrays.asList(new String[] {
                "商品001","商品002","商品003","商品004","商品005"
        });
    }        
    @ModelAttribute("kokyakuList")
    public List<Kokyaku> loadKokyakuList() {
        return kokyakuRepo.findAll();
    }
    @ModelAttribute("jutyuList")
    public List<Jutyu> loadJutyuList() {
        return jutyuRepo.findAll();
    }
    
    //=====(MAP-METHOD)=====
    
    @GetMapping("/jutyu_list")
    public String showJutyuList() {
        return "views/jutyu_list";
    }
    
    //%%%%% パスパラメータの取得 %%%%%
    // 例1）/jutyu/meisai/301
    @GetMapping("/jutyu/meisai/{meisaiId}")
    public ModelAndView getJutyuMeisai(@PathVariable("meisaiId") Long id) {
        List<String> kekkaList = new ArrayList<>();
        kekkaList.add("meeisaiId = " + id);
        return new ModelAndView(VIEW_KEKKA, ATTR_KEKKA_LIST, kekkaList);
    }
    
    // 例2）/jutyu/syohin/100/15
    @GetMapping("/jutyu/syohin/{tanka}/{kosu}")
    public ModelAndView getSyohin(@PathVariable("tanka") int tanka,
                @PathVariable("kosu") int kosu) {
        List<String> kekkaList = new ArrayList<>();
        kekkaList.add("tanka = " + tanka);
        kekkaList.add("kosu = " + kosu);
        return new ModelAndView(VIEW_KEKKA, ATTR_KEKKA_LIST, kekkaList);
    }
    
    //%%%%% クエリパラメータの取得 %%%%%
    // 例1）/kokyaku?id=10&name=納品先01"
    @GetMapping("/kokyaku")
    public ModelAndView getKokyaku(@RequestParam String id,
            @RequestParam String name) {
        List<String> kekkaList = new ArrayList<>();
        kekkaList.add("id = " + id);
        kekkaList.add("name = " + name);
        return new ModelAndView(VIEW_KEKKA, ATTR_KEKKA_LIST, kekkaList);
    }
    
    /**
     * コンバーターを使って文字列から Kokyaku へ変換.
     * @param kokyaku
     * @return
     */
    @GetMapping("/kokyaku/{id}")
    public ModelAndView getKokyaku(@PathVariable("id") Kokyaku kokyaku) {
        log.debug("kokyaku="+kokyaku);
        List<String> kekkaList = new ArrayList<>();
        kekkaList.add("getKokyaku:");
        if (kokyaku != null) {
            kekkaList.add("id = " + kokyaku.getId());
            kekkaList.add("name = " + kokyaku.getName());
            kekkaList.add("email = " + kokyaku.getEmail());
        } else {
            kekkaList.add("not found");
        }
        return new ModelAndView(VIEW_KEKKA, ATTR_KEKKA_LIST, kekkaList);
    }

    /**
     * コンバーターを使って文字列から List<Kokyaku>へ変換.
     * @param kokyakuList
     * @return
     */
    @GetMapping("/kokyaku/search/{str:.+}")
    public ModelAndView searchKokyaku(@PathVariable("str") List<Kokyaku> klist) {
        if (klist!=null) {
        	    log.debug("klist.list.size={}", klist.size());
        }
        return getKekkaMav(klist);
    }
        
    private ModelAndView getKekkaMav(List<Kokyaku> kokyakuList) {
        List<String> kekkaList = new ArrayList<>();
        if (kokyakuList != null) {
            kokyakuList.forEach((k)->
                kekkaList.add("id = " + k.getId() +
                        ", name = " + k.getName() +
                        ", email = " + k.getEmail()));
        } else {
            kekkaList.add("not found");
        }
        return new ModelAndView(VIEW_KEKKA, ATTR_KEKKA_LIST, kekkaList);
    }
}
