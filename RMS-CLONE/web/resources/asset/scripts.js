function setCookie(name,value,days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    document.cookie = name+"="+value+expires+"; path=/";
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function deleteCookie(name) {
    setCookie(name,"",-1);
}
jQuery(function($){
	
	
function checkDatee(dd,mm,yyyy,error_div_class,minYear,maxYear) 
{ 

	var errorMsg = ""; 
    var numbers = /^[0-9]*$/;
	
		if($("#"+dd).val()!= '' && $("#"+mm).val()!="" && $("#"+yyyy).val()!="") 
		{ 
			  if( numbers.test($("#"+dd).val()) && numbers.test($("#"+mm).val()) && numbers.test($("#"+yyyy).val()) )
			  {
			
					if($("#"+dd).val() < 1 || $("#"+dd).val() > 31) 
					{ 
					errorMsg = "Invalid value for day."; 
					$("#"+dd).addClass('error');
					}
					else if($("#"+mm).val() < 1 || $("#"+mm).val() > 12) 
					{ 
					errorMsg = "Invalid value for month."; 
					$("#"+mm).addClass('error');
					} 
					else if($("#"+yyyy).val() < minYear || $("#"+yyyy).val() > maxYear) 
					{
					errorMsg = "Invalid value for year - must be between " + minYear + " and " + maxYear + ".";
					$("#"+yyyy).addClass('error');
					} 
					else if($("#"+mm).val()==2 && $("#"+dd).val() > 28 &&  $("#"+yyyy).val()%4!=0)
					{
					errorMsg = "Invalid value for day."; 
					$("#"+dd).addClass('error');
					}
					else if($("#"+mm).val()==2 && $("#"+dd).val() > 29)
					{
					errorMsg = "Invalid value for day."; 
					$("#"+dd).addClass('error');
					}
			  }
			  else
			  {
				  errorMsg = "Invalid Date Formate."; 
				  /*$("#"+dd).addClass('error');
				  $("#"+mm).addClass('error');
				  $("#"+yyyy).addClass('error');*/
			  }
		} 
		else
		{
			errorMsg = "Required."; 
			/*$("#"+dd).addClass('error');
			$("#"+mm).addClass('error');
			$("#"+yyyy).addClass('error');*/
		}

		
		if(errorMsg != "") 
		{ 
		$("<div class='"+error_div_class+"' title='"+errorMsg+"'></div>").insertAfter("#yyyy");
		return false; 
		} 
	return true; 
}	
	/* --- Js for 4th calculator------ */
	
    /*$('#back2, #back3, #back4').click(function(){
        //slide steps
		window.history.back();
        //location.reload();
	});*/
	
	$('#back2').click(function(){
        //slide steps
		$('#back').val('1st');
		$("#form_Plan1").submit();
        //location.reload();
	});
	$('#back3').click(function(){
        //slide steps
		$('#back').val('2nd');
		$("#form_Plan2").submit();
        //location.reload();
	});
	$('#back4').click(function(){
        //slide steps
		$('#back').val('3rd');
		$("#form_Plan3").submit();
        //location.reload();
	});

	$('#sub1, #sub2, #sub3, #sub4').click(function(){
        //slide steps
		$('#customer_type').val($(this).attr("data"));
		setCookie('customer_type', $(this).attr("data"), 1);
	
		$('#form_container1').submit();
	});

	$('#submit_second').click(function(){
		$("#form_Plan1").submit();
	});

	$('#submit_third').click(function(){
		$("#form_Plan2").submit();
	});
	
	
	var current_date = new Date();
	var current_year=current_date.getFullYear();
	var hundrad_plus_year=current_year+100;
	
	$("#dob" ).datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: 'dd-mm-yy',
		maxDate: current_date ,
		yearRange: '1901:'+current_year,
		onChangeMonthYear: function(year, month, inst)  { $("#dob").datepicker( "setDate", '1-' + month +'-'+ year );}	
	});
	
/*	$("#phone, #mobile").keydown(function(event) {
        // Allow only backspace and delete
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 61 || event.keyCode == 43 || event.keyCode ==107 || event.keyCode ==110) {
            // let it happen, don't do anything
        }
        else {
            // Ensure that it is a number and stop the keypress
            if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault(); 
            }   
        }
    })*/;
	

	$('#form_Plan1').submit(function() {
		 
		if($('#back').val()=='no')
		{
		  $('#body input').removeClass('error').removeClass('valid');
		  $('.error_div').remove();
  
	   //   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
		  var fields = $('#body input[type=text], #body select');
		  //console.log(fields);
		  
		  var error = 0;
		  fields.each(function(){
			  var value = $(this).val();
			  var name=$(this).attr("name");
			  
			  setCookie(name, value, 1);
  
			  if( value.length<1 /*|| value==field_values[$(this).attr('id')]*/ ) {
				  $(this).addClass('error');
				  error++;
			  } else {
				  $(this).addClass('valid');
			  }
		  });
		  
		    var validate_text=$('#customer_name').val();
			if(validate_text.length == 0)
			{
				$("<div class='error_div' title='Name Required'></div>").insertAfter("#customer_name");
				error++;
				//return false;
			}
		  
		  
		    $('#dd').keyup(function() {
			  if($('#dd').val().length==2) $('#mm').focus();
			});
			
			$('#mm').keyup(function() {
			  if($('#mm').val().length==2) $('#yyyy').focus();
			});
			
			var current_date = new Date();
	        var current_year = current_date.getFullYear();
			var maxYear = current_year-1;
			var minYear = 1901;
			
			if(!checkDatee('dd','mm','yyyy','error_div',minYear,maxYear))
			{
				error++;
			}
		  
		  
		  if(error!=0) {
				return false;
		  }
		 }
	});
	
	
	$('#form_Plan2').submit(function() {
	if($('#back').val()=='no')
	{
		 var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		 var email=$('#email').val();
		 
		$('#body input').removeClass('error').removeClass('valid');
		$('.error_div').hide();
		$('.error_div2').hide();
		
		

     //   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
        var fields = $('#body input[type=text], #body select');
		//console.log(fields);
		
        var error = 0;
		fields.each(function(){
			
			var value = $(this).val();
			var name=$(this).attr("name");
			

			
            if( value.length<1 /*|| value==field_values[$(this).attr('id')]*/ ) {
                $(this).addClass('error');
                error++;
            } else {
                $(this).addClass('valid');
            }
        });
		
		if($("#phone").val().length<11)
		{
		  $("#phone").after("<div class='error_div2' title='Phone Number Required (922134593957)'></div>");
		  error++;
		}
		
		if($("#mobile").val().length<11)
		{
		  $("#mobile").after("<div class='error_div2' title='Mobile Number Required (922134593957)'></div>");
		  error++;
		}
		
		if(!emailReg.test(email) || email.length<1 )
		{
		  $("#email").after("<div class='error_div2' title='Valid Email Address Required.'></div>");
		  error++;
		}
		
		
		
		if(error!=0) {
                return false;
        }
	}
	});

	

	/* --- Js for 4th calculator End ------ */


	
});
