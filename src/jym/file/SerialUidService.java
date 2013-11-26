/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * 产生全局无重复 id
 */
@ManagedBean(eager=false)
@ApplicationScoped
public class SerialUidService {

	private static long id;
	
	
	public synchronized long next() {
		return id++;
	}
}
