<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $username = $_POST['username'];
 $email = $_POST['email'];
 $password = $_POST['password'];
 
 require_once('dbConnect.php');
 
 $sql = "INSERT INTO `user` (user_name,email_id,password) VALUES ('$username','$email','$password')";
 
 
 if(mysqli_query($con,$sql)){
 echo "Success";
 }else{
 echo "Could not register";
 
 }
 }else{
echo 'error';
}
?>