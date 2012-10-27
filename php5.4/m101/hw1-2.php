<?php

// Simple check for mongo.so
if (!class_exists('Mongo')) {
    die('You must install mongodb extension http://www.php.net/manual/en/mongo.installation.php');
}

try {
    // Connect to your server
    $mongo = new Mongo($server = "mongodb://localhost:27017", array("connect" => TRUE));
    // Get the collection and the cursor
    $cursor = $mongo->selectCollection('m101', 'funnynumbers')->find();

    $magic = 0;

    // Iterate cursor
    foreach ($cursor as $item) {
        // We don't check for isset($item['value']) here
        if ($item['value'] % 3 === 0) {
            $magic += $item['value'];
        }
    }

    print sprintf("The answer to Homework One, Problem 2 is %s \n", $magic);

} catch (Exception $e) {
    print sprintf('[EXCEPTION] %s', $e->getMessage());
}