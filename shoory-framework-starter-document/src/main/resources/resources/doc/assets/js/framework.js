  $(document).ready(function() {
	  $(".sidebar").load("_sidebar.html");
	  $(".navbar").load("_navbar.html");
	  $(".footer").load("_footer.html");
  });
  
  function activeNav() {
		var pieces = (window.location.href).split("/");
		var filename = pieces[pieces.length - 1];

		$(".nav a[href='"+filename+"']").parent().addClass("active");
		$(".nav a[href!='"+filename+"']").parent().removeClass("active");
  }
  
  function setTitle(title) {
	  $('title').html(title);
  }