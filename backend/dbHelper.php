<?php

/**
 * Open a connection to the database
 * @return mixed, false on failure or the connection on success
 */
function open_database() {
    $connection = mysqli_connect('localhost','root');

    // Check connection
    if (mysqli_connect_errno())
    {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }

    // TODO: add database name
    if (!mysqli_select_db($connection, "Ramdanak")) {
        return false;
    }

    return $connection;
}

/**
 * update the rating for a certain tvshow
 * @param int $show show id
 * @param decimal $user_rating user rating
 * @return boolean, TRUE on sccuess and FALSE on failure
 */
function update_show_rating($show, $user_rating) {
    // get the rating count and the current rating for the show
    $query = "SELECT rating_count, rating FROM Show WHER _id = " . $show;
    $result = mysql_query($query);
    
    if (!$result) {
        die('Could not query:' . mysql_error());
    }
    
    $rating_count = mysql_result($result, 0);
    $rating = mysql_result($result, 1);
    
    // calculate the new rating
    $new_rating = ($rating_count * $rating + $user_rating) / ($rating_count + 1);
    
    $query = "UPDATE Show SET rating_count = " . ($rating_count + 1)
            . " rating = " . $new_rating . " WHERE _id = " . $show;
    
    return mysql_query($query);
}

/**
 * Updates the rating for a channel
 * @param int $channel channel id
 * @param decimal $user_rating user rating
 * @return boolean, TRUE on success and FALSE on failure
 */
function update_channel_rating($channel, $user_rating) {
    // get the rating count and the current rating for the channel
    $query = "SELECT rating_count, rating FROM Channel WHER _id = " . $channel;
    $result = mysql_query($query);
    
    if (!$result) {
        die('Could not query:' . mysql_error());
    }
    
    $rating_count = mysql_result($result, 0);
    $rating = mysql_result($result, 1);
    
    // calculate the new rating
    $new_rating = ($rating_count * $rating + $user_rating) / ($rating_count + 1);
    
    $query = "UPDATE Channel SET rating_count = " . ($rating_count + 1)
            . " rating = " . $new_rating . " WHERE _id = " . $channel;
    
    return mysql_query($query);
}

/**
 * Gets the list of all avialable updates for the user.
 * @param type $last_update last update id user have.
 */
function get_list_of_updates_for_user($last_update) {
    $query = "SELECT * FROM Update WHERE id > " . $last_update;
    $result = mysql_query($query);
    
    if (!$result) {
        
    }
}

/**
 * Closes a given mysql connection
 * @param mysql_connection $connection current open connection
 */
function close_connection($connection) {
    if ($connection != NULL || $connection != FALSE) {
        mysql_close($connection);
    }
}