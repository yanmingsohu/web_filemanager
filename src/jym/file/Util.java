/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import jym.sim.util.Tools;


public class Util {

	public static final Object getBean(FacesContext facesContext, String managedBeanName) {
		
		if (facesContext == null) {
			throw new NullPointerException("Object " + managedBeanName
					+ " cannot be created since the faces context is NULL");
		}
		
		Object resolvedObject = facesContext.getELContext().getELResolver()
					.getValue(facesContext.getELContext(), null, managedBeanName);
		
		return resolvedObject;
	}
	
	public static final Object getBean(String managedBeanName) {
		return getBean(FacesContext.getCurrentInstance(), managedBeanName);
	}
	
	public static final void donotCache(HttpServletResponse resp) {
		resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expire", 0);
	}
	
	public static final void log(Object ...o) {
		System.out.print("[" + new Date() + "] ");
		Tools.pl(o);
	}
	
	/**
	 * 读取配置文件
	 * @param context - 使用应用上下文
	 * @param prop
	 * @param file - 必须是 xml 配置文件, 编码 utf8
	 */
	public static final void readProp(ServletContext context, Properties prop, String file) {
		try {
			URL url = context.getResource(file);
			prop.clear();
			InputStream in = url.openStream();
			prop.loadFromXML(in);
			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
