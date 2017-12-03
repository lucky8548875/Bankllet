<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
	require 'database.php';
	loginUser();
}else{
	echo "Oops! We're sorry! You do not have access to this option!";
}

function loginUser(){
	global $con;
    global $dbUserType;
    
	$IDNumber = $_POST['IDNumber'];
	$userPassword = $_POST['userPassword'];

	$query=mysqli_query($con,"SELECT * FROM tblusers WHERE IDNumber='$IDNumber' AND Password='$userPassword'");
  	$numrows=mysqli_num_rows($query);

  	if($numrows!=0){
    	while($row=mysqli_fetch_assoc($query)){
    		$dbIDNumber = $row['IDNumber'];
      		$dbPassword=$row['Password'];
      		$dbUserType=$row['UserType'];
            $InstitutionID=$row['UserInstitution'];
    	}

	  	if($IDNumber == $dbIDNumber && $userPassword == $dbPassword){
            $_SESSION["InstitutionID"] = $InstitutionID;
            $_SESSION["dbUserType"] = $dbUserType;
	    	if($dbUserType == "Administrator"){
	    		echo "success_administrator";
	    	}else if($dbUserType == "Student"){
	    		echo "success_student";
	    	}else{
	    		echo "success_developer";
	    	}
	   	}
  	}else{
  		echo "invalid";
  	}

	mysqli_close($con);
}

?>