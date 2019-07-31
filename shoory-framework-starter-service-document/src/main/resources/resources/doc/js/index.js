var END_POINT = '.';

$(document).ready(function() {
	window.onhashchange = function() {
		fromHash();
	};
	
	//if (location.hash.length > 1) {
		fromHash();
	//} 
});

function fromHash() {
	var method = location.hash.substring(1);
	fetchService(method);

	if (method != '') {
		fetchMethod(method);
	}
}

function fetchMethod(method) {
	$.get(END_POINT + method, function(result) {
		renderMethodInfo(result);
		renderRequestParams(result);
		renderResponseParams(result);
		renderReturns(result);
	});
}
function fetchService(method) {
	$.get(END_POINT, function(result) {
		//侧边栏
		$(".sidemenu .service").html(result.service);
		
		$(".sidemenu .modules").html("");
		
		var html = '';
		for (var i in result.methods) {
			var item = result.methods[i];
			var units = item.method.split("/");
			var module = units[1];
			if ($(".sidemenu .modules .module_"+module).length == 0) {
				$(".sidemenu .modules").append('<li style="overflow: hidden" class="menu-item menu-item-level-2 module_'+module+'"><span>'+module+'模块<img style="transform: rotate(0deg)" class="menu-toggle" src="img/arrow_down.png" /></span><ul class="methods"></ul></li></ul></li>');
			}
			$(".sidemenu .modules .module_"+module).append('<li class="menu-item menu-item-level-3"><a href="#'+item.method+'" target="_self">'+item.name+' '+item.method+'</a></li>');
			//html += '<li class="menu-item menu-item-level-3"><a href="#'+item.method+'" target="_self">'+item.name+' '+item.method+'</a></li>';
		}
		//$(".sidemenu .methods").html(html);
		//
		$(".sidemenu .methods li").removeClass('menu-item-current');
		$('[href="#'+method+'"]').parent().addClass('menu-item-current');
		
		//右侧
		
	});
}

function renderMethodInfo(result) {
	$(".doc-content .name").html(result.name);
	$(".doc-content .method").html(result.method);
	$(".doc-content .description").html("NOTE:"+result.description);
}
function renderRequestParams(result) {
		var html = renderParams(result.requestFields, "", "");
		$(".requestParams").html(html);
}
function renderResponseParams(result) {
	var html = renderParams(result.responseFields, "", "");
	$(".responseParams").html(html);
}

function renderParams(list, prefix, html) {
	for (var i in list) {
		var item = list[i];
		var classNameArry = (item.className).split(".");
		var className = classNameArry[classNameArry.length-1];
		
		html += '<tr style="'+(prefix == '' ? '' : 'font-style:italic;')+(item.required ? 'text-decoration:underline;' : '')+'">';
		html += '<td>'+prefix+item.field+'</td>';
		html += '<td>'+item.name+'</td>';
		html += '<td>'+className+'</td>';
		html += '<td>'+(item.required ? "Y" : "")+'</td>';
		html += '<td>'+item.description+'</td>';
		html += '<td>'+item.examples+'</td>';
		html += '</tr>';
		
		if (item.params != undefined && item.params != null) {
			html = renderParams(item.params, prefix+"----", html);
		}
	}
	return html;
}

function renderReturns(result) {
	var html = '';
	for (var i in result.returns) {
		var item = result.returns[i];
		
		var color;
		if (item.code.indexOf("SUCCESS") == 0) {
			color  = '#080';
		} else if (item.code.indexOf("WARNING") == 0) {
			color  = '#880';
		}else {
			color  = '#b00';
		}
		
		html += '<tr >';
		html += '<td style="color:'+color+'">'+item.code+'</td>';
		html += '<td>'+item.message+'</td>';
		html += '<td>'+item.description+'</td>';
		html += '</tr>';
	}
	
	$(".returns").html(html);
}