<?php
//session_start();
if($_SESSION!=null){
	loadBalances();
}else{
	echo "Oops! We're sorry! You do not have access to this option!";
}

function loadBalances(){
	global $con;

	$IDNumber = $_SESSION['InstitutionID'];
	

	$query=mysqli_query($con,"SELECT * FROM tblcategory WHERE CategoryInstitutionID='$_SESSION[InstitutionID]'");
    
  	$numrows=mysqli_num_rows($query);

  	if($numrows!=0){
    	while($row=mysqli_fetch_assoc($query)){    //for each loop
    		$CategoryID = $row['CategoryID'];
            
            // HTML
            echo'<div class="card2">';
            echo '<table cellspacing=0 cellpadding=0>';
            echo '<tr><th>';
            /* ECHO */ echo $row['CategoryName'].'<br>';
            echo '</th><th>';
            // Get sum of CategoryID transactions
            $query2=mysqli_query($con,"SELECT SUM(ProductOrItemAmount) FROM tbltransactions WHERE ProductCategory='$CategoryID' GROUP BY ProductCategory");
  	        $numrows2=mysqli_num_rows($query2);
                
            if($numrows!=0){
                while($row2=mysqli_fetch_assoc($query2)){
                    $Sum = $row2['SUM(ProductOrItemAmount)'];
                    /* ECHO */ echo $Sum.'<br>';
                        
                    // HTML
                    echo '</th></tr>';
                    
                }
            }
            else{
                /* ECHO */ echo('<span class="nodata">No data</span>');
            }
            
            // Loop Items
            $query3=mysqli_query($con,"SELECT Product_SubCategory, SUM(ProductOrItemAmount) FROM tbltransactions WHERE ProductCategory='$CategoryID' GROUP BY Product_SubCategory");
            //echo "SELECT Product_SubCategory, SUM(ProductOrItemAmount) FROM tbltransactions WHERE ProductCategory='$CategoryID' GROUP BY Product_SubCategory".'<br>';
            $numrows3=mysqli_num_rows($query3);
            if($numrows3!=0){
                while($row3=mysqli_fetch_assoc($query3)){
                    
                    $Sub_name;
                    $subcat = $row3['Product_SubCategory'];
                    $query4 = mysqli_query($con,"SELECT SubCategoryName from tblsub_category WHERE SubCategoryID='$subcat'");

                    while($row4 = mysqli_fetch_assoc($query4)){
                    $Sub_name = $row4['SubCategoryName'];
                    }
                    $Sum_sub = $row3['SUM(ProductOrItemAmount)'];
                    echo '<tr><td>';
                    /* ECHO */ echo $Sub_name.'</td><td>'.$Sum_sub.'</td></tr>';
                        
                }
            }
            else{
                /* ECHO */ echo('<span class="nodata">No data</span>');
            }
            
            
      		echo '</table>';
        echo '</div>';
    	}
        
  	}
    else{
        /* ECHO */ echo('<span class="nodata">No data</span>');
    }

	mysqli_close($con);
}

?>