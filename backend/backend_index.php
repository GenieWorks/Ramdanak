<?php

include ('dbHelper.php');

/* 
 * We have 2 types of requests.
 * 1. add user rating
 * 2. check for updates
 */

$submit = filter_input(INPUT_POST, 'submit');

if (isset($submit)) {
    
    // determine what the request is about
    $request_type = filter_input(INPUT_POST, 'request_type');
    
    $connection = open_database();
    
    // couldn't open a database connection
    if ($connection == FALSE) {
        header('HTTP/1.1 500 Server Down', true, 500);
    }
    
    if (strcmp($request_type, "updates") == 0) {
        
        $user_last_update = filter_input(INPUT_POST, 'lu');
        
        // TODO: check database for updates, and send to user
        
    } else if (strcmp($request_type, "rating") == 0) {
        
        $user_rating = filter_input(INPUT_POST, 'r');
        $type = filter_input(INPUT_POST, 't');
        
        if (type == 1) {
            $show = filter_input(INPUT_POST, 's');
            update_show_rating($show, $user_rating);
        } else {
            $channel = filter_input(INPUT_POST, 'c');
            update_channel_rating($channel, $user_rating);
        }
        
        // send the okay header
        header('HTTP/1.1 200 OK!', true, 200);
    } else {
        header('HTTP/1.1 400 Bad Request', true, 400);
    }
    
    close_connection($connection);
}
