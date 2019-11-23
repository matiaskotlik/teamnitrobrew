
<#include 'base.ftlh'>

<#macro page_head>
    <title>Team Nitrobrew</title>
</#macro>

<#macro page_body>
    <form action="/search">
        <select name="subject" id="subject">
            <option value="math">Math</option>
            <option value="english">English</option>
            <option value="chemistry">Chemistry</option>
            <option value="biology">Biology</option>
            <option value="physics">Physics</option>
            <option value="spanish">Spanish</option>
        </select>
        <input type="radio" name="type" value="tutee" checked>Tutee<br>
        <input type="radio" name="type" value="tutor">Tutor<br>
    </form>
</#macro>

<@display_page/>


