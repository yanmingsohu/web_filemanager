/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import org.apache.commons.fileupload.disk.DiskFileItem;

/**
 * 上传文件处理器, 需要在 UpFileServlet 中注册
 * @see UploadProcesser.gis.util.UpFileServlet
 */
public interface IUpFileProcesser {

	/**
	 * 一旦接收到文件, 则该方法被回调 <br/>
	 * 在该方法中处理上传的文件, 如果函数返回, 临时文件会被删除
	 * 
	 * @param item - 保存了临时文件的相关信息
	 * @throws Exception
	 */
	void set(DiskFileItem item) throws Exception;
}
