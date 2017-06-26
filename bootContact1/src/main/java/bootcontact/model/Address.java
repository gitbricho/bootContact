package bootcontact.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

@Entity
public class Address extends AbstractPersistentEntity {

	private static final long serialVersionUID = 1L;
	static final String SIZE_MSG = "{max}以下の長さで入力してください";

	@Column(length = 7)
	@Size(max = 7, message = SIZE_MSG)
	@Pattern(regexp = "^([0-9]{3}[-]?[0-9]{4})?$", message = "{emsg.zip}")
	private String yubin;
	@Column(length = 10)
	@Size(max = 10, message = SIZE_MSG)
	private String todofuken;
	@Column(length = 40)
	@Size(max = 40, message = SIZE_MSG)
	private String sikuchoson;
	@Column(length = 50)
	@Size(max = 50, message = SIZE_MSG)
	private String banchi;
	@Column(length = 50)
	@Size(max = 50, message = SIZE_MSG)
	private String banchi2;
	@Column(length = 100)
	@Size(max = 100, message = SIZE_MSG)
	@URL
	private String webpage;
	@Column(length = 20)
	@NotNull
	@Size(min = 1, message = "{emsg.addressType.hissu}")
	private String addressType;

	public Address(Long id, String yubin, String todofuken, String sikuchoson,
			String banchi, String banchi2, String webpage, String addressType) {
		super();
		super.setId(id);
		this.yubin = yubin;
		this.todofuken = todofuken;
		this.sikuchoson = sikuchoson;
		this.banchi = banchi;
		this.banchi2 = banchi2;
		this.webpage = webpage;
		this.addressType = addressType;
	}

	public Address(String yubin, String todofuken, String sikuchoson,
			String banchi, String banchi2, String webpage, String addressType) {
		this(null, yubin, todofuken, sikuchoson, banchi, banchi2, webpage, addressType);
	}

	public Address() {
		this(null, "", "", "", "", "", "", "");
	}

	public Address(Address address) {
		this(address.getId(), address.getYubin(), address.getTodofuken(),
				address.getSikuchoson(), address.getBanchi(), address.getBanchi2(),
				address.getWebpage(), address.getAddressType());
		setId(address.getId());
	}

	@Column(length = 7)
	public String getYubin() {
		return yubin;
	}

	public void setYubin(String yubin) {
		this.yubin = yubin;
	}

	@Column(length = 10)
	public String getTodofuken() {
		return todofuken;
	}

	public void setTodofuken(String todofuken) {
		this.todofuken = todofuken;
	}

	@Column(length = 40)
	public String getSikuchoson() {
		return sikuchoson;
	}

	public void setSikuchoson(String sikuchoson) {
		this.sikuchoson = sikuchoson;
	}

	public String getBanchi() {
		return banchi;
	}

	public void setBanchi(String banchi) {
		this.banchi = banchi;
	}

	public String getBanchi2() {
		return banchi2;
	}

	public void setBanchi2(String banchi2) {
		this.banchi2 = banchi2;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	@Override
	public String toString() {
		return "Address(" + id + ":" + version + ")[ " + yubin +
				", " + todofuken + ", " + sikuchoson +
				", " + banchi + ", " + banchi2 + ", " + 
				", " + addressType + "]";
	}

}
