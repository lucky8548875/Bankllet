<!--
<a href="">Manage Units</a>
<ul>
    <li>Accounting
        <ul>
            <li>Tuition Fee</li>
            <li>Other Fees</li>
        </ul>
    </li>
    <li>Library
        <ul>
            <li>Photocopy Section</li>
            <li>Printing Section</li>
            <li>Overdue Book Fees</li>
        </ul>
    </li>
    <li>Canteen
        <ul>
            <li>Oven Maid</li>
            <li>Nanay's Cuisine</li>
            <li>Tapsi Royale</li>
            <li>Tempura Sam</li>
        </ul>
    </li>
    <li>Item Shop
        <ul>
            <li>Uniform Warehouse</li>
            <li>Animo Shop</li>
        </ul>
    </li>
    <li>Outreach
        <ul>
            <li>Marawi Victims</li>
            <li>University Scholarship Fund</li>
            <li>University Community Outreaches</li>
        </ul>
    </li>
</ul>-->


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
<div class="nav2">
<a href="index.php" class="active">Summary</a>
<a href="manageStudents.php">Manage Students</a>
<a href="manageCategories.php">Manage Categories</a>
</div>
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
<?php 
    include '../../emoney/loadbalancesummary.php';
?>



</div>
        
    </body>



</html>