/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.file.Range;
import jym.file.SecurityManager;
import jym.sim.util.Tools;


public class Download extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Map<UUID, File> filePool = new HashMap<UUID, File>();
	private static final SimpleDateFormat format = 
			new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		SecurityManager.check();
		
		resp.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		
		if (Tools.isNull(id)) {
			resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		
		UUID uid = UUID.fromString(id);
		File file = filePool.get(uid);
		
		if (file == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Tools.pl("client request file:", file);
		
		resp.reset();
		resp.setContentType("application/octet-stream; charset=utf-8");
		resp.setHeader("Last-Modified", format.format( new Date(file.lastModified()) ));
		resp.setHeader("Content-Disposition", 
				"filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
		resp.setHeader("Accept-Ranges", "bytes");
		
		String range_str = req.getHeader("Range");
		try {
			if (range_str == null) {
				directWrite(file, resp, uid);
			} else {
				rangeWrite(file, req, resp, uid, range_str);
			}		
		} catch(Exception e) {
			Tools.pl("write to client err:", e);
			resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}
	
	private void rangeWrite(File file, HttpServletRequest req, 
			HttpServletResponse resp, UUID uid, String range_str) throws Exception {
		Tools.pl("continue download", file, range_str);

		Range range = new Range();
		
		if (!range.parse(range_str, file)) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		String modify = req.getHeader("If-Modified-Since");
		if (modify != null) {
			long last = format.parse(modify).getTime();
			if (file.lastModified() != last) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}

		resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
		resp.setContentLength((int) range.length());
		resp.setHeader("Content-Range", "bytes " + range.getFirstPos() + 
				"-" + range.getLastPos() + "/" + file.length());
		
		InputStream in = range.openInputStream();
		OutputStream out = resp.getOutputStream();
		
		if (writeOut(in, out) == range.length()) {
			filePool.remove(uid);
		}
	}
	
	private void directWrite(File file, HttpServletResponse resp, UUID uid) 
			throws Exception {

		resp.setContentLength((int) file.length());
		InputStream in = new FileInputStream(file);
		OutputStream out = resp.getOutputStream();
		
		if (writeOut(in, out) == file.length()) {
			filePool.remove(uid);
		}
	}
	
	public int writeOut(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buff = new byte[256];
			int len = in.read(buff);
			int total_write = 0;
			
			while (len>0) {
				out.write(buff, 0, len);
				total_write += len;
				len = in.read(buff);
			}
			return total_write;
		} finally {
			in.close();
		}
	}

	public static UUID putFile(File file) {
		if (filePool.size() > 1000) 
			filePool.clear();
		
		UUID u = UUID.randomUUID();
		filePool.put(u, file);
		return u;
	}
	
}
