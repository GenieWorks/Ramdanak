<?php

$connection = mysqli_connect('localhost','root');

// Check connection
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

// TODO: add database name
mysqli_select_db($connection,"") or die('I couldnt find the database');

