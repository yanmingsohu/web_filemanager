/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.tree;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jym.file.FaceBase;

@ManagedBean
@SessionScoped
public class Directory extends FaceBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private File root;
	private List<FileBean> list;
	private Root root_list;
	private String newName;
	
	
	public Directory() throws Exception {
		root_list = new Root();
	}
	
	public void openRoot() throws Exception {
		open(root_list.list(), "根目录");
	}
	
	public void md() {
		if (root != null) {
			File newf = new File(root + "/" + newName);
			newf.mkdir();
			refresh();
		}
	}
	
	public void up() throws Exception {
		if (root == null) return;
		
		File target = root.getParentFile();
		Iterator<FileBean> itr = root_list.list().iterator();
		
		while (itr.hasNext()) {
			String path = itr.next().getFile().getPath();
			if (target.getPath().indexOf(path) >= 0) {
				open(target);
				return;
			}
		}
		
		openRoot();
	}
	
	public void open(File file) {
		root = file;
		name = file.getPath();
		File[] files = root.listFiles();
		list = new ArrayList<FileBean>();
		
		for (int i=0; i<files.length; ++i) {
			FileBean bean = new FileBean();
			bean.setFile(files[i]);
			list.add(bean);
		}
	}
	
	public void open(List<FileBean> list, String name) {
		this.list = list;
		this.name = name;
		root = null;
	}
	
	public List<FileBean> getList() throws Exception {
		if (list == null) {
			openRoot();
		}
		return list;
	}
	
	public boolean isNotRoot() {
		return root != null;
	}
	
	public void refresh() {
		open(root);
	}
	
	public String getName() {
		return name;
	}
	
	public File getRoot() {
		return root;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}
