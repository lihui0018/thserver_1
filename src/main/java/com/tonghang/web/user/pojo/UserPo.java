package com.tonghang.web.user.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="USER")
public class UserPo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="idGenerator",strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	@Column(length=32)
	private String id;

	@Column(length=255,nullable=false)
	private String username;
	
	@Column(length=255,nullable=false)
	private String password;
	
	@Column(length=255,nullable=false,unique=true)
	private String email;
	
	@Column(length=16)
	private String phone;
	
	@Column(length=2)
	private String sex;
	
	@Column
	private String birth;
	
	@Column(columnDefinition="varchar(2) default '1'",insertable=false)
	public String status;
	
	@Column(columnDefinition="varchar(2) default '0'",insertable=false)
	private String isonline;
	
	@Column(length=10)
	private String province;
	
	@Column(length=10)
	private String city;
	
	@Column
	private String tags;
	
	@ManyToMany()
	@JoinTable(name="friends", joinColumns=@JoinColumn(name="id"), inverseJoinColumns=@JoinColumn(name="friend_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<UserPo> friends;

	@ManyToMany()
	@JoinTable(name="blacklist", joinColumns=@JoinColumn(name="id"), inverseJoinColumns=@JoinColumn(name="blocker_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<UserPo> blacklist;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at",updatable=false)
	private Date created_at = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login_at")
	private Date last_login_at;
	
	private double distance;
	
	private double weight;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsonline() {
		return isonline;
	}

	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Set<UserPo> getFriends() {
		return friends;
	}

	public void setFriends(Set<UserPo> friends) {
		this.friends = friends;
	}

	public Set<UserPo> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(Set<UserPo> blacklist) {
		this.blacklist = blacklist;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getLast_login_at() {
		return last_login_at;
	}

	public void setLast_login_at(Date last_login_at) {
		this.last_login_at = last_login_at;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
