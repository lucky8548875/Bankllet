<?php

session_start();
if($_SERVER["REQUEST_METHOD"]=="POST"){
	require '../../../emoney/database.php';
	addUser();
}else{
	echo "Oops! We're sorry! You do not have access to this option!";
}

function addUser(){
    global $con;
    $id = $_POST['id'];
    $pass = $_POST['pass'];
    $institution = $_POST['institution'];
    
    $query="INSERT INTO tblusers (IDNumber,Password,UserType,UserInstitution) Values ($id,$pass,'$institution','Student') ";
    echo $query;

  	if($con->query($query) === TRUE){
        echo 'Success';
        header('location: ../manageStudents.php?success');
}
    else{
        echo 'Server not ready';
    }

}

?>