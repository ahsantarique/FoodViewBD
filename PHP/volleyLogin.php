<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $email = $_POST['email'];
 $password = $_POST['password'];
 
 require_once('dbConnect.php');
 
 $sql = "SELECT * FROM `user` where `email_id`='$email' and `password`='$password';";
 
 $res = mysqli_query($con,$sql);
 if($res->num_rows > 0){
 echo "Success";
 }else{
 echo "Incorrect username or password";
 
 }
 }else{
echo 'error';
}
?>