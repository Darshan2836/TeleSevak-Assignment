package in.darshanudagire.telesevak.task.TabLayout;

public class TaskDescription {

    public String name , due_date,owner;
    public boolean isCompleted;

    public TaskDescription(String name, String due_date, String owner, boolean isCompleted) {
        this.name = name;
        this.due_date = due_date;
        this.owner = owner;
        this.isCompleted = isCompleted;
    }

    public TaskDescription() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
