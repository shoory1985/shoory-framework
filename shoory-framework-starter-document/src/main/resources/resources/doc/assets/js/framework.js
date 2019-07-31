$(document).ready(function() {
	$(".sidebar").load("_sidebar.html");
	$(".navbar").load("_navbar.html");
	$(".footer").load("_footer.html");
});

function activeNav(uri) {
	if (uri == null || uri == undefined) {
		var pieces = decodeURI(window.location.href).split("/");
		uri = pieces[pieces.length - 1];
	}

	$(".nav a[href='" + uri + "']").parent().addClass("active");
	$(".nav a[href!='" + uri + "']").parent().removeClass("active");
}

function setTitle(title) {
	$('title').html(title);
}

function baseType(model) {
	var pureModel = model.replace('[]', '');
	pureModel = pureModel.replace('<Map>', '');
	
	switch (pureModel) {
	case 'Double':
	case 'Integer':
	case 'Long':
	case 'String':
	case 'double':
	case 'int':
	case 'long':
	case 'float':
	case 'Float':
		return true;
	default:
		return false;
	}
}
function modelLink(model) {
	var pureModel = model.replace('[]', '');
	pureModel = pureModel.replace('<Map>', '');
	
	return baseType(model) ? model : '<a href="model.html#'+pureModel+'">'+model+'</a>';
}