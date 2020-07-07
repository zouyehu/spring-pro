<?xml version="1.0" encoding="UTF-8"?>
<xml>
    <userList>
        <user>
            <userName>${userName!}</userName>
            <passWord>${passWord!}</passWord>
            <realName>${realName!}</realName>
            <age>${age!}</age>
            <addr>${addr!}</addr>
        </user>
        <students>
            <#list students as st>
                <student id="${st.id}" name="${st.name}" gender="${st.gender}" address="${st.address}" upDate="${st.upDate}"/>
            </#list>
        </students>
    </userList>
</xml>