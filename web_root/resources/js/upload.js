// J.yanming  yanming-sohu@sohu.com 2013-7-23
// 上传文件脚本
// @see /resources/util/upload.xhtml
// @see jym.file.trans.UploadProcesser
jQuery(function($){
	
if (window.UP_LOAD_JS) return;
window.UP_LOAD_JS = 1;


$('.ui_upload').each(function() {
	
	var ui_upload = this;
	var j_ui_upload = $(ui_upload);
	var iframe = j_ui_upload.find('iframe');
	var upload_now = false;
	var msg = j_ui_upload.find('.progress_msg');
	var jscomplate = this.getAttribute('jscomplate');
	var iTimerID = -1;
	
	
	j_ui_upload.find('form').each(function() {
		var form = this;
		
		j_ui_upload.find('.pm').each(function() {
			form.action = form.action + '&' + this.name + '=' + this.value;
		});
	});
	
	
	var id = msg.attr('mid');
	var url = msg.attr('url');
	
	var update = function() {
		
		function over() {
			clearInterval(iTimerID);
			msg.html('');
			iframe.show();
		}
		
		$.ajax({url: url, data: {'id': id}, dataType: 'text',
			success: function(txt) {

				var o = eval('(' + txt + ')');
				upload_now = !o.over;
				
				if (upload_now) {
					msg.html('第' + o.itemIndex + '个文件已经上传 ' + 
							(o.percentage* 100).toFixed(2) + "%" +
							" (" + (o.total/1024/1024).toFixed(3) + " MB) ");
				} else {
					over();
					
					if (typeof window[jscomplate] == 'function') {
						try {
							window[jscomplate]();
						} catch(E) {} 
					}
				}
			},
			
			error: function() {
				over();
				msg.html("出错.");
			}
		});
	};
	
	
	j_ui_upload.find(':submit').click(function() {
		if (upload_now) return false;
		upload_now = true;
		msg.html("开始上传...");
		iframe.hide();
		
		clearInterval(iTimerID);
		iTimerID = setInterval(update, 300);
	});
});


});