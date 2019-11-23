
<#include 'base.ftlh'>

<#macro page_body>
    <section class="home-banner-area relative">
        <div class="container-fluid">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="header-right col-lg-6 col-md-6">
                    <h1 style="color:rgb(144,175,119)">
                        Tutor Time <br>
                    </h1>
                        <p style="color:rgb(144,175,119)" class="pt-20">
                            Struggling in school? Get convenient, instant access to our active online tutors!
                            Or, sign up to become a tutor today!
                        </p>
                    <a href="tutor?type=tutee" class="main_btn">
                        Tutee
                    </a>
                    <a href="tutor?type=tutor" class="main_btn">
                        Tutor

                    </a>
                </div>

                <div class="col-lg-6 col-md-6 header-left">
                    <div class="">
                        <img class="img-fluid w-100" src="/img/tt-icon.png" alt="">
                    </div>
                    <div class="video-popup d-flex align-items-center">
                        <a class="play-video video-play-button animate" href="https://www.youtube.com/watch?v=XiNR1YaAoZU" data-animate="zoomIn"
                           data-duration="1.5s" data-delay="0.1s">
                            <span></span>
                        </a>
                        <div class="watch">

                            <h5 style="color:rgb(144,175,119)" ">Welcome to Tutor Time!</h5>
                                <p style="color:rgb(144,175,119)"">An introduction to our website</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<@display_page/>


