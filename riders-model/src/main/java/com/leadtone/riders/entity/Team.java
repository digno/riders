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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.common.collect.Lists;

@Entity
@Table(name = "team")
public class Team implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6233309520591117883L;

	private Long tid;
	
	private Long master_id;
	
	private User leader;

	private Date ctime;

	private String declaration;
	
	private String teamname;
	
	private List<User> teamMembers = Lists.newArrayList(); // 有序的关联对象集合

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}
	
	@OneToOne
	@PrimaryKeyJoinColumn
	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public Date getCtime() {
		return ctime;
	}
	

	public Long getMaster_id() {
		return master_id;
	}

	public void setMaster_id(Long master_id) {
		this.master_id = master_id;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	// 多对多定义
	@ManyToMany
	@JoinTable(name = "team_members", joinColumns = { @JoinColumn(name = "tid") }, inverseJoinColumns = { @JoinColumn(name = "uid") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序
	@OrderBy("id ASC")
	// 缓存策略
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<User> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(List<User> teamMembers) {
		this.teamMembers = teamMembers;
	}
	
	

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
