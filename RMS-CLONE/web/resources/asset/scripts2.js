
jQuery(document).ready(function($) {

		//footer quick btn to slide up the navigation
		$("#btn_quicklink").toggle(function(){
			$("#btn_quicklink").removeClass('btn_quicklinkPlus').addClass('btn_quicklinkMinus');
				$('.quickSlider').fadeIn("slow");
			},
		function(){
			$("#btn_quicklink").removeClass('btn_quicklinkMinus').addClass('btn_quicklinkPlus');
			$('.quickSlider').fadeOut("slow");
			return false;
		});
		
		//Top Client select menu btn to slide down the navigation
		$("#btn_selectmenu").toggle(function(){
			$("#btn_selectmenu").removeClass().addClass('btn_scPlus');
				$('.scSlider').fadeIn("slow");
			},
		function(){
			$("#btn_selectmenu").removeClass().addClass('btn_sckMinus');
			$('.scSlider').fadeOut("slow");
			return false;
		});
		
		$(".grid li, .grid2 li").not(".noclickable li").click(function() {
		  window.location = $(this).find("h3:first a:first").attr("href");
		  return false;
		});

		$(".grid li").hover(function() {
		 	$("img",this).andSelf().attr("title", $("h3", this).text() );
		});
		
		   //= $(this).find("h3:first").text();
		
		// Call us expand collapse
		$(".callus-head a").toggle(function(){
			$(this).removeClass().addClass('close');
			$(".callus-head .callus-content").slideDown();
		},
		function(){
			$(this).removeClass().addClass('open');
			$(".callus-head .callus-content").slideUp();
		});
		

	if( $(".members.bod").length > 0 ) { 
		$("a#inline").fancybox({
			'autoDimensions'		: false,
			//'cyclic'				: true,
			'padding'				: 20,
			'centerOnScroll'		: true,
			'width'         		: 450,
			'height'        		: 500
			});
		
		$(".members li").click(function() {
		  $(this).find("a#inline").click();
		});	
	} 	
	
	
	if( $(".gallery").length > 0 ) { 
		$(".gallery a").fancybox({
			'autoDimensions'		: false,
			//'cyclic'				: true,
			'padding'				: 20,
			'centerOnScroll'		: true,
			'width'         		: 450,
			'height'        		: 500
			});	
	} 

	$(".ytvideo").click(function() {
		$.fancybox({
				'padding'		: 0,
				'autoScale'		: false,
				'transitionIn'	: 'none',
				'transitionOut'	: 'none',
				'title'			: this.title,
				'width'		: 680,
				'height'		: 495,
				'href'			: this.href.replace(new RegExp("watch\\?v=", "i"), 'v/'),
				'type'			: 'swf',
				'swf'			: {
					 'wmode'		: 'transparent',
					'allowfullscreen'	: 'true'
				}
			});
	
		return false;
	});

		
		//$("#main #content .grid li:last-child, nav.secondary li:last-child, .SimpleSideNav ul li:last-child, .left-nav .pagenav li:last-child").addClass("last-child");
		$("li:last-child").addClass("last-child");

	

		// home page spinner rotator
		if (!($(".price-rotator li").hasClass('active'))) { $(".price-rotator li:first-child").addClass('active').fadeIn(); }
		
		$(".prev").click( function(e) {			
			var $active = $(".active");
			var $next = $active.prev().length ? $active.prev() : $(".price-rotator li:last");
			
			$active.removeClass('active').fadeOut();
			$next.addClass('active').fadeIn();
		});
		
		$(".next").click( function(e) {
			var $active = $(".active");
			var $next = $active.next().length ? $active.next() : $(".price-rotator li:first");
		
			$active.removeClass('active').fadeOut();
			$next.addClass('active').fadeIn();
		});



			$("a span.icon-policy").parent().attr("target","_blank");

			$("a span.search_link").parent().addClass("search_lnk");
			
            $(".search_lnk").click(function(e) {          
				e.preventDefault();
				
                $("fieldset#search_menu").toggle();
				$(".search_lnk").toggleClass("menu-open");
            });
			
			$("fieldset#search_menu").mouseup(function() {
				return false
			});
			$(document).mouseup(function(e) {
				if($(e.target).parent(".search_lnk").length==0) {
					$(".search_lnk").removeClass("menu-open");
					$("fieldset#search_menu").hide();
				}
			});			


	if( $(".slidetabs").length > 0 ) { 

		$(".slidetabs").tabs(".images .pane", {
		
			// enable "cross-fading" effect
			effect: 'fade',
			fadeOutSpeed: "slow",
		
			// start from the beginning after the last tab
			rotate: true
		
		// use the slideshow plugin. It accepts its own configuration
		}).slideshow({autoplay: false, interval:5000, clickable:false, autopause: false});
		
		function init_1() {
			$(".init_slideshow").click();
		}
		setTimeout (init_1, 2000);
	}
	
	function init_2() {
		jQuery(".price-rotator-nav .next").click();
		setTimeout (init_2, 5000);
	}
	setTimeout (init_2, 5000);
	
	if( $("#dl_accordion").length > 0 ) { 

		$( "#dl_accordion" ).accordion({
			autoHeight: false,
			//navigation: true,
			//collapsible: true,
			active: 0
		});
	
	}

//	$('.member-content').jScrollPane();

});
