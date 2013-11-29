/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.file.Util;
import jym.sim.util.Tools;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;


/**
 * url: /up_file <br/>
 * url 请求参数:<br/>
 * 		service: 服务类型(类名) <br/>
 * <br/>
 * [service = progress] 取得当前进度 url参数:<br/>
 * 		id: 申请到的id (可用  #{serialUidService.next()} 取得新的 id)<br/>
 * <br/>
 * [service = upfile] 上传文件, url参数:<br/>
 * 		type: 预定义的文件处理器类型名称<br/>
 * 		id: 申请到的id<br/>
 */
public class UploadProcesser extends HttpServlet {

	private static final long serialVersionUID = 2L;
	
	private static final String SER_UP_FILE  = "upfile";
	private static final String SER_PROGRESS = "progress";
	private static final String SER_NULL = "null";
	
	private static final String TMP_DIR = "/WEB-INF/tmp_up_file";

	private static final Map<String, IUpFileProcesser> 
			fileprocess = new HashMap<String, IUpFileProcesser>();
	private DiskFileItemFactory factory;
	private Map<Long, String> progressMap;
	
	
	/**
	 * 注册文件处理器, 使用了 fp 对象的简化类名, 作为 type,
	 * 注册后, 可以在前端使用 <x:upload type="type"/> 显示上传组件
	 */
	public static void registerProcesser(IUpFileProcesser fp) {
		fileprocess.put(fp.getClass().getSimpleName(), fp);
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		FileCleaningTracker fileCleaningTracker = 
			 FileCleanerCleanup.getFileCleaningTracker(getServletContext());
		 
		// Create a factory for disk-based file items
		factory = new DiskFileItemFactory();
		factory.setFileCleaningTracker(fileCleaningTracker);

		// Set factory constraints
		factory.setSizeThreshold(0);
		String path = super.getServletContext().getRealPath(TMP_DIR);
		factory.setRepository(new File(path));
		
		progressMap = new HashMap<Long, String>();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		final String service = req.getParameter("service");
		
		if (SER_PROGRESS.equalsIgnoreCase(service)) {
			sendProgress(req, resp, out);
		}
		else if (SER_UP_FILE.equalsIgnoreCase(service)) {
			reviceFile(req, out);
		}
		else if (SER_NULL.equalsIgnoreCase(service)) {
			out.print(' ');
		}
		else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private void sendProgress(HttpServletRequest req, 
			HttpServletResponse resp, PrintWriter out) {
		
		final long id = Long.parseLong(req.getParameter("id"));
		String json = progressMap.get(id);
		resp.setContentType("application/json; charset=utf-8");
		Util.donotCache(resp);

		if (json != null) {
			out.print(json);
		} else {
			out.print("{over: true}");
		}
	}

	private void reviceFile(HttpServletRequest req, PrintWriter out) 
			throws ServletException {

		final long id = Long.parseLong(req.getParameter("id"));
	
		try {
			if (progressMap.containsKey(id)) {
				progressMap.remove(id);
				print(out, "上传被终止");
				return;
			}
			
			final String type = req.getParameter("type");
			IUpFileProcesser fp = fileprocess.get(type);
			if (fp == null) {
				if (Tools.isNull(type)) {
					print(out, "type 参数不能为 null");
					return;
				} else {
					print(out, type + " 处理器没有注册");
					return;
				}
			}
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			PL progressListener = new PL();
			progressListener.id = id;
			upload.setProgressListener(progressListener);
		
			Iterator<FileItem> itr = upload.parseRequest(req).iterator();
			int upcount = 0;
			
			while (itr.hasNext()) {
				FileItem item = itr.next();
				if (item instanceof DiskFileItem) {
					if (item.isInMemory() && item.getSize() >0) {
						throw new RuntimeException("in memory.");
					}
				
					if (!Tools.isNull(item.getName())) {
						fp.set((DiskFileItem) item);
						++upcount;
					}
				}
			}
			
			if (upcount > 0) {
				print(out, "文件上传完成");
			} else {
				print(out, "请选择要上传的文件");
			}
		} catch (Exception e) {
			print(out, "错误: " + e);
		} finally {
			progressMap.remove(id);
		}
	}

	private void print(PrintWriter out, Object msg) {
		out.print("<html>");
		out.print("<head>");
		out.print("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>");
		out.print("</head>");
		out.print("<body style='background-color:transparent'>");
		out.print("<span style='color:#fff; font-size:12px'>");
		out.print(msg);
		out.print("</span></body></html>");
	}
	
	
	private class PL implements ProgressListener {

		private long megaBytes = -1;
		private long id;

		public void update(long pBytesRead, long pContentLength, int pItems) {
			if (pBytesRead < megaBytes)
				return;
			
			megaBytes = pBytesRead + 56 * 1024;
			
			progressMap.put(id, 
				"{ read: " + pBytesRead + 
				" ,total: " + pContentLength +
				" ,percentage: " + (float)((float) pBytesRead/pContentLength ) +
				" ,itemIndex: " + pItems +
				"} ");
		}
	}

}
