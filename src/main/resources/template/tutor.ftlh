<#include 'base.ftlh'>

<#macro page_body>
    <section class="home-banner-area relative">
        <div class="container-fluid">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="header-right col-lg-6 col-md-6">
                    <h1>
                        <#if type == "tutor">
                            Teach a tutee:
                        <#else>
                            Learn from a tutor:
                        </#if>
                        <br>
                    </h1>
                    <form>
                        <fieldset>
                            <label for="subject">Choose a Subject:</label>
                            <select id="subject" name="subject" class="myList">
                                <option value="math">Math</option>
                                <option value="science">Science</option>
                                <option value="cs">Computer Science</option>
                                <option value="english">English</option>
                                <option value="language">Foreign Language</option>
                            </select>
                            <label for="time">How much time:</label>
                            <select id="time" name="time" class="myList">
                                <option value="1">5 min</option>
                                <option value="2">15 min</option>
                                <option value="3">30 min</option>
                                <option value="4">45 min</option>
                                <option value="5">1 hr</option>
                            </select>
                        </fieldset>
                        <input type="hidden" name="type" value="{{ type }}">
                        <input type="submit" id="videocall" class="main_btn">
                        Video Call
                        </input>
                    </form>
                </div>

                <div class="col-lg-6 col-md-6 header-left">
                    <div class="">
                        <img src="img/vidcall-icon.png" height=500 alt="">
                    </div>
                    <p></p>

                </div>
            </div>
        </div>
    </section>
</#macro>
