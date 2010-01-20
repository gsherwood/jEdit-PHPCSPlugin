PHPCSPlugin
===========

A jEdit plugin for [PHP_CodeSniffer][1] to display errors and warnings while
editing your files.

Best implemented by binding a keyboard shortcut to one of the included
actions *Check Current Buffer* and *Check All Buffers*.

Requirements
------------

- Locally installed PHP_CodeSniffer version 1.2.0 or greater
- jEdit ErrorList plugin version 1.7 or greater

Installation
------------

Grab the JAR file from the **PHPCS/dist** directory and place it in
your **.jedit/jars** directory.

Restart jEdit and go to *Plugin Options* and select *PHP_CodeSniffer*. You need to
give it the path to *phpcs* on your machine and the name of the coding standard you
want to use.

Now you can go to the menu item *Plugins > PHP_CodeSniffer > Check Current Buffer*
(or Check All Buffers) to have errors and warnings displayed in the gutter and
error list.

[1]: http://pear.php.net/package/PHP_CodeSniffer