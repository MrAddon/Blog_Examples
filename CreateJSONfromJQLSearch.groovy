import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.issue.search.SearchQuery
import groovy.json.JsonSlurper
import groovy.json.*
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
def searchService = ComponentAccessor.getComponent(SearchService)
def jqlSearch = "project=IESD and resolution = unresolved"
def query = jqlQueryParser.parseQuery(jqlSearch)

//note different order of parameters compared to searchprovider
def resultsPair = searchService .search(user,query, PagerFilter.getUnlimitedFilter())
def customFieldManager = ComponentAccessor.getCustomFieldManager()
def issueManager = ComponentAccessor.getIssueManager()
def results = resultsPair.getResults()
def count = 0
def output
def lstjson=[]
results.each()
{
    documentIssue ->
    def issue = issueManager.getIssueObject(documentIssue.id)
    def cfEmdepSite = customFieldManager.getCustomFieldObjects(issue).find {it.name == "YourCustomField"}
    def emdepSiteValue = issue.getCustomFieldValue(cfEmdepSite)
    if (emdepSiteValue)
    {
        // def jsonItem = issue.toString() + ";"+ issue.reporter.toString() +";"+ emdepSiteValue.toString() + ";" + officeADValue.toString()
        output='''{
        "issue": "'''+issue.toString()+'''",
        "user": "'''+issue.reporter.toString()+'''",
        "emdepsite": "'''+emdepSiteValue.toString()+'''"
        }'''
        lstjson.add(output)
    }
    count++
}

def json= '''{
"items": '''+lstjson+'''
}'''
log.warn("json"+json)

File file = new File("//YourServerPath//output.json")
file.write(json)
