import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.comments.CommentManager
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.project.ProjectManager
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.index.IndexException
import com.atlassian.jira.user.ApplicationUser
import org.apache.log4j.Category
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.link.IssueLink;

def issueLinkManager = ComponentAccessor.getIssueLinkManager()
def issueManager = ComponentAccessor.getIssueManager()
def totalTimeSpent = issue.timeSpent

//all insidelinks
List allOutIssueLink = issueLinkManager.getInwardLinks(issue.id)
for (Iterator outIterator = allOutIssueLink.iterator(); outIterator.hasNext();) {
IssueLink issueLink = (IssueLink) outIterator.next();
def linkedIssue = issueLink.getSourceObject();

if (linkedIssue.getIssueType().name =="Rework")
{
def linkedIssueTimeSpent = linkedIssue.timeSpent
if(linkedIssueTimeSpent) {totalTimeSpent += linkedIssueTimeSpent}
}
}

///all outsidelinks
allOutIssueLink = issueLinkManager.getOutwardLinks(issue.id)
for (Iterator outIterator = allOutIssueLink.iterator(); outIterator.hasNext();) {
IssueLink issueLink = (IssueLink) outIterator.next();
def linkedIssue = issueLink.getSourceObject();

if (linkedIssue.getIssueType().name =="Rework")
{
def linkedIssueTimeSpent = linkedIssue.timeSpent
if(linkedIssueTimeSpent) {totalTimeSpent += linkedIssueTimeSpent}
}
}

if(totalTimeSpent > 0) {
totalTimeSpent = totalTimeSpent
return (long)totalTimeSpent

}
else{
null
}
