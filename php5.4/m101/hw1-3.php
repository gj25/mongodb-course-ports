<?php

// Simple check for mongo.so
if (!class_exists('Mongo')) {
    die('You must install mongodb extension http://www.php.net/manual/en/mongo.installation.php');
}

try {
    // Connect to your server
    $mongo = new Mongo($server = "mongodb://localhost:27017", array("connect" => TRUE));

    $n = (int)$n;

    // Get the collection and the cursor
    $cursor = $mongo->selectCollection('m101', 'funnynumbers')->find()->sort(array(
        'value' => 1
    ))->skip($n)->limit(1);

    foreach ($cursor as $item) {
        print $item['value'] . "\n";
    }

} catch (Exception $e) {
    print sprintf('[EXCEPTION] %s', $e->getMessage());
}