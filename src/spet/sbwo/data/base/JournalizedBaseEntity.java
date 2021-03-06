package spet.sbwo.data.base;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import spet.sbwo.data.table.User;

@MappedSuperclass
public class JournalizedBaseEntity extends BaseEntity {
	@ManyToOne(optional = true)
	@JoinColumn(name = "C_CREATEDBY_ID", nullable = true)
	protected User createdBy;

	@Column(name = "C_CREATEDON")
	protected Timestamp createdOn;

	@ManyToOne(optional = true)
	@JoinColumn(name = "C_CHANGEDBY_ID", nullable = true)
	protected User changedBy;

	@Column(name = "C_CHANGEDON")
	protected Timestamp changedOn;

	@Column(name = "C_DELETED", nullable = false)
	protected boolean deleted;
	

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public User getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}

	public Timestamp getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(Timestamp changedOn) {
		this.changedOn = changedOn;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
