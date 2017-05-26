package scrappers;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.jsoup.nodes.Element;

public class Table extends AbstractTableModel
{
	private Element table ;
	private boolean hasHead,hasBody,hasFoot;
	
	public static Table parse(Element table)
	{
		return new Table(table);
	}
	
	private Table(Element table) 
	{
		super();
		this.table = table ;
		if(!this.table.select("thead").isEmpty())
			this.hasHead = true;
		if(!this.table.select("tbody").isEmpty())
			this.hasBody = true;
		if(!this.table.select("tfoot").isEmpty())
			this.hasFoot=true;
	}
	public Set<String> getHead()
	{
		Set<String> tableHead = new HashSet<String>();
		if(this.hasHead)
		{
			tableHead.add(this.table.select("thead").select("th").iterator().next().text());
		}
		return tableHead;
	}
	public Set<String> getBody()
	{
		Set<String> tableBody = new HashSet<String>();
		if(this.hasBody)
		{
			tableBody.add(this.table.select("tbody").select("td").iterator().next().text());
		}
		return tableBody;
	}
	public Set<String> getFoot()
	{
		Set<String> tableFoot = new HashSet<String>();
		if(this.hasFoot)
		{
			tableFoot.add(this.table.select("tfoot").select("td").iterator().next().text());
		}
		return tableFoot;
	}
	
	public Element getTable() {
		return table;
	}

	public void setTable(Element table) {
		this.table = table;
	}

	@Override
	public int getRowCount()
	{
		return this.table.select("tr").size();
	}
	@Override
	public int getColumnCount()
	{
		if(this.hasHead)
			return this.table.select("tr").first().select("th").size();
		return this.table.select("tr").first().select("td").size();
	}
	@Override
	public String getValueAt(int rowIndex, int columnIndex) 
	{
		if(this.hasHead&&rowIndex==0)
			return this.table.select("tr").get(rowIndex).select("th").get(columnIndex).text();
		return this.table.select("tr").get(rowIndex).select("td").get(columnIndex).text();
	}

	@Override
	public String toString() 
	{
		return "Table [hasHead=" + hasHead + ", hasBody=" + hasBody + ", hasFoot=" + hasFoot +
				", Rows Number " +getRowCount()+", Column number "+getColumnCount() + "]";
	}
	
	
	
}
