<#include 'base.ftlh'>

<#macro page_body>
    <style>
        #rate-container {
            width: 100%;
            height: 500px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
    <section class="home-banner-area relative">
        <div id="rate-container" class="container-fluid">
            <form action="/rate" method="post">
                <div class="rate">
                    <input type="radio" id="star5" name="rate" value="5"/>
                    <label for="star5" title="text">5 stars</label>
                    <input type="radio" id="star4" name="rate" value="4"/>
                    <label for="star4" title="text">4 stars</label>
                    <input type="radio" id="star3" name="rate" value="3"/>
                    <label for="star3" title="text">3 stars</label>
                    <input type="radio" id="star2" name="rate" value="2"/>
                    <label for="star2" title="text">2 stars</label>
                    <input type="radio" id="star1" name="rate" value="1"/>
                    <label for="star1" title="text">1 star</label>
                </div>
                <input name="id" id="id" type="hidden" value="${id!0}">
                <input type="submit" value="Submit rating" class="page-link-wrap">
            </form>
        </div>
    </section>
</#macro>

<@display_page/>


