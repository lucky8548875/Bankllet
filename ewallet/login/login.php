<?php
session_start();
include '../emoney/loginUser.php';


$_SESSION["IDNumber"] = $_POST['IDNumber'];
$_SESSION["userPassword"] = $_POST['userPassword'];


echo $_SESSION["IDNumber"];

// Test Header



if ($dbUserType=='Administrator')
    header('location:../dashboard/administrator/index.php');
if ($dbUserType=='Student')
    header('location:../dashboard/student/index.php');
?>