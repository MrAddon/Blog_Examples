import com.atlassian.confluence.pages.Page
import com.atlassian.confluence.xhtml.api.MacroDefinition
import com.atlassian.confluence.xhtml.api.XhtmlContent
import com.atlassian.renderer.v2.RenderUtils
import com.atlassian.sal.api.component.ComponentLocator
import groovy.xml.MarkupBuilder
import java.sql.Timestamp
import org.apache.log4j.Logger
import static org.apache.log4j.Level.DEBUG
 
 
//def xhtmlContent = ComponentLocator.getComponent(XhtmlContent)
def entity = context.getEntity()
 
def lastUpdate = entity.getLastModificationDate()
 
def daysToAdd = 730 as int
def dateB = Calendar.getInstance()
 
dateB.setTime(lastUpdate)
dateB.add(Calendar.DATE, daysToAdd)
def revisionDate = dateB.getTime()
 
def today = new Timestamp((new Date()).time)
def dif =  revisionDate - today
 
def revisionDateToShow = revisionDate.format('yyyy-MM-01')
def lastUpdateToShow = lastUpdate.format('yyyy-MM-01')
 
double percentaje = (dif / daysToAdd)*100
def color
if (percentaje == 100)
{
                color = "green"
}
else
{
    if (percentaje < 90 && percentaje > 50)
    {
        color =  "green"
    }
    else
    {
        if (percentaje <= 50 && percentaje > 20)
        {
            color = "orange"
        }
        else
        {
            color = "red"
        }
    }
}
def html_table = "<table class='aui'><thead><tr><th>DESCRIPTION</th><th>INFO</th></tr></thead><tbody>"
html_table = html_table + "<tr><td> Revision date: </td><td> $revisionDateToShow </td></tr>" 
html_table = html_table + "<tr><td> Last update: </td><td>$lastUpdateToShow</td></tr>"
html_table = html_table + "<tr><td> Days due next revision are: </td><td> <b><font color='" + color +"'>$dif</b></td></tr>"
html_table = html_table + "<tr><td> Days % for next revision: </td><td> <b><font color='" + color +"'>$percentaje</b></td></tr>"
 
html_table = html_table + "</tbody></table>"
