package hibernate.annotationMapping.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class L00027Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "staff_id", nullable = false)
	private String staffId;
	private String name;
	private Integer salary;

	@OneToMany(cascade = { CascadeType.PERSIST }, mappedBy = "guide")
	private Set<L00027Student> students = new HashSet<L00027Student>();

	public L00027Guide() {
	}

	public L00027Guide(String staffId, String name, Integer salary) {
		this.staffId = staffId;
		this.name = name;
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Set<L00027Student> getStudents() {
		return students;
	}

	public void addStudent(L00027Student student) {
		students.add(student);
		// update the student so that the student entity is associated with this guide
		student.setGuide(this);
	}

}