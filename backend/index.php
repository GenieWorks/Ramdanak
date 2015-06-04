<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title>Ramdanak App</title>
    </head>
    <body>
        <h1>Ramdanak</h1>
        <?php 
            include_once 'dbHelper.php';
            $connection = open_database();
            
            if ($connection == null) {
                echo 'failled';
            } else {
                echo 'sucess';
                close_connection($connection);
            }
            
            
        ?>
    </body>
</html>
