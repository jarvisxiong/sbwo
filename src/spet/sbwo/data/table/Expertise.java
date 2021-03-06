package spet.sbwo.data.table;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import spet.sbwo.data.base.JournalizedBaseEntity;
import spet.sbwo.data.domain.ExpertiseStatus;
import spet.sbwo.data.embed.Tariff;

@Entity
@Table(name = "T_EXPERTISE")
public class Expertise extends JournalizedBaseEntity {

	@Column(name = "C_NUMBER", length = 32)
	private String number;

	@ManyToOne(optional = false)
	@JoinColumn(name = "C_COURT_ID")
	private Court court;

	@Column(name = "C_YEAR")
	private int year;

	@Column(name = "C_TITLE", length = 128)
	private String title;

	@Column(name = "C_STATUS")
	@Enumerated(EnumType.ORDINAL)
	private ExpertiseStatus status;

	@Column(name = "C_NOTE", length = 512)
	private String note;

	@ManyToOne
	@JoinColumn(name = "C_USER_ID")
	private User responsible;

	@Column(name = "C_LAST_CHECKED_ON")
	private Timestamp lastCheckedOn;

	@Column(name = "C_NEXT_HEARING")
	private Date nextHearing;

	@Embedded
	private Tariff tariff;

	@OneToMany(mappedBy = "expertise")
	private List<ExpertiseFine> fines;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ExpertiseStatus getStatus() {
		return status;
	}

	public void setStatus(ExpertiseStatus status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public User getResponsible() {
		return responsible;
	}

	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}

	public Timestamp getLastCheckedOn() {
		return lastCheckedOn;
	}

	public void setLastCheckedOn(Timestamp lastCheckedOn) {
		this.lastCheckedOn = lastCheckedOn;
	}

	public Date getNextHearing() {
		return nextHearing;
	}

	public void setNextHearing(Date nextHearing) {
		this.nextHearing = nextHearing;
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	public List<ExpertiseFine> getFines() {
		return fines;
	}

	public void setFines(List<ExpertiseFine> fines) {
		this.fines = fines;
	}

}
