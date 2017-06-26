package bootcontact.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactCmd {

    private Long id;
    private Integer version;
    // 個人情報
    private String simeiSei; // 氏名:text
    private String simeiMei;
    private String yomiSei; // よみ:text
    private String yomiMei;
    private String seibetu; // 性別:radio
    private String seinenGappi; // 誕生日:datepicker
    private String memo; // メモ:textarea
    private String picture; // 写真:file|DND
    private String syumi; // 趣味:checkbox (マルチ選択のテスト用)
    private List<String> syumiList; // 趣味:checkbox (マルチ選択のテスト用)
    private String syubetu; // 連絡先種別:select

    // 仕事関連
    private String kaisya; // 会社:text

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
        this.simeiSei = c.getSimeiSei();
        this.simeiMei = c.getSimeiMei();
        this.yomiSei = c.getYomiSei();
        this.yomiMei = c.getYomiMei();
        this.seibetu = c.getSeibetu();
        this.seinenGappi = c.getSeinenGappi();
        this.memo = c.getMemo();
        this.picture = c.getPicture();
        this.syumi = c.getSyumi();
        this.syubetu = c.getSyubetu();

        this.kaisya = c.getKaisya();

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

    public String getSimeiSei() {
        return simeiSei;
    }

    public void setSimeiSei(String simeiSei) {
        this.simeiSei = simeiSei;
    }

    public String getSimeiMei() {
		return simeiMei;
	}

	public void setSimeiMei(String simeiMei) {
		this.simeiMei = simeiMei;
	}

	public String getYomiSei() {
        return yomiSei;
    }

    public void setYomiSei(String yomiSei) {
        this.yomiSei = yomiSei;
    }

	public String getYomiMei() {
		return yomiMei;
	}

	public void setYomiMei(String yomiMei) {
		this.yomiMei = yomiMei;
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
