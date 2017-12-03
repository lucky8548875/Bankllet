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
<a href="index.php">Summary</a>
<a href="manageStudents.php" class="active">Manage Students</a>
<a href="manageCategories.php">Manage Categories</a>
</div>
<div class="card">
    <table class="cardnav" width=100%><tr><td>Manage Students</td><td align=right><a href="">Manual Withdraw/Transfer</a></td></tf></tr></table>

<!--
<div class="card2">
<table cellspacing=0 cellpadding=0>
    <tr><th>{Accounting}</th><th>{Php 500}</th></tr>
    <tr><td>{Photocopy Section}</td><td>{Php 200}</td></tr>
    <tr><td>{Photocopy Section}</td><td>{Php 200}</td></tr>
</table>
</div>
-->

<form action="actions/addStudent.php" method="POST" style="margin-top: 0px">
    <center>
<input type=text name="id" placeholder="Enter ID Number">
<input type="submit" style="width: 6em; margin-left: 1em">
<input type="hidden" value="1234" name="pass">
<input type="hidden" value="<?php echo $_SESSION['InstitutionID']; ?>" name="institution">
        </center>
</form>

</div>
        
    </body>



</html>