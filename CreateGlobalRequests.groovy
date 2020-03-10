import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.json.JsonOutput
import groovy.json.JsonBuilder
import groovy.transform.BaseScript
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.customfields.option.Option
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.user.ApplicationUser


@BaseScript CustomEndpointDelegate delegate
def projectManager = ComponentAccessor.getProjectManager()
def linkManager = ComponentAccessor.getIssueLinkManager()
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def optionsManager = ComponentAccessor.getOptionsManager()
def issueFactory = ComponentAccessor.getIssueFactory()
def issueManager = ComponentAccessor.getIssueManager()

CreateGlobalRequests(httpMethod: "GET") { MultivaluedMap queryParams ->
   //  the details of getting and modifying the current issue are ommitted for brevity
    def issueId = queryParams.getFirst("issueId") as String
    def issue = issueManager.getIssueObject(issueId)
    log.info ("Issue: " + issue)
    def cf = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("CustomField")
    def cfConfig = cf.getRelevantConfig(issue)
    def option = ComponentAccessor.optionsManager.getOptions(cfConfig)

    def currentUserObj = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
    def assignee = currentUserObj
    def message = issue
    option.each
    {
                 // create new issue from factory
                 MutableIssue newissue
                 newissue = issueFactory.getIssue()

                 //set values
                 newissue.setProjectObject(issue.projectObject)
                 newissue.setIssueTypeId(issue.getIssueTypeId())
                 newissue.setSummary("[" + it.toString() + "] " + issue.summary)
                 newissue.setDescription(issue.getDescription())
                 newissue.reporter = issue.getReporter()
                 newissue.assignee = (ApplicationUser)assignee   
                 //create actual issue
                 def subTask = issueManager.createIssueObject(currentUserObj, newissue)
                 //link the issue
                 linkManager.createIssueLink (newissue.id, issue.id, Long.parseLong("10300"),Long.valueOf(1), currentUserObj)
                 message =  newissue.getKey() + " " + message
        }
                def flag = [
               type : 'success',
               title: "Created global request",
               close: 'auto',
               body :"The following issues " + message + " Has been created."
        ]

                               
         Response.ok(JsonOutput.toJson(flag)).build()       
}
