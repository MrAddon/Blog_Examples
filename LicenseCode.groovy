import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.user.util.UserUtil
import com.atlassian.jira.event.AbstractEvent
import com.atlassian.crowd.event.user.UserCreatedEvent
import com.atlassian.jira.application.ApplicationAuthorizationService
import com.atlassian.jira.application.ApplicationKeys


UserUtil userUtil = ComponentAccessor.getUserUtil()

int licenses = 0

def CurrentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
def applicationAuthorizationService = ComponentAccessor.getComponent(ApplicationAuthorizationService)

 
def total = 0
def output
def lstjson = []

 
userUtil.getUsers().each{ u ->
    if ((u.active) && (applicationAuthorizationService.canUseApplication(u, ApplicationKeys.SERVICE_DESK)))
    {
        //get the groups of the user
        def groupManager = ComponentAccessor.getGroupManager()
        def groupsNamesForUser = groupManager.getGroupNamesForUser(u)

        output='''{
        "user": "'''+u.emailAddress+'''",
        "displayName": "'''+u.displayName+'''"
        }'''

        licenses++
        lstjson.add(output)
    }
    def json= '''{ "items": '''+ lstjson +''' }'''
    log.warn("json"+json)

  File file = new File("//yourServerPath//licenses.json")
  file.write(json)
}

return licenses
