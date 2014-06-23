<%@page session="true" import="org.apache.catalina.Session"%>
<%
    String firstName = null;
    String lastName = null;
    String userPrincipal = null;
    IAS.Bean.User.LoggedInUserBean userBean = null;
    try {
        userPrincipal = request.getUserPrincipal().getName();
        //HttpSession _session = request.getSession(false);

        if (session != null) {
            userBean = (IAS.Bean.User.LoggedInUserBean) session.getAttribute("userBean");
            firstName = userBean.getFirstName();
            lastName = userBean.getLastName();
        }
    } catch (NullPointerException e) {
        //userPrincipal = null;
    }


%>

<script type="text/javascript">
    $(document).ready(function() {
        $("#jMenu").jMenu({
            ulWidth: 'auto',
            effects: {
                effectSpeedOpen: 300,
                effectTypeClose: 'slide'
            },
            animatedText: false
        });

    });
</script>
<%    String contextPath = request.getContextPath();
%>
<div id="menubar">
    <div id="menu">
        <ul id="jMenu">
            <li><a href="home"><img style="height: 20px; width: 20px;" src="<%=contextPath%>/images/home.gif"/></a>
            <li><a href="#">Inward</a>
                <ul>
                    <li><a href="<%=contextPath + "/main?action=createinward"%>">Create</a></li>
                    <li><a href="<%=contextPath + "/main?action=pendinginwards"%>">Pending</a></li>
                    <li><a href="<%=contextPath + "/main?action=searchinward"%>">Search</a></li>
                </ul>
            </li>
            <li><a href="<%=contextPath + "/main?action=searchsubscriber"%>">Subscriber</a></li>
            <li><a href="#">Subscription</a>
                <ul>
                    <li class=""><a tabindex="-1" href="<%=contextPath + "/main?action=prl"%>">Pl. Refer List</a></li>
                </ul>
            </li>
            <li><a href="#">Mailing / Back Issue List</a>
                <ul>
                    <li><a href="#">Mailing List</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=gml"%>">Generate/Print Mailing List</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=vml"%>">View/Print Mailing List</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Back Issue List</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=gbil"%>">Generate/Print Back Issue List</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=vbil"%>">View/Print Back Issue List</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Missing Claims</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=gmil"%>">Generate/Print Missing Issue</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=vmil"%>">View/Reprint Missing Issue</a></li>
                        </ul>
                    </li>                  
                </ul>
            </li>
            <li>
                <a href="#">Reminders</a>
                <ul>
                    <li><a tabindex="-1" href="<%=contextPath + "/main?action=sendReminders"%>">Send Reminders</a></li>
                    <li><a tabindex="-1" href="<%=contextPath + "/main?action=displayReminders"%>">Display/Resend Reminders</a></li>
                </ul>
            </li>

            <li><a href="#">Reports</a>
                <ul>
                    <li><a href="#">Master Data</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listJournals"%>">List of Journals</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listSubType"%>">List of Subscriber types</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listAgent"%>">List of Agent</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=journalRates"%>">Rates for Journals</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listprintOrder"%>">Print Order</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Subscriber/ Subscription</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listSubscriber"%>">List of Subscribers</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listprofarmaInvoice"%>">List of Invoice</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listBackissue"%>">List of back issue sent</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listReminder"%>">List of  renewal reminder sent</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listmailinglist"%>">List of mailing list sent</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Others</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=statement"%>">Statement of Label</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=circulationFigure"%>">Circulation Figures for journals</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=subscriptionFigures"%>">Subscription Figures</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=subscriptionFiguresLegacy"%>">Subscription Figures Legacy</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listInwards"%>">Inwards</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=outstandingbill"%>">Outstanding Bill</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=listGml"%>">Mailing List</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li><a href="#">Master Data</a>
                <ul>
                    <li><a href="#">Country</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addCountry"%>">Add Country</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchCountry"%>">Search Country</a></li>
                        </ul>
                    </li>
                    <li><a href="#">State</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addState"%>">Add State</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchstate"%>">Search State</a></li>
                        </ul>
                    </li>
                    <li><a href="#">District</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addDistrict"%>">Add District</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchDistrict"%>">Search District</a></li>
                        </ul>
                    </li>
                    <li><a href="#">City</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addCity"%>">Add City</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchCity"%>">Search City</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Subscriber</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addSubType"%>">Add New Subscriber Type</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchSubType"%>">Search Subscriber Type</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Journals</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addJournal"%>">Add Journal</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchJournal"%>">Search Journal</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=jsGroup"%>">Journal Group</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Annual Information</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=journalDetails"%>">Journal Details</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=journalVolumeDetails"%>">Volume Information</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=subRate"%>">Annual Subscription Rate</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=printOrder"%>">Print Order</a></li>
                        </ul>
                    </li>
                    <li><a href="#">Agents</a>
                        <ul>
                            <li><a tabindex="-1" href="<%=contextPath + "/addMasterData?action=addAgent"%>">Add Agent</a></li>
                            <li><a tabindex="-1" href="<%=contextPath + "/main?action=searchAgent"%>">Search Agent</a></li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li><a href="<%=contextPath + "/main?action=sendBulkEmails"%>">Send Bulk Emails</a></li>
            <li><a href="<%=contextPath + "/main?action=templates"%>">Templates</a></li>
            <li><a href="<%=contextPath + "/main?action=help"%>">Help</a></li>            
                <%
                    if (userPrincipal != null && userBean != null) {
                        out.println("<li><a>");
                        out.println(firstName + " " + lastName + "!!!");
                        out.println("</a><ul>");
                        out.println("<li>");
                        out.println("<a href=\"" + request.getContextPath() + "/main2/logout/do\"" + "><font color=\"white\">Logout</font></a>");
                        out.println("</li>");
                        out.println("</ul>");
                        out.println("</li>");
                    }
                %>
            </span></li>
        </ul>
    </div>
</div>

