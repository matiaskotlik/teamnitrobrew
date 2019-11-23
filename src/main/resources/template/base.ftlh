<#macro page_head>
    <title>Tutor Time</title>
</#macro>

<#macro page_body>

</#macro>

<#macro scripts>
    <script src="js/vendor/modernizr-3.7.1.min.js"></script>
    <script src="https://js.pusher.com/4.1/pusher.min.js"></script>
    <script src="js/main.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/popper.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/stellar.js"></script>
    <script src="vendors/nice-select/js/jquery.nice-select.min.js"></script>
    <script src="vendors/isotope/imagesloaded.pkgd.min.js"></script>
    <script src="vendors/isotope/isotope-min.js"></script>
    <script src="vendors/owl-carousel/owl.carousel.min.js"></script>
    <script src="vendors/jquery-ui/jquery-ui.js"></script>
    <script src="js/jquery.ajaxchimp.min.js"></script>
    <script src="js/mail-script.js"></script>
    <script src="vendors/popup/jquery.magnific-popup.min.js"></script>
    <script src="vendors/swiper/js/swiper.min.js"></script>
    <script src="vendors/scroll/jquery.mCustomScrollbar.js"></script>
    <script src="js/theme.js"></script>
</#macro>

<#macro signin>
    <a href="#" class="main_btn" onclick="document.getElementById('id01').style.display='block'">
        Sign In/Sign Up
        <img src="img/next.png" alt="">
    </a>

    <div id="id01" class="modal">

        <form class="modal-content animate" action="/login" method="post">
            <div class="imgcontainer">
                <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
            </div>

            <div class="container">
                <label><b>Username</b></label>
                <input type="text" placeholder="Enter Username" name="uname" required style="font-family:Times">

                <label><b>Password</b></label>
                <input type="password" placeholder="Enter Password" name="psw" required style="font-family:Times">

                <button type="submit" onclick="document.getElementById('id01').style.display='block'">Login</button>
            </div>

            <div class="container" style="background-color:#f1f1f1">
                <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">
                    Cancel
                </button>
                <span class="psw">Forgot <a href="#">password?</a></span>
            </div>
        </form>
    </div>

    <script>
        // Get the modal
        var modal = document.getElementById('id01');

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    </script>
</#macro>

<#macro topmenu>
    <!--================ Offcanvus Menu Area =================-->
    <div class="side_menu">
        <div class="logo">
            <a href="index.html">
                <img src="img/tt-icon.png" alt="" height=200>
            </a>
        </div>
        <ul class="list menu-left">
            <li>
                <a href="/">Home</a>
            </li>
            <li>
                <a href="packages.html">My Profile</a>
            </li>
            <li>
                <a href="about-us.html">About Us</a>
            </li>
            <li>
                <a href="contact.html">Contact Us</a>
            </li>
        </ul>
    </div>
    <!--================ End Offcanvus Menu Area =================-->

    <!--================ Canvus Menu Area =================-->
    <div class="canvus_menu">
        <div class="container">
            <div class="toggle_icon" title="Menu Bar">
                <span></span>
            </div>
        </div>
    </div>
    <!--================ End Canvus Menu Area =================-->
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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css"/>

        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/styles.css">
        <link rel="icon" href="img/tt-icon.png" type="image/png">

        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="vendors/linericon/style.css">
        <link rel="stylesheet" href="css/font-awesome.min.css">
        <link rel="stylesheet" href="vendors/owl-carousel/owl.carousel.min.css">
        <link rel="stylesheet" href="vendors/nice-select/css/nice-select.css">
        <link rel="stylesheet" href="vendors/animate-css/animate.css">
        <link rel="stylesheet" href="vendors/jquery-ui/jquery-ui.css">
        <link rel="stylesheet" href="vendors/popup/magnific-popup.css">
        <link rel="stylesheet" href="vendors/swiper/css/swiper.min.css">
        <link rel="stylesheet" href="vendors/scroll/jquery.mCustomScrollbar.css">
        <link rel="stylesheet" href="css/style.css">

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
        <@signin/>
        <@page_body/>
    </div>

    <!-- JS -->
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