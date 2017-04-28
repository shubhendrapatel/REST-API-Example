var firstname,lastname,url,displayArea;

url = "http://localhost:8080/DumEmployeeSystemRestApi/";

firstname = document.getElementById("firstname");
lastname = document.getElementById("lastname");

displayArea = document.getElementById("displayArea");

var storeUserData = function(){
	console.log("storing user data");
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {
    		console.log(this.responseText);
    		displayArea.innerHTML = this.responseText;
    	}
  	};
  	xhttp.open("POST", url, true);
  	xhttp.setRequestHeader("Accept", "text/plain");
	xhttp.setRequestHeader("Content-type", "application/json");
	xhttp.send(JSON.stringify({
		'firstName' : firstname.value,
		'lastName' : lastname.value
	}));
}

var getAllData = function(){
	var xhttp = new XMLHttpRequest();
	
	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {
    		console.log(this.responseText);
    		displayArea.innerHTML = this.responseText;
    	}
  	};
  	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Accept", "application/json");
	xhttp.setRequestHeader("Content-type", "application/json");
  	xhttp.send();
}