<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
	<TR>
		<TD class=r-dotted-lg-buttom vAlign=top>
			<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
        		<TR><TD>
              	<TABLE border=0 cellPadding=0 cellSpacing=0 >
              		<TR bgColor=#f4f4f2>
                 		<TD bgColor=#c9c9c7 class=box-title
							title='<digi:trn key="aim:myTaskListOfTeamLeader">List of activities pending for approval</digi:trn>'>
								<digi:trn key="aim:myTaskList">My Tasks</digi:trn>
							</TD>
                    	<TD background="module/aim/images/corner-r.gif" 
							height=17 width=17></TD>
						</TR>
					</TABLE>
				</TD></TR>
				<logic:notEmpty name="myTasks" scope="session">
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<bean:size id="taskCount" name="myTasks" scope="session" />
					<c:if test="${taskCount == 0}">
						<digi:trn key="aim:emptyMyTaskList">No pending task</digi:trn>						
					</c:if>
					<c:if test="${taskCount != 0}">
						<digi:link href="/viewMyTask.do~showTask=showTask">	
						<c:out value="${taskCount}" />
						<c:choose>
							<c:when test="${taskCount == 1}">
								<digi:trn key="aim:myPendingTask">Task pending for approval</digi:trn>
							</c:when>
							<c:otherwise>
								<digi:trn key="aim:myPendingTasks">Tasks pending for approval</digi:trn>
							</c:otherwise>
						</c:choose>
						</digi:link>
					</c:if>
				</TD></TR>
				</logic:notEmpty>
				<logic:empty name="myTasks" scope="session">				
				<TR><TD bgColor=#ffffff class=box-border align=left>
					<digi:trn key="aim:emptyMyTaskList">No pending task</digi:trn>										
				</TD></TR>
				</logic:empty>
			</TABLE>	
		</TD>
	</TR>
</TABLE>
