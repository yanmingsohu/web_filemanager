/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class FileFlag implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Object flag;
	private String name;

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Object getFlag() {
		return flag;
	}

	public void setFlag(Object flag) {
		this.flag = flag;
	}
	
	public void clear() {
		flag = null;
		name = null;
	}
}
