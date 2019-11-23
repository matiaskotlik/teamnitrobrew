<#include 'base.ftlh'>

<#macro page_body>
    <section class="home-banner-area common-banner relative">
        <div class="container-fluid">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="header-right col-lg-6 col-md-6">
                    <h1>
                        Contact Us!
                    </h1>
                    <p class="pt-20">
                        For any FAQs or concerns about payment, contact us below!
                    </p>
                    <div class="page-link-wrap">
                        <div class="page_link">
                            <a href="/">Home</a>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6 col-md-6 header-left">
                    <div class="">
                        <img src="/img/contact-icon.png" height=500 alt="">
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--================ End banner section =================-->

    <!--================Contact Area =================-->
    <section class="contact_area section_gap">
        <div class="container">
            <div class="row">
                <div class="col-lg-3">
                    <div class="contact_info">
                        <div class="info_item">
                            <i class="lnr lnr-home"></i>
                            <h6>Illinois, United States</h6>
                            <p>Naperville</p>
                        </div>
                        <div class="info_item">
                            <i class="lnr lnr-phone-handset"></i>
                            <h6><a href="#">555-555-5555</a></h6>
                            <p>Mon to Fri 9am to 9pm</p>
                        </div>
                        <div class="info_item">
                            <i class="lnr lnr-envelope"></i>
                            <h6><a href="#">support@tutortime.com</a></h6>
                            <p>Contact us anytime!</p>
                        </div>
                    </div>
                </div>
                <div class="col-lg-9">
                    <form class="row contact_form" id="contactForm"
                        novalidate="novalidate">
                        <div class="col-md-6">
                            <div class="form-group">
                                <input type="text" class="form-control" id="name" name="name" placeholder="Enter your name">
                            </div>
                            <div class="form-group">
                                <input type="email" class="form-control" id="email" name="email" placeholder="Enter email address">
                            </div>
                            <div class="form-group">
                                <input type="text" class="form-control" id="subject" name="subject" placeholder="Enter Subject">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <textarea class="form-control" name="message" id="message" rows="1" placeholder="Enter Message"></textarea>
                            </div>
                        </div>
                        <div class="col-md-12 text-right">
                            <button type="submit" value="submit" class="main_btn">
                                Send Message
                                <img src="/img/next.png" alt="">
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</#macro>

<@display_page/>