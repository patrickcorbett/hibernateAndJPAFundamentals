package hibernate.annotationMapping.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class L00030Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "passport_id", unique = true)
	private L00030Passport passport;

	public L00030Customer() {
	}

	public L00030Customer(String name, L00030Passport passport) {
		this.name = name;
		this.passport = passport;
	}

	public L00030Passport getPassport() {
		return passport;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassport(L00030Passport passport) {
		this.passport = passport;
	}

}
