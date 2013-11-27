/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jym.file.FaceBase;
import jym.file.SecurityManager;
import jym.file.Util;


public class Root extends FaceBase {
	
	private static final long serialVersionUID = 1L;
	private List<FileBean> list = new ArrayList<FileBean>();
	
	
	public Root() throws Exception {
		Properties prop = new Properties();
		Util.readProp(SecurityManager.getServletContext(), 
				prop, "/WEB-INF/rootlist.xml");
		
		Iterator<Object> itr = prop.keySet().iterator();
		while (itr.hasNext()) {
			String desc = (String) itr.next();
			String filename = prop.getProperty(desc);
			
			FileBean bean = new FileBean();
			bean.setFile(new File(filename));
			bean.setDesc(desc);
			list.add(bean);
		}
	}
	
	public List<FileBean> list() {
		return list;
	}
}
