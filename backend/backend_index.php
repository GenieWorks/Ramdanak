<?php

/* 
 * We have 2 types of requests.
 * 1. add user rating
 * 2. check for updates
 */

$submit = filter_input(INPUT_POST, 'submit');

if (isset($submit)) {
    
    // determine what the request is about
    $request_type = filter_input(INPUT_POST, 'request_type');
    
    include ('config.php');
    
    if (strcmp($request_type, "updates") == 0) {
        
        $user_last_update = filter_input(INPUT_POST, 'lu');
        
        // TODO: check database for updates, and send to user
        
    } else if (strcmp($request_type, "rating") == 0) {
        
        $rating = filter_input(INPUT_POST, 'r');
        
        // TODO: add rating to database
        
    } else {
        header('HTTP/1.1 400 Bad Request', true, 400);
    }
}
