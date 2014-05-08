/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import jym.file.FaceBase;
import jym.file.SecurityManager;
import jym.file.Util;
import jym.file.tree.Directory;
import jym.sim.util.Tools;

import org.apache.commons.fileupload.disk.DiskFileItem;


@ManagedBean(eager=true)
@ApplicationScoped
public class Upload extends FaceBase implements IUpFileProcesser {

	private static final long serialVersionUID = 1L;

	
	public Upload() {
		UploadProcesser.registerProcesser(this);
	}

	public void set(DiskFileItem item) throws Exception {
		Util.log(item);
		try {
			SecurityManager.check();
			
			Directory dir = (Directory) Util.getBean("directory");
			File currDir = dir.getRoot();
			if (currDir == null) return;
			
			File remoteName = new File(item.getName());
			String fileName = remoteName.getName();
		
			File target = new File(currDir.getPath() + "/" + fileName);
			if (!item.getStoreLocation().renameTo(target)) {
				Tools.pl("move file fail, copy that.");
				copy(item, target);
			}
			dir.refresh();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void copy(DiskFileItem item, File target) throws IOException {
		InputStream in = item.getInputStream();
		OutputStream out = new FileOutputStream(target);
		
		try {
			byte[] buff = new byte[256];
			int readlen = in.read(buff);
			while (readlen >= 0) {
				out.write(buff, 0, readlen);
				readlen = in.read(buff);
			}
		} finally {
			in.close();
			out.close();
		}
	}
}
