jQuery(document).ready(function($){
	var $timeline_block = $('.cd-timeline-block');
	

	//hide timeline blocks which are outside the viewport
	$timeline_block.each(function(){
		if($(this).offset().top > $(window).scrollTop()+$(window).height()*0.75) {
			$(this).find('.cd-timeline-img, .cd-timeline-content').addClass('is-hidden');
		}
	});

	//on scolling, show/animate timeline blocks when enter the viewport
	$(window).on('scroll', function(){
		$timeline_block.each(function(){
			if( $(this).offset().top <= $(window).scrollTop()+$(window).height()*0.75 && $(this).find('.cd-timeline-img').hasClass('is-hidden') ) {
				$(this).find('.cd-timeline-img, .cd-timeline-content').removeClass('is-hidden').addClass('bounce-in');
			}
		});
	});
	
	$('.mobile-nav-toggle').on('click', function(){
		$('.navbar .nav').toggleClass('show');
	});

	
	// messaging  functionality
	if($('body').attr('id') == 'messaging-view'){
		
		var winWidth = $(window).width();
			
		// if mobile view, fix the body for the initial inbox view
		if(winWidth < 769){
			$('body').addClass('fixed');
			
			// 'Back to Inbox' click - show Inbox View
			$('.sidenav-toggle').on('click', function(e){
				e.preventDefault();
				if($(e.currentTarget).hasClass('new-message-page')){
					window.location = 'index.html';
				}
				showInboxView();
			});
			
			// When you click on a message in the Inbox - show Message View
			$('.messaging-list').on('click', function(e){
				e.preventDefault();
				showMessageView();
			});
			
			function showInboxView(){
				$('.sidebar-nav.messaging-nav').addClass('show'); // hide the sidenav
				$('body').addClass('fixed'); // unfix the body	
			};
			
			function showMessageView(){
				$('.sidebar-nav.messaging-nav').removeClass('show'); // hide the sidenav
				$('body').removeClass('fixed'); // unfix the body
			};
			
		}else{
			$('body').removeClass('fixed');
		}
	
	}
});