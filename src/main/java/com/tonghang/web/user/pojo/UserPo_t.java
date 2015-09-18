package com.tonghang.web.user.pojo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="USER")
public class UserPo_t {

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
	private Date birth;
	
	@Column(nullable=true,columnDefinition="varchar(2) default '1'")
	public String status;
	
	@Column(nullable=true,columnDefinition="varchar(2) default '0'")
	private String isonline;
	
	@Column(length=10)
	private String province;
	
	@Column(length=10)
	private String city;
	
	@Column
	private String tags;

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

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
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
	
}
