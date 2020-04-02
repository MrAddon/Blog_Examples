import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import org.apache.log4j.Level
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.history.ChangeItemBean
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.IssueInputParameters
import com.atlassian.jira.workflow.TransitionOptions
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.issue.CustomFieldManager
import com.atlassian.jira.issue.fields.CustomField
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.plugin.webfragment.model.JiraHelper

// Get a pointer to the current logged in user
def currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()

 

//current issue
def issue = issue as Issue
log.info("issue : " + issue)

//current status of issue
def issueStatus = issue.getStatus().name
log.info("Issue Status: " + issue)

 

//log.warn("Show User:" + CurrentUser)



def customFieldManager = ComponentAccessor.getCustomFieldManager()
def CommentManager commentManager = ComponentAccessor.getCommentManager()
def cField = customFieldManager.getCustomFieldObjectByName("Progress")
def cFieldValue = issue.getCustomFieldValue(cField).toString()
log.warn("Show cField: " + cField + " Show value: " + cFieldValue)

 

def transition = 0

switch (issueStatus)

{
    case "To Do":
    if ((cFieldValue.toString() == "25") || (cFieldValue.toString() == "50")|| (cFieldValue.toString() == "75")|| (cFieldValue.toString() == "90"))
    {
        transition = 51
    }
    break;

    case "In Progress":
    if (cFieldValue.toString() == "0")
    {
         transition = 91
    }
    if (cFieldValue.toString() == "100")
    {
         transition = 61
    }
    break;

    default:
    break;

}

log.warn ("Transition to be done: " + transition.toString())

if (transition != 0)
{

   //this part of the code do the transition
    IssueService issueService = ComponentAccessor.getIssueService()
    IssueInputParameters issueInputParameters = issueService.newIssueInputParameters()

    def transitionOptions = new TransitionOptions.Builder()
    .skipConditions()
    .skipPermissions()
    .skipValidators()
    .build()


    IssueService.TransitionValidationResult validationResult = issueService.validateTransition(currentUser, issue.id, transition as Integer, issueInputParameters,transitionOptions)
    def errorCollection = validationResult.errorCollection
    if (!errorCollection.hasAnyErrors()) {
        issueService.transition(currentUser, validationResult)
    }  
    else
    {
        log.error(errorCollection)
    }   

    

}
