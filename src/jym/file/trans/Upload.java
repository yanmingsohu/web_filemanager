/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.File;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import jym.file.FaceBase;
import jym.file.SecurityManager;
import jym.file.Util;
import jym.file.tree.Directory;

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
			
			File target = new File(currDir.getPath() + "/" + item.getName());
			item.getStoreLocation().renameTo(target);
			dir.refresh();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
