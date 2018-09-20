<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
	 $category = $_POST['category'];
	 $text = $_POST['text'];
	 
	 require_once('dbConnect.php');
	 
	if($category==="Restaurant"){
		$sql = "SELECT * FROM `restaurant` where `r_name` like '%$text%';";
		$res = mysqli_query($con,$sql);
	 	if($res->num_rows == 0){
	 		echo "empty";
	 	}
	 	else{
	 		while($row = $res->fetch_array(MYSQL_ASSOC)) {
	 		        $myArray[] = $row;
	 		}
	 		echo json_encode($myArray);
	 	}
	}
	elseif($category==="Food Item"){
		$sql = "SELECT * FROM `food_item` join `food_index` where `food_name` like '%$text%';";
		$res = mysqli_query($con,$sql);
	 	if($res->num_rows == 0){
	 		echo "empty";
	 	}
	 	else{
	 		echo $res;
	 	}
	}
	elseif($category==="Food Type"){

	}
	elseif($category==="Location"){

	}
	else{

	}

}
?>