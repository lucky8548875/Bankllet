<html>
<head>
    <link rel="stylesheet" href="../../style/style.css">
</head>
    <body>
        
        <table class="nav"><tr><td>
        
            <?php
                session_start();
                require '../../emoney/database.php';
                global $con;
                $query=mysqli_query($con,"SELECT InstitutionName FROM tblinstitutions WHERE InstitutionID='".$_SESSION["InstitutionID"]."'");
  	             $numrows=mysqli_num_rows($query);

  	if($numrows!=0){
    	while($row=mysqli_fetch_assoc($query)){
    		echo $row['InstitutionName'];
    	}
    }
            
            
            ?>
            
        </td><td align=right><a href="../../login/logout.php">LOGOUT</a></td></tf></tr></table>
        
<div class="card">
    <table class="cardnav" width=100%><tr><td>Balance Summary</td><td align=right><a href="">Manual Withdraw/Transfer</a></td></tf></tr></table>

<!--
<div class="card2">
<table cellspacing=0 cellpadding=0>
    <tr><th>{Accounting}</th><th>{Php 500}</th></tr>
    <tr><td>{Photocopy Section}</td><td>{Php 200}</td></tr>
    <tr><td>{Photocopy Section}</td><td>{Php 200}</td></tr>
</table>
</div>
-->




</div>
        
    </body>



</html>