<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="https://fonts.googleapis.com/css?family=Raleway"
	rel="stylesheet">
<head>
<style>
* {
	box-sizing: border-box;
}

body {
	background-color: #f1f1f1;
}

#regForm {
	background-color: #ffffff;
	margin: 100px auto;
	font-family: Raleway;
	padding: 40px;
	width: 70%;
	min-width: 300px;
}

td {
	width: 100;
	text-align: center;
}

h1 {
	text-align: center;
}

input {
	padding: 10px;
	width: 100%;
	font-size: 17px;
	font-family: Raleway;
	border: 1px solid #aaaaaa;
}

/* Mark input boxes that gets an error on validation: */
input.invalid {
	background-color: #ffdddd;
}

/* Hide all steps by default: */
.tab {
	display: none;
}

button {
	background-color: #4CAF50;
	color: #ffffff;
	border: none;
	padding: 10px 20px;
	font-size: 17px;
	font-family: Raleway;
	cursor: pointer;
}

button:hover {
	opacity: 0.8;
}

#prevBtn {
	background-color: #bbbbbb;
}

/* Make circles that indicate the steps of the form: */
.step {
	height: 15px;
	width: 15px;
	margin: 0 2px;
	background-color: #bbbbbb;
	border: none;
	border-radius: 50%;
	display: inline-block;
	opacity: 0.5;
}

.step.active {
	opacity: 1;
}

/* Mark the steps that are finished and valid: */
.step.finish {
	background-color: #4CAF50;
}
</style>
<head>
<title>Provider Management</title>
</head>
<body style="background-color: powderblue;">
	<c:if test="${empty sessionScope.login}">
		<h1>here</h1>
		<c:redirect url="logout.htm"></c:redirect>
	</c:if>
	<c:if test="${sessionScope.login eq null}">
		<h1>here2</h1>
		<c:redirect url="logout.htm"></c:redirect>
	</c:if>
	<table align="center">
		<tr>
			<td>&nbsp &nbsp &nbsp</td>
		</tr>
		<tr>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td><img src="alpha-logo.png" alt="logo"></td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>
				<h1>ALPHA</h1>
				<h3>Vaccine Management System</h3>
			</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>
				<h5>Final Project</h5>
				<h5>Web Development Tools, Section-1</h5>
				<h5>Northeastern University, Boston</h5>
			</td>
			<td>&nbsp &nbsp &nbsp</td>
			<td>
				<form action="logout.htm" method="post">
					<input type="submit" value="Logout">
				</form>
			</td>
		</tr>
	</table>

	<form id="regForm" method="post" action="manager-approve.htm">
		<h1>Welcome, ${sessionScope.user.person.fName}</h1>
		<h1>Select a vaccine request to approve:</h1>
		<!-- One "tab" for each step in the form: -->
		<div class="tab">
			<div align="center">
				<table border="1">
					<thead>
						<td>Vaccine</td>
						<td>Quantity</td>
						<td>Sender</td>
						<td>Status</td>
						<td>Receiver</td>
						<td>Select</td>
					</thead>
					<c:forEach var="wr" items="${requestScope.lwr}">
						<tr>
							<td>${wr.vaccine.vaccineName}</td>
							<td>${wr.quantity}</td>
							<td>${wr.senderOrganization.organizationName}</td>
							<td>${wr.status}</td>
							<td>${wr.receiverEnterprise.enterpriseName}</td>
							<td><input type="radio" name="select" id="select"
								value="${wr.workRequestId}"></td>
						</tr>
					</c:forEach>
				</table>
				<br>
				<br>


			</div>
		</div>

		<div style="overflow: auto;">
			<div style="float: right;">
				<button type="button" onclick="x()">Refresh</button>
				&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp
				<button type="button" onclick="approve()">Approve</button>
				&nbsp &nbsp &nbsp &nbsp
			</div>
		</div>
	</form>

	<script>
		function x() {
			window.location.reload();
		}
		function http() {
			try {
				var httpRequest = new XMLHttpRequest();

			} catch (e) {
			}
			return httpRequest;
		}

		function approve() {
			var httpRequest = http();
			httpRequest.onreadystatechange = function() {
				if (httpRequest.readyState == 4) {
					var xxx = httpRequest.responseText;
					alert(xxx);
				}
			}
			var select = document.querySelector('input[name="select"]:checked').value;
			httpRequest.open("POST", "./manager-approve.htm?select=" + select,
					true);
			httpRequest.send();
		}
		var currentTab = 0; // Current tab is set to be the first tab (0)
		showTab(currentTab); // Display the current tab

		function showTab(n) {
			// This function will display the specified tab of the form...
			var x = document.getElementsByClassName("tab");
			x[n].style.display = "block";
			//... and fix the Previous/Next buttons:
			if (n == 0) {
				document.getElementById("prevBtn").style.display = "none";
			} else {
				document.getElementById("prevBtn").style.display = "inline";
			}
			if (n == (x.length - 1)) {
				document.getElementById("nextBtn").innerHTML = "Approve";
			} else {
				document.getElementById("nextBtn").innerHTML = "Next";
			}
			//... and run a function that will display the correct step indicator:
			fixStepIndicator(n)
		}

		function nextPrev(n) {
			// This function will figure out which tab to display
			var x = document.getElementsByClassName("tab");
			// Exit the function if any field in the current tab is invalid:
			if (n == 1 && !validateForm())
				return false;
			// Hide the current tab:
			x[currentTab].style.display = "none";
			// Increase or decrease the current tab by 1:
			currentTab = currentTab + n;
			// if you have reached the end of the form...
			if (currentTab >= x.length) {
				// ... the form gets submitted:
				document.getElementById("regForm").submit();
				return false;
			}
			// Otherwise, display the correct tab:
			showTab(currentTab);
		}

		function validateForm() {
			// This function deals with validation of the form fields
			var x, y, i, valid = true;
			x = document.getElementsByClassName("tab");
			y = x[currentTab].getElementsByTagName("input");
			// A loop that checks every input field in the current tab:
			for (i = 0; i < y.length; i++) {
				// If a field is empty...
				if (y[i].value == "") {
					// add an "invalid" class to the field:
					y[i].className += " invalid";
					// and set the current valid status to false
					valid = false;
				}
			}
			// If the valid status is true, mark the step as finished and valid:
			if (valid) {
				document.getElementsByClassName("step")[currentTab].className += " finish";
			}
			return valid; // return the valid status
		}

		function fixStepIndicator(n) {
			// This function removes the "active" class of all steps...
			var i, x = document.getElementsByClassName("step");
			for (i = 0; i < x.length; i++) {
				x[i].className = x[i].className.replace(" active", "");
			}
			//... and adds the "active" class on the current step:
			x[n].className += " active";
		}
	</script>

</body>
</html>
