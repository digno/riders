package com.leadtone.riders.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.common.collect.Lists;

@Entity
@Table(name = "activity")
public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3754934476329455366L;

	private Long aid;

	private User owner;

	private Date ctime;

	private String title;
	
	private String content;
	
	private String expireTime;
	
	private Integer atype;
	
	private List<User> activityMembers = Lists.newArrayList(); // 有序的关联对象集合

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	@NotNull
	@OneToOne
	@JoinColumn(name = "owner_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}


	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	
	

	public Integer getAtype() {
		return atype;
	}

	public void setAtype(Integer atype) {
		this.atype = atype;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "activity_members", joinColumns = { @JoinColumn(name = "aid") }, inverseJoinColumns = { @JoinColumn(name = "uid") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序
	@OrderBy("id ASC")
	// 缓存策略
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<User> getActivityMembers() {
		return activityMembers;
	}

	public void setActivityMembers(List<User> activityMembers) {
		this.activityMembers = activityMembers;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
