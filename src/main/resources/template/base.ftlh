<#macro page_head>
    <title>Base Title</title>
</#macro>

<#macro page_body>
    <p>Base Body</p>
</#macro>

<#macro display_page>
    <!doctype html>
    <html class="no-js" lang="">

    <head>
        <meta charset="utf-8">

        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="manifest" href="site.webmanifest">
        <link rel="apple-touch-icon" href="icon.png">

        <link href="css/material-icons.css" rel="stylesheet">

        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/styles.css">

        <meta name="theme-color" content="#fafafa">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

        <@page_head/>
    </head>

    <body>
    <!--[if IE]>
    <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a
            href="https://browsehappy.com/">upgrade
        your browser</a> to improve your experience and security.</p>
    <![endif]-->

    <!-- Content -->
    <div class="content">
        <@page_body/>
    </div>

    <!-- JS -->
    <script src="js/vendor/modernizr-3.7.1.min.js"></script>
    <script src="js/vendor/jquery-3.3.1.min.js"></script>
    <script src="https://js.pusher.com/4.1/pusher.min.js"></script>
    <script src="js/main.js"></script>

    <@scripts/>

    <!-- Google Analytics: change UA-XXXXX-Y to be your site's ID. -->
    <!--  <script>-->
    <!--    window.ga = function () { ga.q.push(arguments) }; ga.q = []; ga.l = +new Date;-->
    <!--    ga('create', 'UA-XXXXX-Y', 'auto'); ga('set','transport','beacon'); ga('send', 'pageview')-->
    <!--  </script>-->
    <!--  <script src="https://www.google-analytics.com/analytics.js" async defer></script>-->
    </body>

    </html>
</#macro>