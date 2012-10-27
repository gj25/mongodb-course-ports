<?php

/**
 * This is a simple static router for PHP 5.4+ built in server
 */

if (php_sapi_name() !== 'cli-server') {
    throw new Exception('This router only for php 5.4.* built in server.');
}

if (!$route = $_SERVER['SCRIPT_NAME']) {
    throw new Exception('Server script name not found.');
}

$routes = array(
    '/^\/hw1\/(\d+)$/' => array(
        'file' => 'hw1-3.php',
        'arguments' => array(1 => 'n')
    )
);

foreach ($routes as $pattern => $rule) {
    if (preg_match($pattern, $route, $matches)) {

        foreach ($rule['arguments'] as $k => $arg) {
            if (isset($matches[$k])) {
                ${$arg} = $matches[$k];
                require __DIR__ . "/{$rule['file']}";
            }
        }

        break;
    }
}