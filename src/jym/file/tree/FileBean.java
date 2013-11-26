/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import jym.file.FaceBase;
import jym.file.Util;
import jym.file.FaceBase.MessageSender;
import jym.file.trans.Download;
import jym.file.trans.FileFlag;
import jym.sim.util.Tools;


public class FileBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private File file;
	private String desc;
	private String time;
	private String type;
	private String flag;
	

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
				msg.addMessage("删除了文件: " + file);
			} else {
				msg.addMessage("删除失败");
			}
		}
		else {
			msg.addMessage("只读文件");
		}
	}
	
	public void rename() {
		Tools.pl("未完成...");
	}
	
	public File getFile() {
		return file;
	}
	
	@SuppressWarnings("deprecation")
	public void setFile(File file) {
		this.file = file;
		this.desc = file.getName();
		time = new Date(file.lastModified()).toLocaleString();
		flag = file.isDirectory() ? "[" : ".";
		
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

}