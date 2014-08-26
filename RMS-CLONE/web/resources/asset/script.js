function getDateObject(dateString,dateSeperator)
{
//This function return a date object after accepting
//a date string ans dateseparator as arguments
var curValue=dateString;
var sepChar=dateSeperator;
var curPos=0;
var cDate,cMonth,cYear;

//extract day portion
curPos=dateString.indexOf(sepChar);
cDate=dateString.substring(0,curPos);

//extract month portion
endPos=dateString.indexOf(sepChar,curPos+1); cMonth=dateString.substring(curPos+1,endPos);

//extract year portion
curPos=endPos;
endPos=curPos+5;
cYear=curValue.substring(curPos+1,endPos);

//Create Date Object
dtObject=new Date(cYear,cMonth,cDate);
return dtObject;
}

jQuery(function($){
	
	var ctrlDown = false;
    var ctrlKey = 17, vKey = 86, cKey = 67;

    $(document).keydown(function(event)
    {
        if (event.keyCode == ctrlKey) ctrlDown = true;
    }).keyup(function(event)
    {
        if (event.keyCode == ctrlKey) ctrlDown = false;
    });
	
/* --------------stop right click in fields ------*/
 $("body.page-template-tax-calc-php :input[type=text], body.page-template-tmp-calc-financial-php :input[type=text], body.page-template-tmp-calc-plan-finder-php :input[type=text]").oncontextmenu = function() { /*alert('Right mouse button!'); */  return false;};
 $("body.page-template-tax-calc-php :input[type=text], body.page-template-tmp-calc-financial-php :input[type=text], body.page-template-tmp-calc-plan-finder-php :input[type=text]").mousedown(function(e){ 
    if( e.button == 2 ) { 
      /*alert('Right mouse button!'); */
	  e.preventDefault(); 
      return false; 
    } 
    return true; 
  }); 
/* --------------stop right click in fields ------*/
	

$("body.page-template-tax-calc-php :input[type=text], body.page-template-tmp-calc-financial-php :input[type=text], body.page-template-tmp-calc-plan-finder-php :input[type=text]").keydown(function(event) {
	
	       var num_fields_arry=['dd2','mm2','yyyy2','dd','mm','yyyy','ret_age','age' ,'est_cur_value' ,'req_year' ,'amount_when_req' ,'edu_est_cur_value' ,'edu_req_year' ,'edu_amount_when_req' ,'car_est_cur_value' ,'car_req_year' ,'car_amount_when_req','marri_est_cur_value' ,'marri_req_year' ,'marri_amount_when_req','other_est_cur_value' ,'other_req_year' ,'other_amount_when_req','age' ,'today_avg_apend','annual_income' ,'cp_ro' ,'avg_income' ,'asset_worth' ,'liab_worth' ,'sa','phone','mobile','email'];
		   
	var id=$(this).attr("id");
	
	if(jQuery.inArray(id,num_fields_arry)==-1) /* for char only */
	{
	       if (ctrlDown && (event.keyCode == vKey || event.keyCode == cKey)) 
		   {
			   return false;
		   }
		   else
		   {
		    
			   if ( 
			   (event.keyCode > 64 && event.keyCode < 91) ||
			   (event.keyCode == 46) ||
			   (event.keyCode == 32) ||
			   (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 13) 
					) 
				{
					/*alert('yes');*/
				}
					
				else if(event.keyCode == 16)
				{
					/*alert('shfit');*/
					//alert('Please dont enter Special Characters.');
					event.preventDefault(); 
				}
				else if(event.keyCode == 17)
				{
					/*alert('ctrl');*/
					event.preventDefault(); 
				}
				else 
				{ 
					/*alert('no.');*/
					event.preventDefault(); 
				}
		   }
	}
	else { /* for numbers only */
	
			  if(id=='email') /* for char only */
			  {
			  }
			  else
			  {
			  
				  if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 ) {
					  // let it happen, don't do anything
				  }
				  else if(event.keyCode == 16)
				  {
					  //alert('Please dont enter Special Characters.');
					  event.preventDefault(); 
				  }
				  else {
					  // Ensure that it is a number and stop the keypress
					  if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
						  event.preventDefault(); 
					  }   
				  }
			  }
	
	  }
});	

$("#s, #s1").keydown(function(event) {
	       if ((event.keyCode > 64 && event.keyCode < 91) ||
                (event.keyCode == 46) ||
				(event.keyCode == 32) ||
                (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 13) ||
				  (event.keyCode > 96 && event.keyCode < 123)) {
            }
			else if(event.keyCode == 16)
			{
				//alert('Please dont enter Special Characters.');
				event.preventDefault(); 
			}
            else { event.preventDefault();  }
});




	var current_year = new Date();
	var ten_plus_year=current_year.getFullYear()+10;
	

	$('div.entry').prepend($('#pretext'));
	
	$('#fundlist select').css('margin-bottom','10px');
	$('#amountlist input').css('margin-bottom','32px');


	$( "#start_date" ).datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: 'dd-mm-yy',
		yearRange: '1996:'+current_year.getFullYear(),
		onChangeMonthYear: function(year, month, inst)  { $("#start_date").datepicker( "setDate", '1-' + month +'-'+ year );}	
	});
	
	$( "#end_date" ).datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: 'dd-mm-yy',
		yearRange: '1996:'+current_year.getFullYear(),
		onChangeMonthYear: function(year, month, inst)  { $("#end_date").datepicker( "setDate", '1-' + month +'-'+ year );}	
	});
	
	$('#calc-nav a').click(function(){$('#calc-nav a').removeClass('current');$(this).addClass('current');});
		
			
	$('#panelgraph').hide();
	$('#showgraph').empty();
	
	var fundids = '';
	var amounts = '';
	var i = 1;
	
	$('#addnewfund').click(function() {

		i += 1;
		if( i < 8) {
			$("#fundid1").clone(true).removeAttr("id").removeAttr("value").attr("id", "fundid" + (i)).appendTo("#fundlist");
			
			$("#amount1").clone(true).removeAttr("id") .attr("id", "amount" + (i)).appendTo("#amountlist");
			$("#eg1").clone(true).removeAttr("id") .attr("id", "eg" + (i)).empty().appendTo("#amountlist");
			
			$("#incept1").clone(true).removeAttr("id") .attr("id", "incept" + (i)).empty().appendTo("#fundlist");
			$("#amount" + (i)).val("");						 
			
		}
	});
	
	

	$('#removefund').click(function() {
		
			if($('#fundlist select').length > 1){
				 $('#fundid'+(i)).remove();					 
				 $('#amount'+(i)).remove();
				 $('#eg'+(i)).remove();
				 $('#incept'+(i)).remove();
				 i -= 1;
			}
	});
	
/*	$('#fundid'+i).change(function() { 
		$('#fundid+i option').removeAttr('style');
		if($('#fundid'+i).val() != "") {
			$('#fundid2 option').eq($('#fundid option:selected').index()).attr('style','display:none');
		}
		
		$.ajax({
			url:  $('#blogurl').val() + "/getinception.php?fundid=" + $('#fundid+i').val(),
			cache: false,
			success: function(html){
							//$("#incept"+i).empty().append(html);
							alert(html);
					}
			});
	});
	*/
    

	$('#benchmark').click(function() { 
		if($('#benchmark').is(":checked"))
			$('#bench').val('1');
		else 
			$('#bench').val('0');
		$("<img src='" + $("#blogurl").val() + "/icons/move-spinner.gif' id='spinner' />").hide().appendTo("#showgraph");
		$('#spinner').fadeIn();	
		if($('#by').val() == 'daterange') {
		
			
			$.ajax({
				url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=" + $('#by').val() + "&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val() + "&fromDate=" + $('#start_date').val() + "&toDate="+$('#end_date').val(),
				cache: false,
				success: function(data){
						$('#spinner').fadeOut();
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$('#benchinfo').hide();
							}
							else {
							
								$('#error').empty();
								$('#panelgraph').show();	
							}
						
						}
				});
		}
		else {
			
			$.ajax({
				url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=" + $('#by').val() + "&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
				cache: false,
				success: function(data){
						$('#spinner').fadeOut();
						$('#showgraph').empty().append(data);
						
						}
				});
		
		}
	
	}); 

	
	$('#viewgraph').click(function() { 
		var fund_list_chk = true;
		var error = 0;
		
		/*var dd = $('#dd').val();
	    var mm = $('#mm').val();
	    var yyyy = $('#yyyy').val();
	    var st_date = dd+'-'+mm+'-'+yyyy;*/
		
		var st_date = $('#start_date').val();
		
		/*var dd2 = $('#dd2').val();
	    var mm2 = $('#mm2').val();
	    var yyyy2 = $('#yyyy2').val();
	    var e_date = dd2+'-'+mm2+'-'+yyyy2;*/
		
		var e_date = $('#end_date').val();
		
		if(st_date != "" && e_date != "" && $('#fundid').val() != "" ) {
			
			if($('#fundid2').val() == "" || $('#amount').val() == ""){
					$('#fielderror').empty().append('All fields are required.');
					$('#fielderror').show();
					$('#error').hide();
					$('#panelgraph').hide();
					$('#showgraph').hide();
					fund_list_chk = false;
					error = 1;
			}
			var start_d=getDateObject(st_date,'-');
			var end_d=getDateObject(e_date,'-');
			if(start_d>end_d){
					$('#fielderror').empty().append('From date should less than to To date.');
					$('#fielderror').show();
					$('#error').hide();
					$('#panelgraph').hide();
					$('#showgraph').hide();
					fund_list_chk = false;
					error = 1;
			}
			
			
			$('#bench').val('0');
			
			$('#benchmark').removeAttr('checked');
			
			if($('#fundlist').length > 0)
			{
				var totalelem = $('#fundlist').children().length - 1;
				
				fundids = '';
				amounts = '';
				for(var i = 1; i <= totalelem; i++)
				{
					if($("#fundid" + i).val() != "" && $("#amount"+ i).val() != "") { 
						fundids += $("#fundid"+ i).val() + ',';	
						amounts += $("#amount"+ i).val() + ',';
						fund_list_chk = true;
					}
					else {
						
						$('#fielderror').empty().append('All fields are required.');
						$('#fielderror').show();
						$('#error').hide();
						fund_list_chk = false;
						error = 1;
						break;
					}
				}
			
			}
			if (error == 0) {
				$("<img src='" + $("#blogurl").val() + "/icons/move-spinner.gif' id='spinner' />").hide().appendTo("#showgraph");
				$('#spinner').fadeIn();
			
				var params =  "&fundids=" + fundids + "&amounts=" + amounts;
			
				$.ajax({
					url:  $('#blogurl').val() + "/fund-pretext.php?fundid=" + $('#fundid').val()+ "&graph=" + $('#graphtype').val(),
					cache: false,
					success: function(html){
							
						if($(".cal-info").is(':empty') || $("#fundname").text() != $('#fundid option:selected').text() ){
								
							$(".cal-info").empty().append(html);
						}
					}
			
				});
				if(fund_list_chk) {
						$.ajax({
							url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=daterange&fundid=" + $('#fundid').val() + "&fundid2=" + $('#fundid2').val() + "&fromDate=" + st_date + "&toDate="+e_date + "&graph=" + $('#graphtype').val() + "&amount=" + $('#amount').val() + params,
							cache: false,
							success: function(data){
									
									$('#spinner').fadeOut();
									$('#showgraph').empty().append(data);
																			
									if($('#chartgraph').length == 0 || $('#chartgraph').is(':empty')) {
										$('#divbenchmark').hide();								
										$('#error').text('No Data Available.');
										$('#error').show();
										$('#panelgraph').hide();
										$('#cal-info').hide();
										$('#calc-nomp').hide();
										$('#fielderror').hide();
										$('#snav_button').hide();
										$('#chartgraph').hide();
										$('#benchinfo').hide();			
											
									}
									else {
										$('#divbenchmark').show();	
										$('#error').hide();	
										$('#panelgraph').show();
										$('#cal-info').show();
										$('#calc-nomp').show();
										$('#fielderror').hide();
										$('#snav_button').show();
										$('#chartgraph').show();											
									}
									
									if($("#showgraph").length == 0)
									{
										$("#divtopborder").hide();
										}
									else
									{
										$("#divtopborder").show();
										}
									
									$('#by').val('daterange');
										
										
									
									if($('#fundid option:selected').text() == 'UBL Retirement Saving Fund' || $('#fundid option:selected').text() == 'UBL Capital Protected Fund - I' || $('#fundid option:selected').text() == 'UBL Islamic Retirement Savings Fund') {
				
										$('#benchmark').attr('disabled','disabled');
										$('#bench').val('0');
									}
									else {
					
										$('#benchmark').removeAttr('disabled');
										if($('#benchmark').is(":checked"))
											$('#bench').val('1');
										else 
											$('#bench').val('0');
									}
									
								}
							});
							$('#fielderror').hide();
				}
			}
			
		}
		else {
			
			$('#fielderror').empty().append('All fields are required.');
			$('#fielderror').show();
			$('#error').hide();
			$('#panelgraph').hide();
		}
	});
	
	$('#fundid').change(function() { 
	
		$('#fundid2 option').removeAttr('style');
		if($('#fundid').val() != "") {
			$('#fundid2 option').eq($('#fundid option:selected').index()).attr('style','display:none');
		}
		
		$.ajax({
			url:  $('#blogurl').val() + "/getinception.php?fundid=" + $('#fundid').val(),
			cache: false,
			success: function(html){
							$("#incept1").empty().append(html);
					}
			});
	});
	

	
	
	$('#fundid2').change(function() { 
	
		$('#fundid option').removeAttr('style');
		if($('#fundid2').val() != "") {
			
			$('#fundid option').eq($('#fundid2 option:selected').index()).attr('style','display:none');
		}
		
		$.ajax({
			url:  $('#blogurl').val() + "/getinception.php?fundid=" + $('#fundid2').val(),
			cache: false,
			success: function(html){
							$("#incept2").empty().append(html);
					}
			});
	
	});
	
	

	
	
	$('#clickfiveyear').click(function(){
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=5y&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
						$('#by').val('5y');
					}
			});
	});
	
	$('#clickthreeyear').click(function(){
			
			
			
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=3y&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
						
						$('#by').val('3y');
					}
			});
	});
	
	$('#clickoneyear').click(function(){
			
			
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=1y&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
							
						$('#by').val('1y');
					}
			});
	});
	
	$('#clickoneweek').click(function(){
			
			
			
			
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=1w&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();	
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
							
							$('#by').val('1w');
						
					}
			});
	});
	
	$('#clickonemonth').click(function(){
			
			
			
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=1m&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val() + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();	
								$("#divbenchmark").show();
								
							}
							
						$('#by').val('1m');
					}
			});
	});
	
	$('#clickthreemonth').click(function(){
			
						
		
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=3m&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val() + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
						$('#by').val('3m');
					}
			});
	});
	
	$('#clicksixmonth').click(function(){
			
		
			
			$('#customrange').hide();
			
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=6m&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val() + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
						$('#by').val('6m');
					}
			});
	});
	
	$('#clicksi').click(function(){
			
			
			$('#customrange').hide();
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=si&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val()  + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();
								$("#divbenchmark").show();
								
							}
						$('#by').val('si');
					}
			});
	});
	
	$('#clickytd').click(function(){
			
			
			$('#customrange').hide();
			$.ajax({
			url:  $('#blogurl').val() + "/showgraph.php?p= " + $('#postId').val() + "&by=ytd&fundid=" + $('#fundid').val() + "&graph=" + $('#graphtype').val() + "&amount=" + $('#amount').val() + "&fundid2=" + $('#fundid2').val() + "&benchmark=" + $('#bench').val(),
			cache: false,
			success: function(data){
						$('#showgraph').empty().append(data);
						if($('#chartgraph').is(':empty')) {
							
								$('#error').empty().append('No Data Available.');
								$('#error').show();
								$("#divbenchmark").hide();
								$("#benchinfo").hide();
							}
							else {
							
								$('#error').empty();
								$('#error').hide();
								$('#panelgraph').show();	
								$("#divbenchmark").show();
								
							}
						$('#by').val('ytd');
					}
			});
	});

	
});

function updateDropdown(ctrl) {
	
$.ajax({
	
	url:  $('#blogurl').val() + "/getinception.php?fundid=" + $(ctrl).val(),
	cache: false,
	success: function(html){
					var strid = $(ctrl).attr('id');
					var splitid = strid.substr(6);
					$("#incept"+splitid).empty().append(html);
			}
	});
}

