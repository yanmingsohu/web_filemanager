/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jym.sim.util.Tools;

/**
 * 数据保持组件, 用于在两个页面间保存数据
 * <a href="../../../../WebContent/res/js/module/device_manager/detail.xhtml">范例</a>
 */
@ManagedBean
@SessionScoped
public class DataLasting implements Serializable {

	private static final long serialVersionUID = 2L;
	private Map<Object, Object> map;
	private Object flag;
	private boolean DEBUG = false;
	private Map<Class<?>, Object> type;
	private Map<Class<?>, Long> page;
	
	
	public DataLasting() {
		map = new HashMap<Object, Object>();
		type = new HashMap<Class<?>, Object>();
		page = new HashMap<Class<?>, Long>();
	}
	
	public void save(Object key, Object obj) {
		if (DEBUG) Tools.pl("save call", key);
		if (!map.containsKey(key)) {
			if (DEBUG) Tools.pl("save over", key);
			map.put(key, obj);
		}
	}
	
	public void resave(Object key, Object obj) {
		if (DEBUG) Tools.pl("resave", key);
		map.put(key, obj);
	}
	
	public void resaveWithoutNull(Object key, Object obj) {
		if (obj != null) {
			map.put(key, obj);
		}
	}
	
	public Object load(Object key) {
		if (DEBUG) Tools.pl("load", key);
		return map.get(key);
	}
	
	public void clear(Object key) {
		if (DEBUG) Tools.pl("clear", key);
		map.remove(key);
	}
	
	public void clearAll() {
		map.clear();
		type.clear();
		page.clear();
		if (DEBUG) Tools.pl("clear all.");
	}
	
	public void clearPage() {
		page.clear();
	}
	
	public void debug(boolean debug) {
		DEBUG = debug;
	}
	
	/**
	 * @param 设置一个能标识当前页面的标志, 如果标志改变, 则数据被清空
	 */
	public void clearWhenChange(Object _flag) {
		if (_flag == null) return;
		
		if (!_flag.equals(flag)) {
			clearAll();
		}
		flag = _flag;
	}
}

