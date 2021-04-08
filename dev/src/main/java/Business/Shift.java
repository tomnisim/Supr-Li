package Business;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static Business.TypeOfEmployee.*;

public class Shift {
    private TypeOfShift type;
    private Date date;
    private Map<TypeOfEmployee, Integer> constraints;
    private List<Pair<Employee,TypeOfEmployee>> currentShiftEmployees;
    private boolean isSealed;

    private void shiftValidityCheck (Date date) throws Exception
    {

        if (date == null)
        {
            throw new Exception("date can't be null");
        }
        long m = System.currentTimeMillis();
        if (date.before(new Date(m)))
        {
            throw new  Exception("date of available shift cant be in the past");
        }

    }
   /* public Shift(TypeOfShift type, Date date, Map<TypeOfEmployee, Integer> constraints, List<Pair<Employee,TypeOfEmployee>> currentShiftEmployees) throws Exception
    {
        shiftValidityCheck(type, date, constraints, currentShiftEmployees);
        this.type = type;
        this.date = date;
        this.constraints = new HashMap<>();
        this.currentShiftEmployees = currentShiftEmployees;
        this.constraints.put(HRManager,1);

    }*/

    public Shift(TypeOfShift type, Date date) throws Exception
    {
        shiftValidityCheck(date);
        this.type = type;
        this.date = date;
        this.currentShiftEmployees = new LinkedList<>();
        this.constraints = new HashMap<>(); //init
        this.constraints.put(ShiftManager, 1); //Default constraint
        this.isSealed = false;
    }
    //-------------------------------------------------------------------------getters-------------------------------------------------------------------------


    public Date getDate() {
        return date;
    }

    public List<Pair<Employee, TypeOfEmployee>> getCurrentShiftEmployees() {
        return currentShiftEmployees;
    }

    public Map<TypeOfEmployee, Integer> getConstraints() {
        return constraints;
    }

    public TypeOfShift getType() {
        return type;
    }
    public boolean isSealed()
    {
        return this.isSealed;
    }
    //--------------------------------------------------------------------------------setters---------------------------------------------------------------------


    public void setConstraints(Map<TypeOfEmployee, Integer> constraints) throws Exception
    {
        if(constraints == null)
        {
            throw new Exception("Constraints list is null");
        }
        else
        {
            if((!constraints.containsKey(ShiftManager)))
            {
                throw new Exception("Shift must contain a shift manager constraint");
            }
            else if((constraints.get(ShiftManager)<1))
            {
                throw new Exception("Number of shift managers in a shift must be at least 1");
            }
        }
        this.constraints = constraints;
    }

    public void setCurrentShiftEmployees(List<Pair<Employee, TypeOfEmployee>> currentShiftEmployees) throws Exception {
        if(currentShiftEmployees == null)
        {
            throw new Exception("currentShiftEmployees list is null");
        }
        this.currentShiftEmployees = currentShiftEmployees;
    }

    public void setDate(Date date) throws Exception
    {
        if (date == null)
        {
            throw new Exception("date can't be null");
        }
        long m = System.currentTimeMillis();
        if (date.before(new Date(m)))
        {
            throw new  Exception("date of available shift cant be in the past");
        }
        this.date = date;
    }

    public void setType(TypeOfShift type) {
        this.type = type;
    }

    public boolean sealShift() {
        if(!this.isSealed)
        {
            this.isSealed= this.checkFull();
        }
        return this.isSealed;
    }

    //----------------------------------------------------------------------------------methods-----------------------------------------------------------------------
    public void addEmployeeToShift(Employee toAdd, TypeOfEmployee type) throws Exception
    {
        if(toAdd==null)
        {
            throw new Exception("employee can not be null");
        }
        if (!toAdd.getSkills().contains((type)))
        {
            throw new Exception("employee cant be assigned to a skill he doesnt have");
        }
        if (!checkConstraints(toAdd, type))
        {
            throw new Exception("employee cant be assigned to a skill he doesnt have");
        }
        currentShiftEmployees.add((new Pair<Employee, TypeOfEmployee>(toAdd,type)));
        isSealed=sealShift();

    }
    private boolean checkConstraints(Employee toAdd, TypeOfEmployee type) throws  Exception
    {
        if(this.constraints.containsKey(type))
        {
            Integer numOfType = this.constraints.get(type);
            if (getNumberOfcurrType(type) >= numOfType) {
                throw new Exception(("number of employees of this type is exceeded"));
            }
        }
        return true;

    }
    private int getNumberOfcurrType(TypeOfEmployee type) {
        int ans=0;
        for (Pair p :currentShiftEmployees) {
            if (p.second==type)
            {
                ans++;
            }
        }
        return  ans;
    }


    public boolean checkFull()
    {
        Map <TypeOfEmployee, Integer> numOfEmp = new HashMap<>(); //Counts the number of employees of each type in the current shift
        for (Pair pair:currentShiftEmployees)
        {
            TypeOfEmployee typeOfCurrEmp=(TypeOfEmployee) pair.second; //Type of the current employee
            if (!numOfEmp.containsKey(typeOfCurrEmp)) //If current type was yet to be found
            {
                numOfEmp.put(typeOfCurrEmp,1);
            }
            else //Increment number of types found
            {
                numOfEmp.put(typeOfCurrEmp, numOfEmp.get(typeOfCurrEmp) + 1);
            }

        }
        for (TypeOfEmployee type: constraints.keySet())
        {
            if (constraints.get(type)!=numOfEmp.get(type))
            {
                return  false; //Found a type of employee in the constraints that isnt satisfied or exceeded maximum value
            }

        }
        return  true;
    }

    public boolean isEmployeeInShift(String id) {
        for (Pair p: currentShiftEmployees)
        {
            Employee currEmp = (Employee)p.first;
            if(currEmp.getId().equals(id))
            {
                return true;
            }
        }
        return false;

    }

    public void removeEmployee(String id) {
        Employee toRemove=null;
        for (Pair p:currentShiftEmployees) {
            {
                Employee cur = (Employee)p.first;
                if (cur.getId()==id)
                {
                    toRemove=cur;
                }
            }

        }
        currentShiftEmployees.remove(toRemove);
        isSealed=sealShift();

    }

    public void addConstraint(TypeOfEmployee typeOfEmployee, Integer numOfEmp) throws Exception{
        if (numOfEmp<0)
        {
            throw new Exception("amount of Employees must be positive");
        }
        this.constraints.put(typeOfEmployee, numOfEmp);
        isSealed=sealShift();

    }

    public void removeConstraint(TypeOfEmployee typeOfEmployee)throws Exception {
        if (this.constraints.remove(typeOfEmployee)==null)
        {
            throw new Exception("no such restriction");
        }
        isSealed=sealShift();

    }

    private String printStatus()
    {
        if(isSealed)
            return "Sealed";
        return "Open";
    }

    public String toString() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder builder=new StringBuilder();
        builder.append("Shift: \n\t");
        builder.append("Type: " + type);
        builder.append("\n\tShift Date: " + dateFormat.format(date));
        builder.append("\n\tConstraints:");
        for(TypeOfEmployee type:constraints.keySet())
        {
            builder.append("\n\t\tType Of Employee: " + type.toString());
            builder.append("\n\t\tAmount: " + constraints.get(type).toString());
        }
        builder.append("\n");
        builder.append("\n\tCurrent Shift Employees:");
        for(Pair<Employee, TypeOfEmployee> p:currentShiftEmployees)
        {
            builder.append("\n\t\tName: " + p.first.getFirstName() +" "+ p.first.getLastName());
            builder.append("\n\t\tType: " + p.second.toString());
        }
        builder.append("\n\tShift status: " + printStatus());
        builder.append("\n");

        return builder.toString();
    }
}
