package com.soa.facepond.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_deals")
public class UserDeals extends BaseObject {

	private Long id;
	// json
	private String deals;
	
	private Long userId;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "deals")
	@Lob
	public String getDeals() {
		return deals;
	}

	public void setDeals(String deals) {
		this.deals = deals;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deals == null) ? 0 : deals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDeals other = (UserDeals) obj;
		if (deals == null) {
			if (other.deals != null)
				return false;
		} else if (!deals.equals(other.deals))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDeals [id=" + id + ", deals=" + deals + ", userId=" + userId
				+ "]";
	}
	
}
