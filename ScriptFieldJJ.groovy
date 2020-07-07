import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.link.IssueLink

def issueLinkManager = ComponentAccessor.getIssueLinkManager()
def issueManager = ComponentAccessor.getIssueManager()

def customFieldManager = ComponentAccessor.getCustomFieldManager()
def cfWorkOrder = customFieldManager.getCustomFieldObjectByName("Work Order")
def workOrderValue = "Required WO pending from commercial"  

def cfEscalationType = customFieldManager.getCustomFieldObjectByName("Escalation type")
def escalationTypeValue = issue.getCustomFieldValue(cfEscalationType)

if (escalationTypeValue.toString().toLowerCase() == "change")
{
    //all insidelinks
    List<IssueLink> allOutIssueLink = issueLinkManager.getInwardLinks(issue.id)
    for (Iterator<IssueLink> outIterator = allOutIssueLink.iterator(); outIterator.hasNext();) {
        IssueLink issueLink = (IssueLink) outIterator.next();
        def linkedIssue = issueLink.getSourceObject();
        if (linkedIssue.getIssueType().name =="Commercial Analysis")
        {
            workOrderValue = linkedIssue.getCustomFieldValue(cfWorkOrder)
        }
    }
    return workOrderValue
}
else
{
    return null
}
