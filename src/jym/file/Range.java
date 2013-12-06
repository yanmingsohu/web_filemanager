package jym.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// CatfoOD 2008-3-30

/**
 * GET 请求中"Range"域的包装类
 * <pre>
 * HTTP/1.1 
 * 
 * [14.35.1]
 * ranges-specifier = byte-ranges-specifier
 * byte-ranges-specifier = bytes-unit "=" byte-range-set
 * byte-range-set  = 1#( byte-range-spec | suffix-byte-range-spec )
 * byte-range-spec = first-byte-pos "-" [last-byte-pos]
 * first-byte-pos  = 1*DIGIT
 * last-byte-pos   = 1*DIGIT
 * 
 * [3.12]
 * range-unit = bytes-unit | other-range-unit
 * bytes-unit = "bytes"
 * other-range-unit = token
 * </pre>
 */
public class Range {
	
	private static final String BYTESUNIT = "bytes";
	private static final String Dividing = "-";
	private long firstPos = -1;
	private long lastPos = -1;
	private File file;
	
	/**
	 * Range负责解析字符串中关于头域的解释
	 * @param r - 字符串中是 "byte-ranges-specifier";
	 * @throws Exception - 传入的字符串不是合法的"byte-ranges-specifier";
	 */
	public Range() { 
	}
	
	public boolean parse(String r, File file) {
		
		if (r.toLowerCase().startsWith(BYTESUNIT)) {
			int begin = r.indexOf('=');
			
			if (begin >= BYTESUNIT.length()) {
				String byte_range_spec = r.substring(begin+1).trim();
				int div = byte_range_spec.indexOf(Dividing);
				
				try {
					firstPos = Long.parseLong( byte_range_spec.substring(0, div) );
					String last_byte_pos = byte_range_spec.substring(div+1);
					
					if (last_byte_pos.length()>0) {
						lastPos = Long.parseLong( last_byte_pos );
					} else {
						lastPos = file.length() - 1; 
					}

					if (lastPos >= 0 &&
						lastPos < file.length() &&
						lastPos > firstPos) {
					this.file = file;
					return true;
					}
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		firstPos = -1;
		lastPos = -1;
		return false;
	}
	
	public long length() {
		if (firstPos < 0 || lastPos < 0) return -1;
		return lastPos - firstPos + 1;
	}
	
	/** 返回最开始的字节 */
	public long getFirstPos() {
		return firstPos;
	}
	
	/** 返回结束处的字节,如果返回-1说明最后的位置在文件的末尾,(文件长度-1) */
	public long getLastPos() {
		return lastPos;
	}
	
	public InputStream openInputStream() throws IOException {
		return new FileRangeInputStream(file, this);
	}
	
	private class FileRangeInputStream extends FileInputStream {

		private Range range;
		private long current;

		/** 必须保证Range的正确性 */
		public FileRangeInputStream(File f, Range r) throws IOException {
			super(f);
			range = r;
			this.skip(r.getFirstPos());
			current = r.getFirstPos();
		}

		public int read() throws IOException {
			if (current++ <= range.getLastPos()) {
				return super.read();
			}
			else {
				return -1;
			}
		}
	}
}
