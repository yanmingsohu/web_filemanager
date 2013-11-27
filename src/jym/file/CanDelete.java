/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CanDelete implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean can;
	
	public boolean isCan() {
		return can;
	}
	
	public void sw() {
		can = !can;
	}
}
