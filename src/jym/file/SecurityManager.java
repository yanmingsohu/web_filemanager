/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


@ManagedBean
public class SecurityManager extends FaceBase 
		implements PhaseListener, ServletContextListener {

	private static final long serialVersionUID = 1L;
	public static final String LOGIN_PAGE = "/login.xhtml";
	private static ServletContext context;
	private static final Properties prop = new Properties();
	

	public void afterPhase(PhaseEvent event) {
		FacesContext fc = event.getFacesContext();

		if (!LOGIN_PAGE.equalsIgnoreCase(fc.getViewRoot().getViewId())) {
			if (!check(fc)) {
				fc.getViewRoot().setViewId(LOGIN_PAGE);
				//throw new NoLoginException();
			}
		} 
	}
	
	public static void check() {
		if (!check(FacesContext.getCurrentInstance())) {
			throw new NoLoginException();
		}
	}
	
	public static boolean check(FacesContext fc) {
		LoginUser user = (LoginUser) Util.getBean("loginUser");
		return user != null && user.isLogin();
	}
	
	public String login(LoginUser user) {
		String pw = (String) prop.get(user.getName());
		
		if (pw != null) {
			if (pw.equals(user.getPw())) {
				user.login();
				return "root.xhtml";
			} else {
				getSender().addMessage("登录失败");
			}
		}
		return null;
	}

	public void contextInitialized(ServletContextEvent e) {
		context = e.getServletContext();
		Util.readProp(context, prop, "/WEB-INF/userlist.xml");
	}
	
	public static ServletContext getServletContext() {
		return context;
	}
	
	public void beforePhase(PhaseEvent arg0) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	public void contextDestroyed(ServletContextEvent e) {
	}

}
