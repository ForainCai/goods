package com.goods.admin.admin.entity;

public class Admin {
	private String adminId;//����
	private String adminname;//����Ա�ĵ�¼��
	private String adminpwd;//����Ա�ĵ�¼����
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getAdminpwd() {
		return adminpwd;
	}
	public void setAdminpwd(String adminpwd) {
		this.adminpwd = adminpwd;
	}
	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminname=" + adminname
				+ ", adminpwd=" + adminpwd + "]";
	}
	
	
}