/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String pw;
	private boolean login;
	
	
	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
	
	public void login() {
		login = true;
	}
	
	public Object logout() {
		login = false;
		return SecurityManager.LOGIN_PAGE;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPw() {
		return pw;
	}
	
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public String toString() {
		return login ? "已经登录" : "未登录";
	}
}
