

/*   ===== sidebar js code start here =========*/
/*const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		// true (band karna h )
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
		$(".sidebar").css("transition", "2s");
		$(".sidebar").css("transition-timing-function", "linear");

	} else {
		// false (show karna h )
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
		
	}
}
*/
$(document).ready(function() {
    const toggleSidebar = () => {
        const $sidebar = $(".sidebar");
        const $content = $(".content");

        if ($sidebar.is(":visible")) {
            // Hide sidebar with fadeOut animation
            $sidebar.fadeOut("slow", function() {
                $content.css("margin-left", "0%");
            });
        } else {
            // Show sidebar with fadeIn animation
            $sidebar.fadeIn("slow");
            $content.css("margin-left", "20%");
        }
    }

    // Hide the sidebar by default
    $(".sidebar").hide();

    // Toggle sidebar when clicking the button with class 'showmenu'
    $(".showmenu").click(function() {
        toggleSidebar();
    });
});



/*   ===== sidebar js code end here =========*/
