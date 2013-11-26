/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CanDelete {

	private boolean can;
	
	public boolean isCan() {
		return can;
	}
	
	public void sw() {
		can = !can;
	}
}
