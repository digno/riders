package com.leadtone.riders.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "suggestion")
public class Suggestion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7156523983170412151L;

	private Long sid;

	private User proposer;

	private Integer type;

	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	@NotNull
	@OneToOne
	@JoinColumn(name = "uid")
	public User getProposer() {
		return proposer;
	}

	public void setProposer(User proposer) {
		this.proposer = proposer;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
