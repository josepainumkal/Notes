
import java.util.*;

public class Database {
	
  public static class Table {
    private final String name;
    private final String[] columnNames;
    private final List<List<Object>> data;

    public Table(String name, String[] columnNames, List<List<Object>> data) {
      this.name = name;
      this.columnNames = columnNames;
      this.data = data;
    }

    public void insert(Object[] row) {
      data.add(Arrays.asList(row));
    }

    public String getName() {
      return name;
    }

    public String[] getColumnNames() {
      return columnNames;
    }

    public List<List<Object>> getData() {
      return data;
    }

    public Table select(String[] projectedColumnNames) {
      // IMPLEMENT ME  
      List<List<Object>> temp_data = new ArrayList<List<Object>>();
      
      //get a set of indexes of only projectedColumnNames
      Set<Integer> proj_indexes = new HashSet<Integer>();    
      for(String proj_col: projectedColumnNames){
    	  proj_indexes.add((Integer)Arrays.asList(this.columnNames).indexOf(proj_col));
      }
      
      List<Object> temp_row = null;
      
      for(List<Object> row:this.getData()){
    	  temp_row = new ArrayList<Object>();
    	  //System.out.println("rowSize: "+row.size());

    	  for(String proj_col: projectedColumnNames){
    		  int p_index= Arrays.asList(this.columnNames).indexOf(proj_col);
    		  if(p_index<row.size()){
    			  temp_row.add(row.get(p_index));
    		  }else{
    			  temp_row.add("");
    		  }
    		  
    	  }
    	  
    	  temp_data.add(temp_row);
      }

      return new Table("Select", projectedColumnNames, temp_data);
    }

    public Table where(String columnName, Object value) {
     // IMPLEMENT ME
    	
     // filter this.data which satisfies where clause.
     List<List<Object>> temp_data =new ArrayList<List<Object>>();
     
     // get index of columnName
     int col_index = Arrays.asList(this.columnNames).indexOf(columnName); //LEARN
     //System.out.println(col_index);

     for(List<Object> row:this.getData()){
    	 if(row.get(col_index)==value){
    		 temp_data.add(row);
    	 }
     }
   
     return new Table("Where", this.columnNames, temp_data);
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer(String.join(", ", columnNames)).append("\n");
      for (List<Object> row : data) {
        if (row.size() != 0) {
          Object value = row.get(0);
          sb.append(value == null ? "" : value.toString());
          for (int i = 1; i < row.size(); i++) {
            value = row.get(i);
            sb.append(", ").append(value == null ? "" : value.toString());
          }
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }

  private final Map<String, Table> tableMap;

  public Database() {
    this.tableMap = new HashMap<String, Table>();
  }

  public void addTable(Table table) {
    this.tableMap.put(table.getName(), table);
  }

  public Table getTable(String tableName) {
    return tableMap.get(tableName);
  }

  public Table innerJoin(Table leftTable, String leftTableKeyName, Table rightTable, String rightTableKeyName) {
    // IMPLEMENT ME 
	List<List<Object>> temp_data =new ArrayList<List<Object>>();
	int indexLeftKey = Arrays.asList(leftTable.getColumnNames()).indexOf(leftTableKeyName);
	int indexRightKey = Arrays.asList(rightTable.getColumnNames()).indexOf(rightTableKeyName);
	StringBuilder leftColHead = null;
	StringBuilder rightColHead = null;
	List<String> colNames = new ArrayList();
	
	for(String s:leftTable.getColumnNames()){
		leftColHead = new StringBuilder(leftTable.getName());
		leftColHead.append(".").append(s);
		colNames.add(leftColHead.toString());
	}
	
	for(String s:rightTable.getColumnNames()){
		rightColHead = new StringBuilder(rightTable.getName());
		rightColHead.append(".").append(s);
		colNames.add(rightColHead.toString());
	}
	
	String[] colNamesFinal = new String[colNames.size()];
	colNamesFinal = colNames.toArray(colNamesFinal);
	
	
	List<Object> temp_row = null;
	for(List<Object> row: leftTable.getData()){
		temp_row = new ArrayList<Object>();
		int key = 0;
		
		for(Object r: row){
			//add elements of first table
			temp_row.add(r);
			if(row.indexOf(r)==indexLeftKey){
				key = (int)r;
			}
			
		}
		//System.out.println(temp_row.size());
		for(List<Object> rt_row:rightTable.getData()){
			if((int)rt_row.get(indexRightKey)==key){
				for(Object r:rt_row){
					temp_row.add(r);
				}
			}
		}
		
		temp_data.add(temp_row);
	}
		
    return new Table("InnerJoin", colNamesFinal, temp_data);
  }

  public Table leftJoin(Table leftTable, String leftTableKeyName, Table rightTable, String rightTableKeyName) {
    // IMPLEMENT ME
		List<List<Object>> temp_data =new ArrayList<List<Object>>();
		int indexLeftKey = Arrays.asList(leftTable.getColumnNames()).indexOf(leftTableKeyName);
		int indexRightKey = Arrays.asList(rightTable.getColumnNames()).indexOf(rightTableKeyName);
		StringBuilder leftColHead = null;
		StringBuilder rightColHead = null;
		List<String> colNames = new ArrayList();
		
		for(String s:leftTable.getColumnNames()){
			leftColHead = new StringBuilder(leftTable.getName());
			leftColHead.append(".").append(s);
			colNames.add(leftColHead.toString());
		}
		
		for(String s:rightTable.getColumnNames()){
			rightColHead = new StringBuilder(rightTable.getName());
			rightColHead.append(".").append(s);
			colNames.add(rightColHead.toString());
		}
		
		String[] colNamesFinal = new String[colNames.size()];
		colNamesFinal = colNames.toArray(colNamesFinal);
		
		List<Object> temp_row = null;
		for(List<Object> row: leftTable.getData()){
			temp_row = new ArrayList<Object>();
			int key = 0;
			
			int currentIndex = 0;
			
			for(Object r: row){
				temp_row.add(r);
				if (currentIndex == indexLeftKey){
					key = (int)r;
				}
				currentIndex++;
			}
			
			for(List<Object> rt_row:rightTable.getData()){
				if((int)rt_row.get(indexRightKey)==key){
					for(Object r:rt_row){
						temp_row.add(r);
					}
				}
			}

			temp_data.add(temp_row);
		}
		
    return new Table("LeftJoin", colNamesFinal, temp_data);
  }

  public Table rightJoin(Table leftTable, String leftTableKeyName, Table rightTable, String rightTableKeyName) {
    // IMPLEMENT ME
	    List<List<Object>> temp_data =new ArrayList<List<Object>>();
		int indexLeftKey = Arrays.asList(leftTable.getColumnNames()).indexOf(leftTableKeyName);
		int indexRightKey = Arrays.asList(rightTable.getColumnNames()).indexOf(rightTableKeyName);
		StringBuilder leftColHead = null;
		StringBuilder rightColHead = null;
		List<String> colNames = new ArrayList();
		
		for(String s:rightTable.getColumnNames()){
			rightColHead = new StringBuilder(rightTable.getName());
			rightColHead.append(".").append(s);
			colNames.add(rightColHead.toString());
		}
		
		for(String s:leftTable.getColumnNames()){
			leftColHead = new StringBuilder(leftTable.getName());
			leftColHead.append(".").append(s);
			colNames.add(leftColHead.toString());
		}
			
		String[] colNamesFinal = new String[colNames.size()];
		colNamesFinal = colNames.toArray(colNamesFinal);
		
		List<Object> temp_row = null;
		for(List<Object> row: rightTable.getData()){
			temp_row = new ArrayList<Object>();
			int key = 0;
			
			int current_index=0;
			for(Object r: row){
				temp_row.add(r);
				if(current_index==indexRightKey){
					key =(int)r;
				}
				current_index++;
			}
			
			boolean found=false;
			
			for(List<Object> rt_row:leftTable.getData()){
				if((int)rt_row.get(indexLeftKey)==key){
					for(Object r:rt_row){
						temp_row.add(r);
					}
					found=true;
					break;
				}
			}
			//System.out.println(found);
			if(!found){
				// adjusting free spaces
				for(Object r: leftTable.getData().get(0)){
					//System.out.println("Jose");
					temp_row.add("");
				}
			}
		
			temp_data.add(temp_row);
		}	  
    return new Table("RightJoin", colNamesFinal, temp_data);
  }

  public Table outerJoin(Table leftTable, String leftTableKeyName, Table rightTable, String rightTableKeyName) {
    // IMPLEMENT ME
	    List<List<Object>> temp_data =new ArrayList<List<Object>>();
		int indexLeftKey = Arrays.asList(leftTable.getColumnNames()).indexOf(leftTableKeyName);
		int indexRightKey = Arrays.asList(rightTable.getColumnNames()).indexOf(rightTableKeyName);
		StringBuilder leftColHead = null;
		StringBuilder rightColHead = null;
		List<String> colNames = new ArrayList();
		
		for(String s:rightTable.getColumnNames()){
			rightColHead = new StringBuilder(rightTable.getName());
			rightColHead.append(".").append(s);
			colNames.add(rightColHead.toString());
		}
		
		for(String s:leftTable.getColumnNames()){
			leftColHead = new StringBuilder(leftTable.getName());
			leftColHead.append(".").append(s);
			colNames.add(leftColHead.toString());
		}
			
		String[] colNamesFinal = new String[colNames.size()];
		colNamesFinal = colNames.toArray(colNamesFinal);
		
		List<Object> temp_row = null;
		int key=0;
		for(List<Object> row: rightTable.getData()){
			temp_row = new ArrayList<Object>();
			int current_index=0;
			for(Object r: row){
				temp_row.add(r);
				if(current_index == indexRightKey){
					key = (int)r;
				}
				current_index++;
			}
			
			boolean found =false;
			for(List<Object> lrow: leftTable.getData()){
				if((int)lrow.get(indexLeftKey)==key){
					for(Object r:lrow){
						temp_row.add(r);
					}	
					found=true;
					break;
				}
			}
			if(!found){
				// adjusting free spaces
				for(Object r: leftTable.getData().get(0)){
					//System.out.println("Jose");
					temp_row.add("");
				}
			}
			temp_data.add(temp_row);
		}
	  
		
		boolean found=false;
	    for(List<Object> lrow: leftTable.getData()){
	    	found=false;
	    	temp_row = new ArrayList<Object>();
	    
	    	for(List<Object> rrow: rightTable.getData()){
	    		int key_right = (int)rrow.get(indexRightKey);
	    		if(key_right==(int)lrow.get(indexLeftKey)){
	    			found=true;
	    			break;
	    		}
			}
	    	
	    	if(!found){
	    		// adjusting free spaces
				for(Object r: rightTable.getData().get(0)){
					temp_row.add("");
				}
				
	    		for(Object r: lrow){
	    			temp_row .add(r);
	    		}
	    		//System.out.println(temp_row);
	    		temp_data.add(temp_row);
	    	}	
		}

	  //System.out.println(temp_data);
    return new Table("OuterJoin", colNamesFinal, temp_data);
  }

  public static void main(String[] args) {
    Table departmentTable = new Table("departments", new String[]{"id", "name"}, new ArrayList<List<Object>>());
    departmentTable.insert(new Object[] {0, "engineering"});
    departmentTable.insert(new Object[] {1, "finance"});

    Table userTable = new Table("users", new String[]{"id", "department_id", "name"}, new ArrayList<List<Object>>());
    userTable.insert(new Object[] {0, 0, "Ian"});
    userTable.insert(new Object[] {1, 0, "John"});
    userTable.insert(new Object[] {2, 1, "Eddie"});
    userTable.insert(new Object[] {3, 1, "Mark"});

    Table salaryTable = new Table("salaries", new String[]{"id", "user_id", "amount"}, new ArrayList<List<Object>>());
    salaryTable.insert(new Object[] {0, 0, 100});
    salaryTable.insert(new Object[] {1, 1, 150});
    salaryTable.insert(new Object[] {2, 1, 200});
    salaryTable.insert(new Object[] {3, 3, 200});
    salaryTable.insert(new Object[] {4, 3, 300});
    salaryTable.insert(new Object[] {5, 4, 400});

    Database db = new Database();
    db.addTable(departmentTable);
    db.addTable(userTable);
    db.addTable(salaryTable);

    // should print
    // id, department_id, name
    // 1, 0, John
    System.out.println(db.getTable("users").where("id", 1).select(new String[] {"id", "department_id", "name"}));

    // should print
    // users.name, departments.name
    // Ian, engineering
    // John, engineering
    System.out.println(
        db.innerJoin(db.getTable("users"), "department_id", db.getTable("departments"), "id")
            .where("departments.name", "engineering")
            .select(new String[]{"users.name", "departments.name"}));

    // should print
    // users.name, salaries.amount
    // Ian, 100
    // John, 150
    // John, 200
    // Mark, 200
    // Mark, 300
    // Eddie,
    System.out.println(
        db.leftJoin(db.getTable("users"), "id", db.getTable("salaries"), "user_id")
            .select(new String[]{"users.name", "salaries.amount"}));

    // should print
    // users.name, salaries.amount
    // Ian, 100
    // John, 150
    // John, 200
    // Mark, 200
    // Mark, 300
    //     , 400
//    System.out.println(
//            db.rightJoin(db.getTable("users"), "id", db.getTable("salaries"), "user_id"));
//                
    System.out.println(
        db.rightJoin(db.getTable("users"), "id", db.getTable("salaries"), "user_id")
            .select(new String[]{"users.name", "salaries.amount"}));

    // should print
    // users.name, salaries.amount
    // Ian, 100
    // John, 150
    // John, 200
    // Mark, 200
    // Mark, 300
    // Eddie,
    // , 400
    System.out.println(
        db.outerJoin(db.getTable("users"), "id", db.getTable("salaries"), "user_id")
            .select(new String[] {"users.name", "salaries.amount"}));
  }
}
