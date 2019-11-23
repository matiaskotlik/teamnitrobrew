<#include 'base.ftlh'>

<#macro page_body>
<section class="home-banner-area common-banner relative">
    <div class="container-fluid">
        <div class="row d-flex align-items-center justify-content-center">
            <div class="header-right col-lg-6 col-md-6">
                <h1>
                   ${ user.fullname }
                </h1>
                <p class="pt-20">
                   ${ user.desc }
                </p>
                <#if account?? && account.id == user.id>
                    <p class="pt-20">Cash Earned: ${ "$" }${ user.funds?string.currency }</p>
                </#if>
                <div class="page-link-wrap">
                    <div class="page_link">
                        <a href="/">Learn</a>
                    </div>
                </div>
                <div class="page-link-wrap">
                    <div class="page_link">
                        <a href="/">Teach</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 col-md-6 header-left">
                <div class="">
                    <img class="img-fluid w-50" src="/img/${ user.img }" alt="">
                </div>
            </div>
        </div>
    </div>
</section>
<!--================ End banner section =================-->

<!--================ Start Amenities Area =================-->
<#list user.blogs>
    <section class="amenities-area section_gap_bottom">
        <div class="container">
            <div class="row align-items-end justify-content-left">
                <div class="col-lg-5">
                    <div class="main_title">
                        <h1>Recently<br>Practicing</h1>
                        <p>The subjects that you've been working on the most this week.</p>
                    </div>
                </div>
            </div>
            <div class="row justify-content-center">
                <#items as blog>
                    <!-- single-blog -->
                    <div class="col-lg-4 col-md-6 col-sm-6">
                        <div class="single-amenities">
                            <div class="amenities-thumb">
                                <img class="img-fluid" height = 100 src="/img/${ blog.img }" alt="">
                            </div>
                            <div class="amenities-details">
                                <div class="amenities-meta">
                                    <span>${ blog.meta }</span>
                                </div>
                                <h5><a href="#">${ blog.title }</a></h5>
                                <p>${ blog.text }</p>
                            </div>
                        </div>
                    </div>
                </#items>
            </div>
        </divRachel Yao
>
    </section>
<#else>
</#list>
</#macro>

<@display_page/>