<#include 'base.ftlh'>

<#macro page_body>
    <section class="home-banner-area relative">
        <div class="container-fluid">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="header-right col-lg-6 col-md-6">
                    <h1>
                        <#if type == "tutor">
                            Teach a student:
                        <#else>
                            Learn from a tutor:
                        </#if>
                        <br>
                    </h1>
                    <form action="/videocall" method="post">
                        <div class="form-container">
                            <div>
                                <label for="subject">Choose a Subject:</label>
                                <select id="subject" name="subject" class="myList">
                                    <option value="math">Math</option>
                                    <option value="science">Science</option>
                                    <option value="cs">Computer Science</option>
                                    <option value="english">English</option>
                                    <option value="language">Foreign Language</option>
                                </select>
                            </div>
                            <div>
                                <label for="time">How much time:</label>
                                <select id="time" name="time" class="myList">
                                    <option value="5">5 min</option>
                                    <option value="15">15 min</option>
                                    <option value="30">30 min</option>
                                    <option value="45">45 min</option>
                                    <option value="60">1 hr</option>
                                </select>
                            </div>
                        </div>
                        <input type="hidden" name="type" value="${ type }">
                        <input type="submit" class="main_btn" value="Start video call">
                    </form>
                </div>
                <div class="col-lg-6 col-md-6 header-left">
                    <div>
                        <img src="img/vidcall-icon.png" height=500 alt="">
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<@display_page/>