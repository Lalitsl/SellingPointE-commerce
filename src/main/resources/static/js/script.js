

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




// code for payment getway

// first request to server to create order
const paymentStart = () => {
	console.log("payment started........");
	let amountText = $("#paymentFeild").text().trim(); // Get the text and remove leading/trailing whitespaces
	let amount = parseInt(amountText.replace("₹", "").replace(",", "")); // Remove currency symbol and commas
	console.log("PRODUCT AMOUNT: " + amount);
	if (isNaN(amount)) {
		Swal.fire({
			icon: "error",
			title: "Invalid Amount !!!",
		});
		return;
	}
	//    we will use ajax to send request to server to create order - using jquery
	$.ajax(
		{
			url: '/cart/createOrder',
			data: JSON.stringify({ amount: amount, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(Response) {
				// invoked when success
				console.log(Response);
				if (Response.status == 'created') {
					// open payment form 
					let options = {
						key: 'rzp_test_SjOHuxyf14nY18',
						amount: Response.amount,
						currency: 'INR',
						name: 'Selling point e-commerce',
						description: "payment for you want to buy product ",
						image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmszu1ixKHES9xBpI52IIWxe7g6pyAYIgK4uD6xB1VTfwcgR4547fnj_lWZX_GdvDFhrY&usqp=CAU',
						order_id: Response.id,
						handler: function(response) {
							console.log(response.razorpay_payment_id)
							console.log(response.razorpay_order_id)
							console.log(response.razorpay_signature)
							console.log("payment successfull ...")
							//  payment success message code start  
							Swal.fire({
								position: "top-end",
								icon: "success",
								title: "Congratulation...... payment success",
								showConfirmButton: false,
								timer: 3000
							});
							//  payment success message code end  
						},
						prefill: {
							"name": "",
							"email": "",
							"contact": ""
						},
						"notes": {
							"address": "SELLING POINT TEAM CREATE PROJECT FOR LEARNING PURPOSE."
						},
						"theme": {
							"color": "#3399cc"
						}
					};

					let rzp = new Razorpay(options);
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						//  payment failed message code start 
						Swal.fire({
							icon: "error",
							title: "Payment Failed !!!",
							text: "Something went wrong!",
						});
					//	  payment failed message code end 
					});
					rzp.open()
				}
			},
			error: function(error) {
				// invoked when error 
				console.log(error);
				Swal.fire("Something went wrong !!! ");
			}
		}
	)

};

/* ============================ */

// First request to server to create order
/*const paymentStart = () => {
	console.log("payment started........");
	let amountText = $("#paymentFeild").text().trim();
	let amount = parseInt(amountText.replace("₹", "").replace(",", ""));
	console.log("PRODUCT AMOUNT: " + amount);

	if (isNaN(amount)) {
		Swal.fire({
			icon: "error",
			title: "Invalid Amount !!!",
		});
		return;
	}

	// Use AJAX to send a request to the server to create an order
	$.ajax({
		url: '/cart/createOrder',
		data: JSON.stringify({ amount: amount, info: 'order_request' }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response) {
			console.log(response);

			if (response.status == 'created') {
				// Open the payment form
				let options = {
					key: 'rzp_test_SjOHuxyf14nY18',
					amount: response.amount,
					currency: 'INR',
					name: 'Selling point e-commerce',
					description: "Payment for the product",
					image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmszu1ixKHES9xBpI52IIWxe7g6pyAYIgK4uD6xB1VTfwcgR4547fnj_lWZX_GdvDFhrY&usqp=CAU',
					order_id: response.id,
					handler: function(paymentResponse) {
						console.log(paymentResponse.razorpay_payment_id);
						console.log(paymentResponse.razorpay_order_id);
						console.log(paymentResponse.razorpay_signature);
						console.log("Payment successful ...");

						// Generate PDF on the server
						$.ajax({
							url: '/cart/generateBillPdf',
							data: { orderId: response.id },
							type: 'GET',
							success: function(pdfPath) {
								// Send email with the generated PDF attachment
								$.ajax({
									url: '/cart/sendEmail',
									data: { toEmail: 'lalitnarware2000@gmail.com', pdfPath: pdfPath },
									type: 'GET',
									success: function() {
										// Display success message to the user
										Swal.fire({
											position: "top-end",
											icon: "success",
											title: "Congratulation... payment success",
											timer: 3000
										});
									},
									error: function(error) {
										console.log(error);
										Swal.fire("Failed to send email");
									}
								});
							},
							error: function(error) {
								console.log(error);
								Swal.fire("Failed to generate PDF");
							}
						});
					},
					prefill: {
						"name": "",
						"email": "",
						"contact": ""
					},
					"notes": {
						"address": "SELLING POINT TEAM CREATE PROJECT FOR LEARNING PURPOSE."
					},
					"theme": {
						"color": "#3399cc"
					}
				};

				let rzp = new Razorpay(options);

				rzp.on('payment.failed', function(paymentResponse) {
					console.log(paymentResponse.error.code);
					console.log(paymentResponse.error.description);
					console.log(paymentResponse.error.source);
					console.log(paymentResponse.error.step);
					console.log(paymentResponse.error.reason);
					console.log(paymentResponse.error.metadata.order_id);

					// Payment failed message
					Swal.fire({
						icon: "error",
						title: "Payment Failed !!!",
						text: "Something went wrong!",
					});
				});

				rzp.open();
			}
		},
		error: function(error) {
			console.log(error);
			Swal.fire("Something went wrong !!! ");
		}
	});
};
*/



















