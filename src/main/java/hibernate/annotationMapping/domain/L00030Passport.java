package hibernate.annotationMapping.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class L00030Passport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "passport_number")
	private String passportNumber;

	@OneToOne(mappedBy = "passport")
	private L00030Customer customer;

	public L00030Passport() {
	}

	public L00030Passport(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public L00030Customer getCustomer() {
		return customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public void setCustomer(L00030Customer customer) {
		this.customer = customer;
	}

}
