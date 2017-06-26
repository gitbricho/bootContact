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
    @Column(length = 20)
    @Size(min = 1, max = 20, message = "{emsg.sei.hissu}")
    private String simeiSei; // 姓:text
    @Column(length = 20)
    @Size(min = 1, max = 20, message="{emsg.mei.hissu}")
    private String simeiMei; // 名:text
    @Column(length = 20)
    private String yomiSei; // よみ(姓):text
    @Column(length = 20)
    private String yomiMei; // よみ(姓):text
    
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

    // ユーザー
    @ManyToOne
    private User user;

    private String googleid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Phone> phones;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mail> mails;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses;

    public Contact(Long id, User user, String simeiSei, String simeiMei, String yomiSei, 
    		String yomiMei, String seibetu, String seinenGappi, String syubetu) {
        super();
        super.setId(id);
        this.simeiSei = simeiSei;
        this.simeiMei = simeiMei;
        this.yomiSei = yomiSei;
        this.yomiMei = yomiMei;
        this.seibetu = seibetu;
        this.seinenGappi = seinenGappi;
        this.memo = "";
        this.picture = "";
        this.syumi = "";

        this.syubetu = syubetu;

        this.kaisya = "";

        this.user = user;
        googleid = "";

        this.addresses = new ArrayList<Address>();
        this.mails = new ArrayList<Mail>();
        this.phones = new ArrayList<Phone>();
    }
    
    public Contact(User user, String simeiSei, String simeiMei, String yomiSei, 
    		String yomiMei, String seibetu, String seinenGappi, String syubetu) {
    	this(null, user, simeiSei, simeiMei, yomiSei, yomiMei,
    			seibetu, seinenGappi, syubetu);
    }


    public Contact() {
        this(null, "", "", "", "", "", null, "");
    }

    public Contact(Contact c) {
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

        this.addresses.addAll(c.getAddresses());
        this.mails.addAll(c.getMails());
        this.phones.addAll(c.getPhones());
    }

    // #################
    // 1:n 関連
    // #################
    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Contact(").append(id).append(":").append(version)
		  .append(")[simei=").append(simeiSei).append(simeiMei)
		  .append(", yomi=").append(yomiSei + yomiMei)
		  .append(", seibetu=").append(seibetu)
		  .append(", seinenGappi=").append(seinenGappi)
		  .append(", memo=").append(memo)
		  .append(", picture=").append(picture)
		  .append(", syumi=").append(syumi)
		  .append(", syubetu=").append(syubetu)
		  .append(", kaisya=").append(kaisya)
		  .append(", user=").append(user)
		  .append(", googleid=").append(googleid);
		sb.append(", phones=");
		phones.forEach(e->{
			sb.append(e.toString());
		});
		sb.append(", mails=");
		mails.forEach(e->{
			sb.append(e.toString());
		});
		sb.append(", addresses=");
		addresses.forEach(e->{
			sb.append(e.toString());
		});
		sb.append("]");
		return sb.toString();
	}

}
