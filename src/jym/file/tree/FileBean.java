/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import jym.file.FaceBase;
import jym.file.Util;
import jym.file.FaceBase.MessageSender;
import jym.file.trans.Download;
import jym.file.trans.FileFlag;


public class FileBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static final DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private File file;
	private String desc;
	private String time;
	private String type;
	private String flag;
	private String newName;
	

	public void open() throws FileNotFoundException {
		if (file.isDirectory()) {
			Directory dir = (Directory) Util.getBean("directory");
			dir.open(file);
		} else {
			//InputStream in = new FileInputStream(file);
			UUID id = Download.putFile(file);
			FileFlag flag = (FileFlag) Util.getBean("fileFlag");
			flag.setFlag(id);
		}
	}
	
	public void del() {
		MessageSender msg = FaceBase.getSender();
		
		if (file.isDirectory()) {
			msg.addMessage("不能删除目录");
		}
		else if (file.canWrite()) {
			
			if (file.delete()) {
				refreshCurrentDir();
				msg.addMessage("删除了文件: " + file);
			} else {
				msg.addMessage("删除失败");
			}
		}
		else {
			msg.addMessage("只读文件");
		}
	}
	
	private void refreshCurrentDir() {
		Directory dir = (Directory) Util.getBean("directory");
		dir.refresh();
	}
	
	public void rename() {
		MessageSender msg = FaceBase.getSender();
		
		if (newName.indexOf('/') >= 0 || newName.indexOf('\\') >= 0) {
			msg.addMessage("含有无效字符");
			return;
		}
		
		File newFile = new File(file.getParent() + "/" + newName);
		file.renameTo(newFile);
		refreshCurrentDir();
		
		FaceBase.getSender().addMessage("修改了文件名, " + newName);
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
		this.desc = file.getName();
		this.flag = file.isDirectory() ? "[" : ".";
		this.newName = file.getName();

		synchronized (df) {
			time = df.format(new Date(file.lastModified()));
		}
		
		if (file.isDirectory()) {
			type = "dir";
		} else {
			int i = desc.lastIndexOf('.');
			if (i>0 && i<desc.length()-1) {
				type = desc.substring(i+1);
			}
		}
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getSize() {
		long s = file.length();
		if (s < 1024) return s + " Byte";
		s /= 1024;
		if (s < 1024) return s + " KB";
		s /= 1024;
		if (s < 1024) return s + " MB";
		s /= 1024;
		if (s < 1024) return s + " GB";
		s /= 1024;
		return s + " TB";
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}
