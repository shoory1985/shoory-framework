(function($, undefined) {
	$.extend({
		RPC : {
			version : '1.0',
			endPoint : null,
			setup : function(params) {
				this.endPoint = params.endPoint;
				return this;
			},
			// 调用方法
			call : function(options) {
				var loginUser = sesStorage("teapp_admin_loginUser");
				if (loginUser != null) {
					options.data._accessUserId = loginUser.accessToken;
				} else {
					options.data._accessUserId = "";
				}

				var url = this.endPoint + options.method;
				// logger.info("发起请求"+url);
				$.ajax({
					headers : {
					// AccessToken: locStorage("AccessToken")
					// AccessToken: "8c1cbe59-49a2-4f40-b0d0-a77307a2a364"
					},
					url : url,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify(options.data),
					processData : false,
					success : function(result) {
						if (result.code == 'SUCCESS') {
							if (options.success !== undefined) {
								options.success(result);
							}
						} else {
							if (options.error !== undefined) {
								options.error(result.code, result.message);
							} else {
								  Swal.fire({
									    type: 'warning', // 弹框类型
									    title: result.code, //标题
									    text: result.message, //显示内容            
									    confirmButtonColor: '#3085d6',// 确定按钮的 颜色
									    confirmButtonText: '确定'// 确定按钮的 文字
									});
							}
						}
					},
					error : function(e) {
						// 通讯错误
						if (options.error !== undefined) {
							options.error("ERROR_NETWORK", e.message);
						} else {
							  Swal.fire({
								    type: 'warning', // 弹框类型
								    title: '网络错误', //标题
								    text: e.message, //显示内容            
								    confirmButtonColor: '#3085d6',// 确定按钮的 颜色
								    confirmButtonText: '确定'// 确定按钮的 文字
								});
						}
					}
				});
			},

			_defaultError : function(errorCode, errorMessage) {
				window.alert("错误代码：" + result.errorCode + "\n错误消息："
						+ result.errorMessage);
			},
		}
	});
})(jQuery);

function sesStorage(key, value) {
	if (window.sessionStorage) {
		if (arguments.length == 1) {
			try {
				var value = JSON.parse(window.sessionStorage
						.getItem(arguments[0]));
				return value;
			} catch (e) {
				layer.alert(e);
				return null;
			}
		} else {
			if (arguments.length == 2) {
				window.sessionStorage.removeItem(arguments[0]);

				if (arguments[1] !== null) {
					return window.sessionStorage.setItem(arguments[0], JSON
							.stringify(arguments[1]));
				}
			}
		}
	} else {
		layer.alert('当前浏览器不支持sessionStorage');
	}
	return null;
}
