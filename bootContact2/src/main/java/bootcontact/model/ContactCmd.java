package bootcontact.model;

import java.util.List;
import java.util.Map;

public class ContactCmd {

    private Long id;
    private Integer version;
    // 個人情報
    private String simei; // 氏名:text
    private String yomi; // よみ:text
    private String keisyo; // 敬称:radio
    private String seibetu; // 性別:radio
    private String seinenGappi; // 誕生日:datepicker
    private String memo; // メモ:textarea
    private String picture; // 写真:file|DND
    private String syumi; // 趣味:checkbox (マルチ選択のテスト用)
    private List<String> syumiList; // 趣味:checkbox (マルチ選択のテスト用)
    private String syubetu; // 連絡先種別:select

    // 仕事関連
    private String kaisya; // 会社:text
    private String syozoku; // 所属:text
    private String yakusyoku; // 役職:text
    private String syokusyu; // 職種:text

    private User user;
    private String googleid;
    
    // 制御ステータス： 11:追加成功 | 12:更新成功 | 13：削除成功
    // 90:検証エラー | 91:保存失敗 | 93:削除失敗 | 0:初期状態
    private int status;
    // ページ全体のメッセージ
    private String message;
    // フィールドごとのメッセージ
    private Map<String, String> fldErrors;
    
    public ContactCmd() {}

    public ContactCmd(Contact c) {
        init(c);
    }
    
    public ContactCmd(int status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public void init(Contact c) {
        this.id = c.getId();
        this.version = c.getVersion();
        this.simei = c.getSimei();
        this.yomi = c.getYomi();
        this.keisyo = c.getKeisyo();
        this.seibetu = c.getSeibetu();
        this.seinenGappi = c.getSeinenGappi();
        this.memo = c.getMemo();
        this.picture = c.getPicture();
        this.syumi = c.getSyumi();
        this.syubetu = c.getSyubetu();

        this.kaisya = c.getKaisya();
        this.syozoku = c.getSyozoku();
        this.yakusyoku = c.getYakusyoku();
        this.syokusyu = c.getSyokusyu();

        this.user = c.getUser();
        this.googleid = c.getGoogleid();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSimei() {
        return simei;
    }

    public void setSimei(String simei) {
        this.simei = simei;
    }

    public String getYomi() {
        return yomi;
    }

    public void setYomi(String yomi) {
        this.yomi = yomi;
    }

    public String getKeisyo() {
        return keisyo;
    }

    public void setKeisyo(String keisyo) {
        this.keisyo = keisyo;
    }

    public String getSeibetu() {
        return seibetu;
    }

    public void setSeibetu(String seibetu) {
        this.seibetu = seibetu;
    }

    public String getSeinenGappi() {
        return seinenGappi;
    }

    public void setSeinenGappi(String seinenGappi) {
        this.seinenGappi = seinenGappi;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSyumi() {
        return syumi;
    }

    public void setSyumi(String syumi) {
        this.syumi = syumi;
    }

    public List<String> getSyumiList() {
        return syumiList;
    }

    public void setSyumiList(List<String> syumiList) {
        this.syumiList = syumiList;
    }

    public String getSyubetu() {
        return syubetu;
    }

    public void setSyubetu(String syubetu) {
        this.syubetu = syubetu;
    }

    public String getKaisya() {
        return kaisya;
    }

    public void setKaisya(String kaisya) {
        this.kaisya = kaisya;
    }

    public String getSyozoku() {
        return syozoku;
    }

    public void setSyozoku(String syozoku) {
        this.syozoku = syozoku;
    }

    public String getYakusyoku() {
        return yakusyoku;
    }

    public void setYakusyoku(String yakusyoku) {
        this.yakusyoku = yakusyoku;
    }

    public String getSyokusyu() {
        return syokusyu;
    }

    public void setSyokusyu(String syokusyu) {
        this.syokusyu = syokusyu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoogleid() {
        return googleid;
    }

    public void setGoogleid(String googleid) {
        this.googleid = googleid;
    }

    //ステータス
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getFldErrors() {
        return fldErrors;
    }

    public void setFldErrors(Map<String, String> fldErrors) {
        this.fldErrors = fldErrors;
    }
}
