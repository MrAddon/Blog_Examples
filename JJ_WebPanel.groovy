import com.atlassian.jira.component.ComponentAccessor

import com.atlassian.jira.issue.Issue

 

String TimeHHMM (def timeInSeconds)

'{

    timeInSeconds = (long) timeInSeconds

   

    def hoursRest = (timeInSeconds / (3600))

    def hours = hoursRest as Integer

    def minutesRest = ((timeInSeconds % 3600)/60)

    def minutes = minutesRest as Integer

    def valueToShow = "           "  + (hours as Integer).toString() +"h "+ (minutes as Integer).toString() + "m"  

    return valueToShow

'}


def issue = context.issue as Issue

 

def html_table = "<table class='aui'><thead><tr><th>Type</th><th>Time</th></tr></thead><tbody>"


def customFieldManager = ComponentAccessor.getCustomFieldManager()

 

def cfModule = customFieldManager.getCustomFieldObjects(issue).find {it.name == "aggregate time spent"}

def valueToShow = "None"

 

if (issue.getCustomFieldValue(cfModule))

'{

    valueToShow = TimeHHMM(issue.getCustomFieldValue(cfModule))

    html_table = html_table + "<tr><td>" + "Aggregate Logged:" + "</td><td>" + valueToShow + "</td></tr>"

'}

 

html_table = html_table + "</tbody></table>"

 

writer.write(html_table)
