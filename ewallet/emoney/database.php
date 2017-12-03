<?php
define('DB_HOST','localhost:3307');
define('DB_USER','root');
define('DB_PASS','usbw');
define('DB_NAME','dbewallet');
 
$con = mysqli_connect(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die ("could not connect to mysql");
$eWalletUrl = "http://tristanrosales.x10.mx/E-Wallet/";
?>