package bootcontact.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Contact extends AbstractPersistentEntity {
    private static final long serialVersionUID = 1L;

    // 個人情報
    @Column(length = 30)
    @Size(min = 1, max = 30)
    private String simei; // 氏名:text
    @Column(length = 30)
    private String yomi; // よみ:text
    @Column(length = 10)
    private String keisyo; // 敬称:radio
    private String seibetu;
    @Column(length = 10) // yyyy/MM/dd
    private String seinenGappi; // 誕生日:datepicker
    // @Temporal(TemporalType.DATE)
    // @Convert(converter = LocalDateConverter.class)
    // private LocalDate seinenGappi;

    private String memo; // メモ:textarea
    private String picture; // 写真:file|DND
    private String syumi; // 趣味:checkbox (マルチ選択のテスト用)
    @Transient
    private List<String> syumiList; // 趣味:checkbox (マルチ選択のテスト用)
    @NotNull(message = "連絡先種別は必ず設定してください")
    private String syubetu; // 連絡先種別:select

    // 仕事関連
    @Column(length = 40)
    private String kaisya; // 会社:text
    @Column(length = 40)
    private String syozoku; // 所属:text
    @Column(length = 30)
    private String yakusyoku; // 役職:text
    @Column(length = 30)
    private String syokusyu; // 職種:text

    // ユーザー
    @ManyToOne
    private User user;

    private String googleid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Map<String, Phone> phones;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Map<String, Mail> mails;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Map<String, Address> addresses;

    // Todo: Sex
    public Contact(User user, String simei, String yomi, String seibetu,
            String seinenGappi, String syubetu) {
        super();
        this.simei = simei;
        this.yomi = yomi;
        this.keisyo = "様";
        this.seibetu = seibetu;
        this.seinenGappi = seinenGappi;
        this.memo = "";
        this.picture = "";
        this.syumi = "";

        this.syubetu = syubetu;

        this.kaisya = "";
        this.syozoku = "";
        this.yakusyoku = "";
        this.syokusyu = "";

        this.user = user;
        googleid = "";

        this.addresses = new HashMap<>();
        this.mails = new HashMap<>();
        this.phones = new HashMap<>();
    }

    public Contact() {
        this(null, "", "", "", null, "");
    }

    public Contact(Contact c) {
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

        this.addresses = new HashMap<>(c.getAddresses());
        this.mails = new HashMap<>(c.getMails());
        this.phones = new HashMap<>(c.getPhones());
    }

    // #################
    // 1:n 関連
    // #################
    public Map<String, Mail> getMails() {
        return mails;
    }

    public void setMails(Map<String, Mail> mails) {
        this.mails = mails;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, Phone> getPhones() {
        return phones;
    }

    public void setPhones(Map<String, Phone> phones) {
        this.phones = phones;
    }

    // ユーザー
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

    /**
     * 趣味を設定する.
     * <p>
     * 同時に、趣味（カンマ区切り文字列）を趣味リストに変換する
     * </p>
     * 
     * @param syumi
     *            趣味.
     */
    public void setSyumi(String syumi) {
        this.syumi = syumi;
        if (syumi != null && syumi.length() > 0) {
            this.syumiList = Arrays.asList(syumi.split(",", 0));
        }
    }

    /**
     * 趣味リストを取得する.
     * <p>
     * 先に、setSyumi() を呼んで、現在の趣味（カンマ区切り文字列）と 趣味リストの同期させておく.
     * </p>
     * 
     * @return 趣味リスト.
     */
    public List<String> getSyumiList() {
        setSyumi(this.syumi);
        if (this.syumiList == null) {
            this.syumiList = new ArrayList<String>();
        }
        return syumiList;
    }

    /**
     * 趣味のリストを設定する.
     * <p>
     * 入力画面から得られた趣味リストをカンマ区切り文字列に変換して syumi フィールドにセットする.
     * </p>
     *
     * @param syumiList
     *            趣味のリスト.
     */
    public void setSyumiList(List<String> syumiList) {
        this.syumiList = syumiList;
        if (syumiList != null) {
            this.syumi = String.join(",", syumiList);
        }
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

    @Override
    public String toString() {
        return "Contact(" + id + ":" + version + ")[simei=" + simei + ", yomi=" + yomi
                + ", keisyo=" + keisyo + ", seibetu=" + seibetu + ", seinenGappi="
                + seinenGappi + ", memo=" + memo + ", picture=" + picture + ", syumi="
                + syumi + ", syubetu=" + syubetu + ", kaisya=" + kaisya + ", syozoku="
                + syozoku + ", yakusyoku=" + yakusyoku + ", syokusyu=" + syokusyu
                + ", user=" + user + ", googleid=" + googleid + "]";
    }
}
