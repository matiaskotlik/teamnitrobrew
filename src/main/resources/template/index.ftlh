
<#include 'base.ftlh'>

<#macro page_body>
    <section class="home-banner-area relative">
        <div class="container-fluid">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="header-right col-lg-6 col-md-6">
                    <h1 style="color:green">
                        Tutor Time <br>
                    </h1>
                    <font color=”rgb(164,199,149)”>
                        <p class="pt-20">
                            Struggling in school? Get convenient, instant access to our active online tutors!
                            Or, sign up to become a tutor today!
                        </p>
                    </font>
                    <a href="tutee.html" class="main_btn">
                        Tutee
                    </a>
                    <a href="tutor.html" class="main_btn">
                        Tutor

                    </a>
                </div>

                <div class="col-lg-6 col-md-6 header-left">
                    <div class="">
                        <img class="img-fluid w-100" src="img/tt-icon.png" alt="">
                    </div>
                    <div class="video-popup d-flex align-items-center">
                        <a class="play-video video-play-button animate" href="https://www.youtube.com/watch?v=XiNR1YaAoZU" data-animate="zoomIn"
                           data-duration="1.5s" data-delay="0.1s">
                            <span></span>
                        </a>
                        <div class="watch">

                            <h5 style="color:green;">Welcome to Tutor Time!</h5>
                            <font color=”rgb(164,199,149)”>
                                <p>An introduction to our website</p>
                            </font>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<@display_page/>


